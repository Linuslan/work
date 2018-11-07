package com.linuslan.oa.workflow.flows.checkout.model;

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
@Table(name="wf_checkout")
public class Checkout extends BaseFlow {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckoutSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckoutSeq", sequenceName="wf_checkout_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="customer_id")
	private Long customerId;
	
	@Formula("(SELECT t.name FROM wf_customer t WHERE t.id=customer_id)")
	private String customerName;
	
	@Column(name="warehouse_id")
	private Long warehouseId;
	
	@Formula("(SELECT t.name FROM wf_warehouse t WHERE t.id=warehouse_id)")
	private String warehouseName;
	
	/**
	 * 出库类型，对应sys_dictionary的主键
	 */
	@Column(name="checkout_type_id")
	private Long checkoutTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=checkout_type_id)")
	private String checkoutTypeName;
	
	/**
	 * 对应销售订单的序列号
	 * 如果是销售出库，则必填
	 */
	@Column(name="sale_serial_no")
	private String saleSerialNo;
	
	/**
	 * 对应销售订单的ID
	 * 如果是销售出库，则必填
	 */
	@Column(name="sale_id")
	private Long saleId;
	
	@Column(name="checkout_date")
	private Date checkoutDate;
	
	@Column(name="total_amount")
	private BigDecimal totalAmount;
	
	/**
	 * 单位地址
	 */
	@Column(name="address")
	private String address;
	
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="receiver_phone")
	private String receiverPhone;
	
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='checkout' AND is_audit=0")
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getCheckoutTypeId() {
		return checkoutTypeId;
	}

	public void setCheckoutTypeId(Long checkoutTypeId) {
		this.checkoutTypeId = checkoutTypeId;
	}

	public String getCheckoutTypeName() {
		return checkoutTypeName;
	}

	public void setCheckoutTypeName(String checkoutTypeName) {
		this.checkoutTypeName = checkoutTypeName;
	}

	public String getSaleSerialNo() {
		return saleSerialNo;
	}

	public void setSaleSerialNo(String saleSerialNo) {
		this.saleSerialNo = saleSerialNo;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
}
