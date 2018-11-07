package com.linuslan.oa.workflow.flows.salary.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;

public class DepartmentSalary {
	private Long id;
	
	private String name;
	
	private List<SalaryContent> list = new ArrayList<SalaryContent> ();
	
	private List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>> ();

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

	public List<SalaryContent> getList() {
		return list;
	}

	public void setList(List<SalaryContent> list) {
		this.list = list;
	}

	public List<Map<String, Object>> getMapList() {
		return mapList;
	}

	public void setMapList(List<Map<String, Object>> mapList) {
		this.mapList = mapList;
	}
}
