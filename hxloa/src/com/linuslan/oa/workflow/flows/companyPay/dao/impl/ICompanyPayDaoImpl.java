package com.linuslan.oa.workflow.flows.companyPay.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.companyPay.dao.ICompanyPayDao;
import com.linuslan.oa.workflow.flows.companyPay.model.CompanyPay;

@Component("companyPayDao")
public class ICompanyPayDaoImpl extends IBaseDaoImpl implements ICompanyPayDao {
	private static Logger logger = Logger.getLogger(ICompanyPayDaoImpl.class);
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
	public Page<CompanyPay> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<CompanyPay> pageData = null;
		List<CompanyPay> list = new ArrayList<CompanyPay> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM CompanyPay cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM CompanyPay cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(CompanyPay.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			hql.append(" ORDER BY cp.status ASC, cp.id DESC");
			Map<String, Query> queryMap = this.buildQuery(session, CompanyPay.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			countQuery.setParameter("loginUserId", userId);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<CompanyPay> (list, totalRecord, totalPage, page);
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
	public Page<CompanyPay> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<CompanyPay> pageData = null;
		List<CompanyPay> list = new ArrayList<CompanyPay> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			//original sql: FROM CompanyPay c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")
			StringBuffer hql = new StringBuffer("FROM CompanyPay c WHERE c.isDelete=0 AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(CompanyPay.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			hql.append(" ORDER BY c.status ASC, c.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, CompanyPay.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			//query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(CompanyPay.class));
			//countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(CompanyPay.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<CompanyPay> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的企业付款
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<CompanyPay> pageData = null;
		List<CompanyPay> list = new ArrayList<CompanyPay> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM CompanyPay c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(CompanyPay.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			hql.append(" ORDER BY c.status ASC, c.id DESC");
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, CompanyPay.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(CompanyPay.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(CompanyPay.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<CompanyPay> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询企业付款汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<CompanyPay> pageData = null;
		List<CompanyPay> list = new ArrayList<CompanyPay> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM CompanyPay c WHERE c.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(CompanyPay.class, hql, paramMap, "c");
				if(CodeUtil.isNotEmpty(paramMap.get("payStatus"))) {
					if(!paramMap.get("payStatus").equals("4")) {
						subSQL += " AND c.id IN (SELECT wfId FROM AuditLog al WHERE al.wfType='companyPay' AND al.isAudit=0 AND al.status = "+paramMap.get("payStatus")+")";
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
			Map<String, Query> queryMap = this.buildQuery(session, CompanyPay.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<CompanyPay> (list, totalRecord, totalPage, page);
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
			StringBuffer sumAuditSQL = new StringBuffer("SELECT NVL(SUM(cp.money), 0) FROM CompanyPay cp WHERE cp.isDelete=0 AND cp.id" +
					" IN (SELECT al.wfId FROM AuditLog al WHERE al.isAudit=0 AND al.wfType='companyPay' AND al.status = 3)");
			
			//待打款
			StringBuffer sumPaySQL = new StringBuffer("SELECT NVL(SUM(cp.money), 0) FROM CompanyPay cp WHERE cp.isDelete=0 AND cp.id" +
					" IN (SELECT al.wfId FROM AuditLog al WHERE al.isAudit=0 AND al.wfType='companyPay' AND al.status = 5)");
			
			//已打款
			StringBuffer sumPaidSQL = new StringBuffer("SELECT NVL(SUM(cp.money), 0) FROM CompanyPay cp WHERE cp.isDelete=0 AND cp.status = 4");
			
			if(null != paramMap) {
				String conditionSQL = this.getHQL(CompanyPay.class, sumAuditSQL, paramMap, "cp");
				sumAuditSQL.append(conditionSQL);
				sumPaySQL.append(conditionSQL);
				sumPaidSQL.append(conditionSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, CompanyPay.class, sumAuditSQL.toString(), null, paramMap);
			Query sumAuditQuery = queryMap.get(ConstantVar.QUERY);
			queryMap = this.buildQuery(session, CompanyPay.class, sumPaySQL.toString(), null, paramMap);
			Query sumPayQuery = queryMap.get(ConstantVar.QUERY);
			queryMap = this.buildQuery(session, CompanyPay.class, sumPaidSQL.toString(), null, paramMap);
			Query sumPaidQuery = queryMap.get(ConstantVar.QUERY);
			sumAudit = (BigDecimal) sumAuditQuery.uniqueResult();
			sumPay = (BigDecimal) sumPayQuery.uniqueResult();
			sumPaid = (BigDecimal) sumPaidQuery.uniqueResult();
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
	 * 通过ids
	 * @param ids
	 * @return
	 */
	public List<CompanyPay> queryInIds(List<Long> ids) {
		String hql = "FROM CompanyPay cp WHERE cp.isDelete = 0 AND cp.id IN (:ids)";
		List<CompanyPay> list = new ArrayList<CompanyPay> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql).setParameterList("ids", ids);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询企业付款
	 * @param id
	 * @return
	 */
	public CompanyPay queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (CompanyPay) session.get(CompanyPay.class, id);
	}
	
	/**
	 * 新增企业付款
	 * @param companyPay
	 * @return
	 */
	public boolean add(CompanyPay companyPay) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(companyPay);
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param companyPay
	 * @return
	 */
	public boolean update(CompanyPay companyPay) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(companyPay);
		success = true;
		return success;
	}
}
