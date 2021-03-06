package com.linuslan.oa.system.userSalary.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.util.Page;

public interface IUserSalaryService extends IBaseService {

	/**
	 * 通过菜单id分页查询员工薪资
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<UserSalary> queryPageByUserId(Long userId, Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询生效的工资，同时查询year年month月对应的员工的绩效
	 * @return
	 */
	public List<Map<String, Object>> queryAllUserSalarys(int year, int month);
	
	/**
	 * 通过id查询员工薪资
	 * @param id
	 * @return
	 */
	public UserSalary queryById(Long id);
	
	/**
	 * 查询用户的生效工资，如果没有生效工资，则查询即将生效的工资，即生效时间在当前时间之后，且离当前时间最近的工资
	 * @param userId
	 * @return
	 */
	public UserSalary querySalaryByUserId(Long userId);
	
	/**
	 * 新增员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean add(UserSalary userSalary) throws Exception;
	
	/**
	 * 更新员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean update(UserSalary userSalary) throws Exception;
	
	/**
	 * 将用户的其他工资更新为不生效
	 * @param userId
	 * @return
	 */
	public boolean updateUneffectByUserId(Long userId);
	
	/**
	 * 伪删除员工薪资同时真删除角色的员工薪资资源
	 * @param userSalary
	 * @return
	 * @throws Exception
	 */
	public boolean del(UserSalary userSalary) throws Exception;
	
	/**
	 * 批量删除员工薪资（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) throws Exception;
	
	public List<UserSalary> queryByDepartmentIds(List<Long> departmentIds);
	
	/**
	 * 通过员工薪资id集合，删除角色资源里面的员工薪资资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByUserSalaryIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过id集合查询对应的员工薪资集合
	 * @param ids
	 * @return
	 */
	public List<UserSalary> queryByIds(List<Long> ids);
	
}
