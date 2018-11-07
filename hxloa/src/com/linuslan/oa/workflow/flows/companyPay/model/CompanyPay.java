package com.linuslan.oa.workflow.flows.companyPay.model;

import java.math.BigDecimal;
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
@Table(name="wf_company_pay")
public class CompanyPay extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCompanyPaySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCompanyPaySeq", sequenceName="wf_company_pay_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	/**
	 * 费用承担部门
	 */
	@Column(name="pay_dept_id")
	private Long payDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=pay_dept_id)")
	private String payDeptName;
	
	/**
	 * 费用承担公司
	 */
	@Column(name="pay_company_id")
	private Long payCompanyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=pay_company_id)")
	private String payCompanyName;
	
	/**
	 * 费用产生时间
	 */
	@Column(name="money_date")
	private Date moneyDate;
	
	/**
	 * 付款时间
	 */
	@Column(name="pay_date")
	private Date payDate;
	
	/**
	 * 付款项目
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 付款金额
	 */
	@Column(name="money")
	private BigDecimal money;
	
	/**
	 * 付款方式
	 * 0：银行转账
	 * 1：现金
	 */
	@Column(name="pay_type")
	private Integer payType;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=pay_type)")
	private String payTypeName;
	
	/**
	 * 收款方开户行
	 */
	@Column(name="bank")
	private String bank;
	
	/**
	 * 收款方名称
	 */
	@Column(name="receiver")
	private String receiver;
	
	/**
	 * 收款方银行账号
	 */
	@Column(name="bank_no")
	private String bankNo;
	
	/**
	 * 申请人所在部门
	 */
	@Column(name="user_dept_id")
	private Long userDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=user_dept_id)")
	private String userDeptName;
	
	/**
	 * 审核记录
	 * 如果没有加FetchMode.SUBSELECT，则如果是多个onetomany且fetch=FetchType.EAGER，则会报错
	 */
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='companyPay' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;

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

	public Long getPayDeptId() {
		return payDeptId;
	}

	public void setPayDeptId(Long payDeptId) {
		this.payDeptId = payDeptId;
	}

	public String getPayDeptName() {
		return payDeptName;
	}

	public void setPayDeptName(String payDeptName) {
		this.payDeptName = payDeptName;
	}

	public Long getPayCompanyId() {
		return payCompanyId;
	}

	public void setPayCompanyId(Long payCompanyId) {
		this.payCompanyId = payCompanyId;
	}

	public String getPayCompanyName() {
		return payCompanyName;
	}

	public void setPayCompanyName(String payCompanyName) {
		this.payCompanyName = payCompanyName;
	}

	public Date getMoneyDate() {
		return moneyDate;
	}

	public void setMoneyDate(Date moneyDate) {
		this.moneyDate = moneyDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public Long getUserDeptId() {
		return userDeptId;
	}

	public void setUserDeptId(Long userDeptId) {
		this.userDeptId = userDeptId;
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

	public String getUserDeptName() {
		return userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
}
