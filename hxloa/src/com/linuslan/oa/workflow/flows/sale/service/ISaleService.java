package com.linuslan.oa.workflow.flows.sale.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.sale.model.Sale;
import com.linuslan.oa.workflow.flows.sale.model.SaleContent;

public interface ISaleService extends IBaseService {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Sale> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Sale> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的销售订单
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Sale> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Sale queryById(Long id);
	
	/**
	 * 通过销售主表的id查询销售项目
	 * @param id
	 * @return
	 */
	public List<SaleContent> queryContentsBySaleId(Long id);

	/**
	 * 新增申请单
	 * @param sale
	 * @return
	 */
	public boolean add(Sale sale, List<SaleContent> contents);
	
	/**
	 * 更新申请单
	 * @param sale
	 * @return
	 */
	public boolean update(Sale sale, List<SaleContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param sale
	 * @return
	 */
	public boolean del(Sale sale);
	
	/**
	 * 申请人提交申请
	 * @param sale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Sale sale, List<SaleContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param sale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Sale sale, List<SaleContent> contents, int passType, String opinion);
	
	/**
	 * 删除销售项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param sale
	 */
	public void valid(Sale sale);
	
}
