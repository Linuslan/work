package com.linuslan.oa.workflow.flows.workOvertime.service;

import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.workOvertime.model.WorkOvertime;

public interface IWorkOvertimeService extends IBaseService {

	/**
	 * 查询登陆用户的请假申请
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
	 * 通过id查询请假
	 * @param id
	 * @return
	 */
	public WorkOvertime queryById(Long id);

	/**
	 * 新增请假
	 * @param workOvertime
	 * @return
	 */
	public boolean add(WorkOvertime workOvertime);
	
	/**
	 * 更新请假
	 * @param workOvertime
	 * @return
	 */
	public boolean update(WorkOvertime workOvertime);
	
	/**
	 * 删除请假，伪删除，将isDelete=0更新为isDelete=1
	 * @param workOvertime
	 * @return
	 */
	public boolean del(WorkOvertime workOvertime);
	
	/**
	 * 申请人提交申请
	 * @param workOvertime
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(WorkOvertime workOvertime, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param workOvertime
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(WorkOvertime workOvertime, int passType, String opinion);
	
	/**
	 * 计算请假的时长
	 * @param startDateStr 请假开始时间
	 * @param endDateStr 请假结束时间
	 * @return 请假时长
	 */
	public double countWorkOvertimeDuration(String startDateStr, String endDateStr);
	
	/**
	 * 验证对象的有效性
	 * @param workOvertime
	 */
	public void valid(WorkOvertime workOvertime);
	
}
