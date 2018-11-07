package com.linuslan.oa.workflow.flows.customer.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;

public interface ICustomerReceiverDao extends IBaseDao {
	
	/**
	 * 新增客户
	 * @param customerReceiver
	 * @return
	 */
	public boolean add(CustomerReceiver customerReceiver);
	
	/**
	 * 更新客户信息
	 * @param customerReceiver
	 * @return
	 */
	public boolean update(CustomerReceiver customerReceiver);
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param customerReceiver
	 * @return
	 */
	public boolean del(CustomerReceiver customerReceiver);
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public CustomerReceiver queryById(Long id);
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<CustomerReceiver> queryByIds(List<Long> ids);
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<CustomerReceiver> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<CustomerReceiver> queryAll();
	
	/**
	 * 查询登录用户分配的公司对应的客户
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<CustomerReceiver> queryByCustomerId(Long customerId);
	
	/**
	 * 根据公司查询客户
	 * @param companyId
	 * @return
	 */
	public List<CustomerReceiver> queryByCompanyId(Long companyId);
}
