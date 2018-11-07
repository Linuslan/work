package com.linuslan.oa.workflow.flows.achievement.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContent;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentOpinion;
import com.linuslan.oa.workflow.flows.achievement.model.YearAchievement;
import com.linuslan.oa.workflow.flows.achievement.service.IAchievementService;

@Controller
public class AchievementAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(AchievementAction.class);
	
	@Autowired
	private IAchievementService achievementService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IUserService userService;
	
	private Achievement achievement;
	
	private AchievementContent achievementContent;
	
	private List<AchievementContent> contents;
	
	private List<AchievementContentOpinion> contentOpinions;
	
	private Page<Achievement> pageData;
	
	private Page<Map<String, Object>> pageObj;
	
	private List<Company> companys;
	
	private String date;
	
	public void queryPage() {
		try {
			this.pageData = this.achievementService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户报销申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			if(null != paramMap && CodeUtil.isNotEmpty(paramMap.get("date"))) {
				String date = paramMap.get("date");
				try {
					int year = DateUtil.getYear(date);
					if(year > 0) {
						paramMap.put("year", year+"");
					}
					int month = DateUtil.getMonth(date);
					if(month > 0) {
						paramMap.put("month", month + "");
					}
					paramMap.remove("date");
				} catch(Exception ex) {
					
				}
			}
			this.pageData = this.achievementService.queryAuditPage(paramMap, page, rows);
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
			if(null != paramMap && CodeUtil.isNotEmpty(paramMap.get("date"))) {
				String date = paramMap.get("date");
				try {
					int year = DateUtil.getYear(date);
					if(year > 0) {
						paramMap.put("year", year+"");
					}
					int month = DateUtil.getMonth(date);
					if(month > 0) {
						paramMap.put("month", month + "");
					}
					paramMap.remove("date");
				} catch(Exception ex) {
					
				}
			}
			this.pageData = this.achievementService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryScorePage() {
		try {
			if(null != paramMap && CodeUtil.isNotEmpty(paramMap.get("date"))) {
				String date = paramMap.get("date");
				try {
					int year = DateUtil.getYear(date);
					if(year > 0) {
						paramMap.put("year", year+"");
					}
					int month = DateUtil.getMonth(date);
					if(month > 0) {
						paramMap.put("month", month + "");
					}
					paramMap.remove("date");
				} catch(Exception ex) {
					
				}
			}
			this.pageData = this.achievementService.queryScorePage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReportPage() {
		try {
			int year = DateUtil.getYear(new Date());
			int month = DateUtil.getMonth(new Date());
			if(CodeUtil.isNotEmpty(date)) {
				year = DateUtil.getYear(date);
				month = DateUtil.getMonth(date);
			}
			this.pageObj = this.achievementService.queryReportPage(paramMap, year, month, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageObj, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initSelect() {
		try {
			List<User> users = this.userService.queryAll();
			List<Company> companys = this.companyService.queryAllCompanys();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>> ();
			maps.put("id", users);
			maps.put("company_id", companys);
			maps.put("userId", users);
			maps.put("companyId", companys);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			if(null != this.achievement && null != this.achievement.getId()) {
				this.achievement = this.achievementService.queryById(this.achievement.getId());
			}
			this.companys = this.companyService.queryAllCompanys();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryContentsByAchievementId() {
		try {
			List<AchievementContent> contents = this.achievementService.queryContentsByAchievementId(this.achievement.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryContentOpinionsByContentId() {
		try {
			List<AchievementContentOpinion> contents = this.achievementService.queryContentOpinionByContentId(this.achievementContent.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void checkExistByDate() {
		try {
			Long id = null;
			if(null != this.achievement && null != this.achievement.getId()) {
				id = this.achievement.getId();
			}
			if(null != this.achievementService.queryByDate(date, HttpUtil.getLoginUser().getId(), id)) {
				CodeUtil.throwExcep("您所选择的时间已经创建绩效");
			} else {
				this.setupSimpleSuccessMap();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void add() {
		try {
			if(CodeUtil.isEmpty(this.date)) {
				CodeUtil.throwExcep("请选择时间");
			}
			if(null != this.achievementService.queryByDate(date, HttpUtil.getLoginUser().getId(), null)) {
				CodeUtil.throwExcep("您所选择的时间已经创建绩效");
			}
			if(null == this.achievement) {
				this.achievement = new Achievement();
			}
			String[] dateArr = date.split("-");
			
			if(CodeUtil.isNotEmpty(dateArr[0])) {
				this.achievement.setYear(Integer.parseInt(dateArr[0].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的年份");
			}
			if(CodeUtil.isNotEmpty(dateArr[1])) {
				this.achievement.setMonth(Integer.parseInt(dateArr[1].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的月份");
			}
			if(this.achievementService.add(achievement, contents)) {
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
			if(CodeUtil.isEmpty(this.date)) {
				CodeUtil.throwExcep("请选择时间");
			}
			if(null != this.achievementService.queryByDate(date, HttpUtil.getLoginUser().getId(), this.achievement.getId())) {
				CodeUtil.throwExcep("您所选择的时间已经创建绩效");
			}
			String[] dateArr = date.split("-");
			
			if(CodeUtil.isNotEmpty(dateArr[0])) {
				this.achievement.setYear(Integer.parseInt(dateArr[0].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的年份");
			}
			if(CodeUtil.isNotEmpty(dateArr[1])) {
				this.achievement.setMonth(Integer.parseInt(dateArr[1].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的月份");
			}
			if(this.achievementService.update(achievement, contents)) {
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
			if(this.achievementService.del(achievement)) {
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
			String type = "update";
			try {
				type = HttpUtil.getRequest().getParameter("type");
			} catch(Exception ex) {
				type = "update";
			}
			boolean isUpdate = false;
			if("update".equals(type)) {
				isUpdate = true;
			}
			if(CodeUtil.isEmpty(type) || "update".equals(type)) {
				if(CodeUtil.isEmpty(this.date)) {
					CodeUtil.throwExcep("请选择时间");
				}
				if(null == this.achievement) {
					this.achievement = new Achievement();
				}
				if(null != this.achievementService.queryByDate(date, HttpUtil.getLoginUser().getId(), this.achievement.getId())) {
					CodeUtil.throwExcep("您所选择的时间已经创建绩效");
				}
				String[] dateArr = date.split("-");
				
				if(CodeUtil.isNotEmpty(dateArr[0])) {
					this.achievement.setYear(Integer.parseInt(dateArr[0].trim()));
				} else {
					CodeUtil.throwExcep("请选择正确的年份");
				}
				if(CodeUtil.isNotEmpty(dateArr[1])) {
					this.achievement.setMonth(Integer.parseInt(dateArr[1].trim()));
				} else {
					CodeUtil.throwExcep("请选择正确的月份");
				}
			}
			if(this.achievementService.commit(achievement, contents, passType, opinion, isUpdate)) {
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
			if(this.achievementService.audit(achievement, this.contents, this.contentOpinions, passType, opinion)) {
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
	
	public void auditBatch() {
		try {
			String idStr = HttpUtil.getRequest().getParameter("ids");
			List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
			int i = this.achievementService.auditBatch(ids, passType, opinion);
			if(i > 0) {
				this.setupSuccessMap("<font color='red'>"+i+"</font>条批量审核成功");
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
			if(this.achievementService.delContentById(this.achievementContent.getId())) {
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
	 * 导入员工全年的绩效
	 */
	public void exportYearAchievement() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-msdownload");
		
		String fileName = "";
		try {
			int year = CodeUtil.parseInt(HttpUtil.getRequest().getParameter("year"));
			List<YearAchievement> list = this.achievementService.queryYearAchievement(year);
			HSSFWorkbook wb = this.achievementService.getHSSFWorkbook(year, list);
			fileName = URLEncoder.encode(year+"年员工绩效.xls", "UTF-8");
			response.addHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void copy() {
		try {
			String oldDate = HttpUtil.getRequest().getParameter("oldDate");
			if(StringUtils.isEmpty(date)) {
				CodeUtil.throwExcep("创建绩效的日期为空");
			}
			if(StringUtils.isEmpty(oldDate)) {
				CodeUtil.throwExcep("复制绩效的日期为空");
			}
			if(null != this.achievementService.queryByDate(date, HttpUtil.getLoginUser().getId(), null)) {
				CodeUtil.throwExcep("您所选择的时间已经创建绩效");
			}
			Achievement oldAchievement = this.achievementService.queryByDate(oldDate, HttpUtil.getLoginUser().getId(), null);
			if(null == oldAchievement) {
				CodeUtil.throwExcep("您所选择复制的绩效时间未创建绩效");
			}
			List<AchievementContent> oldContents = this.achievementService.queryContentsByAchievementId(oldAchievement.getId());
			Achievement achievement = new Achievement();
			String[] dateArr = date.split("-");
			
			if(CodeUtil.isNotEmpty(dateArr[0])) {
				achievement.setYear(Integer.parseInt(dateArr[0].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的年份");
			}
			if(CodeUtil.isNotEmpty(dateArr[1])) {
				achievement.setMonth(Integer.parseInt(dateArr[1].trim()));
			} else {
				CodeUtil.throwExcep("请选择正确的月份");
			}
			
			List<AchievementContent> contents = new ArrayList<AchievementContent> ();
			if(null != oldContents) {
				AchievementContent content = null;
				for(AchievementContent oldContent : oldContents) {
					content = new AchievementContent();
					content.setContent(oldContent.getContent());
					content.setCreateDate(new Date());
					content.setIsDelete(0);
					content.setFormula(oldContent.getFormula());
					content.setOrderNo(oldContent.getOrderNo());
					content.setScoreWeight(oldContent.getScoreWeight());
					content.setSource(oldContent.getSource());
					content.setTitle(oldContent.getTitle());
					contents.add(content);
				}
			}
			if(this.achievementService.add(achievement, contents)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		} finally {
			this.printResultMap();
		}
		
	}

	public Achievement getAchievement() {
		return achievement;
	}

	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	public Page<Achievement> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Achievement> pageData) {
		this.pageData = pageData;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public AchievementContent getAchievementContent() {
		return achievementContent;
	}

	public void setAchievementContent(AchievementContent achievementContent) {
		this.achievementContent = achievementContent;
	}

	public List<AchievementContent> getContents() {
		return contents;
	}

	public void setContents(List<AchievementContent> contents) {
		this.contents = contents;
	}

	public List<AchievementContentOpinion> getContentOpinions() {
		return contentOpinions;
	}

	public void setContentOpinions(List<AchievementContentOpinion> contentOpinions) {
		this.contentOpinions = contentOpinions;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Page<Map<String, Object>> getPageObj() {
		return pageObj;
	}

	public void setPageObj(Page<Map<String, Object>> pageObj) {
		this.pageObj = pageObj;
	}
}
