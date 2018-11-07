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
@Table(name="workflow_audit_log")
public class AuditLog {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="workflowAuditLogSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="workflowAuditLogSeq", sequenceName="workflow_audit_log_seq")
	private Long id;
	
	/**
	 * 流程主键
	 */
	@Column(name="wf_id")
	private Long wfId;
	
	/**
	 * 流程类型，例如：invoice
	 */
	@Column(name="wf_type")
	private String wfType;
	
	/**
	 * 当前流程实例的状态:
	 * 0：保存，1：退回；2：撤销；3：审核中；4：审核结束
	 */
	@Column(name="status")
	private Integer status;
	
	/**
	 * 当前流程实例的上一个节点的状态
	 */
	@Column(name="last_status")
	private Integer lastStatus;
	
	/**
	 * 当前审核节点
	 * 存放的是节点的名称，例如：rect4,rect5
	 * 如果存放的是节点id，在流程设计器中重新保存会删除旧的节点id，则对应的id就取不到节点了
	 */
	@Column(name="node")
	private String node;
	
	/**
	 * 上一审核节点
	 */
	@Column(name="last_node")
	private String lastNode;
	
	/**
	 * 当前审核用户组
	 */
	@Column(name="auditor")
	private Long auditor;
	
	@Formula("(SELECT g.text FROM sys_group g WHERE g.id=auditor)")
	private String auditorName;
	
	/**
	 * 上一个审核用户组
	 */
	@Column(name="last_auditor")
	private Long lastAuditor;
	
	/**
	 * 是否已审
	 * 0：未审
	 * 1：已审
	 */
	@Column(name="is_audit")
	private int isAudit;
	
	@Column(name="is_read")
	private int isRead;
	
	/**
	 * 创建时间
	 */
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 审核时间
	 */
	@Column(name="audit_date")
	private Date auditDate;
	
	/**
	 * 审核人
	 */
	@Column(name="audit_user")
	private Long auditUser;
	
	@Column(name="pass_btn")
	private int passBtn;
	
	@Column(name="reject_btn")
	private int rejectBtn;
	
	@Column(name="operate")
	private Integer operate;
	
	/**
	 * 归属于哪个流程
	 * 可从此处查出未跑完的流程实例还有多少个
	 */
	@Column(name="flow_id")
	private Long flowId;
	
	/**
	 * 审核人类型
	 * leader: 领导审核
	 * self: 用户自己审核
	 * 在获取审核列表的时候，要加上这个条件
	 */
	@Column(name="auditor_type")
	private String auditorType="leader";
	
	/**
	 * 审核人的审核意见
	 */
	@Column(name="opinion")
	private String opinion;
	
	@Column(name="is_print")
	private int isPrint;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(Integer lastStatus) {
		this.lastStatus = lastStatus;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getLastNode() {
		return lastNode;
	}

	public void setLastNode(String lastNode) {
		this.lastNode = lastNode;
	}

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public Long getLastAuditor() {
		return lastAuditor;
	}

	public void setLastAuditor(Long lastAuditor) {
		this.lastAuditor = lastAuditor;
	}

	public int getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(int isAudit) {
		this.isAudit = isAudit;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Long getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(Long auditUser) {
		this.auditUser = auditUser;
	}

	public int getPassBtn() {
		return passBtn;
	}

	public void setPassBtn(int passBtn) {
		this.passBtn = passBtn;
	}

	public int getRejectBtn() {
		return rejectBtn;
	}

	public void setRejectBtn(int rejectBtn) {
		this.rejectBtn = rejectBtn;
	}

	public Integer getOperate() {
		return operate;
	}

	public void setOperate(Integer operate) {
		this.operate = operate;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public int getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(int isPrint) {
		this.isPrint = isPrint;
	}
}