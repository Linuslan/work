package com.linuslan.oa.workflow.flows.invoice.dao.impl;

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
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.companyPay.model.CompanyPay;
import com.linuslan.oa.workflow.flows.invoice.model.Invoice;
import com.linuslan.oa.workflow.flows.invoice.dao.IInvoiceDao;

@Component("invoiceDao")
public class IInvoiceDaoImpl extends IBaseDaoImpl implements IInvoiceDao {
	
	private static Logger logger = Logger.getLogger(IInvoiceDaoImpl.class);
	
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
	public Page<Invoice> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<Invoice> pageData = null;
		List<Invoice> list = new ArrayList<Invoice> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM Invoice cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Invoice cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Invoice.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Invoice.class, hql.toString(), countHQL.toString(), paramMap);
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
			pageData = new Page<Invoice> (list, totalRecord, totalPage, page);
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
	public Page<Invoice> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Invoice> pageData = null;
		List<Invoice> list = new ArrayList<Invoice> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Invoice c WHERE c.isDelete=0 AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Invoice.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Invoice.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			//query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Invoice.class));
			//countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Invoice.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Invoice> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的开票
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Invoice> pageData = null;
		List<Invoice> list = new ArrayList<Invoice> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Invoice c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Invoice.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Invoice.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(Invoice.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Invoice.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Invoice> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询开票汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Invoice> pageData = null;
		List<Invoice> list = new ArrayList<Invoice> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Invoice c WHERE c.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Invoice.class, hql, paramMap, "c");
				if(CodeUtil.isNotEmpty(paramMap.get("payStatus"))) {
					if(!paramMap.get("payStatus").equals("4")) {
						subSQL += " AND c.id IN (SELECT wfId FROM AuditLog al WHERE al.wfType='invoice' AND al.isAudit=0 AND al.status = "+paramMap.get("payStatus")+")";
					} else {
						subSQL += " AND c.status = "+paramMap.get("payStatus");
					}
				}
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Invoice.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Invoice> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 统计汇总页面的开票总额，待回款总额，已回款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap) {
		Map<String, Object> sumMap = new HashMap<String, Object> ();
		BigDecimal sumTotal = null;
		BigDecimal sumRestream = null;
		BigDecimal sumUnRestream = null;
		try {
			//开票总额
			StringBuffer sumTotalSQL = new StringBuffer("SELECT NVL(SUM(cp.invoiceMoney), 0) FROM Invoice cp WHERE cp.isDelete=0 AND cp.status = 3 AND cp.invoiceStatus NOT IN (5, 6)");
			
			//已回款总额
			StringBuffer sumRestreamSQL = new StringBuffer("SELECT NVL(SUM(cp.actualMoney), 0) FROM Invoice cp WHERE cp.isDelete=0 AND cp.invoiceStatus NOT IN(5, 6)");
			
			if(null != paramMap) {
				String conditionSQL = this.getHQL(CompanyPay.class, sumTotalSQL, paramMap, "cp");
				sumTotalSQL.append(conditionSQL);
				sumRestreamSQL.append(conditionSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Invoice.class, sumTotalSQL.toString(), null, paramMap);
			Query sumTotalQuery = queryMap.get(ConstantVar.QUERY);
			queryMap = this.buildQuery(session, Invoice.class, sumRestreamSQL.toString(), null, paramMap);
			Query sumRestreamQuery = queryMap.get(ConstantVar.QUERY);
			sumTotal = (BigDecimal) sumTotalQuery.uniqueResult();
			sumRestream = (BigDecimal) sumRestreamQuery.uniqueResult();
			sumUnRestream = sumTotal.subtract(sumRestream);
		} catch(Exception ex) {
			ex.printStackTrace();
			sumTotal = new BigDecimal("0");
			sumRestream = new BigDecimal("0");
			sumUnRestream = new BigDecimal("0");
		} finally {
			sumMap.put("sumTotal", sumTotal);
			sumMap.put("sumRestream", sumRestream);
			sumMap.put("sumUnRestream", sumUnRestream);
		}
		
		return sumMap;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Invoice queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Invoice) session.get(Invoice.class, id);
	}
	
	/**
	 * 新增申请
	 * @param invoice
	 * @return
	 */
	public boolean add(Invoice invoice) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(invoice);
		success = true;
		return success;
	}
	
	/**
	 * 更新申请
	 * @param invoice
	 * @return
	 */
	public boolean update(Invoice invoice) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(invoice);
		success = true;
		return success;
	}
}
