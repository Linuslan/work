package com.linuslan.oa.workflow.flows.purchaseReq.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReq;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReqContent;

public interface IPurchaseReqService extends IBaseService {

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
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<PurchaseReqContent> queryContentsByPurchaseReqId(Long id);

	/**
	 * 新增申请单
	 * @param purchaseReq
	 * @return
	 */
	public boolean add(PurchaseReq purchaseReq, List<PurchaseReqContent> contents);
	
	/**
	 * 更新申请单
	 * @param purchaseReq
	 * @return
	 */
	public boolean update(PurchaseReq purchaseReq, List<PurchaseReqContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param purchaseReq
	 * @return
	 */
	public boolean del(PurchaseReq purchaseReq);
	
	/**
	 * 申请人提交申请
	 * @param purchaseReq
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(PurchaseReq purchaseReq, List<PurchaseReqContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param purchaseReq
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(PurchaseReq purchaseReq, int passType, String opinion);
	
	/**
	 * 删除出库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param purchaseReq
	 */
	public void valid(PurchaseReq purchaseReq);
	
}
