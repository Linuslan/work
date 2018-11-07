package com.linuslan.oa.workflow.flows.workOvertime.dao.impl;

import java.util.ArrayList;
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
import com.linuslan.oa.workflow.flows.workOvertime.model.WorkOvertime;
import com.linuslan.oa.workflow.flows.workOvertime.dao.IWorkOvertimeDao;

@Component("workOvertimeDao")
public class IWorkOvertimeDaoImpl extends IBaseDaoImpl implements
		IWorkOvertimeDao {
	private static Logger logger = Logger.getLogger(IWorkOvertimeDaoImpl.class);
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
	public Page<WorkOvertime> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<WorkOvertime> pageData = null;
		List<WorkOvertime> list = new ArrayList<WorkOvertime> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			StringBuffer hql = new StringBuffer("FROM WorkOvertime cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM WorkOvertime cp WHERE cp.userId=:loginUserId AND cp.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(WorkOvertime.class, hql, paramMap, "cp");
				hql.append(subSQL);
				countHQL.append(subSQL);
			}
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, WorkOvertime.class, hql.toString(), countHQL.toString(), paramMap);
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
			pageData = new Page<WorkOvertime> (list, totalRecord, totalPage, page);
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
	public Page<WorkOvertime> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<WorkOvertime> pageData = null;
		List<WorkOvertime> list = new ArrayList<WorkOvertime> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			//从session中获取登录用户的所有用户组id
			List<Long> groupIds = HttpUtil.getLoginUserGroupIds();
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM WorkOvertime c WHERE c.isDelete=0 AND c.userId<>:loginUserId AND c.id IN ("+this.auditSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(WorkOvertime.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, WorkOvertime.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameterList("groupIds", groupIds);
			query.setParameter("wfType", CodeUtil.getClassName(WorkOvertime.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameterList("groupIds", groupIds);
			countQuery.setParameter("wfType", CodeUtil.getClassName(WorkOvertime.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<WorkOvertime> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询登陆用户审核过的加班
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<WorkOvertime> pageData = null;
		List<WorkOvertime> list = new ArrayList<WorkOvertime> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM WorkOvertime c WHERE c.isDelete=0 AND c.id IN ("+this.auditedSQL+")");
			if(null != paramMap) {
				String subSQL = this.getHQL(WorkOvertime.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, WorkOvertime.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("loginUserId", userId);
			query.setParameter("wfType", CodeUtil.getClassName(WorkOvertime.class));
			countQuery.setParameter("loginUserId", userId);
			countQuery.setParameter("wfType", CodeUtil.getClassName(WorkOvertime.class));
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<WorkOvertime> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询加班汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		
		Page<WorkOvertime> pageData = null;
		List<WorkOvertime> list = new ArrayList<WorkOvertime> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			Long userId = HttpUtil.getLoginUser().getId();
			
			StringBuffer hql = new StringBuffer("FROM WorkOvertime c WHERE c.isDelete=0");
			if(null != paramMap) {
				String subSQL = this.getHQL(WorkOvertime.class, hql, paramMap, "c");
				hql.append(subSQL);
			}
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) ");
			countHQL.append(hql.toString());
			Session session = this.sessionFactory.getCurrentSession();
			Map<String, Query> queryMap = this.buildQuery(session, WorkOvertime.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			pageData = new Page<WorkOvertime> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 通过id查询申请
	 * @param id
	 * @return
	 */
	public WorkOvertime queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (WorkOvertime) session.get(WorkOvertime.class, id);
	}
	
	/**
	 * 新增申请
	 * @param workOvertime
	 * @return
	 */
	public boolean add(WorkOvertime workOvertime) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(workOvertime);
		success = true;
		return success;
	}
	
	/**
	 * 更新申请
	 * @param workOvertime
	 * @return
	 */
	public boolean update(WorkOvertime workOvertime) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(workOvertime);
		success = true;
		return success;
	}
}
