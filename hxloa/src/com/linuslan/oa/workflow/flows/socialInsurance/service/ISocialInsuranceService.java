package com.linuslan.oa.workflow.flows.socialInsurance.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsurance;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsuranceContent;

public interface ISocialInsuranceService extends IBaseService {

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
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsBySocialInsuranceId(Long id);
	
	/**
	 * 新增申请单
	 * @param socialInsurance
	 * @return
	 */
	public boolean add(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents);
	
	/**
	 * 更新申请单
	 * @param socialInsurance
	 * @return
	 */
	public boolean update(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param socialInsurance
	 * @return
	 */
	public boolean del(SocialInsurance socialInsurance);
	
	/**
	 * 申请人提交申请
	 * @param socialInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param socialInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(SocialInsurance socialInsurance, int passType, String opinion);
	
	/**
	 * 删除项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param socialInsurance
	 */
	public void valid(SocialInsurance socialInsurance);
	
	/**
	 * 导入社保项目
	 * @param file
	 * @return
	 */
	public List<SocialInsuranceContent> importInsuranceContent(File file);
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids);
	
}
