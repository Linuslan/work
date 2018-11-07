package com.linuslan.oa.workflow.flows.leave.service;

import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.leave.model.Leave;

public interface ILeaveService extends IBaseService {

	/**
	 * 查询登陆用户的请假申请
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
	 * 通过id查询请假
	 * @param id
	 * @return
	 */
	public Leave queryById(Long id);

	/**
	 * 新增请假
	 * @param leave
	 * @return
	 */
	public boolean add(Leave leave);
	
	/**
	 * 更新请假
	 * @param leave
	 * @return
	 */
	public boolean update(Leave leave);
	
	/**
	 * 删除请假，伪删除，将isDelete=0更新为isDelete=1
	 * @param leave
	 * @return
	 */
	public boolean del(Leave leave);
	
	/**
	 * 申请人提交申请
	 * @param leave
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Leave leave, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param leave
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Leave leave, int passType, String opinion);
	
	/**
	 * 计算请假的时长
	 * @param startDateStr 请假开始时间
	 * @param endDateStr 请假结束时间
	 * @return 请假时长
	 */
	public double countLeaveDuration(String startDateStr, String endDateStr);
	
	/**
	 * 验证对象的有效性
	 * @param leave
	 */
	public void valid(Leave leave);
	
}
