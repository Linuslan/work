package com.linuslan.oa.system.cellphone.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.cellphone.model.Cellphone;
import com.linuslan.oa.system.cellphone.service.ICellphoneService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class CellphoneAction extends BaseAction {

	private static Logger logger = Logger.getLogger(CellphoneAction.class);

	@Autowired
	private ICellphoneService cellphoneService;
	private Page<Cellphone> pageData;
	private Cellphone cellphone;

	public void queryPage() {
		try {
			this.pageData = this.cellphoneService.queryPage(this.paramMap,
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
			List<Cellphone> allCellphones = this.cellphoneService.queryAllCellphones();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allCellphones, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.cellphone = this.cellphoneService.queryById(this.cellphone.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.cellphoneService.valid(this.cellphone);
			if (this.cellphoneService.add(this.cellphone))
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
			this.cellphoneService.valid(this.cellphone);
			if (this.cellphoneService.update(this.cellphone))
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
			if ((this.cellphone == null) || (this.cellphone.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Cellphone persist = this.cellphoneService.queryById(this.cellphone
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.cellphoneService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Cellphone> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Cellphone> pageData) {
		this.pageData = pageData;
	}

	public Cellphone getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(Cellphone cellphone) {
		this.cellphone = cellphone;
	}
	
}
