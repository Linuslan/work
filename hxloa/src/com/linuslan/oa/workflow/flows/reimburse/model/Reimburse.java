package com.linuslan.oa.workflow.flows.reimburse.model;

import java.math.BigDecimal;
import java.util.ArrayList;
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
@Table(name="wf_reimburse")
public class Reimburse extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfReimburseSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfReimburseSeq", sequenceName="wf_reimburse_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 流程状态，即所画的流程图中配置的状态
	 */
	@Transient
	private Integer flowStatus;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	/**
	 * 申请人所在部门
	 */
	@Column(name="user_dept_id")
	private Long userDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=user_dept_id)")
	private String userDeptName;
	
	/**
	 * 报销部门
	 */
	@Column(name="reimburse_dept_id")
	private Long reimburseDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=reimburse_dept_id)")
	private String reimburseDeptName;
	
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="bank")
	private String bank;
	
	@Column(name="bank_no")
	private String bankNo;
	
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='reimburse' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	@Formula("(SELECT SUM(t.money) FROM wf_reimburse_content t WHERE t.reimburse_id=id AND t.is_delete=0)")
	private BigDecimal totalMoney;
	
	@Transient
	private List<ReimburseContent> contents = new ArrayList<ReimburseContent> ();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getFlowStatus() {
		this.flowStatus = EngineUtil.getStatus(auditLogs);
		return this.flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Long getUserDeptId() {
		return userDeptId;
	}

	public void setUserDeptId(Long userDeptId) {
		this.userDeptId = userDeptId;
	}

	public String getUserDeptName() {
		return userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}

	public Long getReimburseDeptId() {
		return reimburseDeptId;
	}

	public void setReimburseDeptId(Long reimburseDeptId) {
		this.reimburseDeptId = reimburseDeptId;
	}

	public String getReimburseDeptName() {
		return reimburseDeptName;
	}

	public void setReimburseDeptName(String reimburseDeptName) {
		this.reimburseDeptName = reimburseDeptName;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public List<AuditLog> getAuditLogs() {
		Map<String, Boolean> map = EngineUtil.checkAuditBtn(this.auditLogs);
		this.passBtn = map.get("passBtn");
		this.rejectBtn = map.get("rejectBtn");
		this.print = map.get("print");
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

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<ReimburseContent> getContents() {
		return contents;
	}

	public void setContents(List<ReimburseContent> contents) {
		this.contents = contents;
	}
}
