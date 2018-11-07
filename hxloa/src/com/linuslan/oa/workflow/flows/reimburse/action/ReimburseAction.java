package com.linuslan.oa.workflow.flows.reimburse.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
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
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.reimburse.model.Reimburse;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseContent;
import com.linuslan.oa.workflow.flows.reimburse.service.IReimburseService;

@Controller
public class ReimburseAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(ReimburseAction.class);
	
	@Autowired
	private IReimburseService reimburseService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IUserService userService;
	
	private Reimburse reimburse;
	
	private ReimburseContent reimburseContent;
	
	private List<ReimburseContent> contents;
	
	private Page<Reimburse> pageData;
	
	private List<Company> companys;
	
	private String serialNo;
	
	private Long departmentId;
	
	private List<Reimburse> reimburses;
	
	public void queryPage() {
		try {
			this.pageData = this.reimburseService.queryPage(paramMap, page, rows);
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
			this.pageData = this.reimburseService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.reimburseService.queryAuditedPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户审核记录页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReportPage() {
		try {
			this.pageData = this.reimburseService.queryReportPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询报销汇总页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void sumReportPage() {
		try {
			Map<String, Object> map = this.reimburseService.sumReportPage(paramMap);
			JSONObject json = JSONObject.fromObject(map);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("统计报销汇总页面异常", ex);
		}
	}
	
	public String queryById() {
		try {
			if(null != this.reimburse && null != this.reimburse.getId()) {
				this.reimburse = this.reimburseService.queryById(this.reimburse.getId());
				this.contents = this.reimburseService.queryContentsByReimburseId(this.reimburse.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(Reimburse.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public String queryPrintList() {
		try {
			String idStr = HttpUtil.getRequest().getParameter("ids");
			if(CodeUtil.isNotEmpty(idStr)) {
				List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
				List<Reimburse> reimburses = this.reimburseService.queryInIds(ids);
				Map<Long, Reimburse> map = (Map<Long, Reimburse>) BeanUtil.parseListToMap2(reimburses, "id");
				List<ReimburseContent> contents = reimburseService.queryContentsByReimburseIds(ids);
				Iterator<ReimburseContent> iter = contents.iterator();
				ReimburseContent rc = null;
				/*
				 * 将报销项目分别存放入报销主类里面的contents集合中
				 */
				while(iter.hasNext()) {
					rc = iter.next();
					if(null != rc && null != rc.getReimburseId()) {
						map.get(rc.getReimburseId()).getContents().add(rc);
					}
				}
				
				Iterator<Reimburse> iter2 = reimburses.iterator();
				Reimburse reimburse = null;
				this.reimburses = new ArrayList<Reimburse> ();
				while(iter2.hasNext()) {
					reimburse = iter2.next();
					if(null != reimburse) {
						reimburse.getAuditLogs();
						if(reimburse.isPrint()) {
							this.reimburses.add(reimburse);
						}
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化报销查询选项异常", ex);
		}
	}
	
	public void queryContentsByReimburseId() {
		try {
			List<ReimburseContent> contents = this.reimburseService.queryContentsByReimburseId(this.reimburse.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryReimburseClassesByDepartmentId() {
		try {
			List<ReimburseClass> list = this.reimburseService.queryReimburseClassesByDepartmentId(departmentId);
			JSONArray json = JSONArray.fromObject(list);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		} finally {
			
		}
	}
	
	public void add() {
		try {
			if(this.reimburseService.add(reimburse, contents)) {
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
			if(this.reimburseService.update(reimburse, contents)) {
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
			if(this.reimburseService.del(reimburse)) {
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
			if(this.reimburseService.commit(reimburse, contents, passType, opinion, isUpdate)) {
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
			if(this.reimburseService.audit(reimburse, passType, opinion)) {
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
			if(this.reimburseService.delContentById(this.reimburseContent.getId())) {
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

	public Reimburse getReimburse() {
		return reimburse;
	}

	public void setReimburse(Reimburse reimburse) {
		this.reimburse = reimburse;
	}

	public List<ReimburseContent> getContents() {
		return contents;
	}

	public void setContents(List<ReimburseContent> contents) {
		this.contents = contents;
	}

	public Page<Reimburse> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Reimburse> pageData) {
		this.pageData = pageData;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public ReimburseContent getReimburseContent() {
		return reimburseContent;
	}

	public void setReimburseContent(ReimburseContent reimburseContent) {
		this.reimburseContent = reimburseContent;
	}

	public List<Reimburse> getReimburses() {
		return reimburses;
	}

	public void setReimburses(List<Reimburse> reimburses) {
		this.reimburses = reimburses;
	}
	
}
