package com.linuslan.oa.system.capital.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.capital.dao.ICapitalDao;
import com.linuslan.oa.system.capital.model.Capital;
import com.linuslan.oa.system.capital.service.ICapitalService;
import com.linuslan.oa.util.Page;

@Component("capitalService")
@Transactional
public class ICapitalServiceImpl extends IBaseServiceImpl implements
		ICapitalService {
	@Autowired
	private ICapitalDao capitalDao;
	
	/**
	 * 查询固话
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Capital> queryPage(Map<String, String> paramMap, int year, int month, int currentPage, int limit) {
		return this.capitalDao.queryPage(paramMap, year, month, currentPage, limit);
	}
	
	/**
	 * 不分页查询固资
	 * @param paramMap
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Capital> queryCapitals(Map<String, String> paramMap,int year, int month) {
		return this.capitalDao.queryCapitals(paramMap, year, month);
	}
	
	/**
	 * 查询类别
	 * @return
	 */
	public List<Map<String, Object>> queryClassName() {
		return this.capitalDao.queryClassName();
	}
	
	/**
	 * 查询厂家
	 * @return
	 */
	public List<Map<String, Object>> queryShopName() {
		return this.capitalDao.queryShopName();
	}
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryAddress() {
		return this.capitalDao.queryAddress();
	}
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryUser() {
		return this.capitalDao.queryUser();
	}
	
	public List<Map<String, Object>> queryDepartment() {
		return this.capitalDao.queryDepartment();
	}
	
	public List<Map<String, Object>> queryBorrowDepartment() {
		return this.capitalDao.queryBorrowDepartment();
	}
	
	/**
	 * 通过id查询备用金
	 * @param id
	 * @return
	 */
	public Capital queryById(Long id) {
		return this.capitalDao.queryById(id);
	}
	
	/**
	 * 新增备用金申请
	 * @param pettyCash
	 * @return
	 */
	public Capital add(Capital capital) {
		return this.capitalDao.add(capital);
	}
	
	/**
	 * 更新开票
	 * @param pettyCash
	 * @return
	 */
	public Capital update(Capital capital) {
		return this.capitalDao.update(capital);
	}
	
	/**
	 * 通过id删除备用金
	 * @param id
	 * @return
	 */
	public boolean delById(Long id) {
		return this.capitalDao.delById(id);
	}
}
