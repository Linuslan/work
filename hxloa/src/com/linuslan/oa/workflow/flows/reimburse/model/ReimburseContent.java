package com.linuslan.oa.workflow.flows.reimburse.model;

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

@Entity
@Table(name="wf_reimburse_content")
public class ReimburseContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfReimburseContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfReimburseContentSeq", sequenceName="wf_reimburse_content_seq")
	private Long id;
	
	@Column(name="reimburse_id")
	private Long reimburseId;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 费用产生时间
	 */
	@Column(name="remittance_date")
	private Date remittanceDate;
	
	/**
	 * 报销项目
	 */
	@Column(name="content")
	private String content;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 报销类别
	 */
	@Column(name="reimburse_class_id")
	private Long reimburseClassId;
	
	@Formula("(SELECT t.name FROM wf_reimburse_class t WHERE t.id=reimburse_class_id)")
	private String reimburseClassName;
	
	/**
	 * 报销金额
	 */
	@Column(name="money")
	private BigDecimal money;
	
	/**
	 * 是否删除
	 * 0：未删除
	 * 1：已删除
	 */
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	/**
	 * 排序值
	 */
	@Column(name="order_no")
	private Integer orderNo=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReimburseId() {
		return reimburseId;
	}

	public void setReimburseId(Long reimburseId) {
		this.reimburseId = reimburseId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getRemittanceDate() {
		return remittanceDate;
	}

	public void setRemittanceDate(Date remittanceDate) {
		this.remittanceDate = remittanceDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getReimburseClassId() {
		return reimburseClassId;
	}

	public void setReimburseClassId(Long reimburseClassId) {
		this.reimburseClassId = reimburseClassId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getReimburseClassName() {
		return reimburseClassName;
	}

	public void setReimburseClassName(String reimburseClassName) {
		this.reimburseClassName = reimburseClassName;
	}
	
}
