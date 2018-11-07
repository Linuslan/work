package com.linuslan.oa.workflow.flows.purchaseReq.action;

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
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.flows.purchase.action.PurchaseAction;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReq;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReqContent;
import com.linuslan.oa.workflow.flows.purchaseReq.service.IPurchaseReqService;
import com.linuslan.oa.workflow.flows.supplier.model.Supplier;
import com.linuslan.oa.workflow.flows.supplier.service.ISupplierService;

@Controller
public class PurchaseReqAction extends BaseAction {

	private static Logger logger = Logger.getLogger(PurchaseAction.class);
	
	@Autowired
	private IPurchaseReqService purchaseReqService;
	
	@Autowired
	private ISupplierService supplierService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IUserService userService;
	
	private Page<PurchaseReq> pageData;
	
	private String serialNo;
	
	private PurchaseReq purchaseReq;
	
	private PurchaseReqContent purchaseReqContent;
	
	private List<Supplier> suppliers;
	
	private List<Company> companys;
	
	private List<PurchaseReqContent> contents;
	
	public void queryPage() {
		try {
			if(null == paramMap) {
				paramMap = new HashMap<String, String> ();
			}
			this.pageData = this.purchaseReqService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户采购申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryAuditPage() {
		try {
			this.pageData = this.purchaseReqService.queryAuditPage(paramMap, page, rows);
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
			this.pageData = this.purchaseReqService.queryAuditedPage(paramMap, page, rows);
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
			if(null != this.purchaseReq && null != this.purchaseReq.getId()) {
				this.purchaseReq = this.purchaseReqService.queryById(this.purchaseReq.getId());
			} else {
				this.serialNo = SerialNoFactory.getGroupNodeUniqueID(PurchaseReq.class.getSimpleName());
			}
			this.companys = this.companyService.queryAllCompanys();
			this.suppliers = this.supplierService.queryAll();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void initSelect() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.suppliers = this.supplierService.queryAll();
			List<User> users = this.userService.queryAll();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>>();
			maps.put("companyId", companys);
			maps.put("supplierId", suppliers);
			maps.put("userId", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("初始化开票查询选项异常", ex);
		}
	}
	
	public void queryContentsByPurchaseReqId() {
		try {
			List<PurchaseReqContent> contents = this.purchaseReqService.queryContentsByPurchaseReqId(this.purchaseReq.getId());
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
			if(this.purchaseReqService.add(purchaseReq, contents)) {
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
			if(this.purchaseReqService.update(purchaseReq, contents)) {
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
			if(this.purchaseReqService.del(purchaseReq)) {
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
			if(this.purchaseReqService.commit(purchaseReq, contents, passType, opinion)) {
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
			if(this.purchaseReqService.audit(purchaseReq, passType, opinion)) {
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
			if(this.purchaseReqService.delContentById(this.purchaseReqContent.getId())) {
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public PurchaseReq getPurchaseReq() {
		return purchaseReq;
	}

	public void setPurchaseReq(PurchaseReq purchaseReq) {
		this.purchaseReq = purchaseReq;
	}

	public PurchaseReqContent getPurchaseReqContent() {
		return purchaseReqContent;
	}

	public void setPurchaseReqContent(PurchaseReqContent purchaseReqContent) {
		this.purchaseReqContent = purchaseReqContent;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<PurchaseReqContent> getContents() {
		return contents;
	}

	public void setContents(List<PurchaseReqContent> contents) {
		this.contents = contents;
	}

	public Page<PurchaseReq> getPageData() {
		return pageData;
	}

	public void setPageData(Page<PurchaseReq> pageData) {
		this.pageData = pageData;
	}
	
}
