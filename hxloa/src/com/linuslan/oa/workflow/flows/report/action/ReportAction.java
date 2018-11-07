package com.linuslan.oa.workflow.flows.report.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.report.service.IReportService;

@Controller
public class ReportAction extends BaseAction {
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IArticleService articleService;

	private String date;
	
	private Page<Map<String, Object>> pageData;
	
	public void initSelect() {
		try {
			List<Company> companys = this.companyService.queryAllCompanys();
			List<CheckinArticle> articles = this.articleService.queryAll();
			List<Map<String, Object>> customers = this.reportService.queryAllCustomers();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>> ();
			maps.put("companyId", companys);
			maps.put("articleId", articles);
			maps.put("customerId", customers);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps);
			this.printResult(json.toString());
		} catch(Exception ex) {
			
		}
	}
	
	public void queryStockReportPage() {
		try {
			if(CodeUtil.isEmpty(date)) {
				date = DateUtil.parseDateToStr(new Date(), "yyyy-MM");
			}
			this.pageData = this.reportService.queryStockReportPage(paramMap, date, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryStockDetailReportPage() {
		try {
			this.pageData = this.reportService.queryStockDetailReportPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void querySaleReportPage() {
		try {
			if(CodeUtil.isEmpty(date)) {
				date = DateUtil.parseDateToStr(new Date(), "yyyy-MM");
			}
			this.pageData = this.reportService.querySaleReportPage(paramMap, date, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void querySaleDetailReportPage() {
		try {
			if(CodeUtil.isEmpty(date)) {
				date = DateUtil.parseDateToStr(new Date(), "yyyy-MM-dd");
			}
			this.pageData = this.reportService.querySaleDetailReportPage(paramMap, date, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryPaybackDetailReportPage() {
		try {
			this.pageData = this.reportService.queryPaybackDetailReportPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
