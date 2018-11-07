package com.linuslan.oa.workflow.flows.checkout.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.checkout.model.Checkout;
import com.linuslan.oa.workflow.flows.checkout.model.CheckoutContent;

public interface ICheckoutDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkout> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkout> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的出库
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkout> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Checkout queryById(Long id);
	
	/**
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<CheckoutContent> queryContentsByCheckoutId(Long id);
	
	/**
	 * 通过id查询出库项目
	 * @param ids
	 * @return
	 */
	public List<CheckoutContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param checkout
	 * @return
	 */
	public boolean add(Checkout checkout);
	
	/**
	 * 批量更新出库项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<CheckoutContent> contents);
	
	/**
	 * 更新申请单
	 * @param checkout
	 * @return
	 */
	public boolean update(Checkout checkout);
	
	/**
	 * 删除出库项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long checkoutId);
	
	/**
	 * 删除出库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
