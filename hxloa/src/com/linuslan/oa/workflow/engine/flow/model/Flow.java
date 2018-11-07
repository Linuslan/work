package com.linuslan.oa.workflow.engine.flow.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.system.company.model.Company;

@Entity
@Table(name="workflow_flow")
public class Flow {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="flowNameSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="flowNameSeq", sequenceName="workflow_flow_seq")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	/**
	 * 流程类型，例如：invoice,companyPay
	 */
	@Column(name="type")
	private String type;
	
	@Column(name="memo")
	private String memo;
	
	/**
	 * 创建人
	 */
	@Column(name="user_id")
	private Long userId;
	
	/**
	 * 创建时间
	 */
	@Column(name="create_date")
	private Date createDate;
	
	//@Formula("(SELECT c.company_id FROM flow_company c WHERE c.flow_id=id)")
	@ManyToMany(targetEntity=Company.class, fetch=FetchType.EAGER)
	@JoinTable(name="workflow_flow_company", joinColumns={@JoinColumn(name="flow_id")}, inverseJoinColumns={@JoinColumn(name="company_id")})
	private List<Company> companys;
	
	/**
	 * 是否启用
	 * 0：已启用
	 * 1：未启用
	 * 未启用则所有流程挂起不再执行
	 */
	@Column(name="is_use")
	private Integer isUse=0;
	
	/**
	 * 是否是历史流程，假如有流程实例正在运行，则会保留一份历史遗留的流程
	 * 直到流程实例运行完了，这个才能删除
	 * 0：非历史遗留
	 * 1：历史遗留
	 */
	@Column(name="is_history")
	private Integer isHistory=0;
	
	/**
	 * 是否删除
	 * 0：未删除
	 * 1：已删除
	 */
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Formula("(SELECT COUNT(*) FROM workflow_audit_log t WHERE t.flow_id=id AND t.is_audit=0)")
	private Integer instanceCount;
	
	@Transient
	private String companyNames;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompanyNames() {
		if(null == this.companyNames) {
			this.companyNames = "";
		}
		if(null != this.companys && 0 < companys.size()) {
			Iterator<Company> iter = this.companys.iterator();
			Company company = null;
			while(iter.hasNext()) {
				company = iter.next();
				this.companyNames += company.getName();
				if(iter.hasNext()) {
					this.companyNames+="、";
				}
			}
		}
		return companyNames;
	}

	public void setCompanyNames(String companyNames) {
		this.companyNames = companyNames;
	}

	public Integer getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Integer isHistory) {
		this.isHistory = isHistory;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsUse() {
		return isUse;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public Integer getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(Integer instanceCount) {
		this.instanceCount = instanceCount;
	}
	
}
