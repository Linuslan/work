package com.linuslan.oa.system.contract.action;

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
import com.linuslan.oa.system.contract.model.Contract;
import com.linuslan.oa.system.contract.service.IContractService;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.system.upload.service.IUploadFileService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.UploadUtil;

@Controller
public class ContractAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ContractAction.class);

	@Autowired
	private IContractService contractService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private IUserService userService;
	
	private Page<Contract> pageData;
	private Contract contract;
	private List<UploadFile> uploadFiles;
	private List<File> files;
	
	private List<User> users;
	
	private List<String> filesFileName;
	
	private List<String> filesContentType;

	public void queryPage() {
		try {
			this.pageData = this.contractService.queryPage(this.paramMap,
					this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initSelect() {
		try {
			List<User> users = this.userService.queryAll();
			
			Map<String, List<User>> maps = new HashMap<String, List<User>> ();
			maps.put("contractor", users);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			
		}
	}

	public void queryAll() {
		try {
			List allContracts = this.contractService.queryAllContracts();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allContracts, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.contract = this.contractService.queryById(this.contract.getId());
			this.uploadFiles = this.uploadFileService.queryByTbIdAndTbName(this.contract.getId(), CodeUtil.getClassName(Contract.class));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.users = this.userService.queryAll();
		} catch(Exception ex) {
			
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.uploadFiles = UploadUtil.uploadBatch(this.files, this.filesFileName, CodeUtil.getClassName(Contract.class), CodeUtil.getClassName(Contract.class));
			this.contractService.valid(this.contract);
			if (this.contractService.add(this.contract, this.uploadFiles))
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
			this.uploadFiles = UploadUtil.uploadBatch(this.files, this.filesFileName, CodeUtil.getClassName(Contract.class), CodeUtil.getClassName(Contract.class));
			this.contractService.valid(this.contract);
			if (this.contractService.update(this.contract, this.uploadFiles))
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
			if ((this.contract == null) || (this.contract.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Contract persist = this.contractService.queryById(this.contract
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.contractService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Contract> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Contract> pageData) {
		this.pageData = pageData;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public List<UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
	
}
