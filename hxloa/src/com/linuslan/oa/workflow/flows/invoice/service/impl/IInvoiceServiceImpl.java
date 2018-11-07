package com.linuslan.oa.workflow.flows.invoice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.ValidationUtil;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.invoice.model.Invoice;
import com.linuslan.oa.workflow.flows.invoice.dao.IInvoiceDao;
import com.linuslan.oa.workflow.flows.invoice.service.IInvoiceService;

@Component("invoiceService")
@Transactional
public class IInvoiceServiceImpl extends IBaseServiceImpl implements
		IInvoiceService {
	@Autowired
	private IInvoiceDao invoiceDao;
	
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
	public Page<Invoice> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.invoiceDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.invoiceDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的开票
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.invoiceDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询开票汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.invoiceDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap) {
		return this.invoiceDao.sumReportPage(paramMap);
	}
	
	/**
	 * 通过id查询企业付款
	 * @param id
	 * @return
	 */
	public Invoice queryById(Long id) {
		return this.invoiceDao.queryById(id);
	}
	
	/**
	 * 新增企业付款
	 * @param invoice
	 * @return
	 */
	public boolean add(Invoice invoice) {
		boolean success = false;
		if(null == invoice) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		invoice.setUserId(HttpUtil.getLoginUser().getId());
		invoice.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
		//验证对象的有效性
		this.valid(invoice);
		if(this.invoiceDao.add(invoice)) {
			//启动流程，生成流程实例
			this.engineUtil.startFlow(invoice, CodeUtil.getClassName(Invoice.class));
			success = true;
		}
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param invoice
	 * @return
	 */
	public boolean update(Invoice invoice) {
		boolean success = false;
		if(null == invoice || null == invoice.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		Invoice persist = this.invoiceDao.queryById(invoice.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (Invoice) BeanUtil.updateBean(persist, invoice);
		this.valid(persist);
		if(this.invoiceDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param invoice
	 * @return
	 */
	public boolean del(Invoice invoice) {
		boolean success = false;
		if(null == invoice || null == invoice.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Invoice persist = this.invoiceDao.queryById(invoice.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.invoiceDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param invoice
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Invoice invoice, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == invoice) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != invoice.getId()) {
			Invoice persist = this.invoiceDao.queryById(invoice.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Invoice) BeanUtil.updateBean(persist, invoice);
			invoice = persist;
			this.valid(persist);
			updateSuccess = this.invoiceDao.update(persist);
		} else {
			invoice.setUserId(HttpUtil.getLoginUser().getId());
			invoice.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
			this.valid(invoice);
			updateSuccess = this.invoiceDao.add(invoice);
			this.engineUtil.startFlow(invoice, CodeUtil.getClassName(Invoice.class));
		}
		if(updateSuccess) {
			//验证流程实例是否可以提交
			this.validFlowStatus(invoice, false);
			this.engineUtil.execute(invoice, CodeUtil.getClassName(Invoice.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param invoice
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Invoice invoice, int passType, String opinion) {
		boolean success = false;
		if(null == invoice || null == invoice.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Invoice persist = this.invoiceDao.queryById(invoice.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		BeanUtil.updateBean(persist, invoice);
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.invoiceDao.update(persist);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Invoice.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 验证对象的有效性
	 * @param invoice
	 */
	public void valid(Invoice invoice) {
		if(null == invoice.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		if(null == invoice.getIncomeDeptId()) {
			CodeUtil.throwRuntimeExcep("请选择收入归属部门");
		}
		if(null == invoice.getInvoiceMoney()) {
			CodeUtil.throwRuntimeExcep("请输入开票金额");
		}
		if(null == invoice.getSupposedMoney()) {
			CodeUtil.throwRuntimeExcep("请输入预计回款金额");
		}
		if(null == invoice.getInvoiceDate()) {
			CodeUtil.throwRuntimeExcep("请输入开票时间");
		}
		if(null == invoice.getInvoiceType()) {
			CodeUtil.throwRuntimeExcep("请选择开票方式");
		}
		if(CodeUtil.isEmpty(invoice.getAddress())) {
			CodeUtil.throwRuntimeExcep("请输入对方地址");
		}
		if(CodeUtil.isEmpty(invoice.getBank())) {
			CodeUtil.throwRuntimeExcep("请输入对方开户行");
		}
		if(CodeUtil.isEmpty(invoice.getBankNo())) {
			CodeUtil.throwRuntimeExcep("请输入对方银行账号");
		}
		if(!ValidationUtil.isAlphaNumberic(invoice.getBankNo())) {
			CodeUtil.throwRuntimeExcep("银行卡号格式不正确");
		}
		if(CodeUtil.isEmpty(invoice.getTaxPayerId())) {
			CodeUtil.throwRuntimeExcep("请输入纳税人识别号");
		}
	}
}
