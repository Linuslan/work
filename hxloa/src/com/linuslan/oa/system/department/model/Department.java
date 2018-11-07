package com.linuslan.oa.system.department.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import com.linuslan.oa.common.TreeModel;

@Entity
@Table(name="sys_department")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Department extends TreeModel {
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysDepartmentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysDepartmentSeq", sequenceName="sys_department_seq")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="order_num")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private int isDelete=0;
	
	@Column(name="pid")
	private Long pid;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=pid)")
	private String pname;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 领导用户组
	 */
	@Column(name="leader_group")
	private Long groupId;
	
	@Formula("(SELECT t.text FROM sys_group t WHERE t.id=leader_group)")
	private String groupText;
	
	@Column(name="is_use")
	private int isUse=1;
	
	/**
	 * 部门归属公司，目前只是为了方便判断工资流程的走向
	 * 因为工资是按照部门创建，走向需要根据公司来走
	 */
	@Column(name="company_id")
	private Long companyId;

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
