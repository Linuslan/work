package com.linuslan.oa.workflow.flows.unit.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.unit.model.Unit;
import com.linuslan.oa.workflow.flows.unit.service.IUnitService;

@Controller
public class UnitAction extends BaseAction {

	@Autowired
	private IUnitService unitService;
	
	private Page<Unit> pageData;
	
	private Unit unit;
	
	public void queryPage() {
		try {
			this.pageData = this.unitService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.unit = this.unitService.queryById(this.unit.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.unitService.valid(this.unit);
			if(this.unitService.add(this.unit)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败！");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void update() {
		try {
			this.unitService.valid(this.unit);
			if(this.unitService.update(unit)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep(this.failureMsg);
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void del() {
		try {
			if(null == this.unit || null == this.unit.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Unit persist = this.unitService.queryById(this.unit.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.unitService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<Unit> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Unit> pageData) {
		this.pageData = pageData;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
}
