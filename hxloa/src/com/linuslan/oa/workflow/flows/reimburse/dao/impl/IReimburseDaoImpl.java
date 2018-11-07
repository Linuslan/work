package com.linuslan.oa.workflow.flows.reimburse.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.companyPay.model.CompanyPay;
import com.linuslan.oa.workflow.flows.reimburse.dao.IReimburseDao;
import com.linuslan.oa.workflow.flows.reimburse.model.Reimburse;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseContent;

@Component("reimburseDao")
public class IReimburseDaoImpl extends IBaseDaoImpl implements
		IReimburseDao {

	private static Logger logger = Logger.getLogger(IReimburseDaoImpl.class);
	
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
	public Page<Reimburse> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<Reimburse> pageData = null;
		List<Reimburse> list = new ArrayList<Reimburse> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM Reimburse cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Reimburse cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Reimburse.class, hql, paramMap, "cp");
				String dateSQL = "SELECT reimburseId FROM ReimburseContent t WHERE ";
				boolean hasDate = false;
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					hasDate = true;
					dateSQL += "t.remittanceDate >= TO_DATE(:moneyDate_start, 'yyyy-mm-dd')";
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					hasDate = true;
					if(hasDate) {
						dateSQL += " AND t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					} else {
						dateSQL += " t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					}
				}
				if(hasDate) {
					subSQL += " AND cp.id IN ("+dateSQL+")";
				}
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			hql.append(" ORDER BY cp.status ASC, cp.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Reimburse.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					query.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					countQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					query.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					countQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
				}
			}
			query.setParameter("loginUserId", userId);
			countQuery.setParameter("loginUserId", userId);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Reimburse> (list, totalRecord, totalPage, page);
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
	public Page<Reimburse> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Reimburse> pageData = null;
		List<Reimburse> list = new ArrayList<Reimburse> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Reimburse c WHERE c.isDelete=0 AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Reimburse.class, hql, paramMap, "c");
				String dateSQL = "SELECT reimburseId FROM ReimburseContent t WHERE ";
				boolean hasDate = false;
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					hasDate = true;
					dateSQL += "t.remittanceDate >= TO_DATE(:moneyDate_start, 'yyyy-mm-dd')";
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					hasDate = true;
					if(hasDate) {
						dateSQL += " AND t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					} else {
						dateSQL += " t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					}
				}
				if(hasDate) {
					subSQL += " AND c.id IN ("+dateSQL+")";
				}
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			hql.append(" ORDER BY c.status ASC, c.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Reimburse.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					query.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					countQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					query.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					countQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
				}
			}
			//query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Reimburse.class));
			//countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Reimburse.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Reimburse> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的报销
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Reimburse> pageData = null;
		List<Reimburse> list = new ArrayList<Reimburse> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Reimburse c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Reimburse.class, hql, paramMap, "c");
				String dateSQL = "SELECT reimburseId FROM ReimburseContent t WHERE ";
				boolean hasDate = false;
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					hasDate = true;
					dateSQL += "t.remittanceDate >= TO_DATE(:moneyDate_start, 'yyyy-mm-dd')";
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					hasDate = true;
					if(hasDate) {
						dateSQL += " AND t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					} else {
						dateSQL += " t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					}
				}
				if(hasDate) {
					subSQL += " AND c.id IN ("+dateSQL+")";
				}
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			hql.append(" ORDER BY c.status ASC, c.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Reimburse.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					query.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					countQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					query.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					countQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
				}
			}
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(Reimburse.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Reimburse.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Reimburse> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询报销汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Reimburse> pageData = null;
		List<Reimburse> list = new ArrayList<Reimburse> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Reimburse c WHERE c.isDelete=0 AND c.status >= 3");
			if(null != paramMap) {
				String subSQL = this.getHQL(Reimburse.class, hql, paramMap, "c");
				
				String dateSQL = "SELECT reimburseId FROM ReimburseContent t WHERE ";
				boolean hasDate = false;
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					hasDate = true;
					dateSQL += "t.remittanceDate >= TO_DATE(:moneyDate_start, 'yyyy-mm-dd')";
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					hasDate = true;
					if(hasDate) {
						dateSQL += " AND t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					} else {
						dateSQL += " t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					}
				}
				if(hasDate) {
					subSQL += " AND c.id IN ("+dateSQL+")";
				}
				
				if(CodeUtil.isNotEmpty(paramMap.get("payStatus"))) {
					if(!paramMap.get("payStatus").equals("4")) {
						subSQL += " AND c.id IN (SELECT wfId FROM AuditLog al WHERE al.wfType='reimburse' AND al.isAudit=0 AND al.status IN ("+paramMap.get("payStatus")+"))";
					} else {
						subSQL += " AND c.status = "+paramMap.get("payStatus");
					}
				}
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			hql.append(" ORDER BY c.status ASC, c.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Reimburse.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					query.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					countQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					query.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					countQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
				}
			}
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Reimburse> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap) {
		Map<String, Object> sumMap = new HashMap<String, Object> ();
		BigDecimal sumAudit = null;
		BigDecimal sumPay = null;
		BigDecimal sumPaid = null;
		try {
			//待审核
			StringBuffer sumAuditSQL = new StringBuffer("SELECT SUM(NVL((SELECT SUM(t.money) FROM ReimburseContent t WHERE t.reimburseId=cp.id), 0)) FROM Reimburse cp WHERE cp.isDelete=0 AND cp.id" +
					" IN (SELECT al.wfId FROM AuditLog al WHERE al.isAudit=0 AND al.wfType='reimburse' AND al.status IN (3,5,6)) AND cp.status >= 3");
			
			//待打款
			StringBuffer sumPaySQL = new StringBuffer("SELECT SUM(NVL((SELECT SUM(t.money) FROM ReimburseContent t WHERE t.reimburseId=cp.id), 0)) FROM Reimburse cp WHERE cp.isDelete=0 AND cp.id" +
					" IN (SELECT al.wfId FROM AuditLog al WHERE al.isAudit=0 AND al.wfType='reimburse' AND al.status = 7)  AND cp.status >= 3");
			
			//已打款
			StringBuffer sumPaidSQL = new StringBuffer("SELECT SUM(NVL((SELECT SUM(t.money) FROM ReimburseContent t WHERE t.reimburseId=cp.id), 0)) FROM Reimburse cp WHERE cp.isDelete=0 AND cp.status = 4");
			
			if(null != paramMap) {
				String conditionSQL = this.getHQL(Reimburse.class, sumAuditSQL, paramMap, "cp");
				String dateSQL = "SELECT reimburseId FROM ReimburseContent t WHERE ";
				boolean hasDate = false;
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					hasDate = true;
					dateSQL += "t.remittanceDate >= TO_DATE(:moneyDate_start, 'yyyy-mm-dd')";
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					hasDate = true;
					if(hasDate) {
						dateSQL += " AND t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					} else {
						dateSQL += " t.remittanceDate <= TO_DATE(:moneyDate_end, 'yyyy-mm-dd')";
					}
				}
				if(hasDate) {
					conditionSQL += " AND c.id IN ("+dateSQL+")";
				}
				sumAuditSQL.append(conditionSQL);
				sumPaySQL.append(conditionSQL);
				sumPaidSQL.append(conditionSQL);
			}
			
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Reimburse.class, sumAuditSQL.toString(), null, paramMap);
			Query sumAuditQuery = queryMap.get(ConstantVar.QUERY);
			queryMap = this.buildQuery(session, Reimburse.class, sumPaySQL.toString(), null, paramMap);
			Query sumPayQuery = queryMap.get(ConstantVar.QUERY);
			queryMap = this.buildQuery(session, Reimburse.class, sumPaidSQL.toString(), null, paramMap);
			Query sumPaidQuery = queryMap.get(ConstantVar.QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_start"))) {
					sumAuditQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					sumPayQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
					sumPaidQuery.setParameter("moneyDate_start", paramMap.get("moneyDate_start"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("moneyDate_end"))) {
					sumAuditQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					sumPayQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
					sumPaidQuery.setParameter("moneyDate_end", paramMap.get("moneyDate_end"));
				}
			}
			sumAudit = (BigDecimal) sumAuditQuery.uniqueResult();
			sumAudit = sumAudit == null ? new BigDecimal("0") : sumAudit;
			sumPay = (BigDecimal) sumPayQuery.uniqueResult();
			sumPay = sumPay == null ? new BigDecimal("0") : sumPay;
			sumPaid = (BigDecimal) sumPaidQuery.uniqueResult();
			sumPaid = sumPaid == null ? new BigDecimal("0") : sumPaid;
		} catch(Exception ex) {
			ex.printStackTrace();
			sumAudit = new BigDecimal("0");
			sumPay = new BigDecimal("0");
			sumPaid = new BigDecimal("0");
		} finally {
			sumMap.put("sumAudit", sumAudit);
			sumMap.put("sumPay", sumPay);
			sumMap.put("sumPaid", sumPaid);
		}
		
		return sumMap;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Reimburse queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Reimburse) session.get(Reimburse.class, id);
	}
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<ReimburseContent> queryContentsByReimburseId(Long id) {
		List<ReimburseContent> contents = new ArrayList<ReimburseContent> ();
		if(null != id) {
			String hql = "FROM ReimburseContent rc WHERE rc.reimburseId=:reimburseId AND rc.isDelete=0 ORDER BY rc.orderNo, rc.id ASC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("reimburseId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	public List<Reimburse> queryInIds(List<Long> ids) {
		List<Reimburse> list = new ArrayList<Reimburse> ();
		String hql = "FROM Reimburse r WHERE r.id IN (:ids) AND r.isDelete = 0";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		list.addAll(query.list());
		return list;
	}
	
	public List<ReimburseContent> queryContentsByReimburseIds(List<Long> ids) {
		List<ReimburseContent> list = new ArrayList<ReimburseContent> ();
		String hql = "FROM ReimburseContent rc WHERE rc.reimburseId IN (:ids) AND rc.isDelete=0 ORDER BY rc.orderNo, rc.id ASC";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询报销项目
	 * @param ids
	 * @return
	 */
	public List<ReimburseContent> queryContentsInIds(List<Long> ids) {
		List<ReimburseContent> contents = new ArrayList<ReimburseContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM ReimburseContent rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 通过部门id查询归属的报销类别
	 * @param departmentId
	 * @return
	 */
	public List<ReimburseClass> queryReimburseClassesByDepartmentId(Long departmentId) {
		List<ReimburseClass> list = new ArrayList<ReimburseClass> ();
		if(null != departmentId) {
			String sql = "SELECT t.* FROM wf_reimburse_class t WHERE t.id IN (SELECT t2.reimburse_class_id FROM wf_reimburse_class_department t2 WHERE t2.department_id=:departmentId)";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("departmentId", departmentId);
			query.addEntity(ReimburseClass.class);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 新增申请单
	 * @param reimburse
	 * @return
	 */
	public boolean add(Reimburse reimburse) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(reimburse);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新报销项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<ReimburseContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<ReimburseContent> iter = contents.iterator();
		ReimburseContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param reimburse
	 * @return
	 */
	public boolean update(Reimburse reimburse) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(reimburse);
		success = true;
		return success;
	}
	
	/**
	 * 删除报销项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long reimburseId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE ReimburseContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.reimburseId=:reimburseId";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("reimburseId", reimburseId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE ReimburseContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
}