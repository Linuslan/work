package com.linuslan.oa.workflow.flows.healthInsurance.dao.impl;

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
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.healthInsurance.dao.IHealthInsuranceDao;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsurance;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsuranceContent;
import com.linuslan.oa.workflow.flows.reimburse.dao.impl.IReimburseDaoImpl;

@Component("healthInsuranceDao")
public class IHealthInsuranceDaoImpl extends IBaseDaoImpl implements
		IHealthInsuranceDao {
	
	private static Logger logger = Logger.getLogger(IHealthInsuranceDaoImpl.class);
	
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
	public Page<HealthInsurance> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<HealthInsurance> pageData = null;
		List<HealthInsurance> list = new ArrayList<HealthInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM HealthInsurance cp WHERE cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM HealthInsurance cp WHERE cp.isDelete=0");
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = session.createQuery(countHQL.toString());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
			list.addAll(query.list());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<HealthInsurance> (list, totalRecord, totalPage, page);
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
	public Page<HealthInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<HealthInsurance> pageData = null;
		List<HealthInsurance> list = new ArrayList<HealthInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM HealthInsurance c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = session.createQuery(countHQL.toString());
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(HealthInsurance.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(HealthInsurance.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<HealthInsurance> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的医保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<HealthInsurance> pageData = null;
		List<HealthInsurance> list = new ArrayList<HealthInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM HealthInsurance c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = session.createQuery(countHQL.toString());
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(HealthInsurance.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(HealthInsurance.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<HealthInsurance> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public HealthInsurance queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (HealthInsurance) session.get(HealthInsurance.class, id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的医保
	 * @param date
	 * @return
	 */
	public HealthInsurance queryByDate(String date, Long insuranceId) {
		String hql = "FROM HealthInsurance a WHERE TO_DATE(a.year||'-'||a.month, 'yyyy-mm')=TO_DATE(:date, 'yyyy-mm') AND a.status=4";
		if(null != insuranceId) {
			hql += " AND a.id <> :insuranceId";
		}
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameter("date", date);
		if(null != insuranceId) {
			query.setParameter("insuranceId", insuranceId);
		}
		List<HealthInsurance> list = query.list();
		if(null != list && 0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<HealthInsuranceContent> queryContentsByHealthInsuranceId(Long id) {
		List<HealthInsuranceContent> contents = new ArrayList<HealthInsuranceContent> ();
		if(null != id) {
			String hql = "FROM HealthInsuranceContent rc WHERE rc.insuranceId=:insuranceId AND rc.isDelete=0 ORDER BY rc.orderNo ASC, rc.id ASC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("insuranceId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询报销项目
	 * @param ids
	 * @return
	 */
	public List<HealthInsuranceContent> queryContentsInIds(List<Long> ids) {
		List<HealthInsuranceContent> contents = new ArrayList<HealthInsuranceContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM HealthInsuranceContent rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 新增申请单
	 * @param insurance
	 * @return
	 */
	public boolean add(HealthInsurance insurance) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(insurance);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新报销项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<HealthInsuranceContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<HealthInsuranceContent> iter = contents.iterator();
		HealthInsuranceContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param insurance
	 * @return
	 */
	public boolean update(HealthInsurance insurance) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(insurance);
		success = true;
		return success;
	}
	
	/**
	 * 删除报销项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long insuranceId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE HealthInsuranceContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.insuranceId=:insuranceId";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("insuranceId", insuranceId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE HealthInsuranceContent rc SET rc.isDelete=1 WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
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
		String hql = "UPDATE HealthInsuranceContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
}
