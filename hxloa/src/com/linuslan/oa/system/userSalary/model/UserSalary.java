package com.linuslan.oa.system.userSalary.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.OAUtil;

@Entity
@Table(name="sys_user_salary")
public class UserSalary {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysUserSalarySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysUserSalarySeq", sequenceName="sys_user_salary_seq")
	private Long id;
	
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	/**
	 * 岗位id
	 */
	@Column(name="post_id")
	private Long postId;
	
	@Formula("(SELECT t.name FROM sys_post t WHERE t.id=post_id)")
	private String postName;
	
	/**
	 * 薪资类型
	 * 对应字典表
	 */
	@Column(name="type_id")
	private Long typeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=type_id)")
	private String typeName;
	
	/**
	 * 住房补贴
	 */
	@Column(name="housing_subsidy")
	private String housingSubsidy;
	
	/**
	 * 基本工资
	 */
	@Column(name="basic_salary")
	private BigDecimal basicSalary;
	
	/**
	 * 绩效工资
	 */
	@Column(name="achievement_salary")
	private BigDecimal achievementSalary;
	
	/**
	 * 社保基数
	 */
	@Column(name="social_insurance")
	private String socialInsurance;
	
	/**
	 * 社保开始时间
	 */
	@Column(name="social_insurance_start_date")
	private Date socialInsuranceStartDate;
	
	/**
	 * 医保基数
	 */
	@Column(name="health_insurance")
	private String healthInsurance;
	
	/**
	 * 医保开始时间
	 */
	@Column(name="health_insurance_start_date")
	private Date healthInsuranceStartDate;
	
	/**
	 * 公积金
	 */
	@Column(name="housing_fund")
	private String housingFund;
	
	/**
	 * 公积金开始时间
	 */
	@Column(name="housing_fund_start_date")
	private Date housingFundStartDate;
	
	/**
	 * 话费补贴
	 */
	@Column(name="tel_charge")
	private String telCharge;
	
	/**
	 * 话费补贴开始时间
	 */
	@Column(name="tel_charge_start_date")
	private Date telChargeStartDate;
	
	/**
	 * 交通补贴
	 */
	@Column(name="travel_allowance")
	private String travelAllowance;
	
	/**
	 * 交通补贴开始时间
	 */
	@Column(name="travel_allowance_start_date")
	private Date travelAllowanceStartDate;
	
	/**
	 * 工作餐补贴
	 */
	@Column(name="meal_subsidy")
	private String mealSubsidy;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 工龄工资
	 */
	@Column(name="service_age_salary")
	private String serviceAgeSalary;
	
	/**
	 * 工龄工资开始时间
	 */
	@Column(name="service_age_salary_start_date")
	private Date serviceAgeSalaryStartDate;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 工资调整生效时间
	 */
	@Column(name="effect_date")
	private Date effectDate;
	
	/**
	 * 生效终止时间
	 */
	@Column(name="end_date")
	private Date endDate;
	
	/**
	 * 生效状态
	 * 0：未生效
	 * 1：已生效
	 */
	@Column(name="status")
	private Integer status;
	
	/**
	 * 是否强制生效
	 * 0：否
	 * 1：是
	 */
	@Column(name="is_force_effect")
	private Integer isForceEffect;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=create_user_id)")
	private String createUserName;
	
	/**
	 * 操作人的ip
	 */
	@Column(name="ip")
	private String ip;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	/**
	 * 岗位工资
	 */
	@Column(name="post_salary")
	private String postSalary;
	
	//试用期工资
	@Column(name="probation_salary")
	private BigDecimal probationSalary;
	
	//试用期开始时间
	@Column(name="probation_start_time")
	private Date probationStartTime;
	
	//试用期结束时间
	@Column(name="probation_end_time")
	private Date probationEndTime;
	
	//住房补贴开始时间
	@Column(name="housing_subsidy_start_time")
	private Date housingSubsidyStartTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getHousingSubsidy() {
		return housingSubsidy;
	}

	public void setHousingSubsidy(String housingSubsidy) {
		this.housingSubsidy = housingSubsidy;
	}

	public BigDecimal getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}

	public BigDecimal getAchievementSalary() {
		return achievementSalary;
	}

	public void setAchievementSalary(BigDecimal achievementSalary) {
		this.achievementSalary = achievementSalary;
	}

	public String getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(String socialInsurance) {
		this.socialInsurance = socialInsurance;
	}

	public Date getSocialInsuranceStartDate() {
		return socialInsuranceStartDate;
	}

	public void setSocialInsuranceStartDate(Date socialInsuranceStartDate) {
		this.socialInsuranceStartDate = socialInsuranceStartDate;
	}

	public String getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public Date getHealthInsuranceStartDate() {
		return healthInsuranceStartDate;
	}

	public void setHealthInsuranceStartDate(Date healthInsuranceStartDate) {
		this.healthInsuranceStartDate = healthInsuranceStartDate;
	}

	public String getHousingFund() {
		return housingFund;
	}

	public void setHousingFund(String housingFund) {
		this.housingFund = housingFund;
	}

	public Date getHousingFundStartDate() {
		return housingFundStartDate;
	}

	public void setHousingFundStartDate(Date housingFundStartDate) {
		this.housingFundStartDate = housingFundStartDate;
	}

	public String getTelCharge() {
		return telCharge;
	}

	public void setTelCharge(String telCharge) {
		this.telCharge = telCharge;
	}

	public Date getTelChargeStartDate() {
		return telChargeStartDate;
	}

	public void setTelChargeStartDate(Date telChargeStartDate) {
		this.telChargeStartDate = telChargeStartDate;
	}

	public String getTravelAllowance() {
		return travelAllowance;
	}

	public void setTravelAllowance(String travelAllowance) {
		this.travelAllowance = travelAllowance;
	}

	public Date getTravelAllowanceStartDate() {
		return travelAllowanceStartDate;
	}

	public void setTravelAllowanceStartDate(Date travelAllowanceStartDate) {
		this.travelAllowanceStartDate = travelAllowanceStartDate;
	}

	public String getMealSubsidy() {
		return mealSubsidy;
	}

	public void setMealSubsidy(String mealSubsidy) {
		this.mealSubsidy = mealSubsidy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getServiceAgeSalary() {
		if(this.getStatus() == 0) {
			return "0";
		} else {
			try {
				return ""+OAUtil.getSeniorityPay(DateUtil.parseDateToStr(this.getServiceAgeSalaryStartDate(), "yyyy-MM-dd"),
						DateUtil.parseDateToStr(new Date(), "yyyy-MM"));
			} catch(Exception ex) {
				return "0";
			}
			
		}
		//return serviceAgeSalary;
	}

	public void setServiceAgeSalary(String serviceAgeSalary) {
		this.serviceAgeSalary = serviceAgeSalary;
	}

	public Date getServiceAgeSalaryStartDate() {
		return serviceAgeSalaryStartDate;
	}

	public void setServiceAgeSalaryStartDate(Date serviceAgeSalaryStartDate) {
		this.serviceAgeSalaryStartDate = serviceAgeSalaryStartDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsForceEffect() {
		return isForceEffect;
	}

	public void setIsForceEffect(Integer isForceEffect) {
		this.isForceEffect = isForceEffect;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getPostSalary() {
		return postSalary;
	}

	public void setPostSalary(String postSalary) {
		this.postSalary = postSalary;
	}

	public BigDecimal getProbationSalary() {
		return probationSalary;
	}

	public void setProbationSalary(BigDecimal probationSalary) {
		this.probationSalary = probationSalary;
	}

	public Date getProbationStartTime() {
		return probationStartTime;
	}

	public void setProbationStartTime(Date probationStartTime) {
		this.probationStartTime = probationStartTime;
	}

	public Date getProbationEndTime() {
		return probationEndTime;
	}

	public void setProbationEndTime(Date probationEndTime) {
		this.probationEndTime = probationEndTime;
	}

	public Date getHousingSubsidyStartTime() {
		return housingSubsidyStartTime;
	}

	public void setHousingSubsidyStartTime(Date housingSubsidyStartTime) {
		this.housingSubsidyStartTime = housingSubsidyStartTime;
	}
}
