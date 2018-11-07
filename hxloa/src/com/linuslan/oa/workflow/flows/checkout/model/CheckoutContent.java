package com.linuslan.oa.workflow.flows.checkout.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="wf_checkout_content")
public class CheckoutContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckoutContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckoutContentSeq", sequenceName="wf_checkout_content_seq")
	private Long id;
	
	@Column(name="checkout_id")
	private Long checkoutId;
	
	@Column(name="serial_no")
	private String serialNo;
	
	/**
	 * 选择出库产品之后对应的入库产品的id，
	 */
	@Column(name="checkout_article_id")
	private Long checkoutArticleId;
	
	@Formula("(SELECT t.name FROM wf_checkin_article t WHERE t.id=(SELECT t2.checkin_article_id FROM wf_checkout_article t2 WHERE t2.id=checkout_article_id))")
	private String checkinArticleName;
	
	@Column(name="format_id")
	private Long formatId;
	
	@Formula("(SELECT t.name FROM wf_format t WHERE t.id=format_id)")
	private String formatName;
	
	@Column(name="unit")
	private String unit;
	
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

	public Long getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(Long checkoutId) {
		this.checkoutId = checkoutId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getCheckoutArticleId() {
		return checkoutArticleId;
	}

	public void setCheckoutArticleId(Long checkoutArticleId) {
		this.checkoutArticleId = checkoutArticleId;
	}

	public String getCheckinArticleName() {
		return checkinArticleName;
	}

	public void setCheckinArticleName(String checkinArticleName) {
		this.checkinArticleName = checkinArticleName;
	}

	public Long getFormatId() {
		return formatId;
	}

	public void setFormatId(Long formatId) {
		this.formatId = formatId;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
