package com.linuslan.oa.system.menu.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.common.TreeModel;
import com.linuslan.oa.system.button.model.Button;

@Entity
@Table(name="sys_menu")
public class Menu extends TreeModel {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysMenuSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysMenuSeq", sequenceName="sys_menu_seq")
	private Long id;
	
	@Column(name="text")
	private String text;
	
	@Column(name="value")
	private String value;
	
	@Column(name="order_num")
	private Integer orderNo=0;
	
	@Column(name="url")
	private String url;
	
	@Column(name="img_url")
	private String icon;
	
	@Column(name="parent_id")
	private Long pid;
	
	@Formula("(SELECT t.text FROM sys_menu t WHERE t.id=parent_id)")
	private String pname;
	
	@Column(name="xtype")
	private String xtype;
	
	@Column(name="index_icon")
	private String indexIcon;
	
	@Column(name="index_css")
	private String indexCss;

	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private int isDelete = 0;
	
	@Column(name="content")
	private String content;
	
	@Transient
	private int subMenuNum;
	
	@Transient
	private List<Button> buttons = new ArrayList<Button> ();
	
	@Transient
	private boolean checked=false;
	
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrderNo() {
		return orderNo == null ? 0 : orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	/*
	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}
*/
	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSubMenuNum() {
		return subMenuNum;
	}

	public void setSubMenuNum(int subMenuNum) {
		this.subMenuNum = subMenuNum;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getIndexIcon() {
		return indexIcon;
	}

	public void setIndexIcon(String indexIcon) {
		this.indexIcon = indexIcon;
	}

	public String getIndexCss() {
		return indexCss;
	}

	public void setIndexCss(String indexCss) {
		this.indexCss = indexCss;
	}
}
