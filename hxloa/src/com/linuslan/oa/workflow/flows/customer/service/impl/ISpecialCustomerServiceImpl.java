package com.linuslan.oa.workflow.flows.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ISpecialCustomerDao;
import com.linuslan.oa.workflow.flows.customer.model.SpecialCustomer;
import com.linuslan.oa.workflow.flows.customer.service.ISpecialCustomerService;

@Component("specialCustomerService")
@Transactional
public class ISpecialCustomerServiceImpl extends IBaseServiceImpl implements
		ISpecialCustomerService {
	@Autowired
	private ISpecialCustomerDao specialCustomerDao;
	
	/**
	 * 新增客户
	 * @param specialCustomer
	 * @return
	 */
	public boolean add(SpecialCustomer specialCustomer) {
		return this.specialCustomerDao.add(specialCustomer);
	}
	
	/**
	 * 更新客户信息
	 * @param specialCustomer
	 * @return
	 */
	public boolean update(SpecialCustomer specialCustomer) {
		return this.specialCustomerDao.update(specialCustomer);
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param specialCustomer
	 * @return
	 */
	public boolean del(SpecialCustomer specialCustomer) {
		return this.specialCustomerDao.del(specialCustomer);
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public SpecialCustomer queryById(Long id) {
		return this.specialCustomerDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<SpecialCustomer> queryByIds(List<Long> ids) {
		return this.specialCustomerDao.queryByIds(ids);
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<SpecialCustomer> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.specialCustomerDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<SpecialCustomer> queryAll() {
		return this.specialCustomerDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param specialCustomer
	 * @throws Exception
	 */
	public void valid(SpecialCustomer specialCustomer) throws Exception {
		if(null == specialCustomer.getName() || "".equals(specialCustomer.getName().trim())) {
			CodeUtil.throwExcep("客户名称不能为空");
		}
	}
}
