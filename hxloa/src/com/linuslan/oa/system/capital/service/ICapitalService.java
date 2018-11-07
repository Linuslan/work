package com.linuslan.oa.system.capital.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.capital.model.Capital;
import com.linuslan.oa.util.Page;

public interface ICapitalService extends IBaseService {

	/**
	 * 查询固话
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Capital> queryPage(Map<String, String> paramMap, int year, int month, int currentPage, int limit);
	
	/**
	 * 不分页查询固资
	 * @param paramMap
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Capital> queryCapitals(Map<String, String> paramMap,int year, int month);
	
	/**
	 * 查询类别
	 * @return
	 */
	public List<Map<String, Object>> queryClassName();
	
	/**
	 * 查询厂家
	 * @return
	 */
	public List<Map<String, Object>> queryShopName();
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryAddress();
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryUser();
	
	public List<Map<String, Object>> queryDepartment();
	
	public List<Map<String, Object>> queryBorrowDepartment();
	
	/**
	 * 通过id查询备用金
	 * @param id
	 * @return
	 */
	public Capital queryById(Long id);
	
	/**
	 * 新增备用金申请
	 * @param pettyCash
	 * @return
	 */
	public Capital add(Capital capital);
	
	/**
	 * 更新开票
	 * @param pettyCash
	 * @return
	 */
	public Capital update(Capital capital);
	
	/**
	 * 通过id删除备用金
	 * @param id
	 * @return
	 */
	public boolean delById(Long id);
	
}
