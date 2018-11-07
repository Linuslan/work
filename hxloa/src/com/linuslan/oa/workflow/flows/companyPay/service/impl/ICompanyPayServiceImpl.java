package com.linuslan.oa.workflow.flows.companyPay.service.impl;

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
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.companyPay.dao.ICompanyPayDao;
import com.linuslan.oa.workflow.flows.companyPay.model.CompanyPay;
import com.linuslan.oa.workflow.flows.companyPay.service.ICompanyPayService;

@Component("companyPayService")
@Transactional
public class ICompanyPayServiceImpl extends IBaseServiceImpl implements ICompanyPayService{
	
	@Autowired
	private ICompanyPayDao companyPayDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	/**
	 * 查询登陆用户的企业付款申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.companyPayDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.companyPayDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的企业付款
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.companyPayDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询企业付款汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CompanyPay> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.companyPayDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap) {
		return this.companyPayDao.sumReportPage(paramMap);
	}
	
	/**
	 * 通过id查询企业付款
	 * @param id
	 * @return
	 */
	public CompanyPay queryById(Long id) {
		return this.companyPayDao.queryById(id);
	}
	
	/**
	 * 通过ids
	 * @param ids
	 * @return
	 */
	public List<CompanyPay> queryInIds(List<Long> ids) {
		return this.companyPayDao.queryInIds(ids);
	}
	
	/**
	 * 新增企业付款
	 * @param companyPay
	 * @return
	 */
	public boolean add(CompanyPay companyPay) {
		boolean success = false;
		if(null == companyPay) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		companyPay.setUserId(HttpUtil.getLoginUser().getId());
		companyPay.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
		//验证对象的有效性
		this.valid(companyPay);
		if(this.companyPayDao.add(companyPay)) {
			//启动流程，生成流程实例
			this.engineUtil.startFlow(companyPay, CodeUtil.getClassName(CompanyPay.class));
			success = true;
		}
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param companyPay
	 * @return
	 */
	public boolean update(CompanyPay companyPay) {
		boolean success = false;
		if(null == companyPay || null == companyPay.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		CompanyPay persist = this.companyPayDao.queryById(companyPay.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (CompanyPay) BeanUtil.updateBean(persist, companyPay);
		this.valid(persist);
		if(this.companyPayDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param companyPay
	 * @return
	 */
	public boolean del(CompanyPay companyPay) {
		boolean success = false;
		if(null == companyPay || null == companyPay.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		CompanyPay persist = this.companyPayDao.queryById(companyPay.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.companyPayDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param companyPay
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(CompanyPay companyPay, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == companyPay) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != companyPay.getId()) {
			CompanyPay persist = this.companyPayDao.queryById(companyPay.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (CompanyPay) BeanUtil.updateBean(persist, companyPay);
			companyPay = persist;
			this.valid(persist);
			updateSuccess = this.companyPayDao.update(persist);
		} else {
			companyPay.setUserId(HttpUtil.getLoginUser().getId());
			companyPay.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
			this.valid(companyPay);
			updateSuccess = this.companyPayDao.add(companyPay);
			this.engineUtil.startFlow(companyPay, CodeUtil.getClassName(CompanyPay.class));
		}
		if(updateSuccess) {
			//验证流程实例是否可以提交
			this.validFlowStatus(companyPay, false);
			this.engineUtil.execute(companyPay, CodeUtil.getClassName(CompanyPay.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param companyPay
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(CompanyPay companyPay, int passType, String opinion) {
		boolean success = false;
		if(null == companyPay || null == companyPay.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		CompanyPay persist = this.companyPayDao.queryById(companyPay.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(CompanyPay.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 验证对象的有效性
	 * @param companyPay
	 */
	public void valid(CompanyPay companyPay) {
		if(null == companyPay.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		if(null == companyPay.getPayDeptId()) {
			CodeUtil.throwRuntimeExcep("请选择费用承担部门");
		}
		if(null == companyPay.getPayCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择费用承担公司");
		}
		if(null == companyPay.getMoneyDate()) {
			CodeUtil.throwRuntimeExcep("请输入费用产生时间");
		}
		if(null == companyPay.getPayType()) {
			CodeUtil.throwRuntimeExcep("请选择付款方式");
		}
		if(CodeUtil.isEmpty(companyPay.getReceiver())) {
			CodeUtil.throwRuntimeExcep("请输入收款方名称");
		}
		if(CodeUtil.isEmpty(companyPay.getBank())) {
			CodeUtil.throwRuntimeExcep("请输入收款方开户行");
		}
		if(CodeUtil.isEmpty(companyPay.getBankNo())) {
			CodeUtil.throwRuntimeExcep("请输入收款方银行账号");
		}
	}
}
