package com.linuslan.oa.workflow.flows.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 收货人
 * @author LinusLan
 *
 */
@Entity
@Table(name="wf_customer_receiver")
public class CustomerReceiver {
	
	@Id
	@Column(name="id")
	@SequenceGenerator(allocationSize=1, name="wfCustomerReceiverSeq", sequenceName="wf_customer_receiver_seq")
	@GeneratedValue(generator="wfCustomerReceiverSeq", strategy=GenerationType.SEQUENCE)
	private Long id;
	
	/**
	 * 归属的客户ID
	 */
	@Column(name="customer_id")
	private Long customerId;
	
	/**
	 * 姓名
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 手机
	 */
	@Column(name="cellphone")
	private String cellphone;
	
	/**
	 * 收货地址
	 */
	@Column(name="receiver_address")
	private String receiverAddress;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
