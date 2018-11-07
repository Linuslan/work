package com.linuslan.oa.system.role.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.role.model.RoleResource;
import com.linuslan.oa.util.Page;

public interface IRoleService extends IBaseService {

	/**
	 * 新增角色
	 * @param role
	 * @return
	 */
	public boolean add(Role role);
	
	/**
	 * 更新角色信息
	 * @param role
	 * @return
	 */
	public boolean update(Role role);
	
	/**
	 * 删除角色，伪删除，将isDelete状态改为1
	 * @param role
	 * @return
	 */
	public boolean del(Role role);
	
	/**
	 * 通过id查询角色
	 * @param id
	 * @return
	 */
	public Role queryById(Long id);
	
	/**
	 * 通过用户id查询用户所有的角色
	 * @param userId
	 * @return
	 */
	public List<Role> queryByUserId(Long userId);
	
	/**
	 * 查询指定id的所有角色
	 * @param ids
	 * @return
	 */
	public List<Role> queryByIds(List<Long> ids);
	
	/**
	 * 查询角色列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Role> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的角色
	 * @param paramMap
	 * @return
	 */
	public List<Role> queryAll();
	
	/**
	 * 检测有效值
	 * @param role
	 * @throws Exception
	 */
	public void valid(Role role) throws Exception;
	
	/**
	 * 通过角色id集合查询角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByUserId(Long userId);
	
	/**
	 * 通过角色id集合查询角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByRoleId(Long roleId);
	
	/**
	 * 批量添加角色授权资源
	 * @param resources
	 * @return
	 * @throws RuntimeException
	 */
	public boolean addRoleResourceBatch(List<RoleResource> resources, Long roleId) throws RuntimeException;
	
	/**
	 * 将权限资源转换成角色资源对象
	 * @param list
	 * @return
	 */
	public void parseToResource(List<RoleResource> resources, Role role, List<? extends Object> list) throws Exception;
}
