package com.linuslan.oa.workflow.flows.companyPay.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.companyPay.model.CompanyPay;
import com.linuslan.oa.workflow.flows.companyPay.service.ICompanyPayService;

@Controller
public class CompanyPayAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(CompanyPayAction.class);
	
	private CompanyPay companyPay;
	
	@Autowired
	private ICompanyPayService companyPayService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	private Page<CompanyPay> pageData;
	
	private List<Company> companys;
	
	private String serialNo;
	
	private List<Dictionary> payTypes;
	
	private List<CompanyPay> companyPays;
	
	public void queryPage() {
		try {
			this.pageData = this.companyPayService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户企业付款申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.companyPayService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.companyPayService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReportPage() {
		try {
			this.pageData = this.companyPayService.queryReportPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询企业付款汇总页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void sumReportPage() {
		try {
			Map<String, Object> map = this.companyPayService.sumReportPage(paramMap);
			JSONObject json = JSONObject.fromObject(map);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("统计企业付款汇总页面异常", ex);
		}
	}
	
	public String queryById() {
		try {
			if(null != this.companyPay && null != this.companyPay.getId()) {
				this.companyPay = this.companyPayService.queryById(this.companyPay.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(CompanyPay.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.payTypes = this.dictionaryService.queryByPid(70L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public String queryPrintList() {
		try {
			String idStr = HttpUtil.getRequest().getParameter("ids");
			if(CodeUtil.isNotEmpty(idStr)) {
				List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
				if(ids.size() > 0) {
					List<CompanyPay> list = this.companyPayService.queryInIds(ids);
					Iterator<CompanyPay> iter = list.iterator();
					CompanyPay cp = null;
					this.companyPays = new ArrayList<CompanyPay> ();
					while(iter.hasNext()) {
						cp = iter.next();
						if(null != cp) {
							cp.getAuditLogs();
							if(cp.isPrint()) {
								this.companyPays.add(cp);
							}
						}
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			List<User> users = this.userService.queryAll();
			this.payTypes = this.dictionaryService.queryByPid(70L);
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("payCompanyId", companys);
			maps.put("userId", users);
			maps.put("payType", payTypes);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化企业付款查询选项异常", ex);
		}
	}
	
	public void add() {
		try {
			if(this.companyPayService.add(companyPay)) {
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
			if(this.companyPayService.update(companyPay)) {
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
			if(this.companyPayService.del(companyPay)) {
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
			if(this.companyPayService.commit(companyPay, passType, opinion)) {
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
			if(this.companyPayService.audit(companyPay, passType, opinion)) {
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

	public CompanyPay getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(CompanyPay companyPay) {
		this.companyPay = companyPay;
	}

	public Page<CompanyPay> getPageData() {
		return pageData;
	}

	public void setPageData(Page<CompanyPay> pageData) {
		this.pageData = pageData;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public List<Dictionary> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<Dictionary> payTypes) {
		this.payTypes = payTypes;
	}

	public List<CompanyPay> getCompanyPays() {
		return companyPays;
	}

	public void setCompanyPays(List<CompanyPay> companyPays) {
		this.companyPays = companyPays;
	}
	
}
