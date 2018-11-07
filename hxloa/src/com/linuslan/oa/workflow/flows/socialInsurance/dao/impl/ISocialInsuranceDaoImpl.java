package com.linuslan.oa.workflow.flows.socialInsurance.dao.impl;

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
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.socialInsurance.dao.ISocialInsuranceDao;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsurance;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsuranceContent;

@Component("socialInsuranceDao")
public class ISocialInsuranceDaoImpl extends IBaseDaoImpl implements
		ISocialInsuranceDao {
	private static Logger logger = Logger.getLogger(ISocialInsuranceDaoImpl.class);
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
	public Page<SocialInsurance> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<SocialInsurance> pageData = null;
		List<SocialInsurance> list = new ArrayList<SocialInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM SocialInsurance cp WHERE cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM SocialInsurance cp WHERE cp.isDelete=0");
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
			pageData = new Page<SocialInsurance> (list, totalRecord, totalPage, page);
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
	public Page<SocialInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<SocialInsurance> pageData = null;
		List<SocialInsurance> list = new ArrayList<SocialInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM SocialInsurance c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = session.createQuery(countHQL.toString());
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(SocialInsurance.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(SocialInsurance.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<SocialInsurance> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的社保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<SocialInsurance> pageData = null;
		List<SocialInsurance> list = new ArrayList<SocialInsurance> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM SocialInsurance c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = session.createQuery(countHQL.toString());
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(SocialInsurance.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(SocialInsurance.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<SocialInsurance> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SocialInsurance queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (SocialInsurance) session.get(SocialInsurance.class, id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的社保
	 * @param date
	 * @return
	 */
	public SocialInsurance queryByDate(String date, Long insuranceId) {
		String hql = "FROM SocialInsurance a WHERE TO_DATE(a.year||'-'||a.month, 'yyyy-mm')=TO_DATE(:date, 'yyyy-mm') AND a.status=4";
		if(null != insuranceId) {
			hql += " AND a.id <> :insuranceId";
		}
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameter("date", date);
		if(null != insuranceId) {
			query.setParameter("insuranceId", insuranceId);
		}
		List<SocialInsurance> list = query.list();
		if(null != list && 0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过主表的id查询项目
	 * @param id
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsBySocialInsuranceId(Long id) {
		List<SocialInsuranceContent> contents = new ArrayList<SocialInsuranceContent> ();
		if(null != id) {
			String hql = "FROM SocialInsuranceContent rc WHERE rc.insuranceId=:insuranceId AND rc.isDelete=0 ORDER BY rc.orderNo ASC, rc.id ASC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("insuranceId", id);
			contents.addAll(query.list());
		}
		return contents;
	}
	
	/**
	 * 通过id查询项目
	 * @param ids
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsInIds(List<Long> ids) {
		List<SocialInsuranceContent> contents = new ArrayList<SocialInsuranceContent> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM SocialInsuranceContent rc WHERE rc.id IN (:ids)";
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
	public boolean add(SocialInsurance insurance) {
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
	public boolean mergeContents(List<SocialInsuranceContent> contents) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<SocialInsuranceContent> iter = contents.iterator();
		SocialInsuranceContent content = null;
		while(iter.hasNext()) {
			content = iter.next();
			session.merge(content);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param insurance
	 * @return
	 */
	public boolean update(SocialInsurance insurance) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(insurance);
		success = true;
		return success;
	}
	
	/**
	 * 删除项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long insuranceId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SocialInsuranceContent rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.insuranceId=:insuranceId";
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
		String hql = "UPDATE SocialInsuranceContent rc SET rc.isDelete=1 WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 删除项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE SocialInsuranceContent rc SET rc.isDelete=1 WHERE rc.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
}
