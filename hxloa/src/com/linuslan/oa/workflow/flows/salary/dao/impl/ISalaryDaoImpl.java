package com.linuslan.oa.workflow.flows.salary.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.salary.dao.ISalaryDao;
import com.linuslan.oa.workflow.flows.salary.model.Salary;
import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;

@Component("salaryDao")
public class ISalaryDaoImpl extends IBaseDaoImpl implements ISalaryDao {
private static Logger logger = Logger.getLogger(ISalaryDaoImpl.class);
	
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
	public Page<Salary> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<Salary> pageData = null;
		List<Salary> list = new ArrayList<Salary> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM Salary cp WHERE cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Salary cp WHERE cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Salary.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Salary.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			//query.setParameter("loginUserId", userId);
			//countQuery.setParameter("loginUserId", userId);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Salary> (list, totalRecord, totalPage, page);
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
	public Page<Salary> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Salary> pageData = null;
		List<Salary> list = new ArrayList<Salary> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Salary c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Salary.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Salary.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Salary.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Salary.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Salary> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Salary> pageData = null;
		List<Salary> list = new ArrayList<Salary> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Salary c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Salary.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Salary.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(Salary.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Salary.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Salary> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询已完成薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		Page<Salary> pageData = null;
		List<Salary> list = new ArrayList<Salary> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			StringBuffer hql = new StringBuffer("FROM Salary cp WHERE cp.isDelete=0 AND cp.status = 4");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Salary cp WHERE cp.isDelete=0 AND cp.status = 4");
			if(null != paramMap) {
				String subSQL = this.getHQL(Salary.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Salary.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			//query.setParameter("loginUserId", userId);
			//countQuery.setParameter("loginUserId", userId);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Salary> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Salary queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Salary) session.get(Salary.class, id);
	}
	
	/**
	 * 检查year-month是否有已经创建的工资
	 * @param year
	 * @param month
	 * @return
	 */
	public long checkExistSalary(int year, int month) {
		long count = 0;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "SELECT COUNT(*) FROM Salary s WHERE s.isDelete=0 AND s.year=:year AND s.month=:month";
		Query query = session.createQuery(hql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		count = (Long) query.uniqueResult();
		return count;
	}
	
	/**
	 * 通过工资主表的id查询工资项目
	 * @param id
	 * @return
	 */
	public List<SalaryContent> queryContentsBySalaryId(Long id) {
		List<SalaryContent> contents = new ArrayList<SalaryContent> ();
		if(null != id) {
			String hql = "FROM SalaryContent rc WHERE rc.salaryId=:salaryId AND rc.isDelete=0";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("salaryId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询工资项目
	 * @param ids
	 * @return
	 */
	public List<SalaryContent> queryContentsInIds(List<Long> ids) {
		List<SalaryContent> contents = new ArrayList<SalaryContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM SalaryContent rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 查询登陆用户的生效工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<SalaryContent> queryContentByUserId(int year, int month) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = " FROM SalaryContent sc WHERE sc.userId=:userId AND sc.salaryId IN (SELECT s.id FROM Salary s WHERE s.year=:year AND s.month=:month AND s.status=4)";
		Query query = session.createQuery(hql);
		query.setParameter("userId", HttpUtil.getLoginUser().getId());
		query.setParameter("year", year);
		query.setParameter("month", month);
		List<SalaryContent> list = query.list();
		return list;
	}
	
	/**
	 * 新增申请单
	 * @param salary
	 * @return
	 */
	public boolean add(Salary salary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(salary);
		/*List<SalaryContent> contents = salary.getContents();
		try {
			Connection conn = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO wf_salary_content t(id, salary_id, user_id, user_name, department_id, post_id, type_id," +
					" create_date, supposed_duty_day, actual_duty_day, actual_duty_hour, basic_salary, achievement_salary," +
					" achievement_score, actual_achievement_salary, commission, full_attendance_award, overtime_pay, seniority_pay," +
					" sick_leave_deduct, affair_leave_deduct, late_deduct, punish_deduct, meal_subsidy, supposed_total_salary," +
					" pretax_salary, tax, social_insurance, health_insurance, total_insurance, actual_total_salary," +
					" company_social_insurance, company_health_insurance, tel_charge, travel_allowance, other, info," +
					" order_no, is_delete, housing_subsidy) VALUES(wf_salary_content_seq.nextval, ?, ?, ?, ?, ?, ?," +
					" SYSDATE, ?, ?, ?, ?, ?," +
					" ?, ?, ?, ?, ?, ?," +
					" ?, ?, ?, ?, ?, ?," +
					" ?, ?, ?, ?, ?, ?," +
					" ?, ?, ?, ?, ?, ?," +
					" ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			Iterator<SalaryContent> iter = contents.iterator();
			SalaryContent sc = null;
			while(iter.hasNext()) {
				sc = iter.next();
				ps.setLong(1, sc.getId());
				ps.setLong(2, sc.getUserId());
				ps.setString(3, sc.getUserName());
				ps.setLong(4, sc.getDepartmentId());
				ps.setLong(5, sc.getPostId());
				ps.setLong(6, sc.getTypeId());
				ps.setDouble(7, sc.getSupposedDutyDay());
				ps.setDouble(8, sc.getActualDutyDay());
				ps.setDouble(9, sc.getActualDutyHour());
				ps.setDouble(10, sc.getBasicSalary().doubleValue());
				ps.setDouble(11, sc.getAchievementSalary().doubleValue());
				ps.setDouble(12, sc.getAchievementScore().doubleValue());
				ps.setDouble(13, sc.getActualAchievementSalary().doubleValue());
				ps.setDouble(14, sc.getCommission().doubleValue());
				ps.setDouble(15, sc.getFullAttendanceAward().doubleValue());
				ps.setDouble(16, sc.getOvertimePay().doubleValue());
				ps.setDouble(17, sc.getSeniorityPay().doubleValue());
				ps.setDouble(18, sc.getSickLeaveDeduct().doubleValue());
				ps.setDouble(19, sc.getAffairLeaveDeduct().doubleValue());
				ps.setDouble(20, sc.getLateDeduct().doubleValue());
				ps.setDouble(21, sc.getPunishDeduct().doubleValue());
				ps.setDouble(22, sc.getMealSubsidy().doubleValue());
				ps.setDouble(22, sc.getSupposedTotalSalary().doubleValue());
				ps.setDouble(23, sc.getPretaxSalary().doubleValue());
				ps.setDouble(23, sc.getTax().doubleValue());
				ps.setDouble(24, sc.getSocialInsurance().doubleValue());
				ps.setDouble(25, sc.getHealthInsurance().doubleValue());
				ps.setDouble(26, sc.getTotalInsurance().doubleValue());
				ps.setDouble(27, sc.getActualTotalSalary().doubleValue());
				ps.setDouble(28, sc.getCompanySocialInsurance().doubleValue());
				ps.setDouble(29, sc.getCompanyHealthInsurance().doubleValue());
				ps.setDouble(30, sc.getTelCharge().doubleValue());
				ps.setDouble(31, sc.getTravelAllowance().doubleValue());
				ps.setDouble(32, sc.getOther().doubleValue());
				ps.setString(33, sc.getInfo());
				ps.setInt(34, sc.getOrderNo());
				ps.setInt(35, sc.getIsDelete());
				ps.setDouble(36, sc.getHousingSubsidy().doubleValue());
				ps.addBatch();
			}
			int[] count = ps.executeBatch();
			conn.commit();
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(CodeUtil.getStackTrace(ex));
		}*/
		success = true;
		return success;
	}
	
	/**
	 * 批量更新工资项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SalaryContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<SalaryContent> iter = contents.iterator();
		SalaryContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			if(null != content) {
				if(null == content.getId()) {
					//新增的，则餐补是原始餐补，则将原始餐补这个字段设置成原始餐补
					content.setOriginalMealSubsidy(content.getMealSubsidy());
					session.save(content);
				} else {
					session.merge(content);
				}
			}
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新工资
	 * @param salary
	 * @return
	 */
	public boolean update(Salary salary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(salary);
		success = true;
		return success;
	}
	
	/**
	 * 删除工资项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SalaryContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.salaryId=:id";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除工资项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SalaryContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 查询年月的已生效工资，目前主要用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> queryFinishedByYearAndMonth(int year, int month) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT (SELECT NVL((SELECT NVL(SUM(t2.leader_score), 0) FROM wf_achievement_content t2 WHERE t2.is_delete=0 AND t2.achievement_id=t.id), 0)+NVL(t.add_score, 0) FROM wf_achievement t WHERE t.year=(SELECT t3.year FROM wf_salary t3 WHERE t3.id=t1.salary_id) AND t.month=(SELECT t2.month FROM wf_salary t2 WHERE t2.id=t1.salary_id) AND t.is_delete=0 AND t.status=4 AND t.user_id=t1.user_id) current_achievement_score, t2.id dept_id, t2.name department_name, t2.company_id, t1.* FROM wf_salary t, wf_salary_content t1, sys_department t2 WHERE t.id=t1.salary_id AND t.year=:year AND t.month=:month AND (t1.department_id=t2.id OR t.department_id=t2.id) AND t.is_delete=0 AND t1.is_delete=0 AND t.status=4 ORDER BY t2.order_num";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(query.list());
		return list;
	}
}
