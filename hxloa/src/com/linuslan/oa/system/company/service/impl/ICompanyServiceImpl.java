package com.linuslan.oa.system.company.service.impl;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.company.dao.ICompanyDao;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("companyService")
@Transactional
public class ICompanyServiceImpl extends IBaseServiceImpl implements
		ICompanyService {

	@Autowired
	private ICompanyDao companyDao;

	public Page<Company> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.companyDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Company> queryByIds(List<Long> ids) {
		return this.companyDao.queryByIds(ids);
	}

	public List<Company> queryAllCompanys() {
		return this.companyDao.queryAllCompanys();
	}

	public Company queryById(Long id) {
		return this.companyDao.queryById(id);
	}
	
	/**
	 * 查询分配给用户的公司
	 * @param userId
	 * @return
	 */
	public List<Company> queryByUserId(Long userId) {
		return this.companyDao.queryByUserId(userId);
	}

	public boolean add(Company company) {
		return this.companyDao.add(company);
	}

	public boolean update(Company company) {
		Company persist = this.companyDao.queryById(company.getId());
		BeanUtil.updateBean(persist, company);
		return this.companyDao.update(persist);
	}

	public boolean del(Company company) {
		return this.companyDao.del(company);
	}
}