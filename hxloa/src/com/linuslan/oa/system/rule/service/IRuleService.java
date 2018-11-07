package com.linuslan.oa.system.rule.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.rule.model.Rule;
import com.linuslan.oa.util.Page;

public interface IRuleService extends IBaseService {

	public abstract Page<Rule> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Rule> queryByIds(List<Long> ids);

	public abstract List<Rule> queryAllRules();

	public abstract Rule queryById(Long id);

	public abstract boolean add(Rule rule, File file, String fileName);

	public abstract boolean update(Rule rule, File file);

	public abstract boolean del(Rule rule);
	
}
