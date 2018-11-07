package com.linuslan.oa.workflow.flows.socialInsurance.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsurance;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsuranceContent;

public interface ISocialInsuranceDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的社保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SocialInsurance queryById(Long id);
	
	/**
	 * 通过日期查询用户是否已经有存在的社保
	 * @param date
	 * @return
	 */
	public SocialInsurance queryByDate(String date, Long insuranceId);
	
	/**
	 * 通过主表的id查询项目
	 * @param id
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsBySocialInsuranceId(Long id);
	
	/**
	 * 通过id查询项目
	 * @param ids
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param socialInsurance
	 * @return
	 */
	public boolean add(SocialInsurance socialInsurance);
	
	/**
	 * 批量更新项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SocialInsuranceContent> contents);
	
	/**
	 * 更新申请单
	 * @param socialInsurance
	 * @return
	 */
	public boolean update(SocialInsurance socialInsurance);
	
	/**
	 * 删除项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long insuranceId);
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids);
	
	/**
	 * 删除项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
}
