package com.linuslan.oa.workflow.flows.achievement.model;
import java.util.HashMap;
import java.util.Map;

import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.user.model.User;

/**
 * 职工一整年的绩效列表
 */
public class YearAchievement {
	
	/**
	 * 年份
	 */
	private int year;
	
	/**
	 * 职工
	 */
	private User user;
	
	/**
	 * 职工所在部门
	 */
	private Department department;
	
	/**
	 * 月份对应的绩效列表
	 */
	private Map<Integer, Integer> monthScoreMap = new HashMap<Integer, Integer>();

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Map<Integer, Integer> getMonthScoreMap() {
		return monthScoreMap;
	}

	public void setMonthScoreMap(Map<Integer, Integer> monthScoreMap) {
		this.monthScoreMap = monthScoreMap;
	}
}
