package com.linuslan.oa.workflow.flows.article.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;

@Controller
public class ArticleAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(ArticleAction.class);
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ICustomerService customerService;
	
	private CheckinArticle article;
	
	private Page<CheckinArticle> pageData;
	
	private Page<CheckoutArticle> checkoutArticlePage;
	
	private List<Format> formats;
	
	private List<Company> companys;
	
	private Format format;
	
	private CheckoutArticle checkoutArticle;
	
	private List<CheckinArticle> articles;
	
	private List<Customer> customers;
	
	public void queryPage() {
		try {
			this.pageData = this.articleService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询入库商品页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.article && null != this.article.getId()) {
				this.article = this.articleService.queryById(this.article.getId());
			}
			this.companys = this.companyService.queryAllCompanys();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryCheckinArticlesByCompanyId() {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			Long companyId = Long.parseLong(request.getParameter("companyId"));
			List<CheckinArticle> list = this.articleService.queryCheckinArticlesByCompanyId(companyId);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(list, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryFormatsByArticleId() {
		try {
			List<Format> formats = this.articleService.queryFormatsByArticleId(this.article.getId(), paramMap);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(formats, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void add() {
		try {
			if(this.articleService.add(article, formats)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void update() {
		try {
			if(this.articleService.update(article, formats)) {
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
			if(this.articleService.del(article)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void delFormatById() {
		try {
			if(this.articleService.delFormatById(this.format.getId())) {
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
	
	public void queryCheckoutArticlePage() {
		try {
			this.checkoutArticlePage = this.articleService.queryCheckoutArticlePage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.checkoutArticlePage, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户报销申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryCheckoutArticleById() {
		try {
			if(null != this.checkoutArticle && null != this.checkoutArticle.getId()) {
				this.checkoutArticle = this.articleService.queryCheckoutArticleById(this.checkoutArticle.getId());
			}
			this.articles = this.articleService.queryAll();
			this.customers = this.customerService.queryAll();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryCheckoutArticles() {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			Long companyId = Long.parseLong(request.getParameter("companyId"));
			Long customerId = Long.parseLong(request.getParameter("customerId"));
			List<CheckoutArticle> list = this.articleService.queryCheckoutArticles(companyId, customerId);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(list, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void addCheckoutArticle() {
		try {
			if(this.articleService.addCheckoutArticle(this.checkoutArticle)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void updateCheckoutArticle() {
		try {
			if(this.articleService.updateCheckoutArticle(this.checkoutArticle)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void delCheckoutArticle() {
		try {
			if(this.articleService.delCheckoutArticle(checkoutArticle)) {
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

	public Page<CheckinArticle> getPageData() {
		return pageData;
	}

	public void setPageData(Page<CheckinArticle> pageData) {
		this.pageData = pageData;
	}

	public List<Format> getFormats() {
		return formats;
	}

	public void setFormats(List<Format> formats) {
		this.formats = formats;
	}

	public CheckinArticle getArticle() {
		return article;
	}

	public void setArticle(CheckinArticle article) {
		this.article = article;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public CheckoutArticle getCheckoutArticle() {
		return checkoutArticle;
	}

	public void setCheckoutArticle(CheckoutArticle checkoutArticle) {
		this.checkoutArticle = checkoutArticle;
	}

	public List<CheckinArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<CheckinArticle> articles) {
		this.articles = articles;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public Page<CheckoutArticle> getCheckoutArticlePage() {
		return checkoutArticlePage;
	}

	public void setCheckoutArticlePage(Page<CheckoutArticle> checkoutArticlePage) {
		this.checkoutArticlePage = checkoutArticlePage;
	}
	
}
