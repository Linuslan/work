package com.linuslan.oa.workflow.flows.salary.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.OAUtil;

@Entity
@Table(name="wf_salary_content")
public class SalaryContent {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSalaryContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSalaryContentSeq", sequenceName="wf_salary_content_seq")
	private Long id;
	
	@Column(name="salary_id")
	private Long salaryId;
	
	@Formula("(SELECT t.year FROM wf_salary t WHERE t.id=salary_id)")
	private Integer year;
	
	@Formula("(SELECT t.month FROM wf_salary t WHERE t.id=salary_id)")
	private Integer month;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	@Column(name="post_id")
	private Long postId;
	
	@Formula("(SELECT t.name FROM sys_post t WHERE t.id=post_id)")
	private String postName;
	
	@Column(name="type_id")
	private Long typeId;
	
	@Formula("(SELECT t.text FROM sys_dictionary t WHERE t.id=type_id)")
	private String typeName;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 应出勤天数
	 */
	@Column(name="supposed_duty_day")
	private Double supposedDutyDay;
	
	/**
	 * 实出勤
	 */
	@Column(name="actual_duty_day")
	private Double actualDutyDay;
	
	/**
	 * 实出勤小时
	 */
	@Column(name="actual_duty_hour")
	private Double actualDutyHour;
	
	@Column(name="basic_salary")
	private BigDecimal basicSalary;
	
	@Column(name="achievement_salary")
	private BigDecimal achievementSalary;
	
	@Column(name="achievement_score")
	private Double achievementScore;
	
	//实时的绩效分数
	@Formula("(SELECT NVL((SELECT NVL(SUM(t2.leader_score), 0) FROM wf_achievement_content t2 WHERE t2.is_delete=0 AND t2.achievement_id=t.id), 0)+NVL(t.add_score, 0) FROM wf_achievement t WHERE t.year=(SELECT t3.year FROM wf_salary t3 WHERE t3.id=salary_id) AND t.month=(SELECT t2.month FROM wf_salary t2 WHERE t2.id=salary_id) AND t.is_delete=0 AND t.status=4 AND t.user_id=user_id)")
	private Double currentAchievementScore;
	
	/**
	 * 实发绩效
	 */
	@Column(name="actual_achievement_salary")
	private BigDecimal actualAchievementSalary;
	
	/**
	 * 抽成
	 */
	@Column(name="commission")
	private BigDecimal commission;
	
	/**
	 * 满勤奖
	 */
	@Column(name="full_attendance_award")
	private BigDecimal fullAttendanceAward;
	
	/**
	 * 加班费
	 */
	@Column(name="overtime_pay")
	private BigDecimal overtimePay;
	
	/**
	 * 工龄工资
	 */
	@Column(name="seniority_pay")
	private BigDecimal seniorityPay;
	
	/**
	 * 病假扣款
	 */
	@Column(name="sick_leave_deduct")
	private BigDecimal sickLeaveDeduct;
	
	/**
	 * 事假扣款
	 */
	@Column(name="affair_leave_deduct")
	private BigDecimal affairLeaveDeduct;
	
	/**
	 * 迟到扣款
	 */
	@Column(name="late_deduct")
	private BigDecimal lateDeduct;
	
	/**
	 * 处罚金
	 */
	@Column(name="punish_deduct")
	private BigDecimal punishDeduct;
	
	/**
	 * 午餐补贴
	 */
	@Column(name="meal_subsidy")
	private BigDecimal mealSubsidy;
	
	/**
	 * 原始的餐补
	 */
	@Column(name="original_meal_subsidy")
	private BigDecimal originalMealSubsidy;
	
	/**
	 * 应发合计
	 */
	@Column(name="supposed_total_salary")
	private BigDecimal supposedTotalSalary;
	
	/**
	 * 税前工资
	 */
	@Column(name="pretax_salary")
	private BigDecimal pretaxSalary;
	
	/**
	 * 个人所得税
	 */
	@Column(name="tax")
	private BigDecimal tax;
	
	@Column(name="social_insurance")
	private BigDecimal socialInsurance;
	
	@Column(name="health_insurance")
	private BigDecimal healthInsurance;
	
	@Column(name="total_insurance")
	private BigDecimal totalInsurance;
	
	/**
	 * 实发合计
	 */
	@Column(name="actual_total_salary")
	private BigDecimal actualTotalSalary;
	
	@Column(name="company_social_insurance")
	private BigDecimal companySocialInsurance;
	
	@Column(name="company_health_insurance")
	private BigDecimal companyHealthInsurance;
	
	@Column(name="tel_charge")
	private BigDecimal telCharge;
	
	@Column(name="travel_allowance")
	private BigDecimal travelAllowance;
	
	@Column(name="other")
	private BigDecimal other;
	
	@Column(name="info")
	private String info;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="housing_subsidy")
	private BigDecimal housingSubsidy;
	
	@Column(name="post_salary")
	private BigDecimal postSalary;
	
	@Transient
	private BigDecimal totalSubsidy;
	
	//子女抚养费
	@Column(name="childcare_expense")
	private BigDecimal childcareExpense;
	
	//继续教育费
	@Column(name="continuing_education_fee")
	private BigDecimal continuingEducationFee;
	
	//大病医疗费
	@Column(name="serious_illness_expense")
	private BigDecimal seriousIllnessExpense;
	
	//住房贷款
	@Column(name="housing_loan")
	private BigDecimal housingLoan;
	
	//房租
	@Column(name="housing_rent")
	private BigDecimal housingRent;
	
	//老人赡养费
	@Column(name="alimony")
	private BigDecimal alimony;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(Long salaryId) {
		this.salaryId = salaryId;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getSupposedDutyDay() {
		return supposedDutyDay;
	}

	public void setSupposedDutyDay(Double supposedDutyDay) {
		this.supposedDutyDay = supposedDutyDay;
	}

	public Double getActualDutyDay() {
		return actualDutyDay;
	}

	public void setActualDutyDay(Double actualDutyDay) {
		this.actualDutyDay = actualDutyDay;
	}

	public Double getActualDutyHour() {
		return actualDutyHour;
	}

	public void setActualDutyHour(Double actualDutyHour) {
		this.actualDutyHour = actualDutyHour;
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

	public Double getAchievementScore() {
		if(null == this.currentAchievementScore || 0 >= this.currentAchievementScore) {
			return this.achievementScore;
		} else {
			return this.currentAchievementScore;
		}
	}

	public void setAchievementScore(Double achievementScore) {
		this.achievementScore = achievementScore;
	}

	/**
	 * 实发绩效工资
	 * 实时去计算
	 * @return
	 */
	public BigDecimal getActualAchievementSalary() {
		try {
			return CodeUtil.parseBigDecimal(
					OAUtil.getActualAchievementSalary(
							this.getAchievementScore().doubleValue(),
							this.getAchievementSalary().toString()));
		} catch(Exception ex) {
			return actualAchievementSalary;
		}
	}

	public void setActualAchievementSalary(BigDecimal actualAchievementSalary) {
		this.actualAchievementSalary = actualAchievementSalary;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getFullAttendanceAward() {
		return fullAttendanceAward;
	}

	public void setFullAttendanceAward(BigDecimal fullAttendanceAward) {
		this.fullAttendanceAward = fullAttendanceAward;
	}

	public BigDecimal getOvertimePay() {
		return overtimePay;
	}

	public void setOvertimePay(BigDecimal overtimePay) {
		this.overtimePay = overtimePay;
	}

	public BigDecimal getSeniorityPay() {
		return seniorityPay;
	}

	public void setSeniorityPay(BigDecimal seniorityPay) {
		this.seniorityPay = seniorityPay;
	}

	public BigDecimal getSickLeaveDeduct() {
		return sickLeaveDeduct;
	}

	public void setSickLeaveDeduct(BigDecimal sickLeaveDeduct) {
		this.sickLeaveDeduct = sickLeaveDeduct;
	}

	public BigDecimal getAffairLeaveDeduct() {
		return affairLeaveDeduct;
	}

	public void setAffairLeaveDeduct(BigDecimal affairLeaveDeduct) {
		this.affairLeaveDeduct = affairLeaveDeduct;
	}

	public BigDecimal getLateDeduct() {
		return lateDeduct;
	}

	public void setLateDeduct(BigDecimal lateDeduct) {
		this.lateDeduct = lateDeduct;
	}

	public BigDecimal getPunishDeduct() {
		return punishDeduct;
	}

	public void setPunishDeduct(BigDecimal punishDeduct) {
		this.punishDeduct = punishDeduct;
	}

	public BigDecimal getMealSubsidy() {
		return mealSubsidy;
	}

	public void setMealSubsidy(BigDecimal mealSubsidy) {
		this.mealSubsidy = mealSubsidy;
	}

	public BigDecimal getSupposedTotalSalary() {
		try {
			return OAUtil.getSupposedTotalSalary(this);
		} catch(Exception ex) {
			return supposedTotalSalary;
		}
	}

	public void setSupposedTotalSalary(BigDecimal supposedTotalSalary) {
		this.supposedTotalSalary = supposedTotalSalary;
	}

	public BigDecimal getPretaxSalary() {
		try {
			return OAUtil.getPretaxSalary(this);
		} catch(Exception ex) {
			return pretaxSalary;
		}
		
	}

	public void setPretaxSalary(BigDecimal pretaxSalary) {
		this.pretaxSalary = pretaxSalary;
	}

	public BigDecimal getTax() {
		try {
			return OAUtil.getTax(this);
		} catch(Exception ex) {
			return tax;
		}
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(BigDecimal socialInsurance) {
		this.socialInsurance = socialInsurance;
	}

	public BigDecimal getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(BigDecimal healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public BigDecimal getTotalInsurance() {
		try {
			return OAUtil.getTotalInsurance(this);
		} catch(Exception ex) {
			return totalInsurance;
		}
	}

	public void setTotalInsurance(BigDecimal totalInsurance) {
		this.totalInsurance = totalInsurance;
	}

	public BigDecimal getActualTotalSalary() {
		try {
			return OAUtil.getActualTotalSalary(this);
		} catch(Exception ex) {
			return actualTotalSalary;
		}
	}

	public void setActualTotalSalary(BigDecimal actualTotalSalary) {
		this.actualTotalSalary = actualTotalSalary;
	}

	public BigDecimal getCompanySocialInsurance() {
		return companySocialInsurance;
	}

	public void setCompanySocialInsurance(BigDecimal companySocialInsurance) {
		this.companySocialInsurance = companySocialInsurance;
	}

	public BigDecimal getCompanyHealthInsurance() {
		return companyHealthInsurance;
	}

	public void setCompanyHealthInsurance(BigDecimal companyHealthInsurance) {
		this.companyHealthInsurance = companyHealthInsurance;
	}

	public BigDecimal getTelCharge() {
		return telCharge;
	}

	public void setTelCharge(BigDecimal telCharge) {
		this.telCharge = telCharge;
	}

	public BigDecimal getTravelAllowance() {
		return travelAllowance;
	}

	public void setTravelAllowance(BigDecimal travelAllowance) {
		this.travelAllowance = travelAllowance;
	}

	public BigDecimal getOther() {
		return other;
	}

	public void setOther(BigDecimal other) {
		this.other = other;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public BigDecimal getHousingSubsidy() {
		return housingSubsidy;
	}

	public void setHousingSubsidy(BigDecimal housingSubsidy) {
		this.housingSubsidy = housingSubsidy;
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

	public Double getCurrentAchievementScore() {
		return currentAchievementScore;
	}

	public void setCurrentAchievementScore(Double currentAchievementScore) {
		this.currentAchievementScore = currentAchievementScore;
	}

	public BigDecimal getOriginalMealSubsidy() {
		return originalMealSubsidy;
	}

	public void setOriginalMealSubsidy(BigDecimal originalMealSubsidy) {
		this.originalMealSubsidy = originalMealSubsidy;
	}

	public BigDecimal getPostSalary() {
		return postSalary;
	}

	public void setPostSalary(BigDecimal postSalary) {
		this.postSalary = postSalary;
	}

	public BigDecimal getTotalSubsidy() {
		try {
			return OAUtil.getTotalSubsidy(this);
		} catch(Exception ex) {
			return totalSubsidy;
		}
	}

	public void setTotalSubsidy(BigDecimal totalSubsidy) {
		this.totalSubsidy = totalSubsidy;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public BigDecimal getChildcareExpense() {
		return childcareExpense;
	}

	public void setChildcareExpense(BigDecimal childcareExpense) {
		this.childcareExpense = childcareExpense;
	}

	public BigDecimal getContinuingEducationFee() {
		return continuingEducationFee;
	}

	public void setContinuingEducationFee(BigDecimal continuingEducationFee) {
		this.continuingEducationFee = continuingEducationFee;
	}

	public BigDecimal getSeriousIllnessExpense() {
		return seriousIllnessExpense;
	}

	public void setSeriousIllnessExpense(BigDecimal seriousIllnessExpense) {
		this.seriousIllnessExpense = seriousIllnessExpense;
	}

	public BigDecimal getHousingLoan() {
		return housingLoan;
	}

	public void setHousingLoan(BigDecimal housingLoan) {
		this.housingLoan = housingLoan;
	}

	public BigDecimal getHousingRent() {
		return housingRent;
	}

	public void setHousingRent(BigDecimal housingRent) {
		this.housingRent = housingRent;
	}

	public BigDecimal getAlimony() {
		return alimony;
	}

	public void setAlimony(BigDecimal alimony) {
		this.alimony = alimony;
	}
}
