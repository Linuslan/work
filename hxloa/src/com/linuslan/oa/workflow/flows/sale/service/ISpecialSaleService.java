package com.linuslan.oa.workflow.flows.sale.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSaleContent;

public interface ISpecialSaleService extends IBaseService {

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
	 * 通过销售主表的id查询销售项目
	 * @param id
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsBySpecialSaleId(Long id);

	/**
	 * 新增申请单
	 * @param specialSale
	 * @return
	 */
	public boolean add(SpecialSale specialSale, List<SpecialSaleContent> contents);
	
	/**
	 * 更新申请单
	 * @param specialSale
	 * @return
	 */
	public boolean update(SpecialSale specialSale, List<SpecialSaleContent> contents);
	
	public boolean update(SpecialSale specialSale);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param specialSale
	 * @return
	 */
	public boolean del(SpecialSale specialSale);
	
	/**
	 * 申请人提交申请
	 * @param specialSale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(SpecialSale specialSale, List<SpecialSaleContent> contents, int passType, String opinion, boolean isUpdate);
	
	/**
	 * 审核申请单
	 * @param specialSale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(SpecialSale specialSale, int passType, String opinion);
	
	/**
	 * 删除销售项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param specialSale
	 */
	public void valid(SpecialSale specialSale);
	
	/**
	 * 通过id查询华夏蓝销售订单项目
	 * @param ids
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 批量更新华夏蓝销售订单项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SpecialSaleContent> contents);
	
}
