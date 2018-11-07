package com.linuslan.oa.workflow.flows.customer.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerPaybackDao;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPayback;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPaybackLog;
import com.linuslan.oa.workflow.flows.customer.service.ICustomerPaybackService;

@Component("customerPaybackService")
@Transactional
public class ICustomerPaybackServiceImpl extends IBaseServiceImpl implements
		ICustomerPaybackService {

	@Autowired
	private ICustomerPaybackDao customerPaybackDao;

	public Page<CustomerPayback> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.customerPaybackDao.queryPage(paramMap, currentPage, limit);
	}

	public List<CustomerPayback> queryByIds(List<Long> ids) {
		return this.customerPaybackDao.queryByIds(ids);
	}
	
	public List<CustomerPayback> queryByUserId(Long userId) {
		return this.customerPaybackDao.queryByUserId(userId);
	}

	public List<CustomerPayback> queryAllCustomerPaybacks() {
		return this.customerPaybackDao.queryAllCustomerPaybacks();
	}

	public CustomerPayback queryById(Long id) {
		return this.customerPaybackDao.queryById(id);
	}
	
	/**
	 * 通过公司和客户查询回款统计
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> countByCompanyIdAndCustomerId(Long companyId, Long customerId) {
		return this.customerPaybackDao.countByCompanyIdAndCustomerId(companyId, customerId);
	}

	public boolean add(CustomerPayback customerPayback) {
		boolean success = false;
		try {
			this.customerPaybackDao.add(customerPayback);
			CustomerPaybackLog log = this.buildLog(customerPayback, "add");
			this.customerPaybackDao.addLog(log);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}

	public boolean update(CustomerPayback customerPayback) {
		boolean success = false;
		try {
			CustomerPayback persist = this.customerPaybackDao.queryById(customerPayback.getId());
			BeanUtil.updateBean(persist, customerPayback);
			this.customerPaybackDao.update(persist);
			CustomerPaybackLog log = this.buildLog(customerPayback, "update");
			this.customerPaybackDao.addLog(log);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	public boolean del(CustomerPayback customerPayback) {
		boolean success = false;
		try {
			CustomerPaybackLog log = this.buildLog(customerPayback, "del");
			this.customerPaybackDao.del(customerPayback);
			this.customerPaybackDao.addLog(log);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public void valid(CustomerPayback customerPayback) throws RuntimeException {
		if(null == customerPayback) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		if(null == customerPayback.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择公司");
		}
		if(null == customerPayback.getCustomerId()) {
			CodeUtil.throwRuntimeExcep("请选择客户");
		}
		if(CodeUtil.isEmpty(customerPayback.getMemo())) {
			CodeUtil.throwRuntimeExcep("请输入回款备注");
		}
		if(null == customerPayback.getPayDate()) {
			CodeUtil.throwRuntimeExcep("请输入回款时间");
		}
		if(null == customerPayback.getPayMoney()) {
			CodeUtil.throwRuntimeExcep("请输入回款金额");
		}
		Map<String, Object> paybackInfo = this.customerPaybackDao.countByCompanyIdAndCustomerId(customerPayback.getCompanyId(),
				customerPayback.getCustomerId());
		BigDecimal totalSale = (BigDecimal) paybackInfo.get("TOTAL_SALE");
		BigDecimal totalPayback = (BigDecimal) paybackInfo.get("TOTAL_PAYBACK");
		BigDecimal totalUnpayback = (BigDecimal) paybackInfo.get("TOTAL_UNPAYBACK");
		if(customerPayback.getPayMoney().compareTo(totalUnpayback)>0) {
			CodeUtil.throwRuntimeExcep("回款金额不能大于未回款金额");
		}
	}
	
	public CustomerPaybackLog buildLog(CustomerPayback payback, String operate) {
		CustomerPaybackLog log = new CustomerPaybackLog();
		log.setCreateDate(new Date());
		log.setCustomerPaybackId(payback.getId());
		log.setMemo(payback.getMemo());
		log.setOperate(operate);
		log.setUserId(HttpUtil.getLoginUser().getId());
		log.setUserName(HttpUtil.getLoginUser().getName());
		return log;
	}
	
}
