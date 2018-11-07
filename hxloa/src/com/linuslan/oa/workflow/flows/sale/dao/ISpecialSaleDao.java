package com.linuslan.oa.workflow.flows.sale.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSaleContent;

public interface ISpecialSaleDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的特殊销售
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SpecialSale queryById(Long id);
	
	/**
	 * 通过华夏蓝销售订单主表的id查询华夏蓝销售订单项目
	 * @param id
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsBySpecialSaleId(Long id);
	
	/**
	 * 通过id查询华夏蓝销售订单项目
	 * @param ids
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param specialSale
	 * @return
	 */
	public boolean add(SpecialSale specialSale);
	
	/**
	 * 批量更新华夏蓝销售订单项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SpecialSaleContent> contents);
	
	/**
	 * 更新申请单
	 * @param specialSale
	 * @return
	 */
	public boolean update(SpecialSale specialSale);
	
	/**
	 * 删除华夏蓝销售订单项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long id);
	
	/**
	 * 删除华夏蓝销售订单项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
