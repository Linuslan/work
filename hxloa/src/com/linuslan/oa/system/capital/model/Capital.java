package com.linuslan.oa.system.capital.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="sys_capital")
public class Capital {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysCapitalSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysCapitalSeq", sequenceName="sys_capital_seq")
	private Long id;
	
	@Column(name="class_name")
	private String className;
	
	@Column(name="serial")
	private String serial;
	
	@Column(name="name")
	private String name;
	
	@Column(name="model")
	private String model;
	
	@Column(name="shop_name")
	private String shopName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="department")
	private String department;
	
	@Column(name="borrow_date")
	private Date borrowDate;
	
	@Column(name="borrow_department")
	private String borrowDepartment;
	
	@Column(name="borrow_user")
	private String borrowUser;
	
	@Column(name="num")
	private Long num;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name="buy_date")
	private Date buyDate;
	
	@Column(name="buy_money")
	private String buyMoney;
	
	@Column(name="depreciation_year")
	private Long depreciationYear;
	
	@Column(name="depreciation_money")
	private String depreciationMoney;
	
	@Column(name="depreciation")
	private String depreciation;
	
	@Column(name="depreciation_state")
	private String depreciationState;
	
	/**
	 * 0:删除
	 * 1:作废
	 * 2：正常
	 */
	@Column(name="state")
	private Long state;
	
	@Column(name="info")
	private String info;
	
	@Column(name="netamount")
	private String netamount;
	
	@Column(name="add_id")
	private Long addId;
	
	@Column(name="add_time")
	private Date addTime;
	
	@Column(name="year")
	private int year;
	
	@Column(name="month")
	private int month;
	
	/**
	 * 固资归属人
	 */
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getBorrowDepartment() {
		return borrowDepartment;
	}

	public void setBorrowDepartment(String borrowDepartment) {
		this.borrowDepartment = borrowDepartment;
	}

	public String getBorrowUser() {
		return borrowUser;
	}

	public void setBorrowUser(String borrowUser) {
		this.borrowUser = borrowUser;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}

	public Long getDepreciationYear() {
		return depreciationYear;
	}

	public void setDepreciationYear(Long depreciationYear) {
		this.depreciationYear = depreciationYear;
	}

	public String getDepreciationMoney() {
		return depreciationMoney;
	}

	public void setDepreciationMoney(String depreciationMoney) {
		this.depreciationMoney = depreciationMoney;
	}

	public String getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(String depreciation) {
		this.depreciation = depreciation;
	}

	public String getDepreciationState() {
		return depreciationState;
	}

	public void setDepreciationState(String depreciationState) {
		this.depreciationState = depreciationState;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNetamount() {
		return netamount;
	}

	public void setNetamount(String netamount) {
		this.netamount = netamount;
	}

	public Long getAddId() {
		return addId;
	}

	public void setAddId(Long addId) {
		this.addId = addId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
