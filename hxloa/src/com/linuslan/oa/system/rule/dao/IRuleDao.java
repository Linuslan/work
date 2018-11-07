package com.linuslan.oa.system.rule.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.rule.model.Rule;
import com.linuslan.oa.util.Page;

public interface IRuleDao extends IBaseDao {

	public abstract Page<Rule> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Rule> queryByIds(List<Long> ids);

	public abstract List<Rule> queryAllRules();

	public abstract Rule queryById(Long id);
	
	/**
	 * 检查value值是否唯一
	 * @param rule
	 * @return true: 唯一; false: 重复
	 */
	public boolean checkUniqueValue(Rule rule);

	public abstract boolean add(Rule rule);

	public abstract boolean update(Rule rule);

	public abstract boolean del(Rule rule);
	
}
