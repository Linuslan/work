package com.linuslan.oa.workflow.flows.workOvertime.service.impl;

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
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.workOvertime.model.WorkOvertime;
import com.linuslan.oa.workflow.flows.workOvertime.dao.IWorkOvertimeDao;
import com.linuslan.oa.workflow.flows.workOvertime.service.IWorkOvertimeService;

@Component("workOvertimeService")
@Transactional
public class IWorkOvertimeServiceImpl extends IBaseServiceImpl implements
		IWorkOvertimeService {

	private static Logger logger = Logger.getLogger(IWorkOvertimeServiceImpl.class);
	
	@Autowired
	private IWorkOvertimeDao workOvertimeDao;
	
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
	public Page<WorkOvertime> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.workOvertimeDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.workOvertimeDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的加班
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.workOvertimeDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询加班汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<WorkOvertime> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.workOvertimeDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询请假
	 * @param id
	 * @return
	 */
	public WorkOvertime queryById(Long id) {
		return this.workOvertimeDao.queryById(id);
	}
	
	/**
	 * 新增请假
	 * @param workOvertime
	 * @return
	 */
	public boolean add(WorkOvertime workOvertime) {
		boolean success = false;
		if(null == workOvertime) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		workOvertime.setUserId(HttpUtil.getLoginUser().getId());
		//验证对象的有效性
		this.valid(workOvertime);
		if(this.workOvertimeDao.add(workOvertime)) {
			//启动流程，生成流程实例
			this.engineUtil.startFlow(workOvertime, CodeUtil.getClassName(WorkOvertime.class));
			success = true;
		}
		return success;
	}
	
	/**
	 * 更新请假
	 * @param workOvertime
	 * @return
	 */
	public boolean update(WorkOvertime workOvertime) {
		boolean success = false;
		if(null == workOvertime || null == workOvertime.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		WorkOvertime persist = this.workOvertimeDao.queryById(workOvertime.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (WorkOvertime) BeanUtil.updateBean(persist, workOvertime);
		this.valid(persist);
		if(this.workOvertimeDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除请假，伪删除，将isDelete=0更新为isDelete=1
	 * @param workOvertime
	 * @return
	 */
	public boolean del(WorkOvertime workOvertime) {
		boolean success = false;
		if(null == workOvertime || null == workOvertime.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		WorkOvertime persist = this.workOvertimeDao.queryById(workOvertime.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.workOvertimeDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param workOvertime
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(WorkOvertime workOvertime, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == workOvertime) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != workOvertime.getId()) {
			WorkOvertime persist = this.workOvertimeDao.queryById(workOvertime.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (WorkOvertime) BeanUtil.updateBean(persist, workOvertime);
			workOvertime = persist;
			this.valid(persist);
			updateSuccess = this.workOvertimeDao.update(persist);
		} else {
			workOvertime.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(workOvertime);
			updateSuccess = this.workOvertimeDao.add(workOvertime);
			this.engineUtil.startFlow(workOvertime, CodeUtil.getClassName(WorkOvertime.class));
		}
		if(updateSuccess) {
			//验证流程实例是否可以提交
			this.validFlowStatus(workOvertime, false);
			this.engineUtil.execute(workOvertime, CodeUtil.getClassName(WorkOvertime.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param workOvertime
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(WorkOvertime workOvertime, int passType, String opinion) {
		boolean success = false;
		if(null == workOvertime || null == workOvertime.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		WorkOvertime persist = this.workOvertimeDao.queryById(workOvertime.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(WorkOvertime.class), passType);
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
	public double countWorkOvertimeDuration(String startDateStr, String endDateStr) {
		try {
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDateStr);
			Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDateStr);
			long diff = endTime.getTime() - startTime.getTime();
			double hours = diff/1000/60/60;
			return hours;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep("获取加班时长失败，"+ex.getMessage());
			return 0;
		}
	}
	
	/**
	 * 验证对象的有效性
	 * @param workOvertime
	 */
	public void valid(WorkOvertime workOvertime) {
		if(null == workOvertime.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
}
