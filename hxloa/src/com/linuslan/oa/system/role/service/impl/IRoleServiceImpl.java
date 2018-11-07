package com.linuslan.oa.system.role.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.role.dao.IRoleDao;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.role.model.RoleResource;
import com.linuslan.oa.system.role.service.IRoleService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;

@Component("roleService")
@Transactional
public class IRoleServiceImpl extends IBaseServiceImpl implements IRoleService {
	
	@Autowired
	private IRoleDao roleDao;
	
	/**
	 * 新增角色
	 * @param role
	 * @return
	 */
	public boolean add(Role role) {
		return this.roleDao.add(role);
	}
	
	/**
	 * 更新角色信息
	 * @param role
	 * @return
	 */
	public boolean update(Role role) {
		return this.roleDao.update(role);
	}
	
	/**
	 * 删除角色，伪删除，将isDelete状态改为1
	 * @param role
	 * @return
	 */
	public boolean del(Role role) {
		this.roleDao.delByRoleId(role.getId());
		this.roleDao.delResourceByRoleId(role.getId());
		return this.roleDao.del(role);
	}
	
	/**
	 * 通过id查询角色
	 * @param id
	 * @return
	 */
	public Role queryById(Long id) {
		return this.roleDao.queryById(id);
	}
	
	/**
	 * 通过用户id查询用户所有的角色
	 * @param userId
	 * @return
	 */
	public List<Role> queryByUserId(Long userId) {
		return this.roleDao.queryByUserId(userId);
	}
	
	/**
	 * 查询指定id的所有角色
	 * @param ids
	 * @return
	 */
	public List<Role> queryByIds(List<Long> ids) {
		return this.roleDao.queryByIds(ids);
	}
	
	/**
	 * 查询角色列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Role> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.roleDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的角色
	 * @param paramMap
	 * @return
	 */
	public List<Role> queryAll() {
		return this.roleDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param role
	 * @throws Exception
	 */
	public void valid(Role role) throws Exception {
		if(null == role.getName() || "".equals(role.getName().trim())) {
			CodeUtil.throwExcep("角色名称不能为空");
		}
	}
	
	/**
	 * 通过角色id集合查询角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByUserId(Long userId) {
		return this.roleDao.queryResourcesByUserId(userId);
	}
	
	/**
	 * 通过角色id集合查询角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByRoleId(Long roleId) {
		return this.roleDao.queryResourcesByRoleId(roleId);
	}
	
	/**
	 * 批量添加角色授权资源
	 * @param resources
	 * @return
	 * @throws RuntimeException
	 */
	public boolean addRoleResourceBatch(List<RoleResource> resources, Long roleId) throws RuntimeException {
		return this.roleDao.addRoleResourceBatch(resources, roleId);
	}
	
	/**
	 * 将权限资源转换成角色资源对象
	 * @param list
	 * @return
	 */
	public void parseToResource(List<RoleResource> resources, Role role, List<? extends Object> list) throws Exception {
		if(null == resources) {
			resources = new ArrayList<RoleResource> ();
		}
		if(null != role && null != role.getId()) {
			if(null != list) {
				Iterator<? extends Object> iter = list.iterator();
				Object obj = null;
				RoleResource res = null;
				while(iter.hasNext()) {
					obj = iter.next();
					String idStr = BeanUtil.getValue(obj, "id").toString();
					String value = BeanUtil.getValue(obj, "value").toString();
					String className = obj.getClass().getSimpleName();
					String type = className.substring(0, 1).toLowerCase()+className.substring(1, className.length());
					if(null != idStr && !"".equals(idStr.trim())
							&& null != value && !"".equals(value.trim())
							&& null != type && !"".equals(type.trim())) {
						Long id = Long.parseLong(idStr.trim());
						res = new RoleResource();
						res.setResourceId(id);
						res.setResourceType(type.trim());
						res.setValue(value.trim());
						res.setRoleId(role.getId());
						resources.add(res);
					}
				}
			}
		}
	}
}
