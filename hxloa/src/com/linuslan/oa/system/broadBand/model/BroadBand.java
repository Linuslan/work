package com.linuslan.oa.system.broadBand.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="sys_broad_band")
public class BroadBand {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysBroadBandSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysBroadBandSeq", sequenceName="sys_broad_band_seq")
	private Long id;
	
	/**
	 * 业务号
	 */
	@Column(name="business_no")
	private String businessNo;
	
	/**
	 * 宽带类型
	 */
	@Column(name="type_name")
	private String typeName;
	
	/**
	 * 月租
	 */
	@Column(name="monthly_rent")
	private String monthlyRent;
	
	/**
	 * 带宽
	 */
	@Column(name="kbps")
	private String kbps;
	
	/**
	 * ip
	 */
	@Column(name="ips")
	private String ips;
	
	@Column(name="info")
	private String info;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;
	
	@Column(name="is_remind")
	private Integer isRemind = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(String monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public String getKbps() {
		return kbps;
	}

	public void setKbps(String kbps) {
		this.kbps = kbps;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}
	
}
