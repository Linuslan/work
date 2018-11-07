package com.linuslan.oa.workflow.flows.invoice.service;

import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.invoice.model.Invoice;

public interface IInvoiceService extends IBaseService {

	/**
	 * 查询登陆用户的企业付款申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的开票
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询开票汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Invoice> queryReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap);
	
	/**
	 * 通过id查询企业付款
	 * @param id
	 * @return
	 */
	public Invoice queryById(Long id);

	/**
	 * 新增企业付款
	 * @param invoice
	 * @return
	 */
	public boolean add(Invoice invoice);
	
	/**
	 * 更新企业付款
	 * @param invoice
	 * @return
	 */
	public boolean update(Invoice invoice);
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param invoice
	 * @return
	 */
	public boolean del(Invoice invoice);
	
	/**
	 * 申请人提交申请
	 * @param invoice
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Invoice invoice, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param invoice
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Invoice invoice, int passType, String opinion);
	
	/**
	 * 验证对象的有效性
	 * @param invoice
	 */
	public void valid(Invoice invoice);
	
}
