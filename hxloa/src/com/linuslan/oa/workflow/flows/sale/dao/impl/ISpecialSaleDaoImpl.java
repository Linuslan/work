package com.linuslan.oa.workflow.flows.sale.dao.impl;

import java.util.ArrayList;
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
import com.linuslan.oa.workflow.flows.sale.dao.ISpecialSaleDao;
import com.linuslan.oa.workflow.flows.sale.model.Sale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSaleContent;

@Component("specialSaleDao")
public class ISpecialSaleDaoImpl extends IBaseDaoImpl implements
		ISpecialSaleDao {
	
	private static Logger logger = Logger.getLogger(ISpecialSaleDaoImpl.class);
	
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
	public Page<SpecialSale> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<SpecialSale> pageData = null;
		List<SpecialSale> list = new ArrayList<SpecialSale> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM SpecialSale cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM SpecialSale cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(SpecialSale.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, SpecialSale.class, hql.toString(), countHQL.toString(), paramMap);
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
			pageData = new Page<SpecialSale> (list, totalRecord, totalPage, page);
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
	public Page<SpecialSale> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<SpecialSale> pageData = null;
		List<SpecialSale> list = new ArrayList<SpecialSale> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM SpecialSale c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(SpecialSale.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, SpecialSale.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(SpecialSale.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(SpecialSale.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<SpecialSale> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的特殊销售
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<SpecialSale> pageData = null;
		List<SpecialSale> list = new ArrayList<SpecialSale> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM SpecialSale c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(SpecialSale.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, SpecialSale.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(SpecialSale.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(SpecialSale.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<SpecialSale> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SpecialSale queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (SpecialSale) session.get(SpecialSale.class, id);
	}
	
	/**
	 * 通过华夏蓝销售订单主表的id查询华夏蓝销售订单项目
	 * @param id
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsBySpecialSaleId(Long id) {
		List<SpecialSaleContent> contents = new ArrayList<SpecialSaleContent> ();
		if(null != id) {
			String hql = "FROM SpecialSaleContent rc WHERE rc.specialSaleId=:specialSaleId";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("specialSaleId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询华夏蓝销售订单项目
	 * @param ids
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsInIds(List<Long> ids) {
		List<SpecialSaleContent> contents = new ArrayList<SpecialSaleContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM SpecialSaleContent rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 新增申请单
	 * @param specialSale
	 * @return
	 */
	public boolean add(SpecialSale specialSale) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(specialSale);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新华夏蓝销售订单项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SpecialSaleContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<SpecialSaleContent> iter = contents.iterator();
		SpecialSaleContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新华夏蓝销售订单
	 * @param specialSale
	 * @return
	 */
	public boolean update(SpecialSale specialSale) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(specialSale);
		success = true;
		return success;
	}
	
	/**
	 * 删除华夏蓝销售订单项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SpecialSaleContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.specialSaleId=:id";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除华夏蓝销售订单项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SpecialSaleContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
}
