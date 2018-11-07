package com.linuslan.oa.workflow.flows.workOvertime.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.linuslan.oa.common.BaseFlow;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

@Entity
@Table(name="wf_work_overtime")
public class WorkOvertime extends BaseFlow {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfWorkOvertimeSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfWorkOvertimeSeq", sequenceName="wf_work_overtime_seq")
	private Long id;
	
	@Column(name="class_id")
	private Long classId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=class_id)")
	private String className;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="duration")
	private Double duration;
	
	@Column(name="content")
	private String content;
	
	/**
	 * 审核记录
	 * 如果没有加FetchMode.SUBSELECT，则如果是多个onetomany且fetch=FetchType.EAGER，则会报错
	 */
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='workOvertime' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
	@Formula("(SELECT t.name FROM sys_department t, sys_user t2 WHERE t.id=t2.department_id AND t2.id=user_id)")
	private String userDeptName;
	
	public List<AuditLog> getAuditLogs() {
		Map<String, Boolean> map = EngineUtil.checkAuditBtn(this.auditLogs);
		this.passBtn = map.get("passBtn");
		this.rejectBtn = map.get("rejectBtn");
		return auditLogs;
	}

	public void setAuditLogs(List<AuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public String getAuditors() {
		this.auditors = EngineUtil.getAuditors(auditLogs);
		return auditors;
	}

	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}
	
	public Integer getFlowStatus() {
		this.flowStatus = EngineUtil.getStatus(auditLogs);
		return this.flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserDeptName() {
		return userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}
}
