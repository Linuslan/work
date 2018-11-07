package com.linuslan.oa.workflow.flows.sale.action;

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
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;
import com.linuslan.oa.workflow.flows.sale.model.Sale;
import com.linuslan.oa.workflow.flows.sale.model.SaleContent;
import com.linuslan.oa.workflow.flows.sale.service.ISaleService;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.service.IWarehouseService;

@Controller
public class SaleAction extends BaseAction {

	private static Logger logger = Logger.getLogger(SaleAction.class);
	
	@Autowired
	private ISaleService saleService;
	
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
	
	private Sale sale;
	
	private List<Customer> customers;
	
	private List<Company> companys;
	
	private List<Dictionary> dictionaries;
	
	private List<CheckoutArticle> checkoutArticles;
	
	private Page<Sale> pageData;
	
	private List<SaleContent> contents;
	
	private String serialNo;
	
	private SaleContent saleContent;
	
	private List<Warehouse> warehouses;
	
	public void queryPage() {
		try {
			this.pageData = this.saleService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户销售申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.saleService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.saleService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.sale && null != this.sale.getId()) {
				this.sale = this.saleService.queryById(this.sale.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(Sale.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.customers = this.customerService.queryByUser(HttpUtil.getLoginUser().getId());
			this.dictionaries = this.dictionaryService.queryByPid(5L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			//this.customers = this.customerService.queryAll();
			this.customers = this.customerService.queryByUser(HttpUtil.getLoginUser().getId());
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("customerId", customers);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化开票查询选项异常", ex);
		}
	}
	
	public void queryContentsBySaleId() {
		try {
			List<SaleContent> contents = this.saleService.queryContentsBySaleId(this.sale.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void add() {
		try {
			if(this.saleService.add(sale, contents)) {
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
			if(this.saleService.update(sale, contents)) {
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
			if(this.saleService.del(sale)) {
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
			if(this.saleService.commit(sale, contents, passType, opinion)) {
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
			if(this.saleService.audit(sale, contents, passType, opinion)) {
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
			if(this.saleService.delContentById(this.saleContent.getId())) {
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

	public Page<Sale> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Sale> pageData) {
		this.pageData = pageData;
	}

	public List<SaleContent> getContents() {
		return contents;
	}

	public void setContents(List<SaleContent> contents) {
		this.contents = contents;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public SaleContent getSaleContent() {
		return saleContent;
	}

	public void setSaleContent(SaleContent saleContent) {
		this.saleContent = saleContent;
	}

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public List<CheckoutArticle> getCheckoutArticles() {
		return checkoutArticles;
	}

	public void setCheckoutArticles(List<CheckoutArticle> checkoutArticles) {
		this.checkoutArticles = checkoutArticles;
	}
	
}
