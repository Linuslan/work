package com.linuslan.oa.workflow.flows.sale.model;

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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.linuslan.oa.common.BaseFlow;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

@Entity
@Table(name="wf_sale")
public class Sale extends BaseFlow {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSaleSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSaleSeq", sequenceName="wf_sale_seq")
	private Long id;
	
	/**
	 * 系统自动生成
	 */
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 用户手动填写
	 */
	@Column(name="order_serial_no")
	private String orderSerialNo;
	
	/**
	 * 归属客户
	 */
	@Column(name="customer_id")
	private Long customerId;
	
	@Formula("(SELECT t.name FROM wf_customer t WHERE t.id=customer_id)")
	private String customerName;
	
	@Column(name="sale_date")
	private Date saleDate;
	
	/**
	 * 联系电话
	 */
	@Column(name="telephone")
	private String telephone;
	
	/**
	 * 对应客户接收人的主键id
	 */
	@Column(name="receiver_id")
	private Long receiverId;
	
	@Column(name="receiver")
	private String receiver;
	
	/**
	 * 收货人联系电话
	 */
	@Column(name="receiver_phone")
	private String receiverPhone;
	
	/**
	 * 邮编
	 */
	@Column(name="zip_code")
	private String zipCode;
	
	/**
	 * 收货地址
	 */
	@Column(name="receiver_address")
	private String receiverAddress;
	
	/**
	 * 是否开票
	 * 0：否
	 * 1：是
	 */
	@Column(name="is_invoice")
	private Integer isInvoice;
	
	@Column(name="invoice_money")
	private BigDecimal invoiceMoney;
	
	@Column(name="supposed_money")
	private BigDecimal supposedMoney;
	
	@Column(name="actual_money")
	private BigDecimal actualMoney;
	
	@Column(name="income_department_id")
	private Long incomeDepartmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=income_department_id)")
	private String incomeDepartmentName;
	
	@Column(name="invoice_address")
	private String invoiceAddress;
	
	@Column(name="invoice_phone")
	private String invoicePhone;
	
	@Column(name="invoice_bank")
	private String invoiceBank;
	
	@Column(name="invoice_bank_no")
	private String invoiceBankNo;
	
	@Column(name="plan_restream_date")
	private Date planRestreamDate;
	
	@Column(name="invoice_type_id")
	private Long invoiceTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=invoice_type_id)")
	private String invoiceTypeName;
	
	@Column(name="invoice_content")
	private String invoiceContent;
	
	@Column(name="tax_payer_id")
	private String taxPayerId;
	
	@Column(name="invoice_status")
	private Integer invoiceStatus;
	
	@Column(name="restream_status")
	private Integer restreamStatus;
	
	@Column(name="checkout_status")
	private Integer checkoutStatus;
	
	@Column(name="invoice_user_id")
	private Long invoiceUserId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=invoice_user_id)")
	private String invoiceUserName;
	
	@Column(name="confirm_invoice_date")
	private Date confirmInvoiceDate;
	
	@Column(name="finance_info")
	private String financeInfo;
	
	@Column(name="stockman_info")
	private String stockmanInfo;
	
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
	
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='sale' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOrderSerialNo() {
		return orderSerialNo;
	}

	public void setOrderSerialNo(String orderSerialNo) {
		this.orderSerialNo = orderSerialNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
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

	public Long getIncomeDepartmentId() {
		return incomeDepartmentId;
	}

	public void setIncomeDepartmentId(Long incomeDepartmentId) {
		this.incomeDepartmentId = incomeDepartmentId;
	}

	public String getIncomeDepartmentName() {
		return incomeDepartmentName;
	}

	public void setIncomeDepartmentName(String incomeDepartmentName) {
		this.incomeDepartmentName = incomeDepartmentName;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getInvoicePhone() {
		return invoicePhone;
	}

	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}

	public String getInvoiceBank() {
		return invoiceBank;
	}

	public void setInvoiceBank(String invoiceBank) {
		this.invoiceBank = invoiceBank;
	}

	public String getInvoiceBankNo() {
		return invoiceBankNo;
	}

	public void setInvoiceBankNo(String invoiceBankNo) {
		this.invoiceBankNo = invoiceBankNo;
	}

	public Date getPlanRestreamDate() {
		return planRestreamDate;
	}

	public void setPlanRestreamDate(Date planRestreamDate) {
		this.planRestreamDate = planRestreamDate;
	}

	public Long getInvoiceTypeId() {
		return invoiceTypeId;
	}

	public void setInvoiceTypeId(Long invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getTaxPayerId() {
		return taxPayerId;
	}

	public void setTaxPayerId(String taxPayerId) {
		this.taxPayerId = taxPayerId;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Integer getRestreamStatus() {
		return restreamStatus;
	}

	public void setRestreamStatus(Integer restreamStatus) {
		this.restreamStatus = restreamStatus;
	}

	public Integer getCheckoutStatus() {
		return checkoutStatus;
	}

	public void setCheckoutStatus(Integer checkoutStatus) {
		this.checkoutStatus = checkoutStatus;
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

	public String getStockmanInfo() {
		return stockmanInfo;
	}

	public void setStockmanInfo(String stockmanInfo) {
		this.stockmanInfo = stockmanInfo;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
}
