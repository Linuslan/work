package com.linuslan.oa.workflow.flows.salary.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Lazy;

import com.linuslan.oa.common.BaseFlow;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

@Entity
@Table(name="wf_salary")
public class Salary extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSalarySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSalarySeq", sequenceName="wf_salary_seq")
	private Long id;
	
	@Column(name="year")
	private Integer year;
	
	@Column(name="month")
	private Integer month;
	
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	/**
	 * 应发合计
	 */
	//@Formula("(SELECT SUM(t.supposed_total_salary) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal supposedSum;
	
	/**
	 * 实发合计
	 */
	//@Formula("(SELECT SUM(t.actual_total_salary) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal actualSum;
	
	//@Formula("(SELECT SUM(t.travel_allowance) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal travelAllowanceSum;
	
	//@Formula("(SELECT SUM(t.meal_subsidy) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal mealSubsidySum;
	
	//@Formula("(SELECT SUM(t.tel_charge) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal telChargeSum;
	
	//@Formula("(SELECT SUM(t.housing_subsidy) FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")
	@Transient
	private BigDecimal housingSubsidySum;
	
	/**
	 * 福利合计
	 */
	/*@Formula("(SELECT SUM(t.travel_allowance)" +
			"+SUM(t.meal_subsidy)+SUM(t.tel_charge)+SUM(t.housing_subsidy)" +
			" FROM wf_salary_content t WHERE t.salary_id=id AND t.is_delete=0)")*/
	@Transient
	private BigDecimal benefitSum;
	
	/*@Formula("(SELECT SUM(t.company_social_insurance) FROM wf_salary_content t" +
			" WHERE t.salary_id=id AND t.is_delete=0)")*/
	@Transient
	private BigDecimal socialInsuranceSum;
	
	/*@Formula("(SELECT SUM(t.company_health_insurance) FROM wf_salary_content t" +
			" WHERE t.salary_id=id AND t.is_delete=0)")*/
	@Transient
	private BigDecimal healthInsuranceSum;
	
	/*@Formula("(SELECT SUM(t.company_health_insurance)" +
			"+SUM(t.company_social_insurance) FROM wf_salary_content t" +
			" WHERE t.salary_id=id AND t.is_delete=0)")*/
	@Transient
	private BigDecimal insuranceSum;
	
	/**
	 * 总合计
	 * 公司投保+福利合计+应发工资
	 */
	/*@Formula("(SELECT SUM(t.company_health_insurance)" +
			"+SUM(t.company_social_insurance)+SUM(t.travel_allowance)" +
			"+SUM(t.meal_subsidy)+SUM(t.tel_charge)+SUM(t.housing_subsidy)" +
			"+SUM(t.supposed_total_salary) FROM wf_salary_content t" +
			" WHERE t.salary_id=id AND t.is_delete=0)")*/
	@Transient
	private BigDecimal totalSum;
	
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='salary' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	/**
	 * 流程状态
	 * 
	 */
	@Transient
	private Integer flowStatus;
	
	@OneToMany(targetEntity=SalaryContent.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="salary_id", referencedColumnName="id")
	@Where(clause="is_delete=0")
	private List<SalaryContent> contents;
	
	public List<AuditLog> getAuditLogs() {
		Map<String, Boolean> map = EngineUtil.checkAuditBtn(this.auditLogs);
		this.passBtn = map.get("passBtn");
		this.rejectBtn = map.get("rejectBtn");
		return auditLogs;
	}

	public void setAuditLogs(List<AuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public String getAuditors() {
		this.auditors = EngineUtil.getAuditors(auditLogs);
		return auditors;
	}

	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}
	
	public Integer getFlowStatus() {
		this.flowStatus = EngineUtil.getStatus(auditLogs);
		return this.flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<SalaryContent> getContents() {
		return contents;
	}

	public void setContents(List<SalaryContent> contents) {
		this.contents = contents;
	}

	public BigDecimal getSupposedSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			supposedSum = new BigDecimal("0");
			BigDecimal supposed = null;
			while(iter.hasNext()) {
				sc = iter.next();
				supposed = sc.getSupposedTotalSalary();
				try {
					supposedSum = supposedSum.add(supposed);
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return supposedSum;
	}

	public void setSupposedSum(BigDecimal supposedSum) {
		this.supposedSum = supposedSum;
	}

	public BigDecimal getActualSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			actualSum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					actualSum = actualSum.add(sc.getActualTotalSalary());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return actualSum;
	}

	public void setActualSum(BigDecimal actualSum) {
		this.actualSum = actualSum;
	}

	public BigDecimal getTravelAllowanceSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			travelAllowanceSum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					travelAllowanceSum = travelAllowanceSum.add(sc.getTravelAllowance());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return travelAllowanceSum;
	}

	public void setTravelAllowanceSum(BigDecimal travelAllowanceSum) {
		this.travelAllowanceSum = travelAllowanceSum;
	}

	public BigDecimal getMealSubsidySum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			mealSubsidySum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					mealSubsidySum = mealSubsidySum.add(sc.getMealSubsidy());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return mealSubsidySum;
	}

	public void setMealSubsidySum(BigDecimal mealSubsidySum) {
		this.mealSubsidySum = mealSubsidySum;
	}

	public BigDecimal getTelChargeSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			telChargeSum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					telChargeSum = telChargeSum.add(sc.getTelCharge());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return telChargeSum;
	}

	public void setTelChargeSum(BigDecimal telChargeSum) {
		this.telChargeSum = telChargeSum;
	}

	public BigDecimal getHousingSubsidySum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			housingSubsidySum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					housingSubsidySum = housingSubsidySum.add(sc.getHousingSubsidy());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return housingSubsidySum;
	}

	public void setHousingSubsidySum(BigDecimal housingSubsidySum) {
		this.housingSubsidySum = housingSubsidySum;
	}

	public BigDecimal getBenefitSum() {
		try {
			benefitSum = new BigDecimal("0");
			benefitSum = benefitSum.add(this.getTelChargeSum())
			.add(this.getTravelAllowanceSum())
			.add(this.getMealSubsidySum())
			.add(this.getHousingSubsidySum());
		} catch(Exception ex) {
			//ex.printStackTrace();
		}
		return benefitSum;
	}

	public void setBenefitSum(BigDecimal benefitSum) {
		this.benefitSum = benefitSum;
	}

	public BigDecimal getSocialInsuranceSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			socialInsuranceSum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					socialInsuranceSum = socialInsuranceSum.add(sc.getCompanySocialInsurance());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return socialInsuranceSum;
	}

	public void setSocialInsuranceSum(BigDecimal socialInsuranceSum) {
		this.socialInsuranceSum = socialInsuranceSum;
	}

	public BigDecimal getHealthInsuranceSum() {
		try {
			Iterator<SalaryContent> iter = this.contents.iterator();
			SalaryContent sc = null;
			healthInsuranceSum = new BigDecimal("0");
			while(iter.hasNext()) {
				sc = iter.next();
				try {
					healthInsuranceSum = healthInsuranceSum.add(sc.getCompanyHealthInsurance());
				} catch(Exception ex) {
					//ex.printStackTrace();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return healthInsuranceSum;
	}

	public void setHealthInsuranceSum(BigDecimal healthInsuranceSum) {
		this.healthInsuranceSum = healthInsuranceSum;
	}

	public BigDecimal getInsuranceSum() {
		try {
			this.insuranceSum = new BigDecimal("0");
			insuranceSum = this.insuranceSum.add(this.getHealthInsuranceSum()).add(this.getSocialInsuranceSum());
		} catch(Exception ex) {
			//ex.printStackTrace();
		}
		return insuranceSum;
	}

	public void setInsuranceSum(BigDecimal insuranceSum) {
		this.insuranceSum = insuranceSum;
	}

	public BigDecimal getTotalSum() {
		try {
			this.totalSum = new BigDecimal("0");
			totalSum = this.totalSum.add(this.getBenefitSum()).add(this.getInsuranceSum()).add(this.getSupposedSum());
		} catch(Exception ex) {
			//ex.printStackTrace();
		}
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}
}
