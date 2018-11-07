package com.linuslan.oa.workflow.flows.warehouse.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;

public interface IWarehouseDao extends IBaseDao {

	/**
	 * 新增仓库
	 * @param warehouse
	 * @return
	 */
	public boolean add(Warehouse warehouse);
	
	/**
	 * 更新仓库信息
	 * @param warehouse
	 * @return
	 */
	public boolean update(Warehouse warehouse);
	
	/**
	 * 删除仓库，伪删除，将isDelete状态改为1
	 * @param warehouse
	 * @return
	 */
	public boolean del(Warehouse warehouse);
	
	/**
	 * 通过id查询仓库
	 * @param id
	 * @return
	 */
	public Warehouse queryById(Long id);
	
	/**
	 * 查询指定id的所有仓库
	 * @param ids
	 * @return
	 */
	public List<Warehouse> queryByIds(List<Long> ids);
	
	/**
	 * 查询仓库列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Warehouse> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的仓库
	 * @param paramMap
	 * @return
	 */
	public List<Warehouse> queryAll();
}
