package com.linuslan.oa.system.company.action;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CompanyAction extends BaseAction {
	private static Logger logger = Logger.getLogger(CompanyAction.class);

	@Autowired
	private ICompanyService companyService;
	private Page<Company> pageData;
	private Company company;

	public void queryPage() {
		try {
			this.pageData = this.companyService.queryPage(this.paramMap,
					this.page, this.rows);
			JSONObject json = JSONObject.fromObject(this.pageData);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAll() {
		try {
			List allCompanys = this.companyService.queryAllCompanys();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allCompanys, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.company = this.companyService.queryById(this.company.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}

	public void add() {
		try {
			this.companyService.valid(this.company);
			if (this.companyService.add(this.company))
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
			this.companyService.valid(this.company);
			if (this.companyService.update(this.company))
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
			if ((this.company == null) || (this.company.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Company persist = this.companyService.queryById(this.company
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.companyService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Company> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Company> pageData) {
		this.pageData = pageData;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}