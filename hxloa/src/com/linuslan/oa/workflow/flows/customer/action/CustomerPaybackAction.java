package com.linuslan.oa.workflow.flows.customer.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPayback;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerPaybackService;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;

public class CustomerPaybackAction extends BaseAction {

	private static Logger logger = Logger.getLogger(CustomerPaybackAction.class);

	@Autowired
	private ICustomerPaybackService customerPaybackService;
	@Autowired
	private ICompanyService companyService;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IUserService userService;
	
	private Page<CustomerPayback> pageData;
	private CustomerPayback customerPayback;
	private List<CustomerPayback> customerPaybacks;
	private List<Company> companys;
	private List<Customer> customers;

	public void queryPage() {
		try {
			if(null == this.paramMap) {
				this.paramMap = new HashMap<String, String> ();
			}
			this.paramMap.put("userId", HttpUtil.getLoginUser().getId().toString());
			this.pageData = this.customerPaybackService.queryPage(this.paramMap,
					this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化入库查询选项异常", ex);
		}
	}

	public void queryAll() {
		try {
			List allCustomerPaybacks = this.customerPaybackService.queryAllCustomerPaybacks();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allCustomerPaybacks, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.customerPayback = this.customerPaybackService.queryById(this.customerPayback.getId());
			Map<String, Object> paybackInfo = this.customerPaybackService.countByCompanyIdAndCustomerId(this.customerPayback.getCompanyId(), this.customerPayback.getCustomerId());
			HttpUtil.getRequest().setAttribute("TOTAL_SALE", paybackInfo.get("TOTAL_SALE"));
			HttpUtil.getRequest().setAttribute("TOTAL_PAYBACK", paybackInfo.get("TOTAL_PAYBACK"));
			HttpUtil.getRequest().setAttribute("TOTAL_UNPAYBACK", paybackInfo.get("TOTAL_UNPAYBACK"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.customers = this.customerService.queryByCompanyId(this.customerPayback.getCompanyId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public String queryByUserId() {
		try {
			this.customerPaybacks = this.customerPaybackService.queryByUserId(HttpUtil.getLoginUser().getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "select";
	}
	
	public void countByCompanyIdAndCustomerId() {
		try {
			Long companyId = CodeUtil.parseLong(HttpUtil.getRequest().getParameter("companyId"));
			Long customerId = CodeUtil.parseLong(HttpUtil.getRequest().getParameter("customerId"));
			if(null == companyId || null == customerId) {
				CodeUtil.throwExcep("公司ID和渠道ID无效");
			}
			Map<String, Object> map = this.customerPaybackService.countByCompanyIdAndCustomerId(companyId, customerId);
			this.printMap(map);
		} catch(Exception ex) {
			logger.error("统计公司和客户的回款失败", ex);
		}
	}

	public void add() {
		try {
			this.customerPayback.setUserId(HttpUtil.getLoginUser().getId());
			this.customerPayback.setLogtime(new Date());
			this.customerPaybackService.valid(this.customerPayback);
			if (this.customerPaybackService.add(this.customerPayback))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep("保存失败！");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void update() {
		try {
			this.customerPaybackService.valid(this.customerPayback);
			if (this.customerPaybackService.update(this.customerPayback))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep(this.failureMsg);
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void del() {
		try {
			if ((this.customerPayback == null) || (this.customerPayback.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			CustomerPayback persist = this.customerPaybackService.queryById(this.customerPayback
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.customerPaybackService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<CustomerPayback> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<CustomerPayback> pageData) {
		this.pageData = pageData;
	}

	public CustomerPayback getCustomerPayback() {
		return this.customerPayback;
	}

	public void setCustomerPayback(CustomerPayback customerPayback) {
		this.customerPayback = customerPayback;
	}

	public List<CustomerPayback> getCustomerPaybacks() {
		return customerPaybacks;
	}

	public void setCustomerPaybacks(List<CustomerPayback> customerPaybacks) {
		this.customerPaybacks = customerPaybacks;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	
}
