package com.linuslan.oa.system.rule.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.rule.dao.IRuleDao;
import com.linuslan.oa.system.rule.model.Rule;
import com.linuslan.oa.system.rule.service.IRuleService;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.OfficeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.PropertyUtil;
import com.linuslan.oa.util.UploadUtil;

@Component("ruleService")
@Transactional
public class IRuleServiceImpl extends IBaseServiceImpl implements IRuleService {

	@Autowired
	private IRuleDao ruleDao;
	
	public Page<Rule> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.ruleDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Rule> queryByIds(List<Long> ids) {
		return this.ruleDao.queryByIds(ids);
	}

	public List<Rule> queryAllRules() {
		return this.ruleDao.queryAllRules();
	}

	public Rule queryById(Long id) {
		return this.ruleDao.queryById(id);
	}

	public boolean add(Rule rule, File file, String fileName) {
		boolean success = false;
		UploadFile uploadFile = null;
		try {
			String path = PropertyUtil.getConfigPropertyValue("officeDir");
			String realPath = HttpUtil.getRealPath(path);
			if(null != file) {
				//String html = OfficeUtil.convert2Html(file, realPath, HttpUtil.getAppName()+"/"+path);
				//rule.setContent(html);
				uploadFile = UploadUtil.upload(file, fileName, ConstantVar.CONFIG_RULE_DIR, CodeUtil.getClassName(Rule.class));
				rule.setFilePath(uploadFile.getFilePath());
			}
			success = this.ruleDao.add(rule);
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
			if(null != uploadFile && CodeUtil.isNotEmpty(uploadFile.getFilePath())) {
				UploadUtil.del(uploadFile);
			}
			success = false;
		}
		return success;
	}

	public boolean update(Rule rule, File file) {
		boolean success = false;
		try {
			String path = PropertyUtil.getConfigPropertyValue("officeDir");
			String realPath = HttpUtil.getRealPath(path);
			if(null != file) {
				String html = OfficeUtil.convert2Html(file, realPath, HttpUtil.getAppName()+"/"+path);
				rule.setContent(html);
			}
			Rule persist = this.ruleDao.queryById(rule.getId());
			BeanUtil.updateBean(persist, rule);
			success = this.ruleDao.update(persist);
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
			success = false;
		}
		
		return success;
	}

	public boolean del(Rule rule) {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setFilePath(rule.getFilePath());
		boolean success = true;
		if(this.ruleDao.del(rule)) {
			success = true;
			UploadUtil.del(uploadFile);
		} else {
			success = false;
		}
		return success;
	}
	
	public void valid(Rule rule) throws Exception {
		if(null == rule) {
			CodeUtil.throwExcep("获取制度数据异常");
		}
		if(CodeUtil.isEmpty(rule.getTitle())) {
			CodeUtil.throwExcep("请输入标题");
		}
	}
	
}
