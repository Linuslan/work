package com.linuslan.oa.util;

import java.math.BigDecimal;
import java.util.Date;

import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;

public class OAUtil {
	
	///////////////////工资相关计算////////////////////////////
	
	/**
	 * 获取工龄工资
	 * @param startDateStr 工龄工资生效时间
	 * @param endDateStr 截止时间
	 * @return
	 */
	public static int getSeniorityPay(String startDateStr, String endDateStr) {
		int salary = 0;
		try {
			Date endDate = DateUtil.parseStrToDate(endDateStr, "yyyy-MM");
			Date startDate = DateUtil.parseStrToDate(startDateStr, "yyyy-MM-dd");
			int maxDate = DateUtil.getMaxDayOfMonth(startDateStr);
			int startYear = DateUtil.getYear(startDate);
			int endYear = DateUtil.getYear(endDate);
			int startMonth = DateUtil.getMonth(startDate);
			int endMonth = DateUtil.getMonth(endDate);
			int startDay = DateUtil.getDay(startDate);
			int diffYear = endYear - startYear + 1;
			if(diffYear > 0) {
				if(endMonth > startMonth) {
					salary = diffYear*100;
				} else if(endMonth == startMonth) {
					if(startDay >= 1 && startDay <= 15) {
						salary = diffYear * 100;
					} else if(startDay > 15 && startDay <= maxDate) {
						salary = diffYear*100 -100;
					}
				} else {
					salary = diffYear*100 -100;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		if(salary > 900) {
			salary = 900;
		}
		
		return salary;
	}
	
	/**
	 * 通过绩效分数和标准绩效工资计算出实际绩效工资
	 * @param score
	 * @param supposedAchievementSalary
	 * @return
	 */
	public static String getActualAchievementSalary(double score, String supposedAchievementSalary) {
		String actualAchievementSalary = "0";
		double actualMoney = 0;
		try {
			double money = Float.parseFloat(supposedAchievementSalary);
			if(score >= 95 && score <= 100) {
				actualAchievementSalary = supposedAchievementSalary;
			} else if(score < 95 || score > 100) {
				actualMoney = (score / 100f) * money;
				actualAchievementSalary = actualMoney + "";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return actualAchievementSalary;
	}
	
	/**
	 * 应发合计
	 */
	public static BigDecimal getSupposedTotalSalary(SalaryContent sc) {
		BigDecimal supposedTotalSalary = new BigDecimal("0");
		try {
			supposedTotalSalary = CodeUtil
					.initBigDecimal(sc.getBasicSalary())
					.add(CodeUtil
							.initBigDecimal(sc
									.getActualAchievementSalary()))
					.add(CodeUtil.initBigDecimal(sc.getPostSalary()))
					.add(CodeUtil
							.initBigDecimal(sc.getCommission()))
					.add(CodeUtil
							.initBigDecimal(sc.getFullAttendanceAward()))
					.add(CodeUtil
							.initBigDecimal(sc.getOvertimePay()))
					.add(CodeUtil
							.initBigDecimal(sc.getSeniorityPay()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getSickLeaveDeduct()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getAffairLeaveDeduct()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getLateDeduct()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getPunishDeduct()));
			if(supposedTotalSalary.compareTo(new BigDecimal("0")) < 0) {
				CodeUtil.throwExcep("");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			supposedTotalSalary = new BigDecimal("0");
		}
		return supposedTotalSalary;
	}
	
	/**
	 * 得到个人医社保总和
	 * @param sc
	 * @return
	 */
	public static BigDecimal getTotalInsurance(SalaryContent sc) {
		BigDecimal totalInsurance = new BigDecimal("0");
		try {
			totalInsurance = CodeUtil
					.initBigDecimal(sc.getSocialInsurance())
					.add(CodeUtil
							.initBigDecimal(sc.getHealthInsurance()));
		} catch(Exception ex) {
			ex.printStackTrace();
			totalInsurance = new BigDecimal("0");
		}
		return totalInsurance;
	}
	
	public static BigDecimal getTotalSubsidy(SalaryContent sc) {
		BigDecimal totalSubsidy = new BigDecimal(0);
		try {
			totalSubsidy = sc.getMealSubsidy().add(sc.getTelCharge()).add(sc.getTravelAllowance()).add(sc.getHousingSubsidy());
		} catch(Exception ex) {
			ex.printStackTrace();
			totalSubsidy = new BigDecimal("0");
		}
		return totalSubsidy;
	}
	
	/**
	 * 得到税前工资
	 * @param sc
	 * @return
	 */
	public static BigDecimal getPretaxSalary(SalaryContent sc) {
		BigDecimal salaryBeforeTax = new BigDecimal("0");
		try {
			salaryBeforeTax = CodeUtil
					.initBigDecimal(sc.getSupposedTotalSalary())
					.subtract(CodeUtil
							.initBigDecimal(sc.getTotalInsurance()))
					.add(CodeUtil
							.initBigDecimal(sc.getTotalSubsidy()))
					.add(CodeUtil
							.initBigDecimal(sc.getOther()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getChildcareExpense()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getContinuingEducationFee()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getHousingLoan()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getHousingRent()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getSeriousIllnessExpense()))
					.subtract(CodeUtil
							.initBigDecimal(sc.getAlimony()));
			//如果相减后的数值小于0，则税前工资为0
			//BigDecimal的compareTo方法，如果salaryBeforeTax大于0则返回1
			//如果小于0则返回-1，如果等于0则返回0
			if(salaryBeforeTax.compareTo(new BigDecimal("0")) < 0) {
				CodeUtil.throwExcep("");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			salaryBeforeTax = new BigDecimal("0");
		}
		return salaryBeforeTax;
	}
	
	/**
	 * 得到实发工资，由于华夏蓝暂时没有个税，所以实发工资不扣除个税
	 * @param sc
	 * @return
	 */
	public static BigDecimal getActualTotalSalary(SalaryContent sc) {
		BigDecimal actualTotalSalary = new BigDecimal("0");
		try {
			//2019年，使用新的实发工资的计算公式
			boolean newFormula = false;
			//2019年1月开始使用新工资的计算方式
			if(sc.getYear().intValue() >= 2018) {
				if(sc.getMonth().intValue() >= 1) {
					newFormula = true;
				}
			}
			if(newFormula) {
				actualTotalSalary = CodeUtil.initBigDecimal(sc.getSupposedTotalSalary())
						.subtract(CodeUtil.initBigDecimal(sc.getTotalInsurance()))
						.add(CodeUtil.initBigDecimal(sc.getTotalSubsidy()))
						.add(CodeUtil.initBigDecimal(sc.getOther()))
						.subtract(CodeUtil.initBigDecimal(sc.getTax()));
			} else {
				actualTotalSalary = CodeUtil
						.initBigDecimal(sc.getPretaxSalary())
						.subtract(sc.getTax());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			actualTotalSalary = new BigDecimal("0");
		}
		return actualTotalSalary;
	}
	
	/**
	 * 通过工资获取应交税
	 * @param salary
	 * @return
	 */
	public static BigDecimal getTax(SalaryContent sc) {
		String tax = "0";
		double taxMoney = 0;
		try {
			boolean isNew = true;
			//2018年10月以前的工资按老的税率计算，2018年10月包含10月往后的工资，按新的税率来计算
			if(sc.getYear() <= 2018) {
				if(sc.getMonth() < 10) {
					isNew = false;
				}
			}
			float money = sc.getPretaxSalary().floatValue();
			if(isNew) {
				float balance = money - 5000;
				if(balance <= 3000 && balance > 0) {
					taxMoney = balance*0.03;
				} else if(balance > 3000 && balance <= 12000) {
					taxMoney = balance*0.1 - 210;
				} else if(balance > 12000 && balance <= 25000) {
					taxMoney = balance*0.2 - 1410;
				} else if(balance > 25000 && balance <= 35000) {
					taxMoney = balance*0.25 - 2660;
				} else if(balance > 35000 && balance <= 55000) {
					taxMoney = balance*0.3 - 4410;
				} else if(balance > 55000 && balance <= 80000) {
					taxMoney = balance*0.35 - 7160;
				} else if(balance > 80000) {
					taxMoney = balance*0.45 - 15160;
				}
			} else {
				float balance = money - 3500;
				if(balance <= 1500 && balance > 0) {
					taxMoney = balance*0.03;
				} else if(balance > 1500 && balance <= 4500) {
					taxMoney = balance*0.1 - 105;
				} else if(balance > 4500 && balance <= 9000) {
					taxMoney = balance*0.2 - 555;
				} else if(balance > 9000 && balance <= 35000) {
					taxMoney = balance*0.25 - 1005;
				} else if(balance > 35000 && balance <= 55000) {
					taxMoney = balance*0.3 - 2755;
				} else if(balance > 55000 && balance <= 80000) {
					taxMoney = balance*0.35 - 5505;
				} else if(balance > 80000) {
					taxMoney = balance*0.45 - 13505;
				}
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		tax = taxMoney + "";
		return CodeUtil.parseBigDecimal(tax);
	}
}
