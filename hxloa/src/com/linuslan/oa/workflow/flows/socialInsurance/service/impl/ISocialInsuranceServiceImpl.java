package com.linuslan.oa.workflow.flows.socialInsurance.service.impl;

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
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsurance;
import com.linuslan.oa.workflow.flows.socialInsurance.model.SocialInsuranceContent;
import com.linuslan.oa.workflow.flows.socialInsurance.dao.ISocialInsuranceDao;
import com.linuslan.oa.workflow.flows.socialInsurance.service.ISocialInsuranceService;

@Component("socialInsuranceService")
@Transactional
public class ISocialInsuranceServiceImpl extends IBaseServiceImpl implements
		ISocialInsuranceService {
	private static Logger logger = Logger.getLogger(ISocialInsuranceServiceImpl.class);
	
	@Autowired
	private ISocialInsuranceDao socialInsuranceDao;
	
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
	public Page<SocialInsurance> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.socialInsuranceDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.socialInsuranceDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的社保
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SocialInsurance> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.socialInsuranceDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SocialInsurance queryById(Long id) {
		return this.socialInsuranceDao.queryById(id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的社保
	 * @param date
	 * @return
	 */
	public SocialInsurance queryByDate(String date, Long insuranceId) {
		return this.socialInsuranceDao.queryByDate(date, insuranceId);
	}
	
	/**
	 * 通过主表的id查询项目
	 * @param id
	 * @return
	 */
	public List<SocialInsuranceContent> queryContentsBySocialInsuranceId(Long id) {
		return this.socialInsuranceDao.queryContentsBySocialInsuranceId(id);
	}
	
	/**
	 * 新增
	 * @param socialInsurance
	 * @return
	 */
	public boolean add(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents) {
		boolean success = false;
		if(null == socialInsurance) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		socialInsurance.setUserId(HttpUtil.getLoginUser().getId());
		socialInsurance.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
		//验证对象的有效性
		this.valid(socialInsurance);
		this.socialInsuranceDao.add(socialInsurance);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("insuranceId", socialInsurance.getId());
		//将报销主表的id赋值给报销项目
		BeanUtil.setValueBatch(contents, map);
		//检查报销项目的有效性
		this.validContentBatch(contents);
		this.socialInsuranceDao.mergeContents(contents);
		//启动流程，生成流程实例
		this.engineUtil.startFlow(socialInsurance, CodeUtil.getClassName(SocialInsurance.class));
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param socialInsurance
	 * @return
	 */
	public boolean update(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents) {
		boolean success = false;
		if(null == socialInsurance || null == socialInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		SocialInsurance persist = this.socialInsuranceDao.queryById(socialInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (SocialInsurance) BeanUtil.updateBean(persist, socialInsurance);
		this.valid(persist);
		this.socialInsuranceDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("insuranceId", socialInsurance.getId());
		//获取已保存且未被用户删除的项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		List<SocialInsuranceContent> persists = null;
		if(0 < contentIds.size()) {
			//先删除不在contentIds中的项目，不在contentIds中，即被用户在前端界面删除了
			this.socialInsuranceDao.delContentsNotInIds(contentIds, persist.getId());
			persists = this.socialInsuranceDao.queryContentsInIds(contentIds);
		}
		contents = (List<SocialInsuranceContent>) BeanUtil.updateBeans(persists, contents, "id", map);
		//检查项目的有效性
		this.validContentBatch(contents);
		this.socialInsuranceDao.mergeContents(contents);
		
		success = true;
		return success;
	}
	
	/**
	 * 删除对象，伪删除，将isDelete=0更新为isDelete=1
	 * @param socialInsurance
	 * @return
	 */
	public boolean del(SocialInsurance socialInsurance) {
		boolean success = false;
		if(null == socialInsurance || null == socialInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		SocialInsurance persist = this.socialInsuranceDao.queryById(socialInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.socialInsuranceDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param socialInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(SocialInsurance socialInsurance, List<SocialInsuranceContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == socialInsurance) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != socialInsurance.getId()) {
			SocialInsurance persist = this.socialInsuranceDao.queryById(socialInsurance.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (SocialInsurance) BeanUtil.updateBean(persist, socialInsurance);
			socialInsurance = persist;
			this.valid(persist);
			updateSuccess = this.socialInsuranceDao.update(persist);
		} else {
			socialInsurance.setUserId(HttpUtil.getLoginUser().getId());
			socialInsurance.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
			this.valid(socialInsurance);
			updateSuccess = this.socialInsuranceDao.add(socialInsurance);
			this.engineUtil.startFlow(socialInsurance, CodeUtil.getClassName(SocialInsurance.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("insuranceId", socialInsurance.getId());
			//获取已保存且未被用户删除的项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<SocialInsuranceContent> persists = null;
			//先删除不在contentIds中的项目，不在contentIds中，即被用户在前端界面删除了
			if(0 < contentIds.size()) {
				this.socialInsuranceDao.delContentsNotInIds(contentIds, socialInsurance.getId());
				persists = this.socialInsuranceDao.queryContentsInIds(contentIds);
			}
			contents = (List<SocialInsuranceContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查项目的有效性
			this.validContentBatch(contents);
			this.socialInsuranceDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(socialInsurance, false);
			this.engineUtil.execute(socialInsurance, CodeUtil.getClassName(SocialInsurance.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param socialInsurance
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(SocialInsurance socialInsurance, int passType, String opinion) {
		boolean success = false;
		if(null == socialInsurance || null == socialInsurance.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		SocialInsurance persist = this.socialInsuranceDao.queryById(socialInsurance.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(SocialInsurance.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.socialInsuranceDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param socialInsurance
	 */
	public void valid(SocialInsurance socialInsurance) {
		
	}
	
	public void validContent(SocialInsuranceContent content, int i) {
		
	}
	
	public void validContentBatch(List<SocialInsuranceContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条项目");
		}
		SocialInsuranceContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
	
	/**
	 * 导入社保项目
	 * @param file
	 * @return
	 */
	public List<SocialInsuranceContent> importInsuranceContent(File file) {
		List<SocialInsuranceContent> list = new ArrayList<SocialInsuranceContent> ();
		Workbook wb;
		try {
			if(null != file) {
				wb = WorkbookFactory.create(file);
				int index = wb.getActiveSheetIndex();
				Sheet sh = wb.getSheetAt(index);
				int rowIndex = 0;
				Iterator<Row> rowIter = sh.rowIterator();
				SocialInsuranceContent sic = null;
				while(rowIter.hasNext()) {
					Row row = rowIter.next();
					if(rowIndex > 3) {
						sic = new SocialInsuranceContent();
						int cellIndex=0;
						try {
							sic.setOrderNo(OfficeUtil.getIntegerCellValue(row, cellIndex ++));
							sic.setUserName(OfficeUtil.getStringCellValue(row, cellIndex ++));
							sic.setIdNo(OfficeUtil.getStringCellValue(row, cellIndex ++));
							sic.setCompany(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setCompanyEndowmentBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanyEndowmentRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setCompanyEndowmentCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanyUnemploymentBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanyUnemploymentRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setCompanyUnemploymentCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanyEmploymentInjuryBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanyEmploymentInjuryRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setCompanyEmploymentInjuryCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setCompanySum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserEndowmentBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserEndowmentRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setUserEndowmentCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserUnemploymentBasicCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserUnemploymentRate(OfficeUtil.getStringCellValue(row, cellIndex++));
							sic.setUserUnemploymentCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserEmploymentInjuryCharge(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setUserSum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setTotalSum(OfficeUtil.getBigDecimalCellValue(row, cellIndex++));
							sic.setRemark(OfficeUtil.getStringCellValue(row, cellIndex++));
							if(CodeUtil.isNotEmpty(sic.getUserName())
									&& CodeUtil.isNotEmpty(sic.getIdNo())) {
								list.add(sic);
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
			logger.error("导入社保异常", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("导入社保异常", e);
		}
		return list;
	}
	
	/**
	 * 删除项目的id在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsInIds(List<Long> ids) {
		return this.socialInsuranceDao.delContentsInIds(ids);
	}
}
