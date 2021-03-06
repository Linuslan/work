package com.linuslan.oa.workflow.flows.customer.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;

public interface ICustomerReceiverService extends IBaseService {

	/**
	 * 新增区域
	 * @param customerReceiver
	 * @return
	 */
	public boolean add(CustomerReceiver customerReceiver);
	
	/**
	 * 更新区域信息
	 * @param customerReceiver
	 * @return
	 */
	public boolean update(CustomerReceiver customerReceiver);
	
	/**
	 * 删除区域，伪删除，将isDelete状态改为1
	 * @param customerReceiver
	 * @return
	 */
	public boolean del(CustomerReceiver customerReceiver);
	
	/**
	 * 通过id查询区域
	 * @param id
	 * @return
	 */
	public CustomerReceiver queryById(Long id);
	
	/**
	 * 查询指定id的所有区域
	 * @param ids
	 * @return
	 */
	public List<CustomerReceiver> queryByIds(List<Long> ids);
	
	/**
	 * 查询区域列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<CustomerReceiver> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的区域
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
	
	/**
	 * 检测有效值
	 * @param customerReceiver
	 * @throws Exception
	 */
	public void valid(CustomerReceiver customerReceiver) throws Exception;
	
}
