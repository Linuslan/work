package com.linuslan.oa.workflow.flows.reimburse.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.service.IReimburseClassService;

@Controller
public class ReimburseClassAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ReimburseClassAction.class);

	@Autowired
	private IReimburseClassService reimburseClassService;
	private Page<ReimburseClass> pageData;
	private Page<Map<String, Object>> mapPageData;
	private ReimburseClass reimburseClass;

	public void queryPage() {
		try {
			this.pageData = this.reimburseClassService.queryPage(this.paramMap,
					this.page, this.rows);
			JSONObject json = JSONObject.fromObject(this.pageData);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryPageBySql() {
		try {
			this.mapPageData = this.reimburseClassService.queryPageBySql(paramMap, this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"CREATE_DATE"});
			JSONObject json = JSONObject.fromObject(this.mapPageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAll() {
		try {
			List allReimburseClasss = this.reimburseClassService.queryAllReimburseClasss();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allReimburseClasss, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.reimburseClass = this.reimburseClassService.queryById(this.reimburseClass.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.reimburseClassService.valid(this.reimburseClass);
			if (this.reimburseClassService.add(this.reimburseClass))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep("保存失败！");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void update() {
		try {
			this.reimburseClassService.valid(this.reimburseClass);
			if (this.reimburseClassService.update(this.reimburseClass))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep(this.failureMsg);
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void del() {
		try {
			if ((this.reimburseClass == null) || (this.reimburseClass.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			ReimburseClass persist = this.reimburseClassService.queryById(this.reimburseClass
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.reimburseClassService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<ReimburseClass> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<ReimburseClass> pageData) {
		this.pageData = pageData;
	}

	public ReimburseClass getReimburseClass() {
		return this.reimburseClass;
	}

	public void setReimburseClass(ReimburseClass reimburseClass) {
		this.reimburseClass = reimburseClass;
	}

	public Page<Map<String, Object>> getMapPageData() {
		return mapPageData;
	}

	public void setMapPageData(Page<Map<String, Object>> mapPageData) {
		this.mapPageData = mapPageData;
	}
	
}
