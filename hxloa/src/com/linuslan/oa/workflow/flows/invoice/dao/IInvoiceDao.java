package com.linuslan.oa.workflow.flows.invoice.dao;

import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.invoice.model.Invoice;

public interface IInvoiceDao extends IBaseDao {

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
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Invoice queryById(Long id);
	
	/**
	 * 新增申请单
	 * @param invoice
	 * @return
	 */
	public boolean add(Invoice invoice);
	
	/**
	 * 更新申请单
	 * @param invoice
	 * @return
	 */
	public boolean update(Invoice invoice);
	
}
