package com.linuslan.oa.system.button.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

@Entity
@Table(name="sys_button")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Button {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysButtonSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysButtonSeq", sequenceName="sys_button_seq")
	private Long id;
	
	@Column(name="menu_id")
	private Long menuId;
	
	@Formula("(SELECT t.text FROM sys_menu t WHERE t.id=menu_id)")
	private String menuName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private String value;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="order_num")
	private Integer orderNo=1;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Transient
	private boolean checked = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
