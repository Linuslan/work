package com.linuslan.oa.workflow.flows.reimburse.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.reimburse.dao.IReimburseClassDao;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.service.IReimburseClassService;

@Component("reimburseClassService")
@Transactional
public class IReimburseClassServiceImpl extends IBaseServiceImpl implements
		IReimburseClassService {

	@Autowired
	private IReimburseClassDao reimburseClassDao;

	public Page<ReimburseClass> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.reimburseClassDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * sql查询报销类别页面
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Map<String, Object>> queryPageBySql(Map<String, String> paramMap, int currentPage, int limit) {
		return this.reimburseClassDao.queryPageBySql(paramMap, currentPage, limit);
	}

	public List<ReimburseClass> queryByIds(List<Long> ids) {
		return this.reimburseClassDao.queryByIds(ids);
	}

	public List<ReimburseClass> queryAllReimburseClasss() {
		return this.reimburseClassDao.queryAllReimburseClasss();
	}

	public ReimburseClass queryById(Long id) {
		return this.reimburseClassDao.queryById(id);
	}

	public boolean add(ReimburseClass reimburseClass) {
		return this.reimburseClassDao.add(reimburseClass);
	}

	public boolean update(ReimburseClass reimburseClass) {
		return this.reimburseClassDao.update(reimburseClass);
	}

	public boolean del(ReimburseClass reimburseClass) {
		return this.reimburseClassDao.del(reimburseClass);
	}
	
}
