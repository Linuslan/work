package com.linuslan.oa.workflow.flows.invoice.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

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
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.invoice.model.Invoice;
import com.linuslan.oa.workflow.flows.invoice.service.IInvoiceService;

@Controller
public class InvoiceAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(InvoiceAction.class);
	
	@Autowired
	private IInvoiceService invoiceService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IUserService userService;
	
	private Page<Invoice> pageData;
	
	private Invoice invoice;
	
	private List<Company> companys;
	
	//查询开票类型
	private List<Dictionary> dictionarys;
	
	private String serialNo;
	
	private List<Account> accounts;
	
	public void queryPage() {
		try {
			this.pageData = this.invoiceService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			jsonConfig.registerDefaultValueProcessor(Long.class,
					new DefaultValueProcessor() {
				public Object getDefaultValue(Class type) {
					return null;
				}
			});
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户开票申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.invoiceService.queryAuditPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			jsonConfig.registerDefaultValueProcessor(Long.class,
					new DefaultValueProcessor() {
				public Object getDefaultValue(Class type) {
					return null;
				}
			});
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditedPage() {
		try {
			this.pageData = this.invoiceService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			jsonConfig.registerDefaultValueProcessor(Long.class,
					new DefaultValueProcessor() {
				public Object getDefaultValue(Class type) {
					return null;
				}
			});
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReportPage() {
		try {
			this.pageData = this.invoiceService.queryReportPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询 开票汇总页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void sumReportPage() {
		try {
			Map<String, Object> map = this.invoiceService.sumReportPage(paramMap);
			JSONObject json = JSONObject.fromObject(map);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("统计开票汇总页面异常", ex);
		}
	}
	
	public String queryById() {
		try {
			if(null != this.invoice && null != this.invoice.getId()) {
				this.invoice = this.invoiceService.queryById(this.invoice.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(Invoice.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.dictionarys = this.dictionaryService.queryByPid(5l);
			this.accounts = this.accountService.queryByUserId(HttpUtil.getLoginUser().getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.dictionarys = this.dictionaryService.queryByPid(5l);
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("invoiceType", dictionarys);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化开票查询选项异常", ex);
		}
	}
	
	public void add() {
		try {
			if(this.invoiceService.add(invoice)) {
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
			if(this.invoiceService.update(invoice)) {
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
			if(this.invoiceService.del(invoice)) {
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
			if(this.invoiceService.commit(invoice, passType, opinion)) {
				this.setupSuccessMap("提交成功");
			} else {
				CodeUtil.throwExcep("提交失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void audit() {
		try {
			if(this.invoiceService.audit(invoice, passType, opinion)) {
				this.setupSuccessMap("审核成功");
			} else {
				CodeUtil.throwExcep("审核失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<Invoice> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Invoice> pageData) {
		this.pageData = pageData;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Dictionary> getDictionarys() {
		return dictionarys;
	}

	public void setDictionarys(List<Dictionary> dictionarys) {
		this.dictionarys = dictionarys;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
}
