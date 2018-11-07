package com.linuslan.oa.workflow.flows.customer.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.area.model.Area;
import com.linuslan.oa.workflow.flows.area.service.IAreaService;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerReceiverService;

@Controller
public class CustomerReceiverAction extends BaseAction {

	@Autowired
	private ICustomerReceiverService customerReceiverService;
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private ICompanyService companyService;
	
	private List<Area> areas;
	
	private List<Company> companys;
	
	private CustomerReceiver customerReceiver;
	
	private Page<CustomerReceiver> pageData;
	
	public void queryPage() {
		try {
			this.pageData = this.customerReceiverService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryByCustomerId() {
		try {
			Long customerId = CodeUtil.parseLong(HttpUtil.getRequest().getParameter("customerId"));
			List<CustomerReceiver> list = this.customerReceiverService.queryByCustomerId(customerId);
			JSONArray json = JSONArray.fromObject(list);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.customerReceiver = this.customerReceiverService.queryById(this.customerReceiver.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.areas = this.areaService.queryAll();
			this.companys = this.companyService.queryByUserId(HttpUtil.getLoginUser().getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.customerReceiverService.valid(this.customerReceiver);
			if(this.customerReceiverService.add(this.customerReceiver)) {
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
			this.customerReceiverService.valid(this.customerReceiver);
			if(this.customerReceiverService.update(customerReceiver)) {
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
			if(null == this.customerReceiver || null == this.customerReceiver.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			CustomerReceiver persist = this.customerReceiverService.queryById(this.customerReceiver.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.customerReceiverService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public CustomerReceiver getCustomerReceiver() {
		return customerReceiver;
	}

	public void setCustomerReceiver(CustomerReceiver customerReceiver) {
		this.customerReceiver = customerReceiver;
	}

	public Page<CustomerReceiver> getPageData() {
		return pageData;
	}

	public void setPageData(Page<CustomerReceiver> pageData) {
		this.pageData = pageData;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}
}
