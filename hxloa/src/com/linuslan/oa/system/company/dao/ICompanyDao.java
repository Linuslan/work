package com.linuslan.oa.system.company.dao;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.util.Page;
import java.util.List;
import java.util.Map;

public abstract interface ICompanyDao extends IBaseDao {
	public abstract Page<Company> queryPage(Map<String, String> paramMap,
			int paramInt1, int paramInt2);

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