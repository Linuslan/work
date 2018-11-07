package com.linuslan.oa.workflow.flows.saleStuff.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.model.Effect;
import com.linuslan.oa.workflow.flows.saleStuff.model.Face;
import com.linuslan.oa.workflow.flows.saleStuff.service.IFaceService;

@Controller
public class FaceAction extends BaseAction {

	private static Logger logger = Logger.getLogger(FaceAction.class);
	
	@Autowired
	private IFaceService faceService;
	
	private Page<Face> pageData;
	
	private Face face;
	
	public void queryPage() {
		try {
			this.pageData = this.faceService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryAll() {
		try {
			List<Face> allFaces = this.faceService.queryAll();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(allFaces, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			this.face = this.faceService.queryById(this.face.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.faceService.valid(this.face);
			if(this.faceService.add(this.face)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败！");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void update() {
		try {
			this.faceService.valid(this.face);
			if(this.faceService.update(face)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep(this.failureMsg);
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void del() {
		try {
			if(null == this.face || null == this.face.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Face persist = this.faceService.queryById(this.face.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.faceService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<Face> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Face> pageData) {
		this.pageData = pageData;
	}

	public Face getFace() {
		return face;
	}

	public void setFace(Face face) {
		this.face = face;
	}
}
