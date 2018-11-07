package com.linuslan.oa.workflow.flows.workOvertime.dao;

import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.workOvertime.model.WorkOvertime;

public interface IWorkOvertimeDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的加班
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询加班汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请
	 * @param id
	 * @return
	 */
	public WorkOvertime queryById(Long id);
	
	/**
	 * 新增申请
	 * @param workOvertime
	 * @return
	 */
	public boolean add(WorkOvertime workOvertime);
	
	/**
	 * 更新申请
	 * @param workOvertime
	 * @return
	 */
	public boolean update(WorkOvertime workOvertime);
}
