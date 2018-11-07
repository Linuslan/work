package com.linuslan.oa.workflow.flows.customer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="wf_customer_payback_log")
public class CustomerPaybackLog {
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCustomerPaybackLogSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCustomerPaybackLogSeq", sequenceName="wf_customer_payback_log_seq")
	private Long id;
	
	/**
	 * 对应客户回款主表id
	 */
	@Column(name="customer_payback_id")
	private Long customerPaybackId;
	
	/**
	 * 操作人
	 */
	@Column(name="user_id")
	private Long userId;
	
	/**
	 * 操作人姓名
	 */
	@Column(name="user_name")
	private String userName;
	
	/**
	 * 操作类型
	 * add: 新增
	 * update: 更新
	 * delete: 删除
	 */
	@Column(name="operate")
	private String operate;
	
	/**
	 * 操作备注
	 */
	@Column(name="memo")
	private String memo;
	
	/**
	 * 操作时间
	 */
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerPaybackId() {
		return customerPaybackId;
	}

	public void setCustomerPaybackId(Long customerPaybackId) {
		this.customerPaybackId = customerPaybackId;
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

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
