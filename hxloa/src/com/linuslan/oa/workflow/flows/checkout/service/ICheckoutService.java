package com.linuslan.oa.workflow.flows.checkout.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.checkout.model.Checkout;
import com.linuslan.oa.workflow.flows.checkout.model.CheckoutContent;

public interface ICheckoutService extends IBaseService {

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
	 * 新增申请单
	 * @param checkout
	 * @return
	 */
	public boolean add(Checkout checkout, List<CheckoutContent> contents);
	
	/**
	 * 更新申请单
	 * @param checkout
	 * @return
	 */
	public boolean update(Checkout checkout, List<CheckoutContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkout
	 * @return
	 */
	public boolean del(Checkout checkout);
	
	/**
	 * 申请人提交申请
	 * @param checkout
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Checkout checkout, List<CheckoutContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param checkout
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Checkout checkout, int passType, String opinion);
	
	/**
	 * 删除出库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param checkout
	 */
	public void valid(Checkout checkout);
	
}
