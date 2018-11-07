package com.linuslan.oa.workflow.engine.auditlog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;

@Component("auditLogDao")
public class IAuditLogDaoImpl extends IBaseServiceImpl implements IAuditLogDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 分页查询审核意见记录
	 * @param wfId
	 * @param wfType
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<AuditorOpinion> queryOpinionPage(Long wfId, String wfType, int page, int rows) {
		Page<AuditorOpinion> pageData = null;
		List<AuditorOpinion> list = new ArrayList<AuditorOpinion> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			if(null == wfId || null == wfType || "".equals(wfType.trim())) {
				CodeUtil.throwRuntimeExcep("流程实例的id和流程类型不能为空");
			}
			String hql = "FROM AuditorOpinion ao WHERE ao.wfId=:wfId AND ao.wfType=:wfType AND ao.isDelete=0";
			String countHQL = "SELECT COUNT(*) "+hql;
			hql += " ORDER BY ao.auditDate DESC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			Query countQuery = session.createQuery(countHQL);
			query.setParameter("wfId", wfId);
			query.setParameter("wfType", wfType);
			countQuery.setParameter("wfId", wfId);
			countQuery.setParameter("wfType", wfType);
			list.addAll(query.list());
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep("查询异常，异常原因："+ex.getMessage());
		} finally {
			pageData = new Page<AuditorOpinion> (list, totalRecord, totalPage, page);
		}
 		return pageData;
	}
	
	/**
	 * 添加审核意见
	 * @param opinion
	 * @return
	 */
	public boolean addOpinion(AuditorOpinion opinion) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(opinion);
		success = true;
		return success;
	}
	
	/**
	 * 更新登录用户的已阅
	 * @param type
	 * @return
	 */
	public boolean updateAuditLogByWfType(String type) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE AuditLog t SET t.isRead=1 WHERE t.wfType=:wfType AND t.id IN (SELECT t2.auditLogId FROM AuditUser t2 WHERE t2.userId=:userId)";
		Query query = session.createQuery(hql);
		query.setParameter("wfType", type);
		query.setParameter("userId", HttpUtil.getLoginUser().getId());
		query.executeUpdate();
		success = true;
		return success;
	}
}
