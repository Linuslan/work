package com.linuslan.oa.system.cellphone.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="sys_cellphone")
public class Cellphone {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysCellphoneSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysCellphoneSeq", sequenceName="sys_cellphone_seq")
	private Long id;
	
	@Column(name="department")
	private String department;
	
	@Column(name="phone_no")
	private String phoneNo;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="monthly_rent")
	private String monthlyRent;
	
	@Column(name="phone_package")
	private String phonePackage;
	
	@Column(name="package_end_date")
	private Date packageEndDate;
	
	@Column(name="account_name")
	private String accountName;
	
	@Column(name="payment_way")
	private String paymentWay;
	
	@Column(name="reimburse_limit")
	private String reimburseLimit;
	
	@Column(name="content")
	private String content;
	
	@Column(name="info")
	private String info;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="is_remind")
	private Integer isRemind = 0;
	
	/**
	 * 是否过期
	 * 0：未过期
	 * 1：过期
	 * 2：即将过期
	 * 3：新过期
	 */
	@Column(name="out_of_date")
	private Integer outOfDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(String monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public String getPhonePackage() {
		return phonePackage;
	}

	public void setPhonePackage(String phonePackage) {
		this.phonePackage = phonePackage;
	}

	public Date getPackageEndDate() {
		return packageEndDate;
	}

	public void setPackageEndDate(Date packageEndDate) {
		this.packageEndDate = packageEndDate;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getReimburseLimit() {
		return reimburseLimit;
	}

	public void setReimburseLimit(String reimburseLimit) {
		this.reimburseLimit = reimburseLimit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Integer getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}

	public Integer getOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(Integer outOfDate) {
		this.outOfDate = outOfDate;
	}
}
