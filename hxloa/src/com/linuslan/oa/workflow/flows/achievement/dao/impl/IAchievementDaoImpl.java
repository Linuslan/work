package com.linuslan.oa.workflow.flows.achievement.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.dao.IAchievementDao;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContent;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentOpinion;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentScore;
import com.linuslan.oa.workflow.flows.achievement.model.YearAchievement;

@Component("achievementDao")
public class IAchievementDaoImpl extends IBaseDaoImpl implements
		IAchievementDao {
	private static Logger logger = Logger.getLogger(IAchievementDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 查询登陆用户的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<Achievement> pageData = null;
		List<Achievement> list = new ArrayList<Achievement> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM Achievement cp WHERE cp.userId=:userId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Achievement cp WHERE cp.userId=:userId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Achievement.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			hql.append(" ORDER BY cp.year DESC, cp.month DESC, cp.id DESC, cp.status DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Query query = null;
			Query countQuery = null;
			Map<String, Query> queryMap = this.buildQuery(session, Achievement.class, hql.toString(), countHQL.toString(), paramMap);
			query = this.getQuery(queryMap, ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			countQuery = this.getQuery(queryMap, ConstantVar.COUNT_QUERY);
			query.setParameter("userId", userId);
			countQuery.setParameter("userId", userId);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Achievement> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Achievement> pageData = null;
		List<Achievement> list = new ArrayList<Achievement> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Achievement c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+" AND al.status=3)");
			if(null != paramMap) {
				String subSQL = this.getHQL(Achievement.class, hql, paramMap, "c");
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					subSQL += " AND c.userId IN (SELECT id FROM User u WHERE u.isDelete=0 AND u.isLeave=1 AND u.departmentId=:departmentId)";
				}
				hql.append(subSQL);
			}
			hql.append(" ORDER BY c.year DESC, c.month DESC, c.status DESC, c.id DESC");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Achievement.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					query.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
					countQuery.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
				}
			}
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Achievement> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的绩效
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Achievement> pageData = null;
		List<Achievement> list = new ArrayList<Achievement> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Achievement c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Achievement.class, hql, paramMap, "c");
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					subSQL += " AND c.userId IN (SELECT id FROM User u WHERE u.isDelete=0 AND u.isLeave=1 AND u.departmentId=:departmentId)";
				}
				hql.append(subSQL);
			}
			hql.append(" ORDER BY c.year DESC, c.month DESC, c.status DESC, c.id DESC");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Achievement.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					query.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
					countQuery.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
				}
			}
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Achievement> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询待登陆用户评分的绩效申请
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryScorePage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Achievement> pageData = null;
		List<Achievement> list = new ArrayList<Achievement> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Achievement c, AuditLog al WHERE al.wfType='achievement' AND al.wfId=c.id AND al.isAudit=0 AND c.isDelete=0 AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Achievement.class, hql, paramMap, "c");
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					subSQL += " AND c.userId IN (SELECT id FROM User u WHERE u.isDelete=0 AND u.isLeave=1 AND u.departmentId=:departmentId)";
				}
				hql.append(subSQL);
			}
			hql.append(" ORDER BY al.status DESC, c.year ASC, c.month ASC, c.id DESC");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Achievement.class, "SELECT c "+hql.toString(), countHQL.toString(), paramMap);
			Query query = this.getQuery(queryMap, ConstantVar.QUERY);
			Query countQuery = this.getQuery(queryMap, ConstantVar.COUNT_QUERY);
			//query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			//countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Achievement.class));
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("departmentId"))) {
					query.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
					countQuery.setParameter("departmentId", CodeUtil.parseLong(paramMap.get("departmentId")));
				}
			}
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Achievement> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询绩效汇总
	 * @param paramMap
	 * @param year 当前年
	 * @param month 当前月
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryReportPage(Map<String, String> paramMap, int year, int month, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			int lastMonth = (month - 1) == 0 ? 12 : (month - 1);
			int lastYear = lastMonth == 12 ? (year - 1) : year;
			
			String sql = "SELECT u.id user_id, u.name, u.department_id, sd.name department_name, u.company_id, sc.name company_name, t1.id last_achievement_id," +
					" NVL(t1.year, "+lastYear+") last_year, NVL(t1.month, "+lastMonth+") last_month, NVL(t1.score_weight, 0) last_score_weight," +
					" NVL(t1.user_score, 0) last_user_score, NVL(t1.leader_score, 0) last_leader_score, NVL(t1.add_score, 0) last_add_score," +
					" NVL(t1.add_money, 0) last_add_money, NVL(t1.total_score, 0) last_total_score, t1.status last_status," +
					" t1.flow_status last_flow_status, NVL(t1.auditor, '无') last_auditor," +
					" t2.id achievement_id, NVL(t2.year, "+year+") year, NVL(t2.month, "+month+") month, NVL(t2.score_weight, 0) score_weight," +
					" NVL(t2.user_score, 0) user_score, NVL(t2.leader_score, 0) leader_score, NVL(t2.add_score, 0) add_score," +
					" NVL(t2.add_money, 0) add_money, NVL(t2.total_score, 0) total_score, t2.status, t2.flow_status," +
					" NVL(t2.auditor, '无') auditor" +
					" FROM sys_user u LEFT JOIN achievement_report t1 ON t1.user_id = u.id AND t1.year = :lastYear AND t1.month = :lastMonth" +
					" LEFT JOIN achievement_report t2 ON t2.user_id = u.id AND t2.year = :year AND t2.month = :month" +
					" LEFT JOIN sys_department sd ON sd.id=u.department_id LEFT JOIN sys_company sc ON sc.id=u.company_id" +
					" WHERE u.is_delete = 0 AND u.is_leave = 1";
			sql += getConditionSQL(paramMap);
			sql += " ORDER BY sc.order_num ASC, sc.id DESC, sd.order_num ASC, sd.id DESC, u.order_num ASC, u.id DESC";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("lastYear", lastYear);
			query.setParameter("lastMonth", lastMonth);
			query.setParameter("year", year);
			query.setParameter("month", month);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			
			SQLQuery countQuery = session.createSQLQuery("SELECT COUNT(*) FROM ("+sql+")");
			countQuery.setParameter("lastYear", lastYear);
			countQuery.setParameter("lastMonth", lastMonth);
			countQuery.setParameter("year", year);
			countQuery.setParameter("month", month);
			
			this.setConditionValue(paramMap, query, countQuery);
			
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = totalRecord % rows == 0 ? totalRecord / rows : totalRecord / rows + 1;
		} catch(Exception ex) {
			logger.error("查询绩效汇总页面异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	public String getConditionSQL(Map<String, String> paramMap) {
		String conditionSQL = "";
		if(null != paramMap) {
			Set<Entry<String, String>> entrySet = paramMap.entrySet();
			if(null != entrySet) {
				Iterator<Entry<String, String>> iter = entrySet.iterator();
				Entry<String, String> entry = null;
				String key = null;
				String value = null;
				while(iter.hasNext()) {
					entry = iter.next();
					key = entry.getKey();
					value = entry.getValue();
					if(CodeUtil.isNotEmpty(key) && CodeUtil.isNotEmpty(value)) {
						conditionSQL += " AND u."+key+"=:"+key;
					}
				}
			}
		}
		return conditionSQL;
	}
	
	public void setConditionValue(Map<String, String> paramMap, SQLQuery query, SQLQuery countQuery) {
		if(null != paramMap) {
			Set<Entry<String, String>> entrySet = paramMap.entrySet();
			if(null != entrySet) {
				Iterator<Entry<String, String>> iter = entrySet.iterator();
				Entry<String, String> entry = null;
				String key = null;
				String value = null;
				while(iter.hasNext()) {
					entry = iter.next();
					key = entry.getKey();
					value = entry.getValue();
					if(CodeUtil.isNotEmpty(key) && CodeUtil.isNotEmpty(value)) {
						if(key.equals("department_id")
								|| key.equals("company_id") || key.equals("id")) {
							query.setParameter(key, CodeUtil.parseLong(value));
							countQuery.setParameter(key, CodeUtil.parseLong(value));
						} else {
							query.setParameter(key, value);
							countQuery.setParameter(key, value);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Achievement queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Achievement) session.get(Achievement.class, id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的绩效
	 * @param date
	 * @return
	 */
	public Achievement queryByDate(String date, Long userId, Long achievementId) {
		String hql = "FROM Achievement a WHERE a.isDelete=0 AND TO_DATE(a.year||'-'||a.month, 'yyyy-mm')=TO_DATE(:date, 'yyyy-mm') AND a.userId=:userId";
		if(null != achievementId) {
			hql += " AND a.id <> :achievementId";
		}
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameter("date", date);
		query.setParameter("userId", userId);
		if(null != achievementId) {
			query.setParameter("achievementId", achievementId);
		}
		List<Achievement> list = query.list();
		if(null != list && 0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 查询ids集合中的绩效
	 * @param ids
	 * @return
	 */
	public List<Achievement> queryInIds(List<Long> ids) {
		List<Achievement> list = new ArrayList<Achievement> ();
		String hql = "FROM Achievement a WHERE a.id IN (:ids) AND a.isDelete=0";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过绩效主表的id查询绩效项目
	 * @param id
	 * @return
	 */
	public List<AchievementContent> queryContentsByAchievementId(Long id) {
		List<AchievementContent> contents = new ArrayList<AchievementContent> ();
		if(null != id) {
			String hql = "FROM AchievementContent rc WHERE rc.achievementId=:achievementId AND rc.isDelete=0 ORDER BY rc.orderNo ASC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("achievementId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询绩效项目
	 * @param ids
	 * @return
	 */
	public List<AchievementContent> queryContentsInIds(List<Long> ids) {
		List<AchievementContent> contents = new ArrayList<AchievementContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM AchievementContent rc WHERE rc.id IN (:ids) AND rc.isDelete=0";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 通过绩效项目的id查询绩效的项目的审核意见记录
	 * @param id
	 * @return
	 */
	public List<AchievementContentOpinion> queryContentOpinionByContentId(Long id) {
		List<AchievementContentOpinion> contentOpinions = new ArrayList<AchievementContentOpinion> ();
		if(null != id) {
			String hql = "FROM AchievementContentOpinion rc WHERE rc.contentId=:contentId AND rc.isDelete=0";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("contentId", id);
			contentOpinions.addAll(query.list());
		}
		return contentOpinions;
	}
	
	/**
	 * 查询全年绩效
	 * @param year
	 * @return
	 */
	public List<YearAchievement> queryYearAchievement(int year) {
		Map<String, YearAchievement> map = new HashMap<String, YearAchievement> ();
		List<YearAchievement> yaList = new ArrayList<YearAchievement> ();
		String hql = "SELECT sd.name department_name, sd.id department_id, su.name user_name, t.user_id, t.year, t.month, (SELECT NVL(SUM(t1.leader_score), 0) FROM wf_achievement_content t1 WHERE t1.is_delete=0 AND t1.achievement_id=t.id) leader_score, NVL(t.add_money, 0) add_money, NVL(t.add_score, 0) add_score FROM wf_achievement t LEFT JOIN sys_user su ON su.id=t.user_id LEFT JOIN sys_department sd ON su.department_id=sd.id WHERE t.year = :year AND t.status = 4 AND t.is_delete = 0 ORDER BY sd.order_num ASC";
		Session session = this.sessionFactory.getCurrentSession();
		Map<String, User> userMap = new HashMap<String, User> ();
		try {
			Query query = session.createSQLQuery(hql);
			query.setParameter("year", year);
			//query.setParameter("year", year);
			List<Object> list = query.list();
			if(null != list) {
				Iterator<Object> iter = list.iterator();
				while(iter.hasNext()) {
					Object a = iter.next();
					Object[] arr = (Object[]) a;
					if(null != a) {
						Department d = new Department();
						d.setId(CodeUtil.parseLong(arr[1]));
						d.setName(arr[0].toString());
						User user = new User();
						user.setId(CodeUtil.parseLong(arr[3]));
						user.setName(arr[2].toString());
						YearAchievement ya = new YearAchievement();
						ya.setYear(Integer.parseInt(arr[4].toString()));
						ya.setUser(user);
						ya.setDepartment(d);
						int month = Integer.parseInt(arr[5].toString());
						int score = Integer.parseInt(arr[6].toString()) + Integer.parseInt(arr[8].toString());
						if(null != map.get(String.valueOf(user.getId()))) {
							YearAchievement userYa = map.get(String.valueOf(user.getId()));
							userYa.getMonthScoreMap().put(month, score);
						} else {
							ya.getMonthScoreMap().put(month, score);
							map.put(String.valueOf(user.getId()), ya);
						}
					}
				}
				
				/*
				 * 将已经存放好数据的map，通过已经排好序的list对象取出来，放到yaList中，这样，yaList中的数据就是排好序的集合了
				 */
				iter = list.iterator();
				while(iter.hasNext()) {
					//Achievement a = (Achievement) iter.next();
					Object a = iter.next();
					Object[] arr = (Object[]) a;
					User user = new User();
					user.setId(CodeUtil.parseLong(arr[3].toString()));
					user.setName(arr[2].toString());
					if(null != a) {
						String userId = String.valueOf(arr[3].toString());
						if(null == userMap.get(userId)) {
							YearAchievement ya = map.get(userId);
							yaList.add(ya);
							userMap.put(userId, user);
						}
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			//session.getTransaction().commit();
		}
		return yaList;
	}
	
	/**
	 * 新增申请单
	 * @param achievement
	 * @return
	 */
	public boolean add(Achievement achievement) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(achievement);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新绩效项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<AchievementContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<AchievementContent> iter = contents.iterator();
		AchievementContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param achievement
	 * @return
	 */
	public boolean update(Achievement achievement) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(achievement);
		success = true;
		return success;
	}
	
	/**
	 * 删除绩效项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long achievementId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE AchievementContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.achievementId=:achievementId";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("achievementId", achievementId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除绩效项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE AchievementContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量添加绩效项目的审核意见
	 * @param opinions
	 * @return
	 */
	public boolean saveContentOpinionBatch(List<AchievementContentOpinion> opinions) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		if(null != opinions) {
			Iterator<AchievementContentOpinion> iter = opinions.iterator();
			AchievementContentOpinion opinion = null;;
			while(iter.hasNext()) {
				opinion = iter.next();
				if(null != opinion.getContentId() && CodeUtil.isNotEmpty(opinion.getOpinion())) {
					opinion.setCreateDate(new Date());
					opinion.setUserId(HttpUtil.getLoginUser().getId());
					session.save(opinion);
				}
			}
		}
		return success;
	}
	
	/**
	 * 批量添加绩效项目的审核意见
	 * @param opinions
	 * @return
	 */
	public boolean saveContentScoreBatch(List<AchievementContentScore> scores) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		if(null != scores) {
			Iterator<AchievementContentScore> iter = scores.iterator();
			AchievementContentScore score = null;;
			while(iter.hasNext()) {
				score = iter.next();
				session.save(score);
			}
		}
		return success;
	}
}
