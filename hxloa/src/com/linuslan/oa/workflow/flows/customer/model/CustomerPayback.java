package com.linuslan.oa.workflow.flows.customer.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 客户回款
 * @author LinusLan
 *
 */
@Entity
@Table(name="wf_customer_payback")
public class CustomerPayback {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCustomerPaybackSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCustomerPaybackSeq", sequenceName="wf_customer_payback_seq")
	private Long id;
	
	@Column(name="customer_id")
	private Long customerId;
	
	@Formula("(SELECT t.name FROM wf_customer t WHERE t.id=customer_id)")
	private String customerName;
	
	@Column(name="company_id")
	private Long companyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=company_id)")
	private String companyName;
	
	@Column(name="pay_money")
	private BigDecimal payMoney;
	
	@Column(name="pay_date")
	private Date payDate;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="is_delete")
	private int isDelete=0;
	
	@Column(name="logtime")
	private Date logtime;
	
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	/**
	 * specialCustomer表示华夏蓝的客户回款，对应SpecialCustomer类
	 * customer表示联拓客户回款，对应Customer类
	 */
	@Column(name="type")
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Date getLogtime() {
		return logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
