package com.linuslan.oa.workflow.flows.checkin.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.checkin.model.Checkin;
import com.linuslan.oa.workflow.flows.checkin.model.CheckinContent;

public interface ICheckinDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的入库
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Checkin queryById(Long id);
	
	/**
	 * 通过入库主表的id查询入库项目
	 * @param id
	 * @return
	 */
	public List<CheckinContent> queryContentsByCheckinId(Long id);
	
	/**
	 * 通过id查询入库项目
	 * @param ids
	 * @return
	 */
	public List<CheckinContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 新增申请单
	 * @param checkin
	 * @return
	 */
	public boolean add(Checkin checkin);
	
	/**
	 * 批量更新入库项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<CheckinContent> contents);
	
	/**
	 * 更新申请单
	 * @param checkin
	 * @return
	 */
	public boolean update(Checkin checkin);
	
	/**
	 * 删除入库项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long checkinId);
	
	/**
	 * 删除入库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
}
