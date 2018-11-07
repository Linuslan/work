package com.linuslan.oa.workflow.flows.purchaseReq.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReq;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReqContent;

public interface IPurchaseReqDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<PurchaseReq> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<PurchaseReq> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的采购申请
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<PurchaseReq> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public PurchaseReq queryById(Long id);
	
	/**
	 * 通过采购主表的id查询采购项目
	 * @param id
	 * @return
	 */
	public List<PurchaseReqContent> queryContentsByPurchaseReqId(Long id);
	
	/**
	 * 通过id查询采购项目
	 * @param ids
	 * @return
	 */
	public List<PurchaseReqContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param purchaseReq
	 * @return
	 */
	public boolean add(PurchaseReq purchaseReq);
	
	/**
	 * 批量更新采购项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<PurchaseReqContent> contents);
	
	/**
	 * 更新申请单
	 * @param purchaseReq
	 * @return
	 */
	public boolean update(PurchaseReq purchaseReq);
	
	/**
	 * 删除采购项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long purchaseReqId);
	
	/**
	 * 删除采购项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
