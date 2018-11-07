package com.linuslan.oa.workflow.flows.salary.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.salary.model.Salary;
import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;

public interface ISalaryDao extends IBaseDao {
	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询已完成薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Salary queryById(Long id);
	
	/**
	 * 查询登陆用户的生效工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<SalaryContent> queryContentByUserId(int year, int month);
	
	/**
	 * 检查year-month是否有已经创建的工资
	 * @param year
	 * @param month
	 * @return
	 */
	public long checkExistSalary(int year, int month);
	
	/**
	 * 通过工资主表的id查询工资项目
	 * @param id
	 * @return
	 */
	public List<SalaryContent> queryContentsBySalaryId(Long id);
	
	/**
	 * 通过id查询工资项目
	 * @param ids
	 * @return
	 */
	public List<SalaryContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param salary
	 * @return
	 */
	public boolean add(Salary salary);
	
	/**
	 * 批量更新工资项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SalaryContent> contents);
	
	/**
	 * 更新申请单
	 * @param salary
	 * @return
	 */
	public boolean update(Salary salary);
	
	/**
	 * 删除工资项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long id);
	
	/**
	 * 删除工资项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 查询年月的已生效工资，目前主要用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> queryFinishedByYearAndMonth(int year, int month);
}
