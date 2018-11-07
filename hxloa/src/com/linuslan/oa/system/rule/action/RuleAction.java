package com.linuslan.oa.system.rule.action;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.rule.action.RuleAction;
import com.linuslan.oa.system.rule.model.Rule;
import com.linuslan.oa.system.rule.service.IRuleService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Controller
public class RuleAction extends BaseAction {

	private static Logger logger = Logger.getLogger(RuleAction.class);

	@Autowired
	private IRuleService ruleService;
	private Page<Rule> pageData;
	private Rule rule;
	/**
	 * 上传上的文件
	 */
	private File file;
	
	/**
	 * 文件的内容类型
	 */
	private String fileContentType;
	
	/**
	 * 文件名字
	 */
	private String fileFileName;

	public void queryPage() {
		try {
			this.pageData = this.ruleService.queryPage(this.paramMap,
					this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAll() {
		try {
			List<Rule> allRules = this.ruleService.queryAllRules();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allRules, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.rule = this.ruleService.queryById(this.rule.getId());
			HttpUtil.getRequest().setAttribute("filePath", this.rule.getFilePath());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.ruleService.valid(this.rule);
			if (this.ruleService.add(this.rule, this.file, this.fileFileName))
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
			this.ruleService.valid(this.rule);
			if (this.ruleService.update(this.rule, this.file))
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
			if ((this.rule == null) || (this.rule.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Rule persist = this.ruleService.queryById(this.rule
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.ruleService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Rule> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Rule> pageData) {
		this.pageData = pageData;
	}

	public Rule getRule() {
		return this.rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	
}
