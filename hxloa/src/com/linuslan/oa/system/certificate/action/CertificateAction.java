package com.linuslan.oa.system.certificate.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.certificate.action.CertificateAction;
import com.linuslan.oa.system.certificate.model.Certificate;
import com.linuslan.oa.system.certificate.service.ICertificateService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class CertificateAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(CertificateAction.class);

	@Autowired
	private ICertificateService certificateService;
	private Page<Certificate> pageData;
	private Certificate certificate;

	public void queryPage() {
		try {
			this.pageData = this.certificateService.queryPage(this.paramMap,
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
			List allCertificates = this.certificateService.queryAllCertificates();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allCertificates, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.certificate = this.certificateService.queryById(this.certificate.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.certificateService.valid(this.certificate);
			if (this.certificateService.add(this.certificate))
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
			this.certificateService.valid(this.certificate);
			if (this.certificateService.update(this.certificate))
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
			if ((this.certificate == null) || (this.certificate.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Certificate persist = this.certificateService.queryById(this.certificate
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.certificateService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Certificate> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Certificate> pageData) {
		this.pageData = pageData;
	}

	public Certificate getCertificate() {
		return this.certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	
}
