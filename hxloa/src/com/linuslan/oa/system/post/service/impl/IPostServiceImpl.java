package com.linuslan.oa.system.post.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.system.post.dao.IPostDao;
import com.linuslan.oa.system.post.service.IPostService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("postService")
@Transactional
public class IPostServiceImpl extends IBaseServiceImpl implements IPostService {

	@Autowired
	private IPostDao postDao;
	
	/**
	 * 通过菜单id分页查询按钮
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Post> queryPageByDepartmentId(Long departmentId, Map<String, String> paramMap, int page, int rows) {
		return this.postDao.queryPageByDepartmentId(departmentId, paramMap, page, rows);
	}
	
	/**
	 * 通过部门id查询岗位
	 * @param departmentId
	 * @return
	 */
	public List<Post> queryByDepartmentId(Long departmentId) {
		return this.postDao.queryByDepartmentId(departmentId);
	}
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Post queryById(Long id) {
		return this.postDao.queryById(id);
	}
	
	/**
	 * 新增按钮
	 * @param post
	 * @return
	 */
	public boolean add(Post post) throws Exception {
		return this.postDao.add(post);
	}
	
	/**
	 * 更新按钮
	 * @param post
	 * @return
	 */
	public boolean update(Post post) throws Exception {
		return this.postDao.update(post);
	}
	
	/**
	 * 伪删除按钮同时真删除角色的按钮资源
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public boolean del(Post post) throws Exception {
		boolean success = false;
		List<Post> list = new ArrayList<Post> ();
		list.add(post);
		String idStr = BeanUtil.parseString(list, "id", ",");
		List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
		this.postDao.delResourcesByPostIds(ids);
		this.postDao.delBatchByIds(ids);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) throws Exception {
		return this.postDao.delByDepartmentId(departmentId);
	}
	
	public List<Post> queryByDepartmentIds(List<Long> departmentIds) {
		return this.postDao.queryByDepartmentIds(departmentIds);
	}
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByPostIds(List<Long> ids) throws Exception {
		return this.postDao.delResourcesByPostIds(ids);
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception {
		return this.postDao.delBatchByIds(ids);
	}
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Post> queryByIds(List<Long> ids) {
		return this.postDao.queryByIds(ids);
	}
}
