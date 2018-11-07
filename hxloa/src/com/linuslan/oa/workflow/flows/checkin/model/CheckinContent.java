package com.linuslan.oa.workflow.flows.checkin.model;

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
@Table(name="wf_checkin_content")
public class CheckinContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfCheckinContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfCheckinContentSeq", sequenceName="wf_checkin_content_seq")
	private Long id;
	
	@Column(name="checkin_id")
	private Long checkinId;
	
	@Column(name="article_id")
	private Long articleId;
	
	@Column(name="article_serial_no")
	private String articleSerialNo;
	
	@Column(name="unit")
	private String unit;
	
	@Formula("(SELECT t.name FROM wf_checkin_article t WHERE t.id=article_id)")
	private String articleName;
	
	@Column(name="format_id")
	private Long formatId;
	
	@Formula("(SELECT t.name FROM wf_format t WHERE t.id=format_id)")
	private String formatName;
	
	@Column(name="quantity")
	private Double quantity=0d;
	
	@Column(name="price")
	private BigDecimal price=new BigDecimal("0");
	
	@Column(name="total_price")
	private BigDecimal totalPrice = new BigDecimal("0");
	
	@Column(name="loss")
	private Double loss=0d;
	
	@Column(name="inspection")
	private String inspection;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="order_no")
	private Integer oderNo=0;
	
	@Column(name="remainder")
	private Double remainder=0d;
	
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

	public Long getCheckinId() {
		return checkinId;
	}

	public void setCheckinId(Long checkinId) {
		this.checkinId = checkinId;
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

	public Double getLoss() {
		return loss;
	}

	public void setLoss(Double loss) {
		this.loss = loss;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOderNo() {
		return oderNo;
	}

	public void setOderNo(Integer oderNo) {
		this.oderNo = oderNo;
	}

	public Double getRemainder() {
		return remainder;
	}

	public void setRemainder(Double remainder) {
		this.remainder = remainder;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getArticleSerialNo() {
		return articleSerialNo;
	}

	public void setArticleSerialNo(String articleSerialNo) {
		this.articleSerialNo = articleSerialNo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
