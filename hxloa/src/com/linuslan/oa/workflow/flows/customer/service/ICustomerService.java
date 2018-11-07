package com.linuslan.oa.workflow.flows.customer.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.Customer;

public interface ICustomerService extends IBaseService {

	/**
	 * 新增区域
	 * @param customer
	 * @return
	 */
	public boolean add(Customer customer);
	
	/**
	 * 更新区域信息
	 * @param customer
	 * @return
	 */
	public boolean update(Customer customer);
	
	/**
	 * 删除区域，伪删除，将isDelete状态改为1
	 * @param customer
	 * @return
	 */
	public boolean del(Customer customer);
	
	/**
	 * 通过id查询区域
	 * @param id
	 * @return
	 */
	public Customer queryById(Long id);
	
	/**
	 * 查询指定id的所有区域
	 * @param ids
	 * @return
	 */
	public List<Customer> queryByIds(List<Long> ids);
	
	/**
	 * 查询区域列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Customer> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的区域
	 * @param paramMap
	 * @return
	 */
	public List<Customer> queryAll();
	
	/**
	 * 查询登录用户分配的公司对应的客户
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<Customer> queryByUser(Long userId);
	
	/**
	 * 根据公司查询客户
	 * @param companyId
	 * @return
	 */
	public List<Customer> queryByCompanyId(Long companyId);
	
	/**
	 * 检测有效值
	 * @param customer
	 * @throws Exception
	 */
	public void valid(Customer customer) throws Exception;
	
}
