package com.linuslan.oa.workflow.flows.leave.dao;

import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.leave.model.Leave;

public interface ILeaveDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的请假
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 请假汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请
	 * @param id
	 * @return
	 */
	public Leave queryById(Long id);
	
	/**
	 * 新增申请
	 * @param leave
	 * @return
	 */
	public boolean add(Leave leave);
	
	/**
	 * 更新申请
	 * @param leave
	 * @return
	 */
	public boolean update(Leave leave);
	
}
