package com.linuslan.oa.system.menu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 此类对应的表用于首页提示信息的统计
 * 当workflow_audit_log表中有需要登录用户审核的记录
 * 则会去关联workflow_audit_log中statu=sys_menu_index.value的值
 * 且wf_type=sys_menu.xtype的记录，再group by sys_menu.text, url从而统计有多少条代办，待阅或者待审
 * @author LinusLan
 *
 */
@Entity
@Table(name="sys_menu_index")
public class MenuIndex {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysMenuIndexSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysMenuIndexSeq", sequenceName="sys_menu_index_seq")
	private Long id;
	
	@Column(name="menu_id")
	private Long menuId;
	
	@Formula("(SELECT t.text FROM sys_menu t WHERE t.id=menu_id)")
	private String menuName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private String value;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="remark")
	private String remark;

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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
