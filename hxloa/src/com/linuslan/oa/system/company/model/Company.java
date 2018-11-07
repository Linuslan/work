package com.linuslan.oa.system.company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.system.user.model.User;

@Entity
@Table(name="sys_company")
public class Company {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysCompanySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysCompanySeq", sequenceName="sys_company_seq")
	private Long id;
	
	/**
	 * 公司名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 排序号
	 */
	@Column(name="order_num")
	private Integer orderNo;
	
	/**
	 * 是否删除
	 * 0：未删除；
	 * 1：删除；
	 */
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	/**
	 * 公司的最高领导人
	 */
	@Column(name="leader_id")
	private Long groupId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=leader_id)")
	private String leaderName;

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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
}
