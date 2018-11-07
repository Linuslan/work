package com.linuslan.oa.workflow.flows.healthInsurance.action;

import java.io.File;
import java.util.ArrayList;
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
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsurance;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsuranceContent;
import com.linuslan.oa.workflow.flows.healthInsurance.service.IHealthInsuranceService;

@Controller
public class HealthInsuranceAction extends BaseAction {

	private static Logger logger = Logger.getLogger(HealthInsuranceAction.class);
	
	@Autowired
	private IHealthInsuranceService healthInsuranceService;
	
	private HealthInsurance healthInsurance;
	
	private HealthInsuranceContent healthInsuranceContent;
	
	private List<HealthInsurance> healthInsurances;
	
	private List<HealthInsuranceContent> contents;
	
	private Page<HealthInsurance> pageData;
	
	private String date;
	
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
			this.pageData = this.healthInsuranceService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询用户医保申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.healthInsuranceService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.healthInsuranceService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.healthInsurance && null != this.healthInsurance.getId()) {
				this.healthInsurance = this.healthInsuranceService.queryById(this.healthInsurance.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryContentsByHealthInsuranceId() {
		try {
			List<HealthInsuranceContent> contents = this.healthInsuranceService.queryContentsByHealthInsuranceId(this.healthInsurance.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void add() {
		try {
			if(CodeUtil.isEmpty(this.date)) {
				CodeUtil.throwExcep("请选择时间");
			}
			if(null != this.healthInsuranceService.queryByDate(date, null)) {
				CodeUtil.throwExcep("您所选择的时间已经创建医保，并已审核结束");
			}
			if(null == this.healthInsurance) {
				this.healthInsurance = new HealthInsurance();
			}
			String[] dateArr = date.split("-");
			
			if(CodeUtil.isNotEmpty(dateArr[0])) {
				this.healthInsurance.setYear(Integer.parseInt(dateArr[0].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的年份");
			}
			if(CodeUtil.isNotEmpty(dateArr[1])) {
				this.healthInsurance.setMonth(Integer.parseInt(dateArr[1].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的月份");
			}
			if(null == this.contents) {
				this.contents = new ArrayList<HealthInsuranceContent> ();
			}
			this.contents.addAll(this.healthInsuranceService.importInsuranceContent(this.file));
			if(this.healthInsuranceService.add(healthInsurance, contents)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void update() {
		try {
			if(CodeUtil.isEmpty(this.date)) {
				CodeUtil.throwExcep("请选择时间");
			}
			if(null != this.healthInsuranceService.queryByDate(date, this.healthInsurance.getId())) {
				CodeUtil.throwExcep("您所选择的时间已经创建医保，并已审核结束");
			}
			String[] dateArr = date.split("-");
			
			if(CodeUtil.isNotEmpty(dateArr[0])) {
				this.healthInsurance.setYear(Integer.parseInt(dateArr[0].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的年份");
			}
			if(CodeUtil.isNotEmpty(dateArr[1])) {
				this.healthInsurance.setMonth(Integer.parseInt(dateArr[1].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的月份");
			}
			if(null == this.contents) {
				this.contents = new ArrayList<HealthInsuranceContent> ();
			}
			this.contents.addAll(this.healthInsuranceService.importInsuranceContent(this.file));
			if(this.healthInsuranceService.update(healthInsurance, contents)) {
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
			if(this.healthInsuranceService.del(healthInsurance)) {
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
			String type = HttpUtil.getRequest().getParameter("type");
			boolean isUpdate = false;
			if("commit".equals(type)) {
				isUpdate = false;
			} else {
				isUpdate = true;
			}
			if(isUpdate) {
				if(CodeUtil.isEmpty(this.date)) {
					CodeUtil.throwExcep("请选择时间");
				}
				if(null != this.healthInsuranceService.queryByDate(date, null)) {
					CodeUtil.throwExcep("您所选择的时间已经创建医保，并已审核结束");
				}
				if(null == this.healthInsurance) {
					this.healthInsurance = new HealthInsurance();
				}
				String[] dateArr = date.split("-");
				
				if(CodeUtil.isNotEmpty(dateArr[0])) {
					this.healthInsurance.setYear(Integer.parseInt(dateArr[0].trim()));
				} else {
					CodeUtil.throwExcep("请选择正确的年份");
				}
				if(CodeUtil.isNotEmpty(dateArr[1])) {
					this.healthInsurance.setMonth(Integer.parseInt(dateArr[1].trim()));
				} else {
					CodeUtil.throwExcep("请选择正确的月份");
				}
				if(null == this.contents) {
					this.contents = new ArrayList<HealthInsuranceContent> ();
				}
				this.contents.addAll(this.healthInsuranceService.importInsuranceContent(this.file));
			} else {
				this.healthInsurance = this.healthInsuranceService.queryById(this.healthInsurance.getId());
				this.contents = this.healthInsuranceService.queryContentsByHealthInsuranceId(this.healthInsurance.getId());
			}
			if(this.healthInsuranceService.commit(healthInsurance, contents, passType, opinion)) {
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
			if(this.healthInsuranceService.audit(healthInsurance, passType, opinion)) {
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
			if(this.healthInsuranceService.delContentById(this.healthInsuranceContent.getId())) {
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
	
	public void delContentInIds() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String ids = request.getParameter("ids");
			if(null != ids && 0 < ids.length()) {
				List<Long> idList = BeanUtil.parseStringToLongList(ids, ",");
				if(0 >= idList.size()) {
					CodeUtil.throwExcep("请至少选择一条数据删除");
				}
				if(this.healthInsuranceService.delContentsInIds(idList)) {
					this.setupSuccessMap("删除成功");
				} else {
					CodeUtil.throwExcep("删除失败");
				}
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public HealthInsurance getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(HealthInsurance healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public HealthInsuranceContent getHealthInsuranceContent() {
		return healthInsuranceContent;
	}

	public void setHealthInsuranceContent(
			HealthInsuranceContent healthInsuranceContent) {
		this.healthInsuranceContent = healthInsuranceContent;
	}

	public List<HealthInsurance> getHealthInsurances() {
		return healthInsurances;
	}

	public void setHealthInsurances(List<HealthInsurance> healthInsurances) {
		this.healthInsurances = healthInsurances;
	}

	public List<HealthInsuranceContent> getContents() {
		return contents;
	}

	public void setContents(List<HealthInsuranceContent> contents) {
		this.contents = contents;
	}

	public Page<HealthInsurance> getPageData() {
		return pageData;
	}

	public void setPageData(Page<HealthInsurance> pageData) {
		this.pageData = pageData;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
