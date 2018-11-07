package com.linuslan.oa.workflow.flows.area.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.area.model.Area;

public interface IAreaDao extends IBaseDao {

	/**
	 * 新增区域
	 * @param area
	 * @return
	 */
	public boolean add(Area area);
	
	/**
	 * 更新区域信息
	 * @param area
	 * @return
	 */
	public boolean update(Area area);
	
	/**
	 * 删除区域，伪删除，将isDelete状态改为1
	 * @param area
	 * @return
	 */
	public boolean del(Area area);
	
	/**
	 * 通过id查询区域
	 * @param id
	 * @return
	 */
	public Area queryById(Long id);
	
	/**
	 * 查询指定id的所有区域
	 * @param ids
	 * @return
	 */
	public List<Area> queryByIds(List<Long> ids);
	
	/**
	 * 查询区域列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Area> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的区域
	 * @param paramMap
	 * @return
	 */
	public List<Area> queryAll();
}
