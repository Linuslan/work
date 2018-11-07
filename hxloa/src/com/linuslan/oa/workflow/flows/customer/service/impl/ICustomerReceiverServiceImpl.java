package com.linuslan.oa.workflow.flows.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerReceiverDao;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerReceiverService;

@Component("customerReceiverService")
@Transactional
public class ICustomerReceiverServiceImpl extends IBaseServiceImpl implements
		ICustomerReceiverService {

	@Autowired
	private ICustomerReceiverDao customerReceiverDao;
	
	/**
	 * 新增客户
	 * @param customerReceiver
	 * @return
	 */
	public boolean add(CustomerReceiver customerReceiver) {
		return this.customerReceiverDao.add(customerReceiver);
	}
	
	/**
	 * 更新客户信息
	 * @param customerReceiver
	 * @return
	 */
	public boolean update(CustomerReceiver customerReceiver) {
		return this.customerReceiverDao.update(customerReceiver);
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param customerReceiver
	 * @return
	 */
	public boolean del(CustomerReceiver customerReceiver) {
		return this.customerReceiverDao.del(customerReceiver);
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public CustomerReceiver queryById(Long id) {
		return this.customerReceiverDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<CustomerReceiver> queryByIds(List<Long> ids) {
		return this.customerReceiverDao.queryByIds(ids);
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<CustomerReceiver> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.customerReceiverDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<CustomerReceiver> queryAll() {
		return this.customerReceiverDao.queryAll();
	}
	
	/**
	 * 查询登录用户分配的公司对应的客户
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<CustomerReceiver> queryByCustomerId(Long customerId) {
		return this.customerReceiverDao.queryByCustomerId(customerId);
	}
	
	/**
	 * 根据公司查询客户
	 * @param companyId
	 * @return
	 */
	public List<CustomerReceiver> queryByCompanyId(Long companyId) {
		return this.customerReceiverDao.queryByCompanyId(companyId);
	}
	
	/**
	 * 检测有效值
	 * @param customerReceiver
	 * @throws Exception
	 */
	public void valid(CustomerReceiver customerReceiver) throws Exception {
		if(null == customerReceiver.getName() || "".equals(customerReceiver.getName().trim())) {
			CodeUtil.throwExcep("客户名称不能为空");
		}
	}
}
