package com.linuslan.oa.workflow.flows.socialInsurance.model;

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
@Table(name="wf_social_insurance_content")
public class SocialInsuranceContent {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSocialInsuranceContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSocialInsuranceContentSeq", sequenceName="wf_social_insurance_cont_seq")
	private Long id;
	
	@Column(name="insurance_id")
	private Long insuranceId;
	
	@Column(name="order_no")
	private Integer orderNo;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="id_no")
	private String idNo;
	
	@Column(name="company")
	private String company;
	
	/*****************公司应缴社保*******************/
	
	/**
	 * 养老保险缴费基数
	 */
	@Column(name="company_endowment_basic_charge")
	private BigDecimal companyEndowmentBasicCharge;
	
	/**
	 * 养老险缴费比率
	 */
	@Column(name="company_endowment_rate")
	private String companyEndowmentRate;
	
	/**
	 * 养老险缴纳金额
	 */
	@Column(name="company_endowment_charge")
	private BigDecimal companyEndowmentCharge;
	
	/**
	 * 失业险缴费基数
	 */
	@Column(name="company_unemployment_basic")
	private BigDecimal companyUnemploymentBasicCharge;
	
	/**
	 * 失业险缴费比率
	 */
	@Column(name="company_unemployment_rate")
	private String companyUnemploymentRate;
	
	/**
	 * 失业险缴费金额
	 */
	@Column(name="company_unemployment_charge")
	private BigDecimal companyUnemploymentCharge;
	
	/**
	 * 工伤险缴费基数
	 */
	@Column(name="comp_employment_injury_basic")
	private BigDecimal companyEmploymentInjuryBasicCharge;
	
	/**
	 * 工伤险缴费比率
	 */
	@Column(name="company_employment_injury_rate")
	private String companyEmploymentInjuryRate;
	
	/**
	 * 工伤险缴费金额
	 */
	@Column(name="comp_employment_injury_charge")
	private BigDecimal companyEmploymentInjuryCharge;
	
	/**
	 * 单位缴费合计
	 */
	@Column(name="company_sum")
	private BigDecimal companySum;
	
	/******************************个人应缴社保各险种*********************************/
	/**
	 * 养老险缴费基数
	 */
	@Column(name="user_endowment_basic_charge")
	private BigDecimal userEndowmentBasicCharge;
	
	/**
	 * 养老险缴费比率
	 */
	@Column(name="user_endowment_rate")
	private String userEndowmentRate;
	
	/**
	 * 养老险缴纳金额
	 */
	@Column(name="user_endowment_charge")
	private BigDecimal  userEndowmentCharge;
	
	/**
	 * 失业险缴纳基数
	 */
	@Column(name="user_unemployment_basic_charge")
	private BigDecimal userUnemploymentBasicCharge;
	
	/**
	 * 失业险缴费比率
	 */
	@Column(name="user_unemployment_rate")
	private String userUnemploymentRate;
	
	/**
	 * 失业险缴纳金额
	 */
	@Column(name="user_unemployment_charge")
	private BigDecimal userUnemploymentCharge;
	
	/**
	 * 工伤险缴纳金额
	 */
	@Column(name="user_employment_injury_charge")
	private BigDecimal userEmploymentInjuryCharge;
	
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
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 是否删除
	 * 0：未删除；
	 * 1：已删除；
	 */
	@Column(name="is_delete")
	private Integer isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public BigDecimal getCompanyEndowmentBasicCharge() {
		double money = companyEndowmentBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyEndowmentBasicCharge(
			BigDecimal companyEndowmentBasicCharge) {
		this.companyEndowmentBasicCharge = companyEndowmentBasicCharge;
	}

	public String getCompanyEndowmentRate() {
		return companyEndowmentRate;
	}

	public void setCompanyEndowmentRate(String companyEndowmentRate) {
		this.companyEndowmentRate = companyEndowmentRate;
	}

	public BigDecimal getCompanyEndowmentCharge() {
		double money = companyEndowmentCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyEndowmentCharge(BigDecimal companyEndowmentCharge) {
		this.companyEndowmentCharge = companyEndowmentCharge;
	}

	public BigDecimal getCompanyUnemploymentBasicCharge() {
		double money = companyUnemploymentBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyUnemploymentBasicCharge(
			BigDecimal companyUnemploymentBasicCharge) {
		this.companyUnemploymentBasicCharge = companyUnemploymentBasicCharge;
	}

	public String getCompanyUnemploymentRate() {
		return companyUnemploymentRate;
	}

	public void setCompanyUnemploymentRate(String companyUnemploymentRate) {
		this.companyUnemploymentRate = companyUnemploymentRate;
	}

	public BigDecimal getCompanyUnemploymentCharge() {
		double money = companyUnemploymentCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyUnemploymentCharge(BigDecimal companyUnemploymentCharge) {
		this.companyUnemploymentCharge = companyUnemploymentCharge;
	}

	public BigDecimal getCompanyEmploymentInjuryBasicCharge() {
		double money = companyEmploymentInjuryBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyEmploymentInjuryBasicCharge(
			BigDecimal companyEmploymentInjuryBasicCharge) {
		this.companyEmploymentInjuryBasicCharge = companyEmploymentInjuryBasicCharge;
	}

	public String getCompanyEmploymentInjuryRate() {
		return companyEmploymentInjuryRate;
	}

	public void setCompanyEmploymentInjuryRate(String companyEmploymentInjuryRate) {
		this.companyEmploymentInjuryRate = companyEmploymentInjuryRate;
	}

	public BigDecimal getCompanyEmploymentInjuryCharge() {
		double money = companyEmploymentInjuryCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanyEmploymentInjuryCharge(
			BigDecimal companyEmploymentInjuryCharge) {
		this.companyEmploymentInjuryCharge = companyEmploymentInjuryCharge;
	}

	public BigDecimal getCompanySum() {
		double money = companySum.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setCompanySum(BigDecimal companySum) {
		this.companySum = companySum;
	}

	public BigDecimal getUserEndowmentBasicCharge() {
		double money = userEndowmentBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserEndowmentBasicCharge(BigDecimal userEndowmentBasicCharge) {
		this.userEndowmentBasicCharge = userEndowmentBasicCharge;
	}

	public String getUserEndowmentRate() {
		return userEndowmentRate;
	}

	public void setUserEndowmentRate(String userEndowmentRate) {
		this.userEndowmentRate = userEndowmentRate;
	}

	public BigDecimal getUserEndowmentCharge() {
		double money = userEndowmentCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserEndowmentCharge(BigDecimal userEndowmentCharge) {
		this.userEndowmentCharge = userEndowmentCharge;
	}

	public BigDecimal getUserUnemploymentBasicCharge() {
		double money = userUnemploymentBasicCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserUnemploymentBasicCharge(
			BigDecimal userUnemploymentBasicCharge) {
		this.userUnemploymentBasicCharge = userUnemploymentBasicCharge;
	}

	public String getUserUnemploymentRate() {
		return userUnemploymentRate;
	}

	public void setUserUnemploymentRate(String userUnemploymentRate) {
		this.userUnemploymentRate = userUnemploymentRate;
	}

	public BigDecimal getUserUnemploymentCharge() {
		double money = userUnemploymentCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserUnemploymentCharge(BigDecimal userUnemploymentCharge) {
		this.userUnemploymentCharge = userUnemploymentCharge;
	}

	public BigDecimal getUserEmploymentInjuryCharge() {
		double money = userEmploymentInjuryCharge.doubleValue();
		String moneyStr = NumberUtil.format(money, "0.00");
		return CodeUtil.parseBigDecimal(moneyStr);
	}

	public void setUserEmploymentInjuryCharge(BigDecimal userEmploymentInjuryCharge) {
		this.userEmploymentInjuryCharge = userEmploymentInjuryCharge;
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

	public Long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
}
