package com.linuslan.oa.system.userContract.model;

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
@Table(name="sys_user_contract")
public class UserContract {
	/**
	 * 主键id
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysUserContractSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysUserContractSeq", sequenceName="sys_user_contract_seq")
	private Long id;
	
	/**
	 * 所属用户id
	 */
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	/**
	 * 合同编号
	 */
	@Column(name="num")
	private String num;
	
	/**
	 * 合同标题
	 */
	@Column(name="title")
	private String title;
	
	/**
	 * 合同内容
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 合同生效开始时间
	 */
	@Column(name="start_date")
	private Date startDate;
	
	/**
	 * 合同生效结束时间
	 */
	@Column(name="end_date")
	private Date endDate;
	
	/**
	 * 是否提醒
	 * 0：提醒；
	 * 1：不提醒；
	 */
	@Column(name="is_remind")
	private int isRemind=1;
	
	/**
	 * 是否过期
	 * 0：未过期
	 * 1：过期
	 * 2：即将过期
	 * 3：新过期
	 */
	@Column(name="out_of_date")
	private Integer outOfDate;
	
	/**
	 * 是否生效
	 * 0：生效；
	 * 1：不生效；
	 */
	@Column(name="is_effective")
	private int isEffective=1;
	
	/**
	 * 是否删除
	 * 0：未删除；
	 * 1：删除；
	 */
	@Column(name="is_delete")
	private int isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(int isRemind) {
		this.isRemind = isRemind;
	}

	public int getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(Integer outOfDate) {
		this.outOfDate = outOfDate;
	}
}
