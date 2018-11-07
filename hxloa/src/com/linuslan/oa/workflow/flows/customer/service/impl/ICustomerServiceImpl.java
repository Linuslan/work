package com.linuslan.oa.workflow.flows.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerDao;
import com.linuslan.oa.workflow.flows.customer.model.Customer;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerService;

@Component("customerService")
@Transactional
public class ICustomerServiceImpl extends IBaseServiceImpl implements
		ICustomerService {

	@Autowired
	private ICustomerDao customerDao;
	
	/**
	 * 新增客户
	 * @param customer
	 * @return
	 */
	public boolean add(Customer customer) {
		return this.customerDao.add(customer);
	}
	
	/**
	 * 更新客户信息
	 * @param customer
	 * @return
	 */
	public boolean update(Customer customer) {
		return this.customerDao.update(customer);
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param customer
	 * @return
	 */
	public boolean del(Customer customer) {
		return this.customerDao.del(customer);
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public Customer queryById(Long id) {
		return this.customerDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<Customer> queryByIds(List<Long> ids) {
		return this.customerDao.queryByIds(ids);
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Customer> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.customerDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<Customer> queryAll() {
		return this.customerDao.queryAll();
	}
	
	/**
	 * 查询登录用户分配的公司对应的客户
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<Customer> queryByUser(Long userId) {
		return this.customerDao.queryByUser(userId);
	}
	
	/**
	 * 根据公司查询客户
	 * @param companyId
	 * @return
	 */
	public List<Customer> queryByCompanyId(Long companyId) {
		return this.customerDao.queryByCompanyId(companyId);
	}
	
	/**
	 * 检测有效值
	 * @param customer
	 * @throws Exception
	 */
	public void valid(Customer customer) throws Exception {
		if(null == customer.getName() || "".equals(customer.getName().trim())) {
			CodeUtil.throwExcep("客户名称不能为空");
		}
	}
}
