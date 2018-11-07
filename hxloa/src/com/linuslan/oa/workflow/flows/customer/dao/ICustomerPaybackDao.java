package com.linuslan.oa.workflow.flows.customer.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPayback;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPaybackLog;

public interface ICustomerPaybackDao extends IBaseDao {

	public abstract Page<CustomerPayback> queryPage(Map<String, String> paramMap,
			int page, int rows);

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
	
	/**
	 * 新增日志
	 * @param customerPaybacklog
	 * @return
	 */
	public boolean addLog(CustomerPaybackLog customerPaybacklog);
	
}
