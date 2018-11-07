package com.linuslan.oa.system.company.service;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.util.Page;
import java.util.List;
import java.util.Map;

public abstract interface ICompanyService extends IBaseService {
	public abstract Page<Company> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Company> queryByIds(List<Long> paramList);

	public abstract List<Company> queryAllCompanys();

	public abstract Company queryById(Long paramLong);
	
	/**
	 * 查询分配给用户的公司
	 * @param userId
	 * @return
	 */
	public List<Company> queryByUserId(Long userId);

	public abstract boolean add(Company paramCompany);

	public abstract boolean update(Company paramCompany);

	public abstract boolean del(Company paramCompany);
}