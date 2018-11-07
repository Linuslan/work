package com.linuslan.oa.workflow.flows.article.model;

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
@Table(name="wf_format")
public class Format {

	/**
	 * 主键id，唯一值
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfFormatSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfFormatSeq", sequenceName="wf_format_seq")
	private Long id;
	
	/**
	 * 产品规格名称
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 备注
	 */
	@Column(name="memo")
	private String memo;
	
	/**
	 * 所属的产品
	 */
	@Column(name="article_id")
	private Long articleId;
	
	@Formula("(SELECT t.name FROM wf_checkin_article t WHERE t.id=article_id)")
	private String checkinArticleName;
	
	@Column(name="price")
	private BigDecimal price;
	
	/**
	 * 是否删除
	 */
	@Column(name="is_delete")
	private int isDelete=0;
	
	/**
	 * 创建者
	 */
	//private User user;
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getCheckinArticleName() {
		return checkinArticleName;
	}

	public void setCheckinArticleName(String checkinArticleName) {
		this.checkinArticleName = checkinArticleName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
	
}
