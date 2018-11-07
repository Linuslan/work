package com.linuslan.oa.workflow.flows.purchase.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.linuslan.oa.workflow.flows.leave.model.Leave;
import com.linuslan.oa.workflow.flows.purchase.dao.impl.IPurchaseDaoImpl;
import com.linuslan.oa.workflow.flows.purchase.model.Purchase;
import com.linuslan.oa.workflow.flows.purchase.model.PurchaseContent;
import com.linuslan.oa.workflow.flows.purchase.dao.IPurchaseDao;

@Component("purchaseDao")
public class IPurchaseDaoImpl extends IBaseDaoImpl implements IPurchaseDao {

	private static Logger logger = Logger.getLogger(IPurchaseDaoImpl.class);
	
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
	public Page<Purchase> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<Purchase> pageData = null;
		List<Purchase> list = new ArrayList<Purchase> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM Purchase cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Purchase cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(Purchase.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Purchase.class, hql.toString(), countHQL.toString(), paramMap);
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
			pageData = new Page<Purchase> (list, totalRecord, totalPage, page);
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
	public Page<Purchase> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Purchase> pageData = null;
		List<Purchase> list = new ArrayList<Purchase> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Purchase c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Purchase.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Purchase.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(Purchase.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Purchase.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Purchase> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的采购
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<Purchase> pageData = null;
		List<Purchase> list = new ArrayList<Purchase> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM Purchase c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(Purchase.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, Purchase.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(Purchase.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(Purchase.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<Purchase> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Purchase queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Purchase) session.get(Purchase.class, id);
	}
	
	/**
	 * 通过采购主表的id查询采购项目
	 * @param id
	 * @return
	 */
	public List<PurchaseContent> queryContentsByPurchaseId(Long id) {
		List<PurchaseContent> contents = new ArrayList<PurchaseContent> ();
		if(null != id) {
			String hql = "FROM PurchaseContent rc WHERE rc.purchaseId=:purchaseId AND rc.isDelete=0";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("purchaseId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询采购项目
	 * @param ids
	 * @return
	 */
	public List<PurchaseContent> queryContentsInIds(List<Long> ids) {
		List<PurchaseContent> contents = new ArrayList<PurchaseContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM PurchaseContent rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 新增申请单
	 * @param purchase
	 * @return
	 */
	public boolean add(Purchase purchase) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(purchase);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新采购项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<PurchaseContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<PurchaseContent> iter = contents.iterator();
		PurchaseContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新采购
	 * @param purchase
	 * @return
	 */
	public boolean update(Purchase purchase) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(purchase);
		success = true;
		return success;
	}
	
	/**
	 * 删除采购项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long purchaseId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE PurchaseContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.purchaseId=:purchaseId";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("purchaseId", purchaseId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除采购项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE PurchaseContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
}
