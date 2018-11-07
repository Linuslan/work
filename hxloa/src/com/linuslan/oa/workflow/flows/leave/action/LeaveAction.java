package com.linuslan.oa.workflow.flows.leave.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.NumberUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.leave.model.Leave;
import com.linuslan.oa.workflow.flows.leave.service.ILeaveService;

@Controller
public class LeaveAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(LeaveAction.class);
	
	@Autowired
	private ILeaveService leaveService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IUserService userService;
	
	private Leave leave;
	
	private Page<Leave> pageData;
	
	private List<Dictionary> leaveClasses;
	
	//每天上午工作开始时间
	private String amStartTime;
	
	//每天上午工作结束时间
	private String amEndTime;
	
	//每天下午工作开始时间
	private String pmStartTime;
	
	//每天下午工作结束时间
	private String pmEndTime;
	
	private String perDayDuration;
	
	//请假时间范围
	private String leaveDateRange;
	
	private String startDate;
	
	private String endDate;
	
	public void queryPage() {
		try {
			this.pageData = this.leaveService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.leaveService.queryAuditPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核的申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditedPage() {
		try {
			this.pageData = this.leaveService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReportPage() {
		try {
			this.pageData = this.leaveService.queryReportPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询待登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.leave && null != this.leave.getId()) {
				this.leave = this.leaveService.queryById(this.leave.getId());
			}
			//查询请假类型
			this.leaveClasses = this.dictionaryService.queryByPid(17L);
			
			//得到工作作息时间
			this.amStartTime = this.dictionaryService.queryById(13L).getValue();
			this.amEndTime = this.dictionaryService.queryById(14L).getValue();
			this.pmStartTime = this.dictionaryService.queryById(15L).getValue();
			this.pmEndTime = this.dictionaryService.queryById(16L).getValue();
			String currentDate = DateUtil.parseDateToStr(new Date(), "yyyy-MM-dd");
			String formatStr = "yyyy-MM-dd HH:mm";
			Date amStartDate = DateUtil.parseStrToDate(currentDate+" "+this.amStartTime, formatStr);
			Date amEndDate = DateUtil.parseStrToDate(currentDate+" "+this.amEndTime, formatStr);
			Date pmStartDate = DateUtil.parseStrToDate(currentDate+" "+this.pmStartTime, formatStr);
			Date pmEndDate = DateUtil.parseStrToDate(currentDate+" "+this.pmEndTime, formatStr);
			//得到每天的工作时长
			long perDayMsec = (amEndDate.getTime() - amStartDate.getTime())
					+(pmEndDate.getTime() - pmStartDate.getTime());
			this.perDayDuration = NumberUtil.format(((double)perDayMsec/1000/60)/60, "0.0");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void initSelect() {
		try {
			//查询请假类型
			this.leaveClasses = this.dictionaryService.queryByPid(17L);
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("classId", leaveClasses);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化请假查询选项异常", ex);
		}
	}
	
	public void add() {
		try {
			this.leave.setStartDate(DateUtil.parseStrToDate(startDate, "yyyy-MM-dd HH:mm"));
			this.leave.setEndDate(DateUtil.parseStrToDate(endDate, "yyyy-MM-dd HH:mm"));
			double duration = this.leaveService.countLeaveDuration(startDate, endDate);
			duration = Double.parseDouble(NumberUtil.format(duration, "0.00"));
			this.leave.setDuration(duration);
			this.leave.setCompanyId(HttpUtil.getLoginUser().getId());
			if(this.leaveService.add(leave)) {
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
			this.leave.setStartDate(DateUtil.parseStrToDate(startDate, "yyyy-MM-dd HH:mm"));
			this.leave.setEndDate(DateUtil.parseStrToDate(endDate, "yyyy-MM-dd HH:mm"));
			double duration = this.leaveService.countLeaveDuration(startDate, endDate);
			duration = Double.parseDouble(NumberUtil.format(duration, "0.00"));
			this.leave.setDuration(duration);
			if(this.leaveService.update(leave)) {
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
			if(this.leaveService.del(leave)) {
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
			if(null == this.leave || null == this.leave.getId()) {
				this.leave.setStartDate(DateUtil.parseStrToDate(startDate, "yyyy-MM-dd HH:mm"));
				this.leave.setEndDate(DateUtil.parseStrToDate(endDate, "yyyy-MM-dd HH:mm"));
				double duration = this.leaveService.countLeaveDuration(startDate, endDate);
				duration = Double.parseDouble(NumberUtil.format(duration, "0.00"));
				this.leave.setDuration(duration);
				this.leave.setUserId(HttpUtil.getLoginUser().getId());
				this.leave.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
			} else {
				if(null != this.leave.getId()) {
					try {
						this.leave.setStartDate(DateUtil.parseStrToDate(startDate, "yyyy-MM-dd HH:mm"));
						this.leave.setEndDate(DateUtil.parseStrToDate(endDate, "yyyy-MM-dd HH:mm"));
						double duration = this.leaveService.countLeaveDuration(startDate, endDate);
						duration = Double.parseDouble(NumberUtil.format(duration, "0.00"));
						this.leave.setDuration(duration);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
			if(this.leaveService.commit(leave, passType, opinion)) {
				this.setupSuccessMap("提交成功");
			} else {
				CodeUtil.throwExcep("提交失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void audit() {
		try {
			if(this.leaveService.audit(leave, passType, opinion)) {
				this.setupSuccessMap("审核成功");
			} else {
				CodeUtil.throwExcep("审核失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void countLeaveDuration() {
		try {
			double duration = this.leaveService.countLeaveDuration(this.startDate,
					this.endDate);
			this.setupSuccessMap(NumberUtil.format(duration, "0.00"));
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	public Page<Leave> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Leave> pageData) {
		this.pageData = pageData;
	}

	public List<Dictionary> getLeaveClasses() {
		return leaveClasses;
	}

	public void setLeaveClasses(List<Dictionary> leaveClasses) {
		this.leaveClasses = leaveClasses;
	}

	public String getAmStartTime() {
		return amStartTime;
	}

	public void setAmStartTime(String amStartTime) {
		this.amStartTime = amStartTime;
	}

	public String getAmEndTime() {
		return amEndTime;
	}

	public void setAmEndTime(String amEndTime) {
		this.amEndTime = amEndTime;
	}

	public String getPmStartTime() {
		return pmStartTime;
	}

	public void setPmStartTime(String pmStartTime) {
		this.pmStartTime = pmStartTime;
	}

	public String getPmEndTime() {
		return pmEndTime;
	}

	public void setPmEndTime(String pmEndTime) {
		this.pmEndTime = pmEndTime;
	}

	public String getPerDayDuration() {
		return perDayDuration;
	}

	public void setPerDayDuration(String perDayDuration) {
		this.perDayDuration = perDayDuration;
	}

	public String getLeaveDateRange() {
		return leaveDateRange;
	}

	public void setLeaveDateRange(String leaveDateRange) {
		this.leaveDateRange = leaveDateRange;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
