package com.linuslan.oa.workflow.flows.checkin.model;

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
@Table(name="wf_checkin")
public class Checkin extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckinSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckinSeq", sequenceName="wf_checkin_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="checkin_date")
	private Date checkinDate;
	
	@Column(name="unit_id")
	private Long unitId;
	
	@Formula("(SELECT t.name FROM wf_unit t WHERE t.id=unit_id)")
	private String unitName;
	
	@Column(name="warehouse_id")
	private Long warehouseId;
	
	@Formula("(SELECT t.name FROM wf_warehouse t WHERE t.id=warehouse_id)")
	private String warehouseName;
	
	/**
	 * 入库类型，对应字典表
	 */
	@Column(name="checkin_type_id")
	private Long checkinTypeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=checkin_type_id)")
	private String checkinTypeName;
	
	@Column(name="purchase_id")
	private Long purchaseId;
	
	//@Formula("(SELECT * FROM )")
	@Column(name="purchase_serial_no")
	private String purchaseSerialNo;
	
	@Column(name="total_amount")
	private BigDecimal totalAmount;
	
	/**
	 * 单位联系电话
	 */
	@Column(name="telephone")
	private String telephone;
	
	/**
	 * 单位地址
	 */
	@Column(name="address")
	private String address;
	
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='checkin' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;

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

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Long getCheckinTypeId() {
		return checkinTypeId;
	}

	public void setCheckinTypeId(Long checkinTypeId) {
		this.checkinTypeId = checkinTypeId;
	}

	public String getCheckinTypeName() {
		return checkinTypeName;
	}

	public void setCheckinTypeName(String checkinTypeName) {
		this.checkinTypeName = checkinTypeName;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseSerialNo() {
		return purchaseSerialNo;
	}

	public void setPurchaseSerialNo(String purchaseSerialNo) {
		this.purchaseSerialNo = purchaseSerialNo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getFlowStatus() {
		this.flowStatus = EngineUtil.getStatus(auditLogs);
		return this.flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}
}
