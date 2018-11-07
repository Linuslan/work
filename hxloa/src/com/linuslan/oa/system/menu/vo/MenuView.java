package com.linuslan.oa.system.menu.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主要用于前端左侧树菜单显�?
 * @author LinusLan
 *
 */
public class MenuView {
	
	private Long id;
	
	
	private String text;
	
	
	private String value;
	
	/**
	 * 是否叶子节点，非数据库表字段
	 */
	private boolean leaf;
	
	/**
	 * 是否展开，非数据库表字段
	 */
	private boolean expanded;
	
	
	private int orderNum;
	
	/**
	 * 菜单地址
	 */
	private String url;
	
	/**
	 * 菜单图标地址
	 */
	private String icon;
	
	/**
	 * 父菜单id
	 */
	private Long parentId;
	
	/**
	 * 子菜�?
	 */
	private List<MenuView> children = new ArrayList<MenuView> ();
	
	
	private String xtype;
	
	/**
	 * 是否可以展开，非数据库表字段
	 */
	private boolean expandable;

	/**
	 * 创建时间
	 */
	private Date createDate;
	
	
	private int isDelete = 0;
	
	/**
	 * 菜单信息
	 */
	private String content;
	
	/**
	 * 子菜单的数目
	 */
	private int subMenuNum;
	
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

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
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

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
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

	public List<MenuView> getChildren() {
		return children;
	}

	public void setChildren(List<MenuView> children) {
		this.children = children;
	}
}
