package com.linuslan.oa.workflow.flows.reimburse.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.reimburse.model.Reimburse;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseContent;

public interface IReimburseService extends IBaseService {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的报销
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询报销汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Reimburse queryById(Long id);
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<ReimburseContent> queryContentsByReimburseId(Long id);
	
	public List<Reimburse> queryInIds(List<Long> ids);
	
	public List<ReimburseContent> queryContentsByReimburseIds(List<Long> ids);
	
	/**
	 * 通过部门id查询归属的报销类别
	 * @param departmentId
	 * @return
	 */
	public List<ReimburseClass> queryReimburseClassesByDepartmentId(Long departmentId);

	/**
	 * 新增申请单
	 * @param reimburse
	 * @return
	 */
	public boolean add(Reimburse reimburse, List<ReimburseContent> contents);
	
	/**
	 * 更新申请单
	 * @param reimburse
	 * @return
	 */
	public boolean update(Reimburse reimburse, List<ReimburseContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param reimburse
	 * @return
	 */
	public boolean del(Reimburse reimburse);
	
	/**
	 * 申请人提交申请
	 * @param reimburse
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Reimburse reimburse, List<ReimburseContent> contents, int passType, String opinion, boolean isUpdate);
	
	/**
	 * 审核申请单
	 * @param reimburse
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Reimburse reimburse, int passType, String opinion);
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param reimburse
	 */
	public void valid(Reimburse reimburse);
	
}
