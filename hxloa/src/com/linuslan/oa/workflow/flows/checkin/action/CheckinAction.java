package com.linuslan.oa.workflow.flows.checkin.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
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
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.system.upload.service.IUploadFileService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;
import com.linuslan.oa.workflow.flows.checkin.model.Checkin;
import com.linuslan.oa.workflow.flows.checkin.model.CheckinContent;
import com.linuslan.oa.workflow.flows.checkin.service.ICheckinService;
import com.linuslan.oa.workflow.flows.unit.model.Unit;
import com.linuslan.oa.workflow.flows.unit.service.IUnitService;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.service.IWarehouseService;

@Controller
public class CheckinAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(CheckinAction.class);
	
	@Autowired
	private ICheckinService checkinService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IUnitService unitService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private IUserService userService;
	
	private Checkin checkin;
	
	private Page<Checkin> pageData;
	
	private List<CheckinContent> contents;
	
	private List<Company> companys;
	
	private String serialNo;
	
	private CheckinContent checkinContent;
	
	private List<File> files;
	
	private List<String> filesFileName;
	
	private List<String> filesContentType;
	
	private List<Warehouse> warehouses;
	
	private List<Unit> units;
	
	private List<Dictionary> dictionaries;
	
	private List<UploadFile> uploadFiles;
	
	public void queryPage() {
		try {
			this.pageData = this.checkinService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户入库申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.checkinService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.checkinService.queryAuditedPage(paramMap, page, rows);
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
			if(null != this.checkin && null != this.checkin.getId()) {
				this.checkin = this.checkinService.queryById(this.checkin.getId());
				this.uploadFiles = this.uploadFileService.queryByTbIdAndTbName(this.checkin.getId(), CodeUtil.getClassName(Checkin.class));
			} else {
				//this.serialNo = SerialNoFactory.getGroupNodeUniqueID();
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(Checkin.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.units = this.unitService.queryAll();
			this.dictionaries = this.dictionaryService.queryByPid(30L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryContentsByCheckinId() {
		try {
			List<CheckinContent> contents = this.checkinService.queryContentsByCheckinId(this.checkin.getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(contents, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.warehouses = this.warehouseService.queryAll();
			this.units = this.unitService.queryAll();
			this.dictionaries = this.dictionaryService.queryByPid(30L);
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("warehouseId", warehouses);
			maps.put("unitId", units);
			maps.put("checkinTypeId", dictionaries);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化入库查询选项异常", ex);
		}
	}
	
	public void add() {
		try {
			List<UploadFile> uploadFiles = this.checkinService.getUploadFiles(this.files, this.filesFileName);
			if(this.checkinService.add(checkin, contents, uploadFiles)) {
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
			List<UploadFile> uploadFiles = this.checkinService.getUploadFiles(this.files, this.filesFileName);
			if(this.checkinService.update(checkin, contents, uploadFiles)) {
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
			if(this.checkinService.del(checkin)) {
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
			List<UploadFile> uploadFiles = null;
			if(null != this.files && 0 < this.files.size()) {
				uploadFiles = this.checkinService.getUploadFiles(this.files, this.filesFileName);
			}
			if(this.checkinService.commit(checkin, contents, uploadFiles, passType, opinion)) {
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
			if(this.checkinService.audit(checkin, passType, opinion)) {
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
			if(this.checkinService.delContentById(this.checkinContent.getId())) {
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

	public Checkin getCheckin() {
		return checkin;
	}

	public void setCheckin(Checkin checkin) {
		this.checkin = checkin;
	}

	public Page<Checkin> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Checkin> pageData) {
		this.pageData = pageData;
	}

	public List<CheckinContent> getContents() {
		return contents;
	}

	public void setContents(List<CheckinContent> contents) {
		this.contents = contents;
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

	public CheckinContent getCheckinContent() {
		return checkinContent;
	}

	public void setCheckinContent(CheckinContent checkinContent) {
		this.checkinContent = checkinContent;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<String> getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(List<String> filesFileName) {
		this.filesFileName = filesFileName;
	}

	public List<String> getFilesContentType() {
		return filesContentType;
	}

	public void setFilesContentType(List<String> filesContentType) {
		this.filesContentType = filesContentType;
	}

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public List<UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
}
