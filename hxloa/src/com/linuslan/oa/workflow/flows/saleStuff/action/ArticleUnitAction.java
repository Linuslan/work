package com.linuslan.oa.workflow.flows.saleStuff.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleArticle.model.MaterialFormat;
import com.linuslan.oa.workflow.flows.saleStuff.model.ArticleUnit;
import com.linuslan.oa.workflow.flows.saleStuff.service.IArticleUnitService;

@Controller
public class ArticleUnitAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ArticleUnitAction.class);
	
	@Autowired
	private IArticleUnitService articleUnitService;
	
	private Page<ArticleUnit> pageData;
	
	private ArticleUnit articleUnit;
	
	public void queryPage() {
		try {
			this.pageData = this.articleUnitService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryAll() {
		try {
			List<ArticleUnit> allArticleUnits = this.articleUnitService.queryAll();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(allArticleUnits, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			this.articleUnit = this.articleUnitService.queryById(this.articleUnit.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.articleUnitService.valid(this.articleUnit);
			if(this.articleUnitService.add(this.articleUnit)) {
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
			this.articleUnitService.valid(this.articleUnit);
			if(this.articleUnitService.update(articleUnit)) {
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
			if(null == this.articleUnit || null == this.articleUnit.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			ArticleUnit persist = this.articleUnitService.queryById(this.articleUnit.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.articleUnitService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<ArticleUnit> getPageData() {
		return pageData;
	}

	public void setPageData(Page<ArticleUnit> pageData) {
		this.pageData = pageData;
	}

	public ArticleUnit getArticleUnit() {
		return articleUnit;
	}

	public void setArticleUnit(ArticleUnit articleUnit) {
		this.articleUnit = articleUnit;
	}
}
