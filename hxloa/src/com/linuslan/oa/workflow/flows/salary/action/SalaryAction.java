package com.linuslan.oa.workflow.flows.salary.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.userSalary.service.IUserSalaryService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;
import com.linuslan.oa.workflow.flows.salary.model.Salary;
import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;
import com.linuslan.oa.workflow.flows.salary.service.ISalaryService;
import com.linuslan.oa.workflow.flows.salary.vo.DepartmentSalary;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.service.IWarehouseService;

@Controller
public class SalaryAction extends BaseAction {
	private static Logger logger = Logger.getLogger(SalaryAction.class);
	
	@Autowired
	private ISalaryService salaryService;
	
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
	private IUserSalaryService userSalaryService;
	
	private Salary salary;
	
	private List<Customer> customers;
	
	private List<Company> companys;
	
	private List<Dictionary> dictionaries;
	
	private List<CheckoutArticle> checkoutArticles;
	
	private Page<Salary> pageData;
	
	private List<SalaryContent> contents;
	
	private String serialNo;
	
	private SalaryContent salaryContent;
	
	private List<Warehouse> warehouses;
	
	public void queryPage() {
		try {
			if(null == paramMap) {
				paramMap = new HashMap<String, String> ();
			}
			if(CodeUtil.isEmpty(paramMap.get("date"))) { 
				int year = DateUtil.getYear(new Date());
				int month = DateUtil.getMonth(new Date()) - 1;
				if(month <= 0) {
					year = year - 1;
					month = 12;
				}
				paramMap.put("date", year+"-"+month);
			}
			String date = paramMap.get("date");
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			paramMap.put("year", year+"");
			paramMap.put("month", month+"");
			paramMap.remove("date");
			this.pageData = this.salaryService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户工资申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.salaryService.queryAuditPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	/**
	 * 查询登陆用户审核过的薪资
	 * @return
	 */
	public void queryAuditedPage() {
		try {
			this.pageData = this.salaryService.queryAuditedPage(paramMap, page, rows);
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
			if(null == paramMap) {
				paramMap = new HashMap<String, String> ();
			}
			if(CodeUtil.isEmpty(paramMap.get("date"))) {
				int year = DateUtil.getYear(new Date());
				int month = DateUtil.getMonth(new Date()) - 1;
				if(month <= 0) {
					year = year - 1;
					month = 12;
				}
				paramMap.put("date", year+"-"+month);
			}
			String date = paramMap.get("date");
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			paramMap.put("year", year+"");
			paramMap.put("month", month+"");
			paramMap.remove("date");
			this.pageData = this.salaryService.queryReportPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询工资报表页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.salary && null != this.salary.getId()) {
				this.salary = this.salaryService.queryById(this.salary.getId());
			} else {
				//this.serialNo = SerialNoFactory.getGroupNodeUniqueID();
			}
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.customers = this.customerService.queryAll();
			this.dictionaries = this.dictionaryService.queryByPid(5L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryContentsBySalaryId() {
		try {
			List<SalaryContent> contents = this.salaryService.queryContentsBySalaryId(this.salary.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	/**
	 * 查询登陆用户的生效工资
	 */
	public void queryContentByUserId() {
		try {
			Integer year = CodeUtil.parseInteger(HttpUtil.getRequest().getParameter("year"));
			Integer month = CodeUtil.parseInteger(HttpUtil.getRequest().getParameter("month"));
			if(null == year) {
				year = DateUtil.getYear(new Date());
			}
			if(null == month) {
				month = DateUtil.getMonth(new Date()) - 1 == 0 ? 12 : DateUtil.getMonth(new Date()) - 1;
			}
			List<SalaryContent> list = this.salaryService.queryContentByUserId(year, month);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(list, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("获取登陆用户的工资异常", ex);
		}
	}
	
	public void createSalary() {
		try {
			int year = DateUtil.getYear(new Date());
			
			//得到当前月份，但计算的是上个月的
			int month = DateUtil.getMonth(new Date());
			if(month == 1) {
				year = year - 1;
				month = 12;
			} else {
				month = month - 1;
			}
			long existCount = this.salaryService.checkExistSalary(year, month);
			if(existCount > 0) {
				CodeUtil.throwExcep(year+"年"+month+"月已创建工资");
			}
			List<Map<String, Object>> userSalarys = this.userSalaryService.queryAllUserSalarys(year, month);
			List<Salary> salarys = this.salaryService.createSalarys(year, month, userSalarys);
			int count = this.salaryService.addBatch(salarys);
			this.setupSuccessMap("成功创建 <font color='red'>"+count+"</font> 份工资");
		} catch(Exception ex) {
			logger.error("创建工资异常，"+CodeUtil.getStackTrace(ex));
			this.setupFailureMap("创建工资异常，"+ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void add() {
		try {
			if(this.salaryService.add(salary, contents)) {
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
			if(this.salaryService.update(salary, contents)) {
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
			if(this.salaryService.del(salary)) {
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
		String type = "update";
		try {
			try {
				type = HttpUtil.getRequest().getParameter("type");
			} catch(Exception ex) {
				type = "update";
			}
			boolean isUpdate = false;
			if("update".equals(type)) {
				isUpdate = true;
			}
			if(this.salaryService.commit(salary, contents, passType, opinion, isUpdate)) {
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
			if(this.salaryService.audit(salary, contents, passType, opinion)) {
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
			if(this.salaryService.delContentById(this.salaryContent.getId())) {
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
	
	/**
	 * 计算工资后返回前端
	 */
	public void calculate() {
		String content = null;
		try {
			JSONObject json = JSONObject.fromObject(this.salaryContent);
			content = json.toString();
		} catch(Exception ex) {
			content = "";
		}
		try {
			this.printResult(content);
		} catch(Exception ex) {
			
		}
	}
	
	public void export() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-msdownload");
		
		String fileName = "";
		try {
			int year = CodeUtil.parseInteger(HttpUtil.getRequest().getParameter("year"));
			int month = CodeUtil.parseInteger(HttpUtil.getRequest().getParameter("month"));
			List<DepartmentSalary> list = this.salaryService.queryDepartmentSalary(year, month);
			HSSFWorkbook wb = this.salaryService.createExcel(list);
			fileName = URLEncoder.encode(year+"年"+month+"月员工薪资.xls", "UTF-8");
			response.addHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
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

	public Page<Salary> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Salary> pageData) {
		this.pageData = pageData;
	}

	public List<SalaryContent> getContents() {
		return contents;
	}

	public void setContents(List<SalaryContent> contents) {
		this.contents = contents;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public SalaryContent getSalaryContent() {
		return salaryContent;
	}

	public void setSalaryContent(SalaryContent salaryContent) {
		this.salaryContent = salaryContent;
	}

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public List<CheckoutArticle> getCheckoutArticles() {
		return checkoutArticles;
	}

	public void setCheckoutArticles(List<CheckoutArticle> checkoutArticles) {
		this.checkoutArticles = checkoutArticles;
	}
}
