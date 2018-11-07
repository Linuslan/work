package com.linuslan.oa.workflow.flows.warehouse.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.warehouse.dao.IWarehouseDao;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.service.IWarehouseService;

@Component("warehouseService")
@Transactional
public class IWarehouseServiceImpl extends IBaseServiceImpl implements
		IWarehouseService {
	@Autowired
	private IWarehouseDao warehouseDao;
	
	/**
	 * 新增仓库
	 * @param warehouse
	 * @return
	 */
	public boolean add(Warehouse warehouse) {
		return this.warehouseDao.add(warehouse);
	}
	
	/**
	 * 更新仓库信息
	 * @param warehouse
	 * @return
	 */
	public boolean update(Warehouse warehouse) {
		return this.warehouseDao.update(warehouse);
	}
	
	/**
	 * 删除仓库，伪删除，将isDelete状态改为1
	 * @param warehouse
	 * @return
	 */
	public boolean del(Warehouse warehouse) {
		return this.warehouseDao.del(warehouse);
	}
	
	/**
	 * 通过id查询仓库
	 * @param id
	 * @return
	 */
	public Warehouse queryById(Long id) {
		return this.warehouseDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有仓库
	 * @param ids
	 * @return
	 */
	public List<Warehouse> queryByIds(List<Long> ids) {
		return this.warehouseDao.queryByIds(ids);
	}
	
	/**
	 * 查询仓库列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Warehouse> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.warehouseDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的仓库
	 * @param paramMap
	 * @return
	 */
	public List<Warehouse> queryAll() {
		return this.warehouseDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param warehouse
	 * @throws Exception
	 */
	public void valid(Warehouse warehouse) throws Exception {
		if(null == warehouse.getName() || "".equals(warehouse.getName().trim())) {
			CodeUtil.throwExcep("仓库名称不能为空");
		}
	}
}
