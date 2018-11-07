package com.linuslan.oa.workflow.engine.auditlog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="workflow_audit_user")
public class AuditUser {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="workflowAuditUserSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="workflowAuditUserSeq", sequenceName="workflow_audit_user_seq")
	private Long id;
	
	@Column(name="audit_log_id")
	private Long auditLogId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="user_id")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuditLogId() {
		return auditLogId;
	}

	public void setAuditLogId(Long auditLogId) {
		this.auditLogId = auditLogId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}