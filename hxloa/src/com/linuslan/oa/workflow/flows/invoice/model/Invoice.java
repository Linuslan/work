package com.linuslan.oa.workflow.flows.invoice.model;

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
@Table(name="wf_invoice")
public class Invoice extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfInvoiceSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfInvoiceSeq", sequenceName="wf_invoice_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="title")
	private String title;
	
	@Column(name="user_dept_id")
	private Long userDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=user_dept_id)")
	private String userDeptName;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	/**
	 * 开票时间
	 */
	@Column(name="invoice_date")
	private Date invoiceDate;
	
	/**
	 * 开票金额
	 */
	@Column(name="invoice_money")
	private BigDecimal invoiceMoney;
	
	/**
	 * 预计回款金额
	 */
	@Column(name="supposed_money")
	private BigDecimal supposedMoney;
	
	/**
	 * 实回款金额
	 */
	@Column(name="actual_money")
	private BigDecimal actualMoney;
	
	@Column(name="content")
	private String content;
	
	@Column(name="remark")
	private String remark;
	
	/**
	 * 开票类型，对应字典表的主键id
	 */
	@Column(name="invoice_type")
	private Integer invoiceType;
	
	@Formula("(SELECT d.text FROM sys_dictionary d WHERE d.id=invoice_type)")
	private String invoiceTypeName;
	
	/**
	 * 开票状态
	 * 0：已回款；
	 * 1：部分回款
	 * 2：未回款
	 * 5：未开票作废
	 * 6：已开票作废
	 */
	@Column(name="invoice_status")
	private Long invoiceStatus=2l;
	
	/**
	 * 确认开票人
	 */
	@Column(name="invoice_user_id")
	private Long invoiceUserId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=invoice_user_id)")
	private String invoiceUserName;
	
	@Column(name="confirm_invoice_date")
	private Date confirmInvoiceDate;
	
	@Column(name="finance_info")
	private String financeInfo;
	
	/**
	 * 确认回款人
	 */
	@Column(name="confirm_restream_user_id")
	private Long confirmRestreamUserId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=confirm_restream_user_id)")
	private String confirmRestreamUserName;
	
	/**
	 * 确认回款时间
	 */
	@Column(name="confirm_restream_date")
	private Date confirmRestreamDate;
	
	/**
	 * 收入归属部门
	 */
	@Column(name="income_dept_id")
	private Long incomeDeptId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=income_dept_id)")
	private String incomeDeptName;
	
	/**
	 * 纳税人识别号
	 */
	@Column(name="tax_payer_id")
	private String taxPayerId;
	
	/**
	 * 地址
	 */
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="bank")
	private String bank;
	
	@Column(name="bank_no")
	private String bankNo;
	
	/**
	 * 预计回款时间
	 */
	@Column(name="plan_restream_date")
	private Date planRestreamDate;
	
	/**
	 * 审核记录
	 * 如果没有加FetchMode.SUBSELECT，则如果是多个onetomany且fetch=FetchType.EAGER，则会报错
	 */
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='invoice' AND is_audit=0")
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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceMoney() {
		return invoiceMoney;
	}

	public void setInvoiceMoney(BigDecimal invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}

	public BigDecimal getSupposedMoney() {
		return supposedMoney;
	}

	public void setSupposedMoney(BigDecimal supposedMoney) {
		this.supposedMoney = supposedMoney;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
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

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Long getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Long invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getInvoiceUserId() {
		return invoiceUserId;
	}

	public void setInvoiceUserId(Long invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}

	public String getInvoiceUserName() {
		return invoiceUserName;
	}

	public void setInvoiceUserName(String invoiceUserName) {
		this.invoiceUserName = invoiceUserName;
	}

	public Date getConfirmInvoiceDate() {
		return confirmInvoiceDate;
	}

	public void setConfirmInvoiceDate(Date confirmInvoiceDate) {
		this.confirmInvoiceDate = confirmInvoiceDate;
	}

	public String getFinanceInfo() {
		return financeInfo;
	}

	public void setFinanceInfo(String financeInfo) {
		this.financeInfo = financeInfo;
	}

	public Long getConfirmRestreamUserId() {
		return confirmRestreamUserId;
	}

	public void setConfirmRestreamUserId(Long confirmRestreamUserId) {
		this.confirmRestreamUserId = confirmRestreamUserId;
	}

	public String getConfirmRestreamUserName() {
		return confirmRestreamUserName;
	}

	public void setConfirmRestreamUserName(String confirmRestreamUserName) {
		this.confirmRestreamUserName = confirmRestreamUserName;
	}

	public Date getConfirmRestreamDate() {
		return confirmRestreamDate;
	}

	public void setConfirmRestreamDate(Date confirmRestreamDate) {
		this.confirmRestreamDate = confirmRestreamDate;
	}

	public Long getIncomeDeptId() {
		return incomeDeptId;
	}

	public void setIncomeDeptId(Long incomeDeptId) {
		this.incomeDeptId = incomeDeptId;
	}

	public String getIncomeDeptName() {
		return incomeDeptName;
	}

	public void setIncomeDeptName(String incomeDeptName) {
		this.incomeDeptName = incomeDeptName;
	}

	public String getTaxPayerId() {
		return taxPayerId;
	}

	public void setTaxPayerId(String taxPayerId) {
		this.taxPayerId = taxPayerId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Date getPlanRestreamDate() {
		return planRestreamDate;
	}

	public void setPlanRestreamDate(Date planRestreamDate) {
		this.planRestreamDate = planRestreamDate;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}
	
}
