package com.linuslan.oa.workflow.flows.leave.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.dictionary.dao.IDictionaryDao;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.NumberUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.leave.dao.ILeaveDao;
import com.linuslan.oa.workflow.flows.leave.model.Leave;
import com.linuslan.oa.workflow.flows.leave.service.ILeaveService;

@Component("leaveService")
@Transactional
public class ILeaveServiceImpl extends IBaseServiceImpl implements
		ILeaveService {

	private static Logger logger = Logger.getLogger(ILeaveServiceImpl.class);
	
	@Autowired
	private ILeaveDao leaveDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	@Autowired
	private IDictionaryDao dictionaryDao;
	
	/**
	 * 查询登陆用户的请假申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.leaveDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.leaveDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的请假
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.leaveDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 请假汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Leave> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.leaveDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询请假
	 * @param id
	 * @return
	 */
	public Leave queryById(Long id) {
		return this.leaveDao.queryById(id);
	}
	
	/**
	 * 新增请假
	 * @param leave
	 * @return
	 */
	public boolean add(Leave leave) {
		boolean success = false;
		if(null == leave) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		leave.setUserId(HttpUtil.getLoginUser().getId());
		leave.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
		//验证对象的有效性
		this.valid(leave);
		if(this.leaveDao.add(leave)) {
			//启动流程，生成流程实例
			this.engineUtil.startFlow(leave, CodeUtil.getClassName(Leave.class));
			success = true;
		}
		return success;
	}
	
	/**
	 * 更新请假
	 * @param leave
	 * @return
	 */
	public boolean update(Leave leave) {
		boolean success = false;
		if(null == leave || null == leave.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		Leave persist = this.leaveDao.queryById(leave.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (Leave) BeanUtil.updateBean(persist, leave);
		this.valid(persist);
		if(this.leaveDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除请假，伪删除，将isDelete=0更新为isDelete=1
	 * @param leave
	 * @return
	 */
	public boolean del(Leave leave) {
		boolean success = false;
		if(null == leave || null == leave.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Leave persist = this.leaveDao.queryById(leave.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.leaveDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param leave
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Leave leave, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == leave) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != leave.getId()) {
			Leave persist = this.leaveDao.queryById(leave.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Leave) BeanUtil.updateBean(persist, leave);
			leave = persist;
			this.valid(persist);
			updateSuccess = this.leaveDao.update(persist);
		} else {
			this.valid(leave);
			updateSuccess = this.leaveDao.add(leave);
			this.engineUtil.startFlow(leave, CodeUtil.getClassName(Leave.class));
		}
		if(updateSuccess) {
			//验证流程实例是否可以提交
			this.validFlowStatus(leave, false);
			this.engineUtil.execute(leave, CodeUtil.getClassName(Leave.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param leave
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Leave leave, int passType, String opinion) {
		boolean success = false;
		if(null == leave || null == leave.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Leave persist = this.leaveDao.queryById(leave.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Leave.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 计算请假的时长
	 * @param startDateStr 请假开始时间
	 * @param endDateStr 请假结束时间
	 * @return 请假时长
	 */
	public double countLeaveDuration(String startDateStr, String endDateStr) {
		try {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDateStr);
			Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDateStr);
			long diff = endDate.getTime() - startDate.getTime();
			double hours = diff/1000/60/60;
			double days = hours/24;
			
			double totalDays = 0;
			/*
			 * 剔除第一天和最后一天之后，中间请假的天数
			 * 头尾两天在下面单独计算
			 * 最后请假时长总和为中间请假时长+头尾两天的请假时长
			 */
			if(days >= 1) {
				totalDays = days - 1;
			}
			double totalHours = totalDays*24;
			double totalMsec = 0;
			/*
			 * 从数据字典中获取每天工作的上午开始时间，上午结束时间，下午开始时间和下午结束时间
			 */
			Dictionary morningStartTimeDict = this.dictionaryDao.queryById(13L);
			Dictionary morningEndTimeDict = this.dictionaryDao.queryById(14L);
			Dictionary afternoonStartTimeDict = this.dictionaryDao.queryById(15L);
			Dictionary afternoonEndTimeDict = this.dictionaryDao.queryById(16L);
			String morningStartTime = morningStartTimeDict.getValue();
			if(CodeUtil.isEmpty(morningStartTime)) {
				CodeUtil.throwExcep("获取上午工作开始时间失败");
			}
			String morningEndTime = morningEndTimeDict.getValue();
			if(CodeUtil.isEmpty(morningEndTime)) {
				CodeUtil.throwExcep("获取上午工作结束时间失败");
			}
			String afternoonStartTime = afternoonStartTimeDict.getValue();
			if(CodeUtil.isEmpty(afternoonStartTime)) {
				CodeUtil.throwExcep("获取下午工作开始时间失败");
			}
			String afternoonEndTime = afternoonEndTimeDict.getValue();
			if(CodeUtil.isEmpty(afternoonEndTime)) {
				CodeUtil.throwExcep("获取下午工作结束时间失败");
			}
			//开始时间转换成yyyy-MM-dd模式
			String startDay = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			//结束时间转换成yyyy-MM-dd模式
			String endDay = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
			
			//开始时间上午的开始时间
			String startDayMorningStartStr = startDay + " "+morningStartTime;
			Date startDayMorningStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDayMorningStartStr);
			
			//开始时间上午的结束时间
			String startDayMorningEndStr = startDay + " "+morningEndTime;
			Date startDayMorningEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDayMorningEndStr);
			
			//开始时间下午的开始时间
			String startDayAfternoonStartStr = startDay + " "+afternoonStartTime;
			Date startDayAfternoonStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDayAfternoonStartStr);
			
			//开始时间下午的结束时间
			String startDayAfternoonEndStr = startDay + " "+afternoonEndTime;
			Date startDayAfternoonEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDayAfternoonEndStr);
			
			//计算一天工作时间总和用于下面转换天数或者时长
			long perDayMsec = (startDayMorningEnd.getTime() - startDayMorningStart.getTime())
					+(startDayAfternoonEnd.getTime() - startDayAfternoonStart.getTime());
			
			//结束时间上午的开始时间
			String endDayMorningStartStr = endDay + " "+morningStartTime;
			Date endDayMorningStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDayMorningStartStr);
			
			//结束时间上午的结束时间
			String endDayMorningEndStr = endDay + " "+morningEndTime;
			Date endDayMorningEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDayMorningEndStr);
			
			//结束时间下午的开始时间
			String endDayAfternoonStartStr = endDay + " "+afternoonStartTime;
			Date endDayAfternoonStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDayAfternoonStartStr);
			
			//结束时间下午的结束时间
			String endDayAfternoonEndStr = endDay + " "+afternoonEndTime;
			Date endDayAfternoonEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDayAfternoonEndStr);
			
			/*
			 * 计算头尾两天
			 */
			//请假开始时间需在请假结束时间之前才可以计算
			if(startTime.before(endTime)) {
				
				/*
				 * 计算头天的请假时长
				 */
				//如果请假开始时间在每天上午上班开始时间和上午上班结束时间之间，例如8：30至12：00
				if((startTime.equals(startDayMorningStart) || startTime.after(startDayMorningStart))
						&& (startTime.equals(startDayMorningEnd) || startTime.before(startDayMorningEnd))) {
					
					//如果结束时间和请假开始时间是在同一天，且结束时间在下午开始工作时间和结束时间之间
					//例如14:00至17：30，则请假时长为（上午工作结束时间-开始时间）+（请假结束时间-下午工作开始时间）
					if((endTime.equals(startDayAfternoonStart) || endTime.after(startDayAfternoonStart))
							&& (endTime.equals(startDayAfternoonEnd) || endTime.before(startDayAfternoonEnd))) {
						totalMsec += startDayMorningEnd.getTime() - startTime.getTime();
						totalMsec += endTime.getTime() - startDayAfternoonStart.getTime();
					} else if(endTime.after(startDayAfternoonEnd)) {
						
						//如果结束时间在下午工作结束时间之后，不管和请假开始时间在不在同一天
						//则请假时长为（上午工作结束时间-请假开始时间）+下午半天时间
						totalMsec += startDayMorningEnd.getTime() - startTime.getTime();
						totalMsec += startDayAfternoonEnd.getTime() - startDayAfternoonStart.getTime();
					} else if((endTime.equals(startDayMorningStart) || endTime.after(startDayMorningStart))
							&& (endTime.equals(startDayMorningEnd) || endTime.before(startDayMorningEnd))) {
						
						//如果请假的开始时间和结束时间都在同一天上午，则时长为开始时间-结束时间
						totalMsec += endTime.getTime() - startTime.getTime();
					} else {
						
						//排除以上几种情况，则请假结束时间只可能在中午午休时分
						//则时长为上午工作结束时间-开始时间
						totalMsec += startDayMorningEnd.getTime() - startTime.getTime();
					}
					//如果开始时间在下午工作开始时间和下午工作结束时间之间
				} else if((startTime.equals(startDayAfternoonStart) || startTime.after(startDayAfternoonStart))
						&& (startTime.equals(startDayAfternoonEnd) || startTime.before(startDayAfternoonEnd))) {
					//如果结束时间在下午工作结束时间之后，则请假时长为（下午工作结束时间-请假开始时间）
					if(endTime.after(startDayAfternoonEnd)) {
						totalMsec += startDayAfternoonEnd.getTime() - startTime.getTime();
					} else {
						//否则结束时间只能是和请假开始时间在同一天的下午，则时长为（请假结束时间-请假开始时间）
						totalMsec += endTime.getTime() - startTime.getTime();
					}
					//如果开始时间在中午休息十分
				} else if(startTime.after(startDayMorningEnd) && startTime.before(startDayAfternoonStart)) {
					//如果结束时间和开始时间在同一天，且在下午工作开始时间之后且在下午工作结束时间之前，即下午工作时间
					//则时长为（请假结束时间-下午工作开始时间）
					if(endTime.after(startDayAfternoonStart)
							&& (endTime.before(startDayAfternoonEnd) || endTime.equals(startDayAfternoonEnd))) {
						totalMsec += endTime.getTime() - startDayAfternoonStart.getTime();
					} else if(endTime.after(startDayAfternoonEnd)) {
						//否则结束时间在下午工作结束时间之后的，则时长为整个下午工作时间，即半天
						totalMsec += startDayAfternoonEnd.getTime() - startDayAfternoonStart.getTime();
					}
					//如果开始时间是在当天上午开始工作时间之前
				} else if(startTime.before(startDayMorningStart)) {
					//如果结束时间在下午工作时间之间的，则请假时间为上午半天+（结束时间-下午工作开始时间）
					if((endTime.equals(startDayAfternoonStart) || endTime.after(startDayAfternoonStart))
							&& (endTime.equals(startDayAfternoonEnd) || endTime.before(startDayAfternoonEnd))) {
						totalMsec += startDayMorningEnd.getTime() - startTime.getTime();
						totalMsec += endTime.getTime() - startDayAfternoonStart.getTime();
					} else if(endTime.after(startDayAfternoonEnd)) {
						//如果结束时间在下午工作结束时间之后的，则请假时长则为一整天
						totalMsec += startDayMorningEnd.getTime() - startDayMorningStart.getTime();
						totalMsec += startDayAfternoonEnd.getTime() - startDayAfternoonStart.getTime();
					} else if((endTime.equals(startDayMorningStart) || endTime.after(startDayMorningStart))
							&& (endTime.equals(startDayMorningEnd) || endTime.before(startDayMorningEnd))) {
						//如果结束时间在请假当天的上午，则时长为结束时间-上午工作开始时间
						totalMsec += endTime.getTime() - startDayMorningStart.getTime();
					} else if(endTime.after(startDayMorningEnd)
							&& endTime.before(startDayAfternoonStart)) {
						//如果结束时间为中午休息时间，则请假时间为上午半天
						totalMsec += startDayMorningEnd.getTime() - startDayMorningStart.getTime();
					}
				}
				
				/*
				 * 计算尾天的请假时长
				 */
				//此处计算请假开始时间和结束时间不在同一天的情况，同一的情况在上面已经考虑完成了
				if(startDate.before(endDate)) {
					//如果结束时间在上午工作时间，则时长为结束时间-上午工作开始时间
					if((endTime.equals(endDayMorningStart) || endTime.after(endDayMorningStart))
							&& (endTime.equals(endDayMorningEnd) || endTime.before(endDayMorningEnd))) {
						totalMsec += endTime.getTime() - endDayMorningStart.getTime();
					} else if((endTime.equals(endDayAfternoonStart) || endTime.after(endDayAfternoonStart))
							&& (endTime.equals(endDayAfternoonEnd) || endTime.before(endDayAfternoonEnd))) {
						//如果结束时间在下午工作时间，则时长为（结束时间-下午工作开始时间）+上午半天的时间
						totalMsec += endTime.getTime() - endDayAfternoonStart.getTime();
						totalMsec += endDayMorningEnd.getTime() - endDayMorningStart.getTime();
					} else if(endTime.after(endDayAfternoonEnd)) {
						//如果结束时间在下午工作时间之后，则时长为一整天
						totalMsec += endDayAfternoonEnd.getTime() - endDayAfternoonStart.getTime();
						totalMsec += endDayMorningEnd.getTime() - endDayMorningStart.getTime();
					} else if(endTime.after(endDayMorningEnd) && endTime.before(endDayAfternoonStart)) {
						//如果结束时间在中午休息时分，则时长为上午半天
						totalMsec += endDayMorningEnd.getTime() - endDayMorningStart.getTime();
					}
				}
			}
			//中间请假的天数计算时长并不是乘以24小时，而是乘以一天工作的时长
			totalHours = totalDays*(perDayMsec/1000/60/60) + totalMsec/1000/60/60;
			totalDays = totalHours/(perDayMsec/1000/60/60);
			return totalHours;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep("获取请假时长失败，"+ex.getMessage());
			return 0;
		}
	}
	
	/**
	 * 验证对象的有效性
	 * @param leave
	 */
	public void valid(Leave leave) {
		if(null == leave.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
}
