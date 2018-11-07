package com.linuslan.oa.workflow.flows.unit.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.unit.model.Unit;

public interface IUnitService extends IBaseService {

	/**
	 * 新增单位
	 * @param unit
	 * @return
	 */
	public boolean add(Unit unit);
	
	/**
	 * 更新单位信息
	 * @param unit
	 * @return
	 */
	public boolean update(Unit unit);
	
	/**
	 * 删除单位，伪删除，将isDelete状态改为1
	 * @param unit
	 * @return
	 */
	public boolean del(Unit unit);
	
	/**
	 * 通过id查询单位
	 * @param id
	 * @return
	 */
	public Unit queryById(Long id);
	
	/**
	 * 查询指定id的所有单位
	 * @param ids
	 * @return
	 */
	public List<Unit> queryByIds(List<Long> ids);
	
	/**
	 * 查询单位列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Unit> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的单位
	 * @param paramMap
	 * @return
	 */
	public List<Unit> queryAll();
	
	/**
	 * 检测有效值
	 * @param unit
	 * @throws Exception
	 */
	public void valid(Unit unit) throws Exception;
}
