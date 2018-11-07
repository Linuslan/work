package com.linuslan.oa.workflow.flows.sale.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.sale.model.Sale;
import com.linuslan.oa.workflow.flows.sale.model.SaleContent;

public interface ISaleDao extends IBaseDao {

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
	 * 通过id查询销售项目
	 * @param ids
	 * @return
	 */
	public List<SaleContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param sale
	 * @return
	 */
	public boolean add(Sale sale);
	
	/**
	 * 批量更新销售项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SaleContent> contents);
	
	/**
	 * 更新申请单
	 * @param sale
	 * @return
	 */
	public boolean update(Sale sale);
	
	/**
	 * 删除销售项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long id);
	
	/**
	 * 删除销售项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
