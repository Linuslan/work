package com.linuslan.oa.system.group.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.group.model.Group;

public interface IGroupDao extends IBaseDao {
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Group> queryAll();
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id);
	
	/**
	 * 通过用户id查询用户拥有的其他用户组
	 * @param userId
	 * @return
	 */
	public List<Group> queryByUserId(Long userId);
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Group queryById(Long id);
	
	/**
	 * 通过id集合查询用户集合
	 * @param ids
	 * @return
	 */
	public List<Group> queryInIds(List<Long> ids);
	
	/**
	 * 新增部门
	 * @param group
	 * @return
	 */
	public boolean add(Group group);
	
	/**
	 * 更新部门
	 * @param group
	 * @return
	 */
	public boolean update(Group group);
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid);
	
	/**
	 * 检查groupId是否唯一
	 * @param groupId
	 * @param id
	 * @return
	 */
	public boolean checkeUniqueGroupId(String groupId, Long id);
	
	/**
	 * 新增用户的用户组
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	public boolean addUserGroups(Long userId, List<Long> groupIds) throws RuntimeException;
	
	/**
	 * 通过用户id删除用户组
	 * @param userId
	 * @return
	 */
	public boolean delByUserId(Long userId);
}
