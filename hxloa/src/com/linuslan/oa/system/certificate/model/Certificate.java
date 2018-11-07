package com.linuslan.oa.system.certificate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 公司证件
 * @author LinusLan
 *
 */
@Entity
@Table(name="sys_certificate")
public class Certificate {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysCertificateSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysCertificateSeq", sequenceName="sys_certificate_seq")
	private Long id;
	
	@Column(name="company")
	private String company;
	
	/**
	 * 证件名称
	 */
	@Column(name="name")
	private String name;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="rest_time")
	private Double restTime;
	
	@Column(name="inspection_date")
	private Date inspectionDate;
	
	/**
	 * 文件状态，1：正常；2：过期；0：即将到期；
	 * 3：新过期
	 */
	@Column(name="status")
	private Integer status = 0;
	
	/**
	 * 文件年检状态，1：正常；2：过期；0：即将过期；
	 * 3：新过期
	 */
	@Column(name="inspection_status")
	private Integer inspectionStatus=0;
	
	/**
	 * 是否提醒，0：是；1：否；
	 */
	@Column(name="is_remind")
	private Integer isRemind=0;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getRestTime() {
		return restTime;
	}

	public void setRestTime(Double restTime) {
		this.restTime = restTime;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(Integer inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public Integer getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
