package com.linuslan.oa.workflow.flows.purchase.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.purchase.model.Purchase;
import com.linuslan.oa.workflow.flows.purchase.model.PurchaseContent;

public interface IPurchaseService extends IBaseService {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的采购
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Purchase queryById(Long id);
	
	/**
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<PurchaseContent> queryContentsByPurchaseId(Long id);

	/**
	 * 新增申请单
	 * @param purchase
	 * @return
	 */
	public boolean add(Purchase purchase, List<PurchaseContent> contents);
	
	/**
	 * 更新申请单
	 * @param purchase
	 * @return
	 */
	public boolean update(Purchase purchase, List<PurchaseContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param purchase
	 * @return
	 */
	public boolean del(Purchase purchase);
	
	/**
	 * 申请人提交申请
	 * @param purchase
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Purchase purchase, List<PurchaseContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param purchase
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Purchase purchase, int passType, String opinion);
	
	/**
	 * 删除出库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param purchase
	 */
	public void valid(Purchase purchase);
	
}
