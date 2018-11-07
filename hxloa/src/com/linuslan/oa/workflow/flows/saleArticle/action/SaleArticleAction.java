package com.linuslan.oa.workflow.flows.saleArticle.action;

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
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleArticle.model.SaleArticle;
import com.linuslan.oa.workflow.flows.saleArticle.model.MaterialFormat;
import com.linuslan.oa.workflow.flows.saleArticle.model.Material;
import com.linuslan.oa.workflow.flows.saleArticle.service.ISaleArticleService;

@Controller
public class SaleArticleAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(SaleArticleAction.class);
	
	@Autowired
	private ISaleArticleService saleArticleService;
	
	private Page<SaleArticle> pageData;
	
	private SaleArticle saleArticle;
	
	private Material material;
	
	private MaterialFormat materialFormat;
	
	private List<Material> materials;
	
	private List<MaterialFormat> materialFormats;
	
	public void queryPage() {
		try {
			this.pageData = this.saleArticleService.queryPage(paramMap, page, rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error("查询登陆用户报销申请页面异常："+CodeUtil.getStackTrace(ex));
		}
	}
	
	public String queryById() {
		try {
			if(null != this.saleArticle && null != this.saleArticle.getId()) {
				this.saleArticle = this.saleArticleService.queryById(this.saleArticle.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryAllSaleArticles() {
		try {
			List<SaleArticle> list = this.saleArticleService.queryAll();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(list, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryMaterialFormatsByArticleId() {
		try {
			List<MaterialFormat> materialFormats = this.saleArticleService.queryMaterialFormatsByArticleId(this.saleArticle.getId(), paramMap);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(materialFormats, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void queryMaterialsByArticleId() {
		try {
			List<Material> materials = this.saleArticleService.queryMaterialsByArticleId(this.saleArticle.getId(), paramMap);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONArray json = JSONArray.fromObject(materials, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}
	
	public void add() {
		try {
			if(this.saleArticleService.add(saleArticle, materialFormats, materials)) {
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
			if(this.saleArticleService.update(saleArticle, materialFormats, materials)) {
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
			if(this.saleArticleService.del(saleArticle)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void delMaterialFormatById() {
		try {
			if(this.saleArticleService.delMaterialFormatById(this.materialFormat.getId())) {
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
	
	public void delMaterialById() {
		try {
			if(this.saleArticleService.delMaterialById(this.material.getId())) {
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

	public SaleArticle getSaleArticle() {
		return saleArticle;
	}

	public void setSaleArticle(SaleArticle saleArticle) {
		this.saleArticle = saleArticle;
	}

	public Page<SaleArticle> getPageData() {
		return pageData;
	}

	public void setPageData(Page<SaleArticle> pageData) {
		this.pageData = pageData;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialFormat getMaterialFormat() {
		return materialFormat;
	}

	public void setMaterialFormat(MaterialFormat materialFormat) {
		this.materialFormat = materialFormat;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

	public List<MaterialFormat> getMaterialFormats() {
		return materialFormats;
	}

	public void setMaterialFormats(List<MaterialFormat> materialFormats) {
		this.materialFormats = materialFormats;
	}
	
}
