package com.linuslan.oa.workflow.engine.auditlog.action;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.auditlog.service.IAuditLogService;

@Controller
public class AuditLogAction extends BaseAction {
	
	@Autowired
	private IAuditLogService auditLogService;
	
	Page<AuditorOpinion> opinionPage;
	
	private Long wfId;
	
	private String wfType;
	
	/**
	 * 查询审核意见列表
	 */
	public void queryOpinionPage() {
		try {
			opinionPage = this.auditLogService.queryOpinionPage(wfId, wfType, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject json = JSONObject.fromObject(opinionPage, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 更新登录用户的已阅
	 * @param type
	 * @return
	 */
	public void updateAuditLogByWfType() {
		try {
			if(this.auditLogService.updateAuditLogByWfType(this.wfType)) {
				this.setupSuccessMap("更新已阅成功");
			} else {
				this.setupFailureMap("更新已阅失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap("更新已阅异常");
		}
		this.printResultMap();
	}

	public Page<AuditorOpinion> getOpinionPage() {
		return opinionPage;
	}

	public void setOpinionPage(Page<AuditorOpinion> opinionPage) {
		this.opinionPage = opinionPage;
	}

	public Long getWfId() {
		return wfId;
	}

	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}

	public String getWfType() {
		return wfType;
	}

	public void setWfType(String wfType) {
		this.wfType = wfType;
	}
}
