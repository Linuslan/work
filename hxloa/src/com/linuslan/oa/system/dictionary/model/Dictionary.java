package com.linuslan.oa.system.dictionary.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.common.TreeModel;

@Entity
@Table(name="sys_dictionary")
public class Dictionary extends TreeModel {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysDictionarySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysDictionarySeq", sequenceName="sys_dictionary_seq")
	private Long id;
	
	@Column(name="text")
	private String text;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private int isDelete=0;
	
	@Column(name="pid")
	private Long pid;
	
	@Formula("(SELECT t.text FROM sys_group t WHERE t.id=pid)")
	private String pname;
	
	@Column(name="value")
	private String value;
	
	/**
	 * 是否启用
	 * 0：启用
	 * 1：未启用
	 */
	@Column(name="is_use")
	private Integer isUse;
	
	@Column(name="content")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getIsUse() {
		return isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
