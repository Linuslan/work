package com.linuslan.oa.workflow.flows.customer.action;

import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.SpecialCustomer;
import com.linuslan.oa.workflow.flows.customer.service.ISpecialCustomerService;

@Controller
public class SpecialCustomerAction extends BaseAction {
	
	@Autowired
	private ISpecialCustomerService specialCustomerService;
	
	private SpecialCustomer specialCustomer;
	
	private Page<SpecialCustomer> pageData;
	
	public void queryPage() {
		try {
			this.pageData = this.specialCustomerService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.specialCustomer = this.specialCustomerService.queryById(this.specialCustomer.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.specialCustomer.setCreateDate(new Date());
			this.specialCustomer.setCreateUserId(HttpUtil.getLoginUser().getId());
			this.specialCustomerService.valid(this.specialCustomer);
			if(this.specialCustomerService.add(this.specialCustomer)) {
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
			this.specialCustomerService.valid(this.specialCustomer);
			if(this.specialCustomerService.update(specialCustomer)) {
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
			if(null == this.specialCustomer || null == this.specialCustomer.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			SpecialCustomer persist = this.specialCustomerService.queryById(this.specialCustomer.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.specialCustomerService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public SpecialCustomer getSpecialCustomer() {
		return specialCustomer;
	}

	public void setSpecialCustomer(SpecialCustomer specialCustomer) {
		this.specialCustomer = specialCustomer;
	}

	public Page<SpecialCustomer> getPageData() {
		return pageData;
	}

	public void setPageData(Page<SpecialCustomer> pageData) {
		this.pageData = pageData;
	}
	
}
