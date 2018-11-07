package com.linuslan.oa.workflow.flows.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 客户
 * @author LinusLan
 *
 */
@Entity
@Table(name="wf_customer")
public class Customer {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCustomerSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCustomerSeq", sequenceName="wf_customer_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="name")
	private String name;
	
	/**
	 * 单位电话
	 */
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="cellphone")
	private String cellphone;
	
	/**
	 * 联系人
	 */
	@Column(name="linkman")
	private String linkman;
	
	@Column(name="email")
	private String email;
	
	@Column(name="address")
	private String address;
	
	/**
	 * 开户名称
	 */
	@Column(name="bank_account_name")
	private String bankAccountName;
	
	/**
	 * 开户行
	 */
	@Column(name="bank_name")
	private String bankName;
	
	/**
	 * 开户行地址
	 */
	@Column(name="bank_address")
	private String bankAddress;
	
	/**
	 * 账号
	 */
	@Column(name="bank_account")
	private String bankAccount;
	
	@Column(name="receiver")
	private String receiver;
	
	/**
	 * 邮编
	 */
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="receiver_address")
	private String receiverAddress;
	
	@Column(name="receiver_phone")
	private String receiverPhone;
	
	@Column(name="order_no")
	private Integer orderNo;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="area_id")
	private Long areaId;
	
	@Formula("(SELECT t.name FROM wf_area t WHERE t.id=area_id)")
	private String areaName;
	
	@Column(name="company_id")
	private Long companyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=company_id)")
	private String companyName;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
