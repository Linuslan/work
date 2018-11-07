package com.linuslan.oa.system.post.model;

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
@Table(name="sys_post")
public class Post {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysPostSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysPostSeq", sequenceName="sys_post_seq")
	private Long id;
	
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="create_date")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
