package com.linuslan.oa.system.post.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.system.post.dao.IPostDao;
import com.linuslan.oa.util.Page;

@Component("postDao")
public class IPostDaoImpl extends IBaseDaoImpl implements IPostDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过菜单id分页查询按钮
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Post> queryPageByDepartmentId(Long departmentId, Map<String, String> paramMap, int page, int rows) {
		Page<Post> pageData = null;
		List<Post> list = new ArrayList<Post> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM Post b WHERE b.isDelete=0 AND b.departmentId=:departmentId");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.departmentId ASC, b.orderNo ASC");
		query.setParameter("departmentId", departmentId);
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		countQuery.setParameter("departmentId", departmentId);
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<Post> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 通过部门id查询岗位
	 * @param departmentId
	 * @return
	 */
	public List<Post> queryByDepartmentId(Long departmentId) {
		List<Post> list = new ArrayList<Post> ();
		
		String hql = "FROM Post p WHERE p.isDelete=0 AND p.departmentId=:departmentId ORDER BY p.orderNo ASC, p.id DESC";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameter("departmentId", departmentId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Post queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Post post = (Post) session.get(Post.class, id);
		return post;
	}
	
	/**
	 * 新增按钮
	 * @param post
	 * @return
	 */
	public boolean add(Post post) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(post);
		success = true;
		return success;
	}
	
	/**
	 * 更新按钮
	 * @param post
	 * @return
	 */
	public boolean update(Post post) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(post);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Post b SET b.isDelete=1 WHERE b.departmentId=:departmentId";
		Query query = session.createQuery(hql);
		query.setParameter("departmentId", departmentId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentIds(List<Long> departmentIds) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Post b SET b.isDelete=1 WHERE b.departmentId IN (:departmentIds)";
		Query query = session.createQuery(hql);
		query.setParameterList("departmentIds", departmentIds);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过菜单id查询按钮
	 */
	public List<Post> queryByDepartmentIds(List<Long> departmentIds) {
		List<Post> list = new ArrayList<Post> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Post b WHERE b.departmentId IN (:departmentIds) AND b.isDelete=0 ORDER BY b.departmentId ASC, b.orderNo ASC";
		Query query = session.createQuery(hql);
		query.setParameterList("departmentIds", departmentIds);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByPostIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_resources t WHERE t.resources_type='post' AND t.resources_id IN(:ids)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameterList("ids", ids);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("UPDATE sys_post t2 SET t2.is_delete=1 WHERE t2.id IN (:ids)");
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Post> queryByIds(List<Long> ids) {
		List<Post> posts = new ArrayList<Post> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Post b WHERE b.isDelete=0 AND b.id IN (:ids) ORDER BY b.departmentId ASC, b.orderNo ASC");
		query.setParameterList("ids", ids);
		posts.addAll(query.list());
		return posts;
	}
}
