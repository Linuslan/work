package com.linuslan.oa.workflow.flows.healthInsurance.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsurance;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsuranceContent;

public interface IHealthInsuranceDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的医保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public HealthInsurance queryById(Long id);
	
	/**
	 * 通过日期查询用户是否已经有存在的医保
	 * @param date
	 * @return
	 */
	public HealthInsurance queryByDate(String date, Long insuranceId);
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<HealthInsuranceContent> queryContentsByHealthInsuranceId(Long id);
	
	/**
	 * 通过id查询报销项目
	 * @param ids
	 * @return
	 */
	public List<HealthInsuranceContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param healthInsurance
	 * @return
	 */
	public boolean add(HealthInsurance healthInsurance);
	
	/**
	 * 批量更新报销项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<HealthInsuranceContent> contents);
	
	/**
	 * 更新申请单
	 * @param healthInsurance
	 * @return
	 */
	public boolean update(HealthInsurance healthInsurance);
	
	/**
	 * 删除报销项目的id不在ids集合中的
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
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
