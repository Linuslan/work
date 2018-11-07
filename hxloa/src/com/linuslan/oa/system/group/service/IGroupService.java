package com.linuslan.oa.system.group.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.group.model.Group;

public interface IGroupService extends IBaseService {
	
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Group> queryAll() throws Exception;
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id) throws Exception;
	
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
	public Group queryById(Long id) throws Exception;
	
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
	public boolean add(Group group) throws Exception;
	
	/**
	 * 更新部门
	 * @param group
	 * @return
	 */
	public boolean update(Group group) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid);
	
	public void valid(Group group) throws Exception;
	
}
