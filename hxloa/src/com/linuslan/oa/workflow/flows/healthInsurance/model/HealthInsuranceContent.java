package com.linuslan.oa.workflow.flows.healthInsurance.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.NumberUtil;

@Entity
@Table(name="wf_health_insurance_content")
public class HealthInsuranceContent {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfHealthInsuranceContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfHealthInsuranceContentSeq", sequenceName="wf_health_insurance_cont_seq")
	private Long id;
	
	@Column(name="insurance_id")
	private Long insuranceId;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="company")
	private String company;
	
	/**
	 * 身份证号
	 */
	@Column(name="id_no")
	private String idNo;
	
	/**
	 * 公司医疗基数
	 */
	@Column(name="company_medical_basic_charge")
	private BigDecimal companyMedicalBasicCharge;
	
	/**
	 * 公司缴纳比例
	 */
	@Column(name="company_medical_rate")
	private String companyMedicalRate;
	
	/**
	 * 公司医疗保险缴纳金额
	 */
	@Column(name="company_medical_charge")
	private BigDecimal companyMedicalCharge;
	
	/**
	 * 公司生育保险缴纳基数
	 */
	@Column(name="company_meternity_basic_charge")
	private BigDecimal companyMeternityBasicCharge;
	
	/**
	 * 公司生育保险缴费比率
	 */
	@Column(name="company_meternity_rate")
	private String companyMeternityRate;
	
	/**
	 * 公司生育保险缴纳金额
	 */
	@Column(name="company_meternity_charge")
	private BigDecimal companyMeternityCharge;
	
	/**
	 * 大病补充医疗保险费
	 */
	@Column(name="company_illness_charge")
	private BigDecimal companyIllnessCharge;
	
	@Column(name="company_sum")
	private BigDecimal companySum;
	
	/**
	 * 个人医疗保险缴纳基数
	 */
	@Column(name="user_medical_basic_charge")
	private BigDecimal userMedicalBasicCharge;
	
	/**
	 * 个人医疗保险缴费比率
	 */
	@Column(name="user_medical_rate")
	private String userMedicalRate;
	
	/**
	 * 个人医疗保险缴纳金额
	 */
	@Column(name="user_medical_charge")
	private BigDecimal userMedicalCharge;
	
	/**
	 * 个人生育保险缴纳基数
	 */
	@Column(name="user_meternity_basic_charge")
	private BigDecimal userMeternityBasicCharge;
	
	/**
	 * 个人生育保险缴费比率
	 */
	@Column(name="user_meternity_rate")
	private String userMeternityRate;
	
	/**
	 * 个人生育保险缴纳金额
	 */
	@Column(name="user_meternity_charge")
	private BigDecimal userMeternityCharge;
	
	/**
	 * 个人缴费合计
	 */
	@Column(name="user_sum")
	private BigDecimal userSum;
	
	/**
	 * 总合计
	 */
	@Column(name="total_sum")
	private BigDecimal totalSum;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="is_delete")
	private Integer isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public BigDecimal getCompanyMedicalBasicCharge() {
		double money = companyMedicalBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyMedicalBasicCharge(BigDecimal companyMedicalBasicCharge) {
		this.companyMedicalBasicCharge = companyMedicalBasicCharge;
	}

	public String getCompanyMedicalRate() {
		return companyMedicalRate;
	}

	public void setCompanyMedicalRate(String companyMedicalRate) {
		this.companyMedicalRate = companyMedicalRate;
	}

	public void setCompanyMeternityRate(String companyMeternityRate) {
		this.companyMeternityRate = companyMeternityRate;
	}

	public void setUserMedicalRate(String userMedicalRate) {
		this.userMedicalRate = userMedicalRate;
	}

	public void setUserMeternityRate(String userMeternityRate) {
		this.userMeternityRate = userMeternityRate;
	}

	public BigDecimal getCompanyMedicalCharge() {
		double money = companyMedicalCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyMedicalCharge(BigDecimal companyMedicalCharge) {
		this.companyMedicalCharge = companyMedicalCharge;
	}

	public BigDecimal getCompanyMeternityBasicCharge() {
		double money = companyMeternityBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyMeternityBasicCharge(
			BigDecimal companyMeternityBasicCharge) {
		this.companyMeternityBasicCharge = companyMeternityBasicCharge;
	}

	public BigDecimal getCompanyMeternityCharge() {
		double money = companyMeternityCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyMeternityCharge(BigDecimal companyMeternityCharge) {
		this.companyMeternityCharge = companyMeternityCharge;
	}

	public BigDecimal getCompanyIllnessCharge() {
		double money = companyIllnessCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyIllnessCharge(BigDecimal companyIllnessCharge) {
		this.companyIllnessCharge = companyIllnessCharge;
	}

	public BigDecimal getCompanySum() {
		double money = companySum.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanySum(BigDecimal companySum) {
		this.companySum = companySum;
	}

	public BigDecimal getUserMedicalBasicCharge() {
		double money = userMedicalBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserMedicalBasicCharge(BigDecimal userMedicalBasicCharge) {
		this.userMedicalBasicCharge = userMedicalBasicCharge;
	}

	public BigDecimal getUserMedicalCharge() {
		double money = userMedicalCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserMedicalCharge(BigDecimal userMedicalCharge) {
		this.userMedicalCharge = userMedicalCharge;
	}

	public BigDecimal getUserMeternityBasicCharge() {
		double money = userMeternityBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserMeternityBasicCharge(BigDecimal userMeternityBasicCharge) {
		this.userMeternityBasicCharge = userMeternityBasicCharge;
	}

	public BigDecimal getUserMeternityCharge() {
		double money = userMeternityCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserMeternityCharge(BigDecimal userMeternityCharge) {
		this.userMeternityCharge = userMeternityCharge;
	}

	public BigDecimal getUserSum() {
		double money = userSum.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserSum(BigDecimal userSum) {
		this.userSum = userSum;
	}

	public BigDecimal getTotalSum() {
		double money = totalSum.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getCompanyMeternityRate() {
		return companyMeternityRate;
	}

	public String getUserMedicalRate() {
		return userMedicalRate;
	}

	public String getUserMeternityRate() {
		return userMeternityRate;
	}
}
