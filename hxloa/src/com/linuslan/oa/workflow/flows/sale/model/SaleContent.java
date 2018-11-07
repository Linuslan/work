package com.linuslan.oa.workflow.flows.sale.model;

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
@Table(name="wf_sale_content")
public class SaleContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSaleContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSaleContentSeq", sequenceName="wf_sale_content_seq")
	private Long id;
	
	@Column(name="sale_id")
	private Long saleId;
	
	/**
	 * 出库商品编号
	 */
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="article_id")
	private Long articleId;
	
	@Formula("(SELECT t.name FROM wf_checkin_article t WHERE t.id=(SELECT t2.checkin_article_id FROM wf_checkout_article t2 WHERE t2.id=article_id))")
	private String articleName;
	
	@Column(name="format_id")
	private Long formatId;
	
	@Formula("(SELECT t.name FROM wf_format t WHERE t.id=format_id)")
	private String formatName;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name="deliver_date")
	private Date deliverDate;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="quality")
	private String quality;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="total_amount")
	private BigDecimal totalAmount;
	
	@Column(name="checkout_quantity")
	private Double checkoutQuantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
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

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getCheckoutQuantity() {
		return checkoutQuantity;
	}

	public void setCheckoutQuantity(Double checkoutQuantity) {
		this.checkoutQuantity = checkoutQuantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
