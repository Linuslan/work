package com.linuslan.oa.workflow.flows.article.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="wf_checkin_article")
public class CheckinArticle {

	/**
	 * 主键id
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckinArticleSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckinArticleSeq", sequenceName="wf_checkin_article_seq")
	private Long id;
	
	/**
	 * 商品名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 入库的商品编号
	 */
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 单位
	 */
	@Column(name="unit")
	private String unit;
	
	/**
	 * 是否删除
	 * 0：未删除
	 * 1：删除
	 */
	@Column(name="is_delete")
	private Integer isDelete = 0;
	
	/**
	 * 创建人
	 */
	//private User user;
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	@Column(name="company_id")
	private Long companyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=company_id)")
	private String companyName;
	
	@Column(name="order_no")
	private Integer orderNo=0;

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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsDelete() {
		return isDelete;
	}
	
}
