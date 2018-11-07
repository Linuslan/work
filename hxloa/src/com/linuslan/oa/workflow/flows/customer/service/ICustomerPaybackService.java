package com.linuslan.oa.workflow.flows.customer.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPayback;

public interface ICustomerPaybackService extends IBaseService {

	public abstract Page<CustomerPayback> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<CustomerPayback> queryByIds(List<Long> ids);
	
	public List<CustomerPayback> queryByUserId(Long userId);

	public abstract List<CustomerPayback> queryAllCustomerPaybacks();

	public abstract CustomerPayback queryById(Long paramLong);
	
	/**
	 * 通过公司和客户查询回款统计
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> countByCompanyIdAndCustomerId(Long companyId, Long customerId);

	public abstract boolean add(CustomerPayback paramCustomerPayback);

	public abstract boolean update(CustomerPayback paramCustomerPayback);

	public abstract boolean del(CustomerPayback paramCustomerPayback);
	
}
