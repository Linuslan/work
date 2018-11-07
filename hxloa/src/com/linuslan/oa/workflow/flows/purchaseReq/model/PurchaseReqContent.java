package com.linuslan.oa.workflow.flows.purchaseReq.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="wf_purchase_req_content")
public class PurchaseReqContent {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfPurchaseReqContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfPurchaseReqContentSeq", sequenceName="wf_purchase_req_content_seq")
	private Long id;
	
	@Column(name="purchase_req_id")
	private Long purchaseReqId;
	
	@Column(name="article")
	private String article;
	
	@Column(name="format")
	private String format;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name="arrive_date")
	private Date arriveDate;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="total_price")
	private BigDecimal totalPrice;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private Integer isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPurchaseReqId() {
		return purchaseReqId;
	}

	public void setPurchaseReqId(Long purchaseReqId) {
		this.purchaseReqId = purchaseReqId;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
