package com.linuslan.oa.workflow.flows.customer.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.SpecialCustomer;

public interface ISpecialCustomerService extends IBaseService {

	/**
	 * 新增客户
	 * @param specialCustomer
	 * @return
	 */
	public boolean add(SpecialCustomer specialCustomer);
	
	/**
	 * 更新客户信息
	 * @param specialCustomer
	 * @return
	 */
	public boolean update(SpecialCustomer specialCustomer);
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param specialCustomer
	 * @return
	 */
	public boolean del(SpecialCustomer specialCustomer);
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public SpecialCustomer queryById(Long id);
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<SpecialCustomer> queryByIds(List<Long> ids);
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<SpecialCustomer> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<SpecialCustomer> queryAll();
	
	/**
	 * 检测有效值
	 * @param specialCustomer
	 * @throws Exception
	 */
	public void valid(SpecialCustomer specialCustomer) throws Exception;
	
}
