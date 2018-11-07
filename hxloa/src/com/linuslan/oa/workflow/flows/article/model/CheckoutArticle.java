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
@Table(name="wf_checkout_article")
public class CheckoutArticle {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckoutArticleSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckoutArticleSeq", sequenceName="wf_checkout_article_seq")
	private Long id;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Formula("(SELECT t.name FROM wf_checkin_article t WHERE t.id=checkin_article_id)")
	private String checkinArticleName;
	
	@Formula("(SELECT t.unit FROM wf_checkin_article t WHERE t.id=checkin_article_id)")
	private String unit;
	
	@Column(name="checkin_article_id")
	private Long checkinArticleId;
	
	@Column(name="customer_id")
	private Long customerId;
	
	@Formula("(SELECT t.name FROM wf_customer t WHERE t.id=customer_id)")
	private String customerName;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="user_id")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getCheckinArticleId() {
		return checkinArticleId;
	}

	public void setCheckinArticleId(Long checkinArticleId) {
		this.checkinArticleId = checkinArticleId;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCheckinArticleName() {
		return checkinArticleName;
	}

	public void setCheckinArticleName(String checkinArticleName) {
		this.checkinArticleName = checkinArticleName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}
