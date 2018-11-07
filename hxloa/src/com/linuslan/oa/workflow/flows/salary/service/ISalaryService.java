package com.linuslan.oa.workflow.flows.salary.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.salary.model.Salary;
import com.linuslan.oa.workflow.flows.salary.model.SalaryContent;
import com.linuslan.oa.workflow.flows.salary.vo.DepartmentSalary;

public interface ISalaryService extends IBaseService {
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
	 * 新增申请单
	 * @param salary
	 * @return
	 */
	public boolean add(Salary salary, List<SalaryContent> contents);
	
	/**
	 * 批量生成工资表
	 * @param salarys
	 * @return
	 */
	public int addBatch(List<Salary> salarys);
	
	/**
	 * 更新申请单
	 * @param salary
	 * @return
	 */
	public boolean update(Salary salary, List<SalaryContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param salary
	 * @return
	 */
	public boolean del(Salary salary);
	
	/**
	 * 申请人提交申请
	 * @param salary
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Salary salary, List<SalaryContent> contents, int passType, String opinion, boolean isUpdate);
	
	/**
	 * 审核申请单
	 * @param salary
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Salary salary, List<SalaryContent> contents, int passType, String opinion);
	
	/**
	 * 删除工资项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param salary
	 */
	public void valid(Salary salary);
	
	/**
	 * 创建工资
	 * @param userSalarys
	 */
	public List<Salary> createSalarys(int year, int month, List<Map<String, Object>> userSalarys) throws Exception;
	
	/**
	 * 查询部门工资，用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<DepartmentSalary> queryDepartmentSalary(int year, int month);
	
	/**
	 * 查询年月的已生效工资，目前主要用于导出
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> queryFinishedByYearAndMonth(int year, int month);
	
	/**
	 * 生成导出的excel
	 * @param list
	 * @return
	 */
	public HSSFWorkbook createExcel(List<DepartmentSalary> list);
}
