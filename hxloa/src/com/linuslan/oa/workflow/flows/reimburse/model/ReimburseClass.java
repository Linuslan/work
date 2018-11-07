package com.linuslan.oa.workflow.flows.reimburse.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="wf_reimburse_class")
public class ReimburseClass {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfReimburseClassSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfReimburseClassSeq", sequenceName="wf_reimburse_class_seq")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete=0;

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

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
