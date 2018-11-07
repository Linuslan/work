package com.linuslan.oa.workflow.flows.healthInsurance.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsurance;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsuranceContent;

public interface IHealthInsuranceService extends IBaseService {

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
	 * 新增申请单
	 * @param healthInsurance
	 * @return
	 */
	public boolean add(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents);
	
	/**
	 * 更新申请单
	 * @param healthInsurance
	 * @return
	 */
	public boolean update(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param healthInsurance
	 * @return
	 */
	public boolean del(HealthInsurance healthInsurance);
	
	/**
	 * 申请人提交申请
	 * @param healthInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param healthInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(HealthInsurance healthInsurance, int passType, String opinion);
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param healthInsurance
	 */
	public void valid(HealthInsurance healthInsurance);
	
	/**
	 * 导入医保项目
	 * @param file
	 * @return
	 */
	public List<HealthInsuranceContent> importInsuranceContent(File file);
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids);
	
}
