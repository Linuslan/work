package com.linuslan.oa.workflow.engine.auditlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.auditlog.service.IAuditLogService;
import com.opensymphony.xwork2.interceptor.annotations.Allowed;

@Component("auditLogService")
@Transactional
public class IAuditLogServiceImpl extends IBaseServiceImpl implements
		IAuditLogService {
	@Autowired
	private IAuditLogDao auditLogDao;
	
	/**
	 * 分页查询审核意见记录
	 * @param wfId
	 * @param wfType
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<AuditorOpinion> queryOpinionPage(Long wfId, String wfType, int page, int rows) {
		return this.auditLogDao.queryOpinionPage(wfId, wfType, page, rows);
	}
	
	/**
	 * 添加审核意见
	 * @param opinion
	 * @return
	 */
	public boolean addOpinion(AuditorOpinion opinion) {
		return this.auditLogDao.addOpinion(opinion);
	}
	
	/**
	 * 更新登录用户的已阅
	 * @param type
	 * @return
	 */
	public boolean updateAuditLogByWfType(String type) {
		return this.auditLogDao.updateAuditLogByWfType(type);
	}
}
