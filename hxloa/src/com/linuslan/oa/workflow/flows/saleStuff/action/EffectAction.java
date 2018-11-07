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
import com.linuslan.oa.workflow.flows.saleStuff.model.Effect;
import com.linuslan.oa.workflow.flows.saleStuff.service.IEffectService;

@Controller
public class EffectAction extends BaseAction {

	private static Logger logger = Logger.getLogger(EffectAction.class);
	
	@Autowired
	private IEffectService effectService;
	
	private Page<Effect> pageData;
	
	private Effect effect;
	
	public void queryPage() {
		try {
			this.pageData = this.effectService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryAll() {
		try {
			List<Effect> allEffects = this.effectService.queryAll();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(allEffects, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			this.effect = this.effectService.queryById(this.effect.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.effectService.valid(this.effect);
			if(this.effectService.add(this.effect)) {
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
			this.effectService.valid(this.effect);
			if(this.effectService.update(effect)) {
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
			if(null == this.effect || null == this.effect.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Effect persist = this.effectService.queryById(this.effect.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.effectService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<Effect> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Effect> pageData) {
		this.pageData = pageData;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}
}
