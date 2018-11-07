package com.linuslan.oa.workflow.flows.checkout.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.checkout.model.Checkout;
import com.linuslan.oa.workflow.flows.checkout.model.CheckoutContent;
import com.linuslan.oa.workflow.flows.checkout.service.ICheckoutService;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.service.IWarehouseService;

@Controller
public class CheckoutAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(CheckoutAction.class);
	
	@Autowired
	private ICheckoutService checkoutService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IUserService userService;
	
	private Checkout checkout;
	
	private List<Customer> customers;
	
	private List<Company> companys;
	
	private List<Dictionary> dictionaries;
	
	private List<CheckoutArticle> checkoutArticles;
	
	private Page<Checkout> pageData;
	
	private List<CheckoutContent> contents;
	
	private String serialNo;
	
	private CheckoutContent checkoutContent;
	
	private List<Warehouse> warehouses;
	
	public void queryPage() {
		try {
			this.pageData = this.checkoutService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户出库申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.checkoutService.queryAuditPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditedPage() {
		try {
			this.pageData = this.checkoutService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.checkout && null != this.checkout.getId()) {
				this.checkout = this.checkoutService.queryById(this.checkout.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(Checkout.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.customers = this.customerService.queryAll();
			this.dictionaries = this.dictionaryService.queryByPid(35L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryContentsByCheckoutId() {
		try {
			List<CheckoutContent> contents = this.checkoutService.queryContentsByCheckoutId(this.checkout.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.customers = this.customerService.queryAll();
			this.dictionaries = this.dictionaryService.queryByPid(35L);
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("warehouseId", warehouses);
			maps.put("customerId", customers);
			maps.put("checkoutTypeId", dictionaries);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化入库查询选项异常", ex);
		}
	}
	
	public void add() {
		try {
			if(this.checkoutService.add(checkout, contents)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void update() {
		try {
			if(this.checkoutService.update(checkout, contents)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void del() {
		try {
			if(this.checkoutService.del(checkout)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void commit() {
		try {
			if(this.checkoutService.commit(checkout, contents, passType, opinion)) {
				this.setupSuccessMap("提交成功");
			} else {
				CodeUtil.throwExcep("提交失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void audit() {
		try {
			if(this.checkoutService.audit(checkout, passType, opinion)) {
				this.setupSuccessMap("审核成功");
			} else {
				CodeUtil.throwExcep("审核失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void delContentById() {
		try {
			if(this.checkoutService.delContentById(this.checkoutContent.getId())) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public List<CheckoutArticle> getCheckoutArticles() {
		return checkoutArticles;
	}

	public void setCheckoutArticles(List<CheckoutArticle> checkoutArticles) {
		this.checkoutArticles = checkoutArticles;
	}

	public Page<Checkout> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Checkout> pageData) {
		this.pageData = pageData;
	}

	public List<CheckoutContent> getContents() {
		return contents;
	}

	public void setContents(List<CheckoutContent> contents) {
		this.contents = contents;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public CheckoutContent getCheckoutContent() {
		return checkoutContent;
	}

	public void setCheckoutContent(CheckoutContent checkoutContent) {
		this.checkoutContent = checkoutContent;
	}

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Checkout getCheckout() {
		return checkout;
	}

	public void setCheckout(Checkout checkout) {
		this.checkout = checkout;
	}
}
