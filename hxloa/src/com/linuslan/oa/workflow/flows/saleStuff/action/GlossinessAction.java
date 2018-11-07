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
import com.linuslan.oa.workflow.flows.saleStuff.model.Face;
import com.linuslan.oa.workflow.flows.saleStuff.model.Glossiness;
import com.linuslan.oa.workflow.flows.saleStuff.service.IGlossinessService;

@Controller
public class GlossinessAction extends BaseAction {

	private static Logger logger = Logger.getLogger(GlossinessAction.class);
	
	@Autowired
	private IGlossinessService glossinessService;
	
	private Page<Glossiness> pageData;
	
	private Glossiness glossiness;
	
	public void queryPage() {
		try {
			this.pageData = this.glossinessService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryAll() {
		try {
			List<Glossiness> allGlossinesss = this.glossinessService.queryAll();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(allGlossinesss, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			this.glossiness = this.glossinessService.queryById(this.glossiness.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.glossinessService.valid(this.glossiness);
			if(this.glossinessService.add(this.glossiness)) {
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
			this.glossinessService.valid(this.glossiness);
			if(this.glossinessService.update(glossiness)) {
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
			if(null == this.glossiness || null == this.glossiness.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Glossiness persist = this.glossinessService.queryById(this.glossiness.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.glossinessService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<Glossiness> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Glossiness> pageData) {
		this.pageData = pageData;
	}

	public Glossiness getGlossiness() {
		return glossiness;
	}

	public void setGlossiness(Glossiness glossiness) {
		this.glossiness = glossiness;
	}
}
