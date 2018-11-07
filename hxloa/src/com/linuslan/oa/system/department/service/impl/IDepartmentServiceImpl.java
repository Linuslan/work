package com.linuslan.oa.system.department.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.department.dao.IDepartmentDao;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.department.service.IDepartmentService;
import com.linuslan.oa.util.CodeUtil;

@Component("departmentService")
@Transactional
public class IDepartmentServiceImpl extends IBaseServiceImpl implements IDepartmentService {
	
	@Autowired
	private IDepartmentDao departmentDao;
	
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Department> queryAll() throws Exception {
		return this.departmentDao.queryAll();
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id) throws Exception {
		return this.departmentDao.queryChildrenByPid(id);
	}
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Department queryById(Long id) throws Exception {
		return this.departmentDao.queryById(id);
	}
	
	/**
	 * 通过id集合查询部门集合
	 * @param ids
	 * @return
	 */
	public List<Department> queryInIds(List<Long> ids) {
		return this.departmentDao.queryInIds(ids);
	}
	
	/**
	 * 新增部门
	 * @param department
	 * @return
	 */
	public boolean add(Department department) throws Exception {
		boolean success = this.departmentDao.add(department);
		return success;
	}
	
	/**
	 * 更新部门
	 * @param department
	 * @return
	 */
	public boolean update(Department department) throws Exception {
		boolean success = this.departmentDao.update(department);
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) {
		boolean success = this.departmentDao.delBatchByPid(pid);
		return success;
	}
	
	public void valid(Department department) throws Exception {
		if(null == department.getName() || "".equals(department.getName().trim())) {
			CodeUtil.throwExcep("请输入部门名称");
		}
		department.setName(department.getName().trim());
	}
	
	public boolean addClass(Long departmentId, Long reimburseClassId) {
		return this.departmentDao.addClass(departmentId, reimburseClassId);
	}
	
	public boolean delClass(Long departmentId, Long reimburseClassId) {
		return this.departmentDao.delClass(departmentId, reimburseClassId);
	}
}
