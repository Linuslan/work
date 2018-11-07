package com.linuslan.oa.workflow.flows.sale.action;

import java.util.ArrayList;
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
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.system.account.service.IAccountService;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerReceiverService;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;
import com.linuslan.oa.workflow.flows.customer.service.ISpecialCustomerService;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSaleContent;
import com.linuslan.oa.workflow.flows.sale.service.ISpecialSaleService;

@Controller
public class SpecialSaleAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(SpecialSaleAction.class);
	
	@Autowired
	private ISpecialSaleService specialSaleService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISpecialCustomerService specialCustomerService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private ICustomerReceiverService customerReceiverService;
	
	private Page<SpecialSale> pageData;
	
	private String serialNo;
	
	private SpecialSale specialSale;
	
	private SpecialSaleContent specialSaleContent;
	
	private List<Dictionary> payTypes;
	
	private List<Dictionary> deliverTypes;
	
	private List<Dictionary> invoiceTypes;
	
	private List<SpecialSaleContent> contents;
	
	private List<Company> companys;
	
	//private List<SpecialCustomer> customers;
	private List<Customer> customers;
	
	private List<Account> accounts;
	
	private List<CustomerReceiver> receivers;
	
	public void queryPage() {
		try {
			this.pageData = this.specialSaleService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户采购申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.specialSaleService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.specialSaleService.queryAuditedPage(paramMap, page, rows);
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
			if(null != this.specialSale && null != this.specialSale.getId()) {
				this.specialSale = this.specialSaleService.queryById(this.specialSale.getId());
				if("statement".equals(this.returnType) || "checkout".equals(this.returnType)) {
					this.contents = this.specialSaleService.queryContentsBySpecialSaleId(this.specialSale.getId());
				}
				if(null != this.specialSale && null != this.specialSale.getCustomerId()) {
					this.receivers = this.customerReceiverService.queryByCustomerId(this.specialSale.getCustomerId());
				}
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(SpecialSale.class.getSimpleName());
			}
			this.invoiceTypes = this.dictionaryService.queryByPid(5l);
			this.payTypes = this.dictionaryService.queryByPid(38l);
			this.companys = this.companyService.queryAllCompanys();
			this.deliverTypes = this.dictionaryService.queryByPid(45l);
			//this.customers = this.specialCustomerService.queryAll();
			this.customers = this.customerService.queryByUser(HttpUtil.getLoginUser().getId());
			this.accounts = this.accountService.queryByType(2);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.payTypes = this.dictionaryService.queryByPid(38l);
			this.deliverTypes = this.dictionaryService.queryByPid(45l);
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("payTypeId", payTypes);
			maps.put("deliverTypeId", deliverTypes);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化销售查询选项异常", ex);
		}
	}
	
	public void queryContentsBySpecialSaleId() {
		try {
			List<SpecialSaleContent> contents = this.specialSaleService.queryContentsBySpecialSaleId(this.specialSale.getId());
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
			if(this.specialSaleService.add(specialSale, contents)) {
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
			if(this.specialSaleService.update(specialSale, contents)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void updateSale() {
		try {
			if(null == specialSale || null == specialSale.getId()) {
				CodeUtil.throwExcep("数据异常");
			}
			SpecialSale persist = this.specialSaleService.queryById(this.specialSale.getId());
			BeanUtil.updateBean(persist, this.specialSale);
			if(this.specialSaleService.update(persist)) {
				this.setupSuccessMap("保存成功");
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
			if(this.specialSaleService.del(specialSale)) {
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
			String type = "update";
			try {
				type = HttpUtil.getRequest().getParameter("type");
			} catch(Exception ex) {
				type = "update";
			}
			boolean isUpdate = false;
			if("update".equals(type)) {
				isUpdate = true;
			}
			if(this.specialSaleService.commit(specialSale, contents, passType, opinion, isUpdate)) {
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
			if(this.specialSaleService.audit(specialSale, passType, opinion)) {
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
			if(this.specialSaleService.delContentById(this.specialSaleContent.getId())) {
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
	
	public void updateContentBatch() {
		try {
			if(null != contents && contents.size() > 0) {
				String idStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
				List<SpecialSaleContent> saleContents = this.specialSaleService.queryContentsInIds(ids);
				List<String> unSetKeys = new ArrayList<String> ();
				unSetKeys.add("isDelete");
				unSetKeys.add("height");
				unSetKeys.add("width");
				unSetKeys.add("quantity");
				unSetKeys.add("orderNo");
				List<SpecialSaleContent> lists = (List<SpecialSaleContent>) BeanUtil.updateBeans(saleContents, contents, "id", null, unSetKeys);
				if(this.specialSaleService.mergeContents(lists)) {
					this.setupSuccessMap("保存成功");
				} else {
					CodeUtil.throwExcep("保存失败");
				}
			} else {
				CodeUtil.throwExcep("未获取到项目");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public SpecialSale getSpecialSale() {
		return specialSale;
	}

	public void setSpecialSale(SpecialSale specialSale) {
		this.specialSale = specialSale;
	}

	public SpecialSaleContent getSpecialSaleContent() {
		return specialSaleContent;
	}

	public void setSpecialSaleContent(SpecialSaleContent specialSaleContent) {
		this.specialSaleContent = specialSaleContent;
	}

	public List<SpecialSaleContent> getContents() {
		return contents;
	}

	public void setContents(List<SpecialSaleContent> contents) {
		this.contents = contents;
	}

	public Page<SpecialSale> getPageData() {
		return pageData;
	}

	public void setPageData(Page<SpecialSale> pageData) {
		this.pageData = pageData;
	}

	public List<Dictionary> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<Dictionary> payTypes) {
		this.payTypes = payTypes;
	}

	public List<Dictionary> getInvoiceTypes() {
		return invoiceTypes;
	}

	public void setInvoiceTypes(List<Dictionary> invoiceTypes) {
		this.invoiceTypes = invoiceTypes;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Dictionary> getDeliverTypes() {
		return deliverTypes;
	}

	public void setDeliverTypes(List<Dictionary> deliverTypes) {
		this.deliverTypes = deliverTypes;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public List<CustomerReceiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<CustomerReceiver> receivers) {
		this.receivers = receivers;
	}
}
