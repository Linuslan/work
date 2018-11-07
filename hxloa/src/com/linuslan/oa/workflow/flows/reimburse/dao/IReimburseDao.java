package com.linuslan.oa.workflow.flows.reimburse.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.reimburse.model.Reimburse;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseContent;

public interface IReimburseDao extends IBaseDao {

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
	 * 通过id查询报销项目
	 * @param ids
	 * @return
	 */
	public List<ReimburseContent> queryContentsInIds(List<Long> ids);
	
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
	public boolean add(Reimburse reimburse);
	
	/**
	 * 批量更新报销项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<ReimburseContent> contents);
	
	/**
	 * 更新申请单
	 * @param reimburse
	 * @return
	 */
	public boolean update(Reimburse reimburse);
	
	/**
	 * 删除报销项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long reimburseId);
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
