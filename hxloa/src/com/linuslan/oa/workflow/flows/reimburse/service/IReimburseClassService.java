package com.linuslan.oa.workflow.flows.reimburse.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;

public interface IReimburseClassService extends IBaseService {

	public abstract Page<ReimburseClass> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);
	
	/**
	 * sql查询报销类别页面
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Map<String, Object>> queryPageBySql(Map<String, String> paramMap, int currentPage, int limit);

	public abstract List<ReimburseClass> queryByIds(List<Long> paramList);

	public abstract List<ReimburseClass> queryAllReimburseClasss();

	public abstract ReimburseClass queryById(Long paramLong);

	public abstract boolean add(ReimburseClass paramReimburseClass);

	public abstract boolean update(ReimburseClass paramReimburseClass);

	public abstract boolean del(ReimburseClass paramReimburseClass);
}
