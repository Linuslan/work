package com.linuslan.oa.system.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 客户账户管理，当前主要用于开票和企业付款
 * @author LinusLan
 *
 */
@Entity
@Table(name="sys_account")
public class Account {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysAccountSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysAccountSeq", sequenceName="sys_account_seq")
	private Long id;
	
	/**
	 * 收款方名称
	 */
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="tax_payer_id")
	private String taxPayerId;
	
	@Column(name="address")
	private String address;
	
	@Column(name="cellphone")
	private String cellphone;
	
	@Column(name="bank_no")
	private String bankNo;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="info")
	private String info;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="user_id")
	private Long userId;
	
	/**
	 * 类型
	 * 0：普通申请单账号
	 * 1：开票申请单账号
	 */
	@Column(name="type")
	private Integer type;
	
	@Column(name="order_no")
	private Integer orderNo = 0 ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}
