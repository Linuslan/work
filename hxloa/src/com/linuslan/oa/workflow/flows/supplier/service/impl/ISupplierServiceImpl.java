package com.linuslan.oa.workflow.flows.supplier.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.supplier.dao.ISupplierDao;
import com.linuslan.oa.workflow.flows.supplier.model.Supplier;
import com.linuslan.oa.workflow.flows.supplier.service.ISupplierService;

@Component("supplierService")
@Transactional
public class ISupplierServiceImpl extends IBaseServiceImpl implements
		ISupplierService {

	@Autowired
	private ISupplierDao supplierDao;
	
	/**
	 * 新增供货商
	 * @param supplier
	 * @return
	 */
	public boolean add(Supplier supplier) {
		return this.supplierDao.add(supplier);
	}
	
	/**
	 * 更新供货商信息
	 * @param supplier
	 * @return
	 */
	public boolean update(Supplier supplier) {
		return this.supplierDao.update(supplier);
	}
	
	/**
	 * 删除供货商，伪删除，将isDelete状态改为1
	 * @param supplier
	 * @return
	 */
	public boolean del(Supplier supplier) {
		return this.supplierDao.del(supplier);
	}
	
	/**
	 * 通过id查询供货商
	 * @param id
	 * @return
	 */
	public Supplier queryById(Long id) {
		return this.supplierDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有供货商
	 * @param ids
	 * @return
	 */
	public List<Supplier> queryByIds(List<Long> ids) {
		return this.supplierDao.queryByIds(ids);
	}
	
	/**
	 * 查询供货商列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Supplier> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.supplierDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的供货商
	 * @param paramMap
	 * @return
	 */
	public List<Supplier> queryAll() {
		return this.supplierDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param supplier
	 * @throws Exception
	 */
	public void valid(Supplier supplier) throws Exception {
		if(null == supplier.getName() || "".equals(supplier.getName().trim())) {
			CodeUtil.throwExcep("供货商名称不能为空");
		}
	}
}
