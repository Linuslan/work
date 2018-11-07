package com.linuslan.oa.workflow.flows.supplier.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.area.model.Area;
import com.linuslan.oa.workflow.flows.area.service.IAreaService;
import com.linuslan.oa.workflow.flows.supplier.model.Supplier;
import com.linuslan.oa.workflow.flows.supplier.service.ISupplierService;

@Controller
public class SupplierAction extends BaseAction {

	@Autowired
	private ISupplierService supplierService;
	
	@Autowired
	private IAreaService areaService;
	
	private List<Area> areas;
	
	private Supplier supplier;
	
	private Page<Supplier> pageData;
	
	public void queryPage() {
		try {
			this.pageData = this.supplierService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.supplier = this.supplierService.queryById(this.supplier.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.areas = this.areaService.queryAll();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.supplierService.valid(this.supplier);
			if(this.supplierService.add(this.supplier)) {
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
			this.supplierService.valid(this.supplier);
			if(this.supplierService.update(supplier)) {
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
			if(null == this.supplier || null == this.supplier.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Supplier persist = this.supplierService.queryById(this.supplier.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.supplierService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Page<Supplier> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Supplier> pageData) {
		this.pageData = pageData;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}
