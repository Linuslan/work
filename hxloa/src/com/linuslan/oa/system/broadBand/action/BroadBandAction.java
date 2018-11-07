package com.linuslan.oa.system.broadBand.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.broadBand.action.BroadBandAction;
import com.linuslan.oa.system.broadBand.model.BroadBand;
import com.linuslan.oa.system.broadBand.service.IBroadBandService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class BroadBandAction extends BaseAction {

	private static Logger logger = Logger.getLogger(BroadBandAction.class);

	@Autowired
	private IBroadBandService broadBandService;
	private Page<BroadBand> pageData;
	private BroadBand broadBand;

	public void queryPage() {
		try {
			this.pageData = this.broadBandService.queryPage(this.paramMap,
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
			List<BroadBand> allBroadBands = this.broadBandService.queryAllBroadBands();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allBroadBands, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.broadBand = this.broadBandService.queryById(this.broadBand.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.broadBandService.valid(this.broadBand);
			if (this.broadBandService.add(this.broadBand))
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
			this.broadBandService.valid(this.broadBand);
			if (this.broadBandService.update(this.broadBand))
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
			if ((this.broadBand == null) || (this.broadBand.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			BroadBand persist = this.broadBandService.queryById(this.broadBand
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.broadBandService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<BroadBand> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<BroadBand> pageData) {
		this.pageData = pageData;
	}

	public BroadBand getBroadBand() {
		return this.broadBand;
	}

	public void setBroadBand(BroadBand broadBand) {
		this.broadBand = broadBand;
	}
	
}
