package com.linuslan.oa.workflow.engine.auditlog.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="workflow_auditor_opinion")
public class AuditorOpinion {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="workflowAuditorOpinionSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="workflowAuditorOpinionSeq", sequenceName="workflow_auditor_opinion_seq")
	private Long id;
	
	@Column(name="wf_id")
	private Long wfId;
	
	@Column(name="wf_type")
	private String wfType;
	
	@Column(name="auditor")
	private Long auditor;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=auditor)")
	private String auditorName;
	
	@Column(name="audit_date")
	private Date auditDate;
	
	@Column(name="opinion")
	private String opinion;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="pass_type")
	private Integer passType=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getPassType() {
		return passType;
	}

	public void setPassType(Integer passType) {
		this.passType = passType;
	}
	
}
