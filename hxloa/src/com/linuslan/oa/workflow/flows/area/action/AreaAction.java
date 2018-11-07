package com.linuslan.oa.workflow.flows.area.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.area.model.Area;
import com.linuslan.oa.workflow.flows.area.service.IAreaService;

@Controller
public class AreaAction extends BaseAction {
	
	@Autowired
	private IAreaService areaService;
	
	private Area area;
	
	private Page<Area> pageData;
	
	public void queryPage() {
		try {
			this.pageData = this.areaService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.area = this.areaService.queryById(this.area.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.areaService.valid(this.area);
			if(this.areaService.add(this.area)) {
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
			this.areaService.valid(this.area);
			if(this.areaService.update(area)) {
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
			if(null == this.area || null == this.area.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Area persist = this.areaService.queryById(this.area.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.areaService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Page<Area> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Area> pageData) {
		this.pageData = pageData;
	}
}
