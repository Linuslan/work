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
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.NumberUtil;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

/**
 * 特殊的销售流程，目前用于华夏蓝
 * @author LinusLan
 *
 */
@Entity
@Table(name="wf_special_sale")
public class SpecialSale extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSpecialSaleSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSpecialSaleSeq", sequenceName="wf_special_sale_seq")
	private Long id;
	
	/**
	 * 订单号
	 */
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 订单日期
	 */
	@Column(name="sale_date")
	private Date saleDate;
	
	/**
	 * 出货日期
	 * 根据甲方需求，2016年5月31号改版后添加字段
	 */
	@Column(name="checkout_date")
	private Date checkoutDate;
	
	/**
	 * 结款日期
	 */
	@Column(name="pay_date")
	private Date payDate;
	
	/**
	 * 付款方式，货到付款或者款到发货
	 * 关联字典表
	 */
	@Column(name="pay_type_id")
	private Long payTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=pay_type_id)")
	private String payTypeName;
	
	/**
	 * 送货方式
	 * 关联字典表
	 * 根据甲方需求，2016年5月31号改版后添加字段
	 */
	@Column(name="deliver_type_id")
	private Long deliverTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=deliver_type_id)")
	private String deliverTypeName;
	
	/**
	 * 总金额
	 * 运费+订单金额
	 */
	@Column(name="total_amount")
	private BigDecimal totalAmount;
	
	/**
	 * 定金
	 */
	@Column(name="front_money")
	private BigDecimal frontMoney;
	
	/**
	 * 成品金额
	 * 根据甲方需求，2016年5月31号改版后添加字段
	 */
	@Column(name="product_amount")
	private BigDecimal productAmount;
	
	/**
	 * 运费
	 */
	@Column(name="freight")
	private BigDecimal freight;
	
	/**
	 * 客户
	 */
	@Column(name="customer")
	private String customer;
	
	@Column(name="customer_id")
	private Long customerId;
	
	/**
	 * 客户联系电话
	 */
	@Column(name="customer_phone")
	private String customerPhone;
	
	/**
	 * 对应客户接收人的主键id
	 */
	@Column(name="receiver_id")
	private Long receiverId;
	
	/**
	 * 收货人
	 */
	@Column(name="receiver")
	private String receiver;
	
	/**
	 * 收货人联系电话
	 */
	@Column(name="receiver_phone")
	private String receiverPhone;
	
	/**
	 * 物流
	 * 根据甲方需求，2016年5月31号改版后暂时不用
	 */
	@Column(name="logistics")
	private String logistics;
	
	/**
	 * 收货地址
	 */
	@Column(name="receiver_address")
	private String receiverAddress;
	
	/**
	 * 是否开票
	 * 0:不开票
	 * 1：开票
	 */
	@Column(name="is_invoice")
	private Integer isInvoice;
	
	/************开票相关信息**************/
	/**
	 * 开票金额
	 */
	@Column(name="invoice_money")
	private BigDecimal invoiceMoney;
	
	/**
	 * 改为应收账款，财务回款审核阶段填写
	 */
	@Column(name="supposed_money")
	private BigDecimal supposedMoney;
	
	/**
	 * 改为回款金额，财务回款审核阶段填写
	 */
	@Column(name="actual_money")
	private BigDecimal actualMoney;
	
	/**
	 * 收入归属部门
	 */
	@Column(name="income_department_id")
	private Long incomeDepartmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=income_department_id)")
	private String incomeDepartmentName;
	
	/**
	 * 对方开票地址
	 */
	@Column(name="invoice_address")
	private String invoiceAddress;
	
	@Column(name="invoice_phone")
	private String invoicePhone;
	
	@Column(name="invoice_bank")
	private String invoiceBank;
	
	@Column(name="invoice_bank_no")
	private String invoiceBankNo;
	
	/**
	 * 预计回款时间
	 */
	@Column(name="plan_restream_date")
	private Date planRestreamDate;
	
	/**
	 * 开票类型，对应字典表主键
	 */
	@Column(name="invoice_type_id")
	private Long invoiceTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=invoice_type_id)")
	private String invoiceTypeName;
	
	/**
	 * 开票纳税人识别号
	 */
	@Column(name="tax_payer_id")
	private String taxPayerId;
	
	/**
	 * 开票状态
	 * 0：已回款；
	 * 1：部分回款
	 * 2：未回款
	 * 5：未开票作废
	 * 6：已开票作废
	 */
	@Column(name="invoice_status")
	private Long invoiceStatus;
	
	/**
	 * 回款状态，在财务审核阶段有用到
	 * 0：未回款
	 * 1：部分回款
	 * 2：已回款
	 */
	@Column(name="restream_status")
	private Long restreamStatus;
	
	/**
	 * 出库状态
	 * 0：未出库
	 * 1：部分出库
	 * 2：已出库
	 */
	@Column(name="checkout_status")
	private Long checkoutStatus;
	
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
	@Where(clause="wf_type='specialSale' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
	/**
	 * 业务员
	 */
	@Column(name="salesman")
	private String salesman;
	
	/**
	 * 结算单时选择的账号对应Account的主键
	 */
	@Column(name="account_id")
	private Long accountId;
	
	@Formula("(SELECT receiver FROM sys_account t WHERE t.id=account_id)")
	private String accountName;
	
	/**
	 * 结算单时选择的账号对应的银行账号
	 */
	@Column(name="bank_no")
	private String bankNo;
	
	/**
	 * 结算单时选择的账号对应的银行
	 */
	@Column(name="bank")
	private String bank;
	
	@Transient
	private String allFlowStatus;
	
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

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Long getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Long payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public BigDecimal getTotalAmount() {
		if(null != this.totalAmount) {
			return CodeUtil.parseBigDecimal(NumberUtil.format(this.totalAmount.doubleValue(), "#.##"));
		} else {
			return totalAmount;
		}
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getFrontMoney() {
		return frontMoney;
	}

	public void setFrontMoney(BigDecimal frontMoney) {
		this.frontMoney = frontMoney;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
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

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
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

	public String getTaxPayerId() {
		return taxPayerId;
	}

	public void setTaxPayerId(String taxPayerId) {
		this.taxPayerId = taxPayerId;
	}

	public Long getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Long invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getCheckoutStatus() {
		return checkoutStatus;
	}

	public void setCheckoutStatus(Long checkoutStatus) {
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

	public Long getRestreamStatus() {
		return restreamStatus;
	}

	public void setRestreamStatus(Long restreamStatus) {
		this.restreamStatus = restreamStatus;
	}

	public String getStockmanInfo() {
		return stockmanInfo;
	}

	public void setStockmanInfo(String stockmanInfo) {
		this.stockmanInfo = stockmanInfo;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Long getDeliverTypeId() {
		return deliverTypeId;
	}

	public void setDeliverTypeId(Long deliverTypeId) {
		this.deliverTypeId = deliverTypeId;
	}

	public String getDeliverTypeName() {
		return deliverTypeName;
	}

	public void setDeliverTypeName(String deliverTypeName) {
		this.deliverTypeName = deliverTypeName;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getAllFlowStatus() {
		this.allFlowStatus = EngineUtil.getAllStatus(auditLogs);
		return allFlowStatus;
	}

	public void setAllFlowStatus(String allFlowStatus) {
		this.allFlowStatus = allFlowStatus;
	}
}
