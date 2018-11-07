package com.linuslan.oa.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

/**
 * 流程类的基本父类
 * @author LinusLan
 *
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BaseFlow implements Serializable {
	
	@Column(name="flow_id")
	private Long flowId;
	
	@Column(name="company_id")
	private Long companyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=company_id)")
	private String companyName;
	
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="last_status")
	private Integer lastStatus;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Transient
	protected boolean passBtn;
	
	@Transient
	protected boolean rejectBtn;
	
	/**
	 * 是否允许打印
	 */
	@Transient
	protected boolean print;

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(Integer lastStatus) {
		this.lastStatus = lastStatus;
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

	public boolean isPassBtn() {
		return passBtn;
	}

	public void setPassBtn(boolean passBtn) {
		this.passBtn = passBtn;
	}

	public boolean isRejectBtn() {
		return rejectBtn;
	}

	public void setRejectBtn(boolean rejectBtn) {
		this.rejectBtn = rejectBtn;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}
}
