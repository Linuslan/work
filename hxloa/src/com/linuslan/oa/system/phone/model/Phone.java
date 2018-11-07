package com.linuslan.oa.system.phone.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="sys_phone")
public class Phone {
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysPhoneSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysPhoneSeq", sequenceName="sys_phone_seq")
	private Long id;
	
	@Column(name="department")
	private String department;
	
	/**
	 * 电话号
	 */
	@Column(name="phone_no")
	private String phoneNo;
	
	/**
	 * 使用人
	 */
	@Column(name="user_name")
	private String userName;
	
	/**
	 * 使用地点
	 */
	@Column(name="address")
	private String address;
	
	/**
	 * 月租
	 */
	@Column(name="monthly_rent")
	private String monthlyRent;
	
	/**
	 * 套餐
	 */
	@Column(name="phone_package")
	private String phonePackage;
	
	/**
	 * 套餐结束时间
	 */
	@Column(name="package_end_date")
	private Date packageEndDate;
	
	/**
	 * 户名
	 */
	@Column(name="account_name")
	private String accountName;
	
	/**
	 * 缴费方式
	 */
	@Column(name="payment_way")
	private String paymentWay;
	
	/**
	 * 用途
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 备注
	 */
	@Column(name="info")
	private String info;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	/**
	 * 是否提醒
	 * 0：是
	 * 1：否
	 */
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
