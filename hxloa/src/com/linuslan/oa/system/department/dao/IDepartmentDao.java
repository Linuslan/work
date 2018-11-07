package com.linuslan.oa.system.department.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.department.model.Department;

public interface IDepartmentDao extends IBaseDao {

	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Department> queryAll();
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id);
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Department queryById(Long id);
	
	/**
	 * 通过id集合查询部门集合
	 * @param ids
	 * @return
	 */
	public List<Department> queryInIds(List<Long> ids);
	
	/**
	 * 新增部门
	 * @param department
	 * @return
	 */
	public boolean add(Department department);
	
	/**
	 * 更新部门
	 * @param department
	 * @return
	 */
	public boolean update(Department department);
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid);
	
	public boolean addClass(Long departmentId, Long reimburseClassId);
	
	public boolean delClass(Long departmentId, Long reimburseClassId);
	
}
