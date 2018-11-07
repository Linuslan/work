package com.linuslan.oa.workflow.engine.auditlog.service;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;

public interface IAuditLogService extends IBaseService {

	/**
	 * 分页查询审核意见记录
	 * @param wfId
	 * @param wfType
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<AuditorOpinion> queryOpinionPage(Long wfId, String wfType, int page, int rows);
	
	/**
	 * 添加审核意见
	 * @param opinion
	 * @return
	 */
	public boolean addOpinion(AuditorOpinion opinion);
	
	/**
	 * 更新登录用户的已阅
	 * @param type
	 * @return
	 */
	public boolean updateAuditLogByWfType(String type);
}
