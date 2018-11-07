package com.linuslan.oa.system.contract.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.linuslan.oa.common.BaseFlow;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

@Entity
@Table(name="sys_contract")
public class Contract {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfContractSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfContractSeq", sequenceName="sys_contract_seq")
	private Long id;
	
	@Column(name="contract_num")
	private String contractNum;
	
	@Column(name="contractor")
	private Long contractor;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=contractor)")
	private String contractorName;
	
	/**
	 * 申请人所在部门
	 */
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="content")
	private String content;
	
	@Column(name="info")
	private String info;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="script_Num")
	private int scriptNum=0;
	
	@Column(name="copy_num")
	private int copyNum=0;
	
	@Column(name="end_date")
	private Date endDate;
	
	/**
	 * 是否自动延续
	 * 0：非自动延续；
	 * 1：自动延续；
	 */
	@Column(name="is_reload")
	private int isReload = 0;
	
	/**
	 * 是否过期
	 * 0：未过期
	 * 1：过期
	 * 2：即将过期
	 * 3：新过期
	 */
	@Column(name="out_of_date")
	private int outOfDate;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	/**
	 * 签约时间
	 */
	@Column(name="sign_date")
	private Date signDate;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 是否提醒
	 * 0：是
	 * 1：否
	 */
	@Column(name="is_remind")
	private Integer isRemind = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public Long getContractor() {
		return contractor;
	}

	public void setContractor(Long contractor) {
		this.contractor = contractor;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getScriptNum() {
		return scriptNum;
	}

	public void setScriptNum(int scriptNum) {
		this.scriptNum = scriptNum;
	}

	public int getCopyNum() {
		return copyNum;
	}

	public void setCopyNum(int copyNum) {
		this.copyNum = copyNum;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getIsReload() {
		return isReload;
	}

	public void setIsReload(int isReload) {
		this.isReload = isReload;
	}

	public int getOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(int outOfDate) {
		this.outOfDate = outOfDate;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}
}
