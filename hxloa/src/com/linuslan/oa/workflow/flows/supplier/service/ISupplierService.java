package com.linuslan.oa.workflow.flows.supplier.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.supplier.model.Supplier;

public interface ISupplierService extends IBaseService {

	/**
	 * 新增供货商
	 * @param supplier
	 * @return
	 */
	public boolean add(Supplier supplier);
	
	/**
	 * 更新供货商信息
	 * @param supplier
	 * @return
	 */
	public boolean update(Supplier supplier);
	
	/**
	 * 删除供货商，伪删除，将isDelete状态改为1
	 * @param supplier
	 * @return
	 */
	public boolean del(Supplier supplier);
	
	/**
	 * 通过id查询供货商
	 * @param id
	 * @return
	 */
	public Supplier queryById(Long id);
	
	/**
	 * 查询指定id的所有供货商
	 * @param ids
	 * @return
	 */
	public List<Supplier> queryByIds(List<Long> ids);
	
	/**
	 * 查询供货商列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Supplier> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的供货商
	 * @param paramMap
	 * @return
	 */
	public List<Supplier> queryAll();
	
	/**
	 * 检测有效值
	 * @param supplier
	 * @throws Exception
	 */
	public void valid(Supplier supplier) throws Exception;
	
}
