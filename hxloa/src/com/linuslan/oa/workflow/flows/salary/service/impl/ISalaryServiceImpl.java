package com.linuslan.oa.workflow.flows.salary.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.department.dao.IDepartmentDao;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.OAUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.salary.dao.ISalaryDao;
import com.linuslan.oa.workflow.flows.salary.model.Salary;
import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;
import com.linuslan.oa.workflow.flows.salary.service.ISalaryService;
import com.linuslan.oa.workflow.flows.salary.vo.DepartmentSalary;

@Component("salaryService")
@Transactional
public class ISalaryServiceImpl extends IBaseServiceImpl implements
		ISalaryService {
	private Logger logger = Logger.getLogger(ISalaryServiceImpl.class);
	
	@Autowired
	private ISalaryDao salaryDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	@Autowired
	private IDepartmentDao departmentDao;
	
	/**
	 * 查询登陆用户的申请单列表
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.salaryDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.salaryDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.salaryDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询已完成薪资
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Salary> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.salaryDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Salary queryById(Long id) {
		return this.salaryDao.queryById(id);
	}
	
	/**
	 * 查询登陆用户的生效工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<SalaryContent> queryContentByUserId(int year, int month) {
		return this.salaryDao.queryContentByUserId(year, month);
	}
	
	/**
	 * 检查year-month是否有已经创建的工资
	 * @param year
	 * @param month
	 * @return
	 */
	public long checkExistSalary(int year, int month) {
		return this.salaryDao.checkExistSalary(year, month);
	}
	
	/**
	 * 通过工资主表的id查询工资项目
	 * @param id
	 * @return
	 */
	public List<SalaryContent> queryContentsBySalaryId(Long id) {
		return this.salaryDao.queryContentsBySalaryId(id);
	}
	
	/**
	 * 新增工资
	 * @param salary
	 * @return
	 */
	public boolean add(Salary salary, List<SalaryContent> contents) {
		boolean success = false;
		try {
			if(null == salary) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			salary.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(salary);
			this.salaryDao.add(salary);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("salaryId", salary.getId());
			//将工资主表的id赋值给工资项目
			BeanUtil.setValueBatch(contents, map);
			//检查工资项目的有效性
			this.validContentBatch(contents);
			this.salaryDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(salary, CodeUtil.getClassName(Salary.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 批量生成工资表
	 * @param salarys
	 * @return
	 */
	public int addBatch(List<Salary> salarys) {
		int count = 0;
		try {
			Iterator<Salary> iter = salarys.iterator();
			Salary salary = null;
			while(iter.hasNext()) {
				salary = iter.next();
				this.add(salary, salary.getContents());
				count ++;
			}
		} catch(Exception ex) {
			count = 0;
			CodeUtil.throwRuntimeExcep(ex);
		}
		return count;
	}
	
	/**
	 * 更新工资
	 * @param salary
	 * @return
	 */
	public boolean update(Salary salary, List<SalaryContent> contents) {
		boolean success = false;
		try {
			if(null == salary || null == salary.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			Salary persist = this.salaryDao.queryById(salary.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (Salary) BeanUtil.updateBean(persist, salary);
			this.valid(persist);
			this.salaryDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("salaryId", salary.getId());
			//获取已保存且未被用户删除的工资项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<SalaryContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的工资项目，不在contentIds中，即被用户在前端界面删除了
				this.salaryDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.salaryDao.queryContentsInIds(contentIds);
			}
			contents = (List<SalaryContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查工资项目的有效性
			this.validContentBatch(contents);
			this.salaryDao.mergeContents(contents);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除工资，伪删除，将isDelete=0更新为isDelete=1
	 * @param salary
	 * @return
	 */
	public boolean del(Salary salary) {
		boolean success = false;
		if(null == salary || null == salary.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Salary persist = this.salaryDao.queryById(salary.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.salaryDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param salary
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Salary salary, List<SalaryContent> contents, int passType, String opinion, boolean isUpdate) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == salary) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != salary.getId()) {
			Salary persist = this.salaryDao.queryById(salary.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Salary) BeanUtil.updateBean(persist, salary);
			salary = persist;
			this.valid(persist);
			updateSuccess = this.salaryDao.update(persist);
		} else {
			this.valid(salary);
			updateSuccess = this.salaryDao.add(salary);
		}
		if(updateSuccess) {
			
			if(isUpdate) {
				Map<String, Long> map = new HashMap<String, Long> ();
				map.put("salaryId", salary.getId());
				//获取已保存且未被用户删除的工资项目
				String contentIdStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
				List<SalaryContent> persists = null;
				//先删除不在contentIds中的工资项目，不在contentIds中，即被用户在前端界面删除了
				if(0 < contentIds.size()) {
					this.salaryDao.delContentsNotInIds(contentIds, salary.getId());
					persists = this.salaryDao.queryContentsInIds(contentIds);
				}
				contents = (List<SalaryContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			} else {
				contents = this.salaryDao.queryContentsBySalaryId(salary.getId());
			}
			
			//检查工资项目的有效性
			this.validContentBatch(contents);
			this.salaryDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(salary, false);
			this.engineUtil.execute(salary, CodeUtil.getClassName(Salary.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param salary
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Salary salary, List<SalaryContent> contents, int passType, String opinion) {
		boolean success = false;
		if(null == salary || null == salary.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Salary persist = this.salaryDao.queryById(salary.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		BeanUtil.updateBean(persist, salary);
		this.salaryDao.update(persist);
		//获取已保存且未被用户删除的工资项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		//先删除不在contentIds中的工资项目，不在contentIds中，即被用户在前端界面删除了
		if(0 < contentIds.size()) {
			List<SalaryContent> persists = this.salaryDao.queryContentsInIds(contentIds);
			contents = (List<SalaryContent>) BeanUtil.updateBeans(persists, contents, "id", null);
			//检查工资项目的有效性
			this.validContentBatch(contents);
			this.salaryDao.mergeContents(contents);
		}
		this.engineUtil.execute(persist, CodeUtil.getClassName(Salary.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除工资项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.salaryDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param salary
	 */
	public void valid(Salary salary) {
		if(null == salary.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(SalaryContent content, int i) {
		
		if(null == content.getSalaryId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的工资单id位空");
		}
		
	}
	
	public void validContentBatch(List<SalaryContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取工资项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条工资项目");
		}
		SalaryContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
	
	/**
	 * 创建工资
	 * @param userSalarys
	 */
	public List<Salary> createSalarys(int year, int month, List<Map<String, Object>> userSalarys) throws Exception {
		List<Salary> salarys = new ArrayList<Salary> ();
		Map<Long, Salary> salaryMap = new HashMap<Long, Salary> ();
		Map<String, Object> userSalary = null;
		if(null != userSalarys) {
			Iterator<Map<String, Object>> iter = userSalarys.iterator();
			while(iter.hasNext()) {
				userSalary = iter.next();
				this.parseToSalary(year, month, userSalary, salaryMap);
			}
		}
		Set<Long> keySet = salaryMap.keySet();
		Iterator<Long> iter = keySet.iterator();
		Long departmentId = null;
		Salary salary = null;
		while(iter.hasNext()) {
			departmentId = iter.next();
			salary = salaryMap.get(departmentId);
			salarys.add(salary);
		}
		return salarys;
	}
	
	public void parseToSalary(int year, int month, Map<String, Object> userSalary, Map<Long, Salary> salaryMap) throws Exception {
		if(null == salaryMap) {
			CodeUtil.throwExcep("生成工资异常，salaryMap为空");
		}
		Salary salary = null;
		SalaryContent content = null;
		if(null != userSalary) {
			content = this.initSalaryContent(year, month, userSalary);
			if(null != content) {
				Long departmentId = CodeUtil.parseLong(userSalary.get("DEPARTMENT_ID"));
				Long companyId = CodeUtil.parseLong(userSalary.get("COMPANY_ID"));
				if(null != salaryMap.get(departmentId)) {
					salary = salaryMap.get(departmentId);
				} else {
					salary = new Salary();
					salary.setCompanyId(companyId);
					salary.setCreateDate(new Date());
					salary.setDepartmentId(departmentId);
					salary.setMonth(month);
					salary.setYear(year);
					salary.setUserId(HttpUtil.getLoginUser().getId());
					salaryMap.put(departmentId, salary);
				}
				if(null == salary.getContents()) {
					salary.setContents(new ArrayList<SalaryContent> ());
				}
				salary.getContents().add(content);
			}
		}
	}
	
	/**
	 * 主要用于创建工资时，初始化工资内容对象
	 * @param year
	 * @param month
	 * @param userSalary
	 * @return
	 */
	public SalaryContent initSalaryContent(int year, int month, Map<String, Object> userSalary) {
		SalaryContent content = null;
		Long companyId = CodeUtil.parseLong(userSalary.get("COMPANY_ID"));
		Long userId = CodeUtil.parseLong(userSalary.get("USER_ID"));
		String userName = CodeUtil.parseString(userSalary.get("USER_NAME"));
		Long departmentId = CodeUtil.parseLong(userSalary.get("DEPARTMENT_ID"));
		Long postId = CodeUtil.parseLong(userSalary.get("POST_ID"));
		Long typeId = CodeUtil.parseLong(userSalary.get("TYPE_ID"));
		Double achievementScore = CodeUtil.parseDouble(userSalary.get("LEADER_SCORE"));
		achievementScore = achievementScore == null ? 0d : achievementScore;
		Double addScore = CodeUtil.parseDouble(userSalary.get("ADD_SCORE"));
		addScore = addScore == null ? 0d : addScore;
		Double totalScore = 0d;
		try {
			totalScore = achievementScore + addScore;
		} catch(Exception ex) {
			totalScore = 0d;
		}
		BigDecimal addMoney = CodeUtil.parseBigDecimal(userSalary.get("ADD_MONEY"));
		BigDecimal housingSubsidy = CodeUtil.parseBigDecimal(userSalary.get("HOUSING_SUBSIDY"));
		BigDecimal basicSalary = CodeUtil.parseBigDecimal(userSalary.get("BASIC_SALARY"));
		BigDecimal achievementSalary = CodeUtil.parseBigDecimal(userSalary.get("ACHIEVEMENT_SALARY"));
		BigDecimal postSalary = CodeUtil.parseBigDecimal(userSalary.get("POST_SALARY"));
		BigDecimal telCharge = CodeUtil.parseBigDecimal(userSalary.get("TEL_CHARGE"));
		BigDecimal travelAllowance = CodeUtil.parseBigDecimal(userSalary.get("TRAVEL_ALLOWANCE"));
		BigDecimal mealSubsidy = CodeUtil.parseBigDecimal(userSalary.get("MEAL_SUBSIDY"));
		BigDecimal serviceAgeSalary = CodeUtil.parseBigDecimal(userSalary.get("SERVICE_AGE_SALARY"));
		String seniorityStartDateStr = "";
		try {
			Date seniorityStartDate = DateUtil.parseStrToDate(CodeUtil.parseString(userSalary.get("SERVICE_AGE_SALARY_START_DATE")), "yyyy-MM-dd");
			seniorityStartDateStr = DateUtil.parseDateToStr(seniorityStartDate, "yyyy-MM-dd");
		} catch(Exception ex) {
			ex.printStackTrace();
			seniorityStartDateStr = "";
		}
		
		String salaryDate = year + "-" + month;
		if(null != companyId && null != userId && null != departmentId) {
			content = new SalaryContent();
			content.setCreateDate(new Date());
			content.setDepartmentId(departmentId);
			content.setAchievementSalary(achievementSalary);
			content.setAchievementScore(totalScore);
			content.setBasicSalary(basicSalary);
			content.setPostSalary(postSalary);
			//得到初始化的实际绩效工资
			content.setActualAchievementSalary(
					CodeUtil.parseBigDecimal(
							OAUtil.getActualAchievementSalary(
									totalScore, achievementSalary.toString())));
			content.setCommission(addMoney);
			content.setHousingSubsidy(housingSubsidy);
			content.setMealSubsidy(mealSubsidy);
			content.setPostId(postId);
			//得到工龄工资
			try {
				content.setSeniorityPay(
						CodeUtil.parseBigDecimal(
								OAUtil.getSeniorityPay(
										seniorityStartDateStr, salaryDate)));
			} catch(Exception ex) {
				content.setSeniorityPay(CodeUtil.parseBigDecimal("0.00"));
			}
			
			content.setTelCharge(telCharge);
			content.setTravelAllowance(travelAllowance);
			content.setTypeId(typeId);
			content.setUserId(userId);
			content.setUserName(userName);
		}
		return content;
	}
	
	/**
	 * 查询部门工资，用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<DepartmentSalary> queryDepartmentSalary(int year, int month) {
		List<Map<String, Object>> list = this.salaryDao.queryFinishedByYearAndMonth(year, month);
		//Map<Long, List<Map<String, Object>>> departmentSalaryMap = new HashMap<Long, List<Map<String, Object>>> ();
		Map<Long, List<SalaryContent>> departmentSalaryMap = new HashMap<Long, List<SalaryContent>> ();
		List<DepartmentSalary> departmentSalaries = new ArrayList<DepartmentSalary> ();
		if(null != list) {
			Iterator<Map<String, Object>> iter = list.iterator();
			Map<String, Object> map = null;
			SalaryContent content = null;
			List<Long> departmentIds = new ArrayList<Long> ();
			while(iter.hasNext()) {
				map = iter.next();
				content = this.parseToSalaryContent(map);
				Long departmentId = CodeUtil.parseLong(map.get("DEPARTMENT_ID"));
				if(null == departmentSalaryMap.get(departmentId)) {
					//departmentSalaryMap.put(departmentId, new ArrayList<Map<String, Object>> ());
					departmentSalaryMap.put(departmentId, new ArrayList<SalaryContent> ());
				}
				departmentSalaryMap.get(departmentId).add(content);
				departmentIds.add(departmentId);
			}
			List<Department> departments = this.departmentDao.queryInIds(departmentIds);
			Iterator<Department> deptIter = departments.iterator();
			Department department = null;
			DepartmentSalary deptSalary = null;
			
			while(deptIter.hasNext()) {
				department = deptIter.next();
				deptSalary = new DepartmentSalary();
				deptSalary.setId(department.getId());
				deptSalary.setName(department.getName());
				//deptSalary.setMapList(departmentSalaryMap.get(department.getId()));
				deptSalary.setList(departmentSalaryMap.get(department.getId()));
				departmentSalaries.add(deptSalary);
			}
		}
		return departmentSalaries;
	}
	
	public SalaryContent parseToSalaryContent(Map<String, Object> map) {
		SalaryContent sc = null;
		if(null != map) {
			sc = new SalaryContent();
			try {
				sc.setAchievementSalary(CodeUtil.parseBigDecimal(map.get("ACHIEVEMENT_SALARY")));
				sc.setAchievementScore(CodeUtil.parsedouble(map.get("ACHIEVEMENT_SCORE")));
				sc.setCurrentAchievementScore(CodeUtil.parsedouble(map.get("CURRENT_ACHIEVEMENT_SCORE")));
				sc.setActualAchievementSalary(CodeUtil.parseBigDecimal(map.get("ACTUAL_ACHIEVEMENT_SALARY")));
				sc.setActualDutyDay(CodeUtil.parsedouble(map.get("ACTUAL_DUTY_DAY")));
				sc.setActualDutyHour(CodeUtil.parsedouble(map.get("ACTUAL_DUTY_HOUR")));
				sc.setAffairLeaveDeduct(CodeUtil.parseBigDecimal(map.get("AFFAIR_LEAVE_DEDUCT")));
				sc.setBasicSalary(CodeUtil.parseBigDecimal(map.get("BASIC_SALARY")));
				sc.setCommission(CodeUtil.parseBigDecimal(map.get("COMMISSION")));
				sc.setCompanyHealthInsurance(CodeUtil.parseBigDecimal(map.get("COMPANY_HEALTH_INSURANCE")));
				sc.setCompanySocialInsurance(CodeUtil.parseBigDecimal(map.get("COMPANY_SOCIAL_INSURANCE")));
				sc.setDepartmentId(CodeUtil.parseLong(map.get("DEPARTMENT_ID")));
				sc.setDepartmentName(CodeUtil.parseString(map.get("DEPARTMENT_NAME")));
				sc.setFullAttendanceAward(CodeUtil.parseBigDecimal(map.get("FULL_ATTENDANCE_AWARD")));
				sc.setHealthInsurance(CodeUtil.parseBigDecimal(map.get("HEALTH_INSURANCE")));
				sc.setHousingSubsidy(CodeUtil.parseBigDecimal(map.get("HOUSING_SUBSIDY")));
				sc.setId(CodeUtil.parseLong(map.get("ID")));
				sc.setInfo(CodeUtil.parseString(map.get("INFO")));
				sc.setIsDelete(CodeUtil.parseInteger(map.get("IS_DELETE")));
				sc.setLateDeduct(CodeUtil.parseBigDecimal(map.get("LATE_DEDUCT")));
				sc.setMealSubsidy(CodeUtil.parseBigDecimal(map.get("MEAL_SUBSIDY")));
				sc.setOrderNo(CodeUtil.parseInteger(map.get("ORDER_NO")));
				sc.setOther(CodeUtil.parseBigDecimal(map.get("OTHER")));
				sc.setOvertimePay(CodeUtil.parseBigDecimal(map.get("OVERTIME_PAY")));
				sc.setPostId(CodeUtil.parseLong(map.get("POST_ID")));
				sc.setPunishDeduct(CodeUtil.parseBigDecimal(map.get("PUNISH_DEDUCT")));
				sc.setSalaryId(CodeUtil.parseLong(map.get("SALARY_ID")));
				sc.setSeniorityPay(CodeUtil.parseBigDecimal(map.get("SENIORITY_PAY")));
				sc.setSickLeaveDeduct(CodeUtil.parseBigDecimal(map.get("SICK_LEAVE_DEDUCT")));
				sc.setSocialInsurance(CodeUtil.parseBigDecimal(map.get("SOCIAL_INSURANCE")));
				sc.setSupposedDutyDay(CodeUtil.parsedouble(map.get("SUPPOSED_DUTY_DAY")));
				sc.setTax(CodeUtil.parseBigDecimal(map.get("TAX")));
				sc.setTelCharge(CodeUtil.parseBigDecimal(map.get("TEL_CHARGE")));
				sc.setTravelAllowance(CodeUtil.parseBigDecimal(map.get("TRAVEL_ALLOWANCE")));
				sc.setUserId(CodeUtil.parseLong(map.get("USER_ID")));
				sc.setUserName(CodeUtil.parseString(map.get("USER_NAME")));
				sc.setPostSalary(CodeUtil.parseBigDecimal(map.get("POST_SALARY")));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return sc;
	}
	
	/**
	 * 查询年月的已生效工资，目前主要用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> queryFinishedByYearAndMonth(int year, int month) {
		return this.salaryDao.queryFinishedByYearAndMonth(year, month);
	}
	
	/**
	 * 生成导出的excel
	 * @param list
	 * @return
	 */
	public HSSFWorkbook createExcel(List<DepartmentSalary> list) {
		HSSFWorkbook wb = new HSSFWorkbook();
		if(null != list) {
			Iterator<DepartmentSalary> iter = list.iterator();
			DepartmentSalary ds = null;
			Sheet sheet = null;
			while(iter.hasNext()) {
				ds = iter.next();
				sheet = wb.createSheet(ds.getName());
				sheet.setDefaultRowHeightInPoints(30);
				sheet.setColumnWidth(0, 200*10);
				sheet.setColumnWidth(1, 300*15);
				
				CellStyle cs = wb.createCellStyle();
				Font font = wb.createFont();
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				cs.setFont(font);
				cs.setAlignment(CellStyle.ALIGN_CENTER);
				
				int rowIndex = 0;
				
				Row titleRow = sheet.createRow(rowIndex ++);
				titleRow.setHeightInPoints(30);
				Cell titleCell = titleRow.createCell(0);
				titleCell.setCellValue(ds.getName()+"员工薪资列表");
				titleCell.setCellStyle(cs);
				
				Row infoRow = sheet.createRow(rowIndex ++);
				
				Row infoRow2 = sheet.createRow(rowIndex ++);
				
				infoRow.setHeightInPoints(30);
				CellStyle cs2 = wb.createCellStyle();
				Font font2 = wb.createFont();
				font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
				cs2.setFont(font2);
				cs2.setAlignment(CellStyle.ALIGN_CENTER);
				int cellIndex = 0;
				
				//姓名列占两行
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				Cell cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("姓名");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("应出勤");
				cell.setCellStyle(cs2);
				
				//实出勤占一行两列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 1));
				cell = infoRow.createCell(cellIndex);
				cell.setCellValue("实出勤");
				cell.setCellStyle(cs2);
				
				Cell subCell1 = infoRow2.createCell(cellIndex ++);
				subCell1.setCellValue("天");
				subCell1.setCellStyle(cs2);
				Cell subCell2 = infoRow2.createCell(cellIndex ++);
				subCell2.setCellValue("时");
				subCell2.setCellStyle(cs2);
				
				//基本工资占两行
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("基本工资");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("岗位工资");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("绩效标准");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("绩效分数");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("实发绩效");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("抽成");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("满勤奖");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("加班费");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("工龄工资");
				cell.setCellStyle(cs2);
				
				//实出勤占一行三列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 2));
				cell = infoRow.createCell(cellIndex);
				cell.setCellValue("出勤扣款");
				cell.setCellStyle(cs2);
				
				subCell1 = infoRow2.createCell(cellIndex ++);
				subCell1.setCellValue("病假");
				subCell1.setCellStyle(cs2);
				subCell2 = infoRow2.createCell(cellIndex ++);
				subCell2.setCellValue("事假");
				subCell2.setCellStyle(cs2);
				Cell subCell3 = infoRow2.createCell(cellIndex ++);
				subCell3.setCellValue("迟到");
				subCell3.setCellStyle(cs2);
				
				//处罚金占两行一列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("处罚金");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("应发合计");
				cell.setCellStyle(cs2);
				
				//个人应扣款占一行三列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 2));
				cell = infoRow.createCell(cellIndex);
				cell.setCellValue("个人应扣款");
				cell.setCellStyle(cs2);
				
				subCell1 = infoRow2.createCell(cellIndex ++);
				subCell1.setCellValue("社保");
				subCell1.setCellStyle(cs2);
				subCell2 = infoRow2.createCell(cellIndex ++);
				subCell2.setCellValue("医保");
				subCell2.setCellStyle(cs2);
				subCell3 = infoRow2.createCell(cellIndex ++);
				subCell3.setCellValue("总计");
				subCell3.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("实发工资");
				cell.setCellStyle(cs2);
				
				//补贴占一行四列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 3));
				cell = infoRow.createCell(cellIndex);
				cell.setCellValue("补贴");
				cell.setCellStyle(cs2);
				
				subCell1 = infoRow2.createCell(cellIndex ++);
				subCell1.setCellValue("话补");
				subCell1.setCellStyle(cs2);
				subCell2 = infoRow2.createCell(cellIndex ++);
				subCell2.setCellValue("餐补");
				subCell2.setCellStyle(cs2);
				subCell3 = infoRow2.createCell(cellIndex ++);
				subCell3.setCellValue("车补");
				subCell3.setCellStyle(cs2);
				Cell subCell4 = infoRow2.createCell(cellIndex ++);
				subCell4.setCellValue("住房补贴");
				subCell4.setCellStyle(cs2);
				
				//其他占两行一列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("其他");
				cell.setCellStyle(cs2);
				
				//其他占两行一列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("税前工资");
				cell.setCellStyle(cs2);
				
				//其他占两行一列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("个税");
				cell.setCellStyle(cs2);
				
				//公司投保占一行两列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 1));
				cell = infoRow.createCell(cellIndex);
				cell.setCellValue("公司投保");
				cell.setCellStyle(cs2);
				
				subCell1 = infoRow2.createCell(cellIndex ++);
				subCell1.setCellValue("社保");
				subCell1.setCellStyle(cs2);
				subCell2 = infoRow2.createCell(cellIndex ++);
				subCell2.setCellValue("医保");
				subCell2.setCellStyle(cs2);
				
				//备注占两行一列
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
				cell = infoRow.createCell(cellIndex ++);
				cell.setCellValue("备注");
				cell.setCellStyle(cs2);
				
				//首行标题的合并
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellIndex - 1));
				
				//List<Map<String, Object>> salaries = ds.getMapList();
				List<SalaryContent> salaries = ds.getList();
				//Iterator<Map<String, Object>> iter2 = salaries.iterator();
				Iterator<SalaryContent> iter2 = salaries.iterator();
				//Map<String, Object> map = null;
				SalaryContent map = null;
				Row row = null;
				BigDecimal supposedTotal = new BigDecimal(0);
				BigDecimal actualTotal = new BigDecimal(0);
				BigDecimal telChargeTotal = new BigDecimal(0);
				BigDecimal mealSubsidyTotal = new BigDecimal(0);
				BigDecimal travelAllowanceTotal = new BigDecimal(0);
				BigDecimal housingSubsidyTotal = new BigDecimal(0);
				BigDecimal companySocialInsuranceTotal = new BigDecimal(0);
				BigDecimal companyHealthInsuranceTotal = new BigDecimal(0);
				BigDecimal benefitTotal = new BigDecimal(0);
				BigDecimal companyInsuranceTotal = new BigDecimal(0);
				BigDecimal totalSalary = new BigDecimal(0);
				while(iter2.hasNext()) {
					map = iter2.next();
					cellIndex = 0;
					row = sheet.createRow(rowIndex ++);
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("USER_NAME")));
					cell.setCellValue(map.getUserName());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("SUPPOSED_DUTY_DAY")));
					cell.setCellValue(map.getSupposedDutyDay());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_DUTY_DAY")));
					cell.setCellValue(map.getActualDutyDay());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_DUTY_HOUR")));
					cell.setCellValue(map.getActualDutyHour());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("BASIC_SALARY")));
					cell.setCellValue(CodeUtil.parseString(map.getBasicSalary()));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(CodeUtil.parseString(map.getPostSalary()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACHIEVEMENT_SALARY")));
					cell.setCellValue(CodeUtil.parseString(map.getAchievementSalary()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACHIEVEMENT_SCORE")));
					cell.setCellValue(map.getAchievementScore());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_ACHIEVEMENT_SALARY")));
					cell.setCellValue(CodeUtil.parseString(map.getActualAchievementSalary()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("COMMISSION")));
					cell.setCellValue(CodeUtil.parseString(map.getCommission()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("FULL_ATTENDANCE_AWARD")));
					cell.setCellValue(CodeUtil.parseString(map.getFullAttendanceAward()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("OVERTIME_PAY")));
					cell.setCellValue(CodeUtil.parseString(map.getOvertimePay()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("SENIORITY_PAY")));
					cell.setCellValue(CodeUtil.parseString(map.getSeniorityPay()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("SICK_LEAVE_DEDUCT")));
					cell.setCellValue(CodeUtil.parseString(map.getSickLeaveDeduct()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("AFFAIR_LEAVE_DEDUCT")));
					cell.setCellValue(CodeUtil.parseString(map.getAffairLeaveDeduct()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("LATE_DEDUCT")));
					cell.setCellValue(CodeUtil.parseString(map.getLateDeduct()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("PUNISH_DEDUCT")));
					cell.setCellValue(CodeUtil.parseString(map.getPunishDeduct()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("SUPPOSED_TOTAL_SALARY")));
					cell.setCellValue(CodeUtil.parseString(map.getSupposedTotalSalary()));
					
					//supposedTotal = supposedTotal.add(CodeUtil.parseBigDecimal(map.get("SUPPOSED_TOTAL_SALARY")));
					supposedTotal = supposedTotal.add(map.getSupposedTotalSalary());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("SOCIAL_INSURANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getSocialInsurance()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("HEALTH_INSURANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getHealthInsurance()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("TOTAL_INSURANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getTotalInsurance()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_TOTAL_SALARY")));
					cell.setCellValue(CodeUtil.parseString(map.getActualTotalSalary()));
					
					//actualTotal = actualTotal.add(CodeUtil.parseBigDecimal(map.get("ACTUAL_TOTAL_SALARY")));
					actualTotal = actualTotal.add(map.getActualTotalSalary());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("TEL_CHARGE")));
					cell.setCellValue(CodeUtil.parseString(map.getTelCharge()));
					
					//telChargeTotal = telChargeTotal.add(CodeUtil.parseBigDecimal(map.get("TEL_CHARGE")));
					telChargeTotal = telChargeTotal.add(map.getTelCharge());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("MEAL_SUBSIDY")));
					cell.setCellValue(CodeUtil.parseString(map.getMealSubsidy()));
					
					//mealSubsidyTotal = mealSubsidyTotal.add(CodeUtil.parseBigDecimal(map.get("MEAL_SUBSIDY")));
					mealSubsidyTotal = mealSubsidyTotal.add(map.getMealSubsidy());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("TRAVEL_ALLOWANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getTravelAllowance()));
					
					//travelAllowanceTotal = travelAllowanceTotal.add(CodeUtil.parseBigDecimal(map.get("TRAVEL_ALLOWANCE")));
					travelAllowanceTotal = travelAllowanceTotal.add(map.getTravelAllowance());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("HOUSING_SUBSIDY")));
					cell.setCellValue(CodeUtil.parseString(map.getHousingSubsidy()));
					
					//housingSubsidyTotal = housingSubsidyTotal.add(CodeUtil.parseBigDecimal(map.get("HOUSING_SUBSIDY")));
					housingSubsidyTotal = housingSubsidyTotal.add(map.getHousingSubsidy());

					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
					cell.setCellValue(CodeUtil.parseString(map.getOther()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
					cell.setCellValue(CodeUtil.parseString(map.getPretaxSalary()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
					cell.setCellValue(CodeUtil.parseString(map.getTax()));
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("COMPANY_SOCIAL_INSURANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getCompanySocialInsurance()));
					
					//companySocialInsuranceTotal = companySocialInsuranceTotal.add(CodeUtil.parseBigDecimal(map.get("COMPANY_SOCIAL_INSURANCE")));
					companySocialInsuranceTotal = companySocialInsuranceTotal.add(map.getCompanySocialInsurance());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("COMPANY_HEALTH_INSURANCE")));
					cell.setCellValue(CodeUtil.parseString(map.getCompanyHealthInsurance()));
					
					//companyHealthInsuranceTotal = companyHealthInsuranceTotal.add(CodeUtil.parseBigDecimal(map.get("COMPANY_HEALTH_INSURANCE")));
					companyHealthInsuranceTotal = companyHealthInsuranceTotal.add(map.getCompanyHealthInsurance());
					
					cell = row.createCell(cellIndex ++);
					//cell.setCellValue(CodeUtil.parseString(map.get("INFO")));
					cell.setCellValue(map.getInfo());
				}
				benefitTotal = benefitTotal.add(telChargeTotal).add(mealSubsidyTotal).add(travelAllowanceTotal).add(housingSubsidyTotal);
				companyInsuranceTotal = companyInsuranceTotal.add(companySocialInsuranceTotal).add(companyHealthInsuranceTotal);
				totalSalary = totalSalary.add(supposedTotal).add(benefitTotal).add(companyInsuranceTotal);
				
				Row sumRow = sheet.createRow(rowIndex ++);
				
				cell = sumRow.createCell(16);
				cell.setCellValue("合计");
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(17);
				cell.setCellValue(CodeUtil.parseString(supposedTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(21);
				cell.setCellValue(CodeUtil.parseString(actualTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(22);
				cell.setCellValue(CodeUtil.parseString(telChargeTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(23);
				cell.setCellValue(CodeUtil.parseString(mealSubsidyTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(24);
				cell.setCellValue(CodeUtil.parseString(travelAllowanceTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(25);
				cell.setCellValue(CodeUtil.parseString(housingSubsidyTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(29);
				cell.setCellValue(CodeUtil.parseString(companySocialInsuranceTotal));
				cell.setCellStyle(cs2);
				
				cell = sumRow.createCell(30);
				cell.setCellValue(CodeUtil.parseString(companyHealthInsuranceTotal));
				cell.setCellStyle(cs2);
				
				Row benefitSumRow = sheet.createRow(rowIndex ++);
				cell = benefitSumRow.createCell(16);
				cell.setCellValue("福利合计");
				cell.setCellStyle(cs2);
				
				cell = benefitSumRow.createCell(17);
				cell.setCellValue(CodeUtil.parseString(supposedTotal));
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 22, 25));
				cell = benefitSumRow.createCell(22);
				cell.setCellValue(CodeUtil.parseString(benefitTotal));
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 29, 30));
				cell = benefitSumRow.createCell(29);
				cell.setCellValue(CodeUtil.parseString(companyInsuranceTotal));
				cell.setCellStyle(cs2);
				
				Row totalSalaryRow = sheet.createRow(rowIndex ++);
				
				cell = totalSalaryRow.createCell(16);
				cell.setCellValue("工资总计");
				cell.setCellStyle(cs2);
				
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 17, 30));
				cell = totalSalaryRow.createCell(17);
				cell.setCellValue(CodeUtil.parseString(totalSalary));
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}
}
