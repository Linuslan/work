package com.linuslan.oa.system.post.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.util.Page;

public interface IPostService extends IBaseService {

	/**
	 * 通过菜单id分页查询按钮
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Post> queryPageByDepartmentId(Long departmentId, Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过部门id查询岗位
	 * @param departmentId
	 * @return
	 */
	public List<Post> queryByDepartmentId(Long departmentId);
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Post queryById(Long id);
	
	/**
	 * 新增按钮
	 * @param post
	 * @return
	 */
	public boolean add(Post post) throws Exception;
	
	/**
	 * 更新按钮
	 * @param post
	 * @return
	 */
	public boolean update(Post post) throws Exception;
	
	/**
	 * 伪删除按钮同时真删除角色的按钮资源
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public boolean del(Post post) throws Exception;
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) throws Exception;
	
	public List<Post> queryByDepartmentIds(List<Long> departmentIds);
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByPostIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Post> queryByIds(List<Long> ids);
	
}
