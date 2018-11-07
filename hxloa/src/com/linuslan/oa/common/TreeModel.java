package com.linuslan.oa.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

public class TreeModel implements Serializable {
	
	private Long id = null;
	
	private List<TreeModel> children = new ArrayList<TreeModel> ();
	
	private Long pid = null;
	
	private boolean leaf = true;
	
	private boolean expanded = false;
	
	private Integer orderNo = 0;
	
	private int childCount = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public boolean isLeaf() {
		if(children.size() > 0) {
			return false;
		} else {
			return true;
		}
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

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public int getChildCount() {
		return children.size();
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
}
