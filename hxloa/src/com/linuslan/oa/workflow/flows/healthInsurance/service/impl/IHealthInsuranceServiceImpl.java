package com.linuslan.oa.workflow.flows.healthInsurance.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.OfficeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.healthInsurance.dao.IHealthInsuranceDao;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsurance;
import com.linuslan.oa.workflow.flows.healthInsurance.model.HealthInsuranceContent;
import com.linuslan.oa.workflow.flows.healthInsurance.service.IHealthInsuranceService;

@Component("healthInsuranceService")
@Transactional
public class IHealthInsuranceServiceImpl extends IBaseServiceImpl implements
		IHealthInsuranceService {

	private Logger logger = Logger.getLogger(IHealthInsuranceServiceImpl.class);
	
	@Autowired
	private IHealthInsuranceDao healthInsuranceDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	/**
	 * 查询登陆用户的申请单列表
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.healthInsuranceDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.healthInsuranceDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的医保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<HealthInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.healthInsuranceDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public HealthInsurance queryById(Long id) {
		return this.healthInsuranceDao.queryById(id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的医保
	 * @param date
	 * @return
	 */
	public HealthInsurance queryByDate(String date, Long insuranceId) {
		return this.healthInsuranceDao.queryByDate(date, insuranceId);
	}
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<HealthInsuranceContent> queryContentsByHealthInsuranceId(Long id) {
		return this.healthInsuranceDao.queryContentsByHealthInsuranceId(id);
	}
	
	/**
	 * 新增企业付款
	 * @param healthInsurance
	 * @return
	 */
	public boolean add(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents) {
		boolean success = false;
		if(null == healthInsurance) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		healthInsurance.setUserId(HttpUtil.getLoginUser().getId());
		healthInsurance.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
		//验证对象的有效性
		this.valid(healthInsurance);
		this.healthInsuranceDao.add(healthInsurance);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("insuranceId", healthInsurance.getId());
		//将报销主表的id赋值给报销项目
		BeanUtil.setValueBatch(contents, map);
		//检查报销项目的有效性
		this.validContentBatch(contents);
		this.healthInsuranceDao.mergeContents(contents);
		//启动流程，生成流程实例
		this.engineUtil.startFlow(healthInsurance, CodeUtil.getClassName(HealthInsurance.class));
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param healthInsurance
	 * @return
	 */
	public boolean update(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents) {
		boolean success = false;
		if(null == healthInsurance || null == healthInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		HealthInsurance persist = this.healthInsuranceDao.queryById(healthInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (HealthInsurance) BeanUtil.updateBean(persist, healthInsurance);
		this.valid(persist);
		this.healthInsuranceDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("insuranceId", healthInsurance.getId());
		//获取已保存且未被用户删除的报销项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		List<HealthInsuranceContent> persists = null;
		if(0 < contentIds.size()) {
			//先删除不在contentIds中的报销项目，不在contentIds中，即被用户在前端界面删除了
			this.healthInsuranceDao.delContentsNotInIds(contentIds, persist.getId());
			persists = this.healthInsuranceDao.queryContentsInIds(contentIds);
		}
		contents = (List<HealthInsuranceContent>) BeanUtil.updateBeans(persists, contents, "id", map);
		//检查报销项目的有效性
		this.validContentBatch(contents);
		this.healthInsuranceDao.mergeContents(contents);
		
		success = true;
		return success;
	}
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param healthInsurance
	 * @return
	 */
	public boolean del(HealthInsurance healthInsurance) {
		boolean success = false;
		if(null == healthInsurance || null == healthInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		HealthInsurance persist = this.healthInsuranceDao.queryById(healthInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.healthInsuranceDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param healthInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(HealthInsurance healthInsurance, List<HealthInsuranceContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == healthInsurance) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != healthInsurance.getId()) {
			HealthInsurance persist = this.healthInsuranceDao.queryById(healthInsurance.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (HealthInsurance) BeanUtil.updateBean(persist, healthInsurance);
			healthInsurance = persist;
			this.valid(persist);
			updateSuccess = this.healthInsuranceDao.update(persist);
		} else {
			healthInsurance.setUserId(HttpUtil.getLoginUser().getId());
			healthInsurance.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
			this.valid(healthInsurance);
			updateSuccess = this.healthInsuranceDao.add(healthInsurance);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(healthInsurance, CodeUtil.getClassName(HealthInsurance.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("insuranceId", healthInsurance.getId());
			//获取已保存且未被用户删除的报销项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			//先删除不在contentIds中的报销项目，不在contentIds中，即被用户在前端界面删除了
			List<HealthInsuranceContent> persists = null;
			if(0 < contentIds.size()) {
				this.healthInsuranceDao.delContentsNotInIds(contentIds, healthInsurance.getId());
				persists = this.healthInsuranceDao.queryContentsInIds(contentIds);
			}
			
			contents = (List<HealthInsuranceContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查报销项目的有效性
			this.validContentBatch(contents);
			this.healthInsuranceDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(healthInsurance, false);
			this.engineUtil.execute(healthInsurance, CodeUtil.getClassName(HealthInsurance.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param healthInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(HealthInsurance healthInsurance, int passType, String opinion) {
		boolean success = false;
		if(null == healthInsurance || null == healthInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		HealthInsurance persist = this.healthInsuranceDao.queryById(healthInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(HealthInsurance.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.healthInsuranceDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param healthInsurance
	 */
	public void valid(HealthInsurance healthInsurance) {
		
	}
	
	public void validContent(HealthInsuranceContent content, int i) {
		
	}
	
	public void validContentBatch(List<HealthInsuranceContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取报销项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条报销项目");
		}
		HealthInsuranceContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
	
	/**
	 * 导入医保项目
	 * @param file
	 * @return
	 */
	public List<HealthInsuranceContent> importInsuranceContent(File file) {
		List<HealthInsuranceContent> list = new ArrayList<HealthInsuranceContent> ();
		Workbook wb;
		try {
			if(null != file) {
				wb = WorkbookFactory.create(file);
				int index = wb.getActiveSheetIndex();
				Sheet sh = wb.getSheetAt(index);
				int rowIndex = 0;
				Iterator<Row> rowIter = sh.rowIterator();
				HealthInsuranceContent hic = null;
				while(rowIter.hasNext()) {
					Row row = rowIter.next();
					if(rowIndex > 3) {
						hic = new HealthInsuranceContent();
						int cellIndex=0;
						try {
							hic.setOrderNo(OfficeUtil.getIntegerCellValue(row, cellIndex ++));
							hic.setUserName(OfficeUtil.getStringCellValue(row, cellIndex ++));
							hic.setIdNo(OfficeUtil.getStringCellValue(row, cellIndex ++));
							hic.setCompany(OfficeUtil.getStringCellValue(row, cellIndex++));
							hic.setCompanyMedicalBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setCompanyMedicalRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							hic.setCompanyMedicalCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setCompanyMeternityBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setCompanyMeternityRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							hic.setCompanyMeternityCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setCompanyIllnessCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setCompanySum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setUserMedicalBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setUserMedicalRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							hic.setUserMedicalCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setUserMeternityBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setUserMeternityRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							hic.setUserMeternityCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setUserSum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setTotalSum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							hic.setRemark(OfficeUtil.getStringCellValue(row, cellIndex++));
							if(CodeUtil.isNotEmpty(hic.getUserName())
									&& CodeUtil.isNotEmpty(hic.getIdNo())) {
								list.add(hic);
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
					rowIndex ++;
				}
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			logger.error("导入医保异常", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("导入医保异常", e);
		}
		return list;
	}
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids) {
		return this.healthInsuranceDao.delContentsInIds(ids);
	}
}
