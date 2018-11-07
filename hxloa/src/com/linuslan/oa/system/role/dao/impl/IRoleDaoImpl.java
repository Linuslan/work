package com.linuslan.oa.system.role.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.role.dao.IRoleDao;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.role.model.RoleResource;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("roleDao")
public class IRoleDaoImpl extends IBaseDaoImpl implements IRoleDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增角色
	 * @param role
	 * @return
	 */
	public boolean add(Role role) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(role);
		success = true;
		return success;
	}
	
	/**
	 * 更新角色信息
	 * @param role
	 * @return
	 */
	public boolean update(Role role) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(role);
		success = true;
		return success;
	}
	
	/**
	 * 删除角色，伪删除，将isDelete状态改为1
	 * @param role
	 * @return
	 */
	public boolean del(Role role) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		role.setIsDelete(1);
		session.update(role);
		success = true;
		return success;
	}
	
	/**
	 * 通过用户id删除用户角色
	 * @param userId
	 * @return
	 */
	public boolean delByUserId(Long userId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_user t WHERE t.user_id=:userId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.executeUpdate();
		return success;
	}
	
	/**
	 * 通过角色id删除拥有该角色的用户
	 * @param roleId
	 * @return
	 */
	public boolean delByRoleId(Long roleId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_user t WHERE t.role_id=:roleId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("roleId", roleId);
		query.executeUpdate();
		return success;
	}
	
	/**
	 * 通过id查询角色
	 * @param id
	 * @return
	 */
	public Role queryById(Long id) {
		Role role = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		role = (Role) session.get(Role.class, id);
		return role;
	}
	
	/**
	 * 通过用户id查询用户所有的角色
	 * @param userId
	 * @return
	 */
	public List<Role> queryByUserId(Long userId) {
		List<Role> roles = new ArrayList<Role> ();
		Session session = null;
		if(null != userId) {
			StringBuffer hql = new StringBuffer("SELECT r.* FROM sys_role r INNER JOIN sys_role_user ru ON r.id=ru.role_id WHERE ru.user_id=:userId AND r.is_delete=0");
			session = this.sessionFactory.getCurrentSession();
			Query query = session.createSQLQuery(hql.toString()).addEntity(Role.class);
			query.setParameter("userId", userId);
			roles.addAll(query.list());
		}
		return roles;
	}
	
	/**
	 * 查询指定id的所有角色
	 * @param ids
	 * @return
	 */
	public List<Role> queryByIds(List<Long> ids) {
		List<Role> roles = new ArrayList<Role> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Role r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			roles.addAll(query.list());
		}
		
		return roles;
	}
	
	/**
	 * 查询角色列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Role> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Role> list = null;
		Page<Role> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Role r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Role r WHERE r.isDelete=0");
		if(null != paramMap) {
			String subSQL = this.getHQL(Role.class, hql, paramMap, "r");
			hql.append(subSQL);
			countHQL.append(subSQL);
		}
		
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = this.buildQuery(session, Role.class, hql.toString(), countHQL.toString(), paramMap);
		if(null != queryMap.get(ConstantVar.QUERY)) {
			query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((currentPage - 1)*limit).setMaxResults(limit);
			list = query.list();
		}
		
		long totalRecord = 0;
		long totalPage = 0;
		if(null != queryMap.get(ConstantVar.COUNT_QUERY)) {
			countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = totalRecord%limit > 0 ? totalRecord/limit + 1 : totalRecord/limit;
		}
		if(null != list) {
			page = new Page<Role>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的角色
	 * @param paramMap
	 * @return
	 */
	public List<Role> queryAll() {
		List<Role> roles = new ArrayList<Role>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Role r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		roles.addAll(query.list());
		
		return roles;
	}
	
	private void getHQL(Map<String, String> paramMap, StringBuffer hql, StringBuffer countHQL) {
		try {
			if(null != paramMap) {
				Set<String> keySet = paramMap.keySet();
				Iterator<String> iter = keySet.iterator();
				while(iter.hasNext()) {
					String key = iter.next();
					if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
						if(0 < hql.indexOf("WHERE")) {
							hql.append(" AND");
						} else {
							hql.append(" WHERE");
						}
						
						if(0 < countHQL.indexOf("WHERE")) {
							countHQL.append(" AND");
						} else {
							countHQL.append(" WHERE");
						}
						
						hql.append(" r."+key+" LIKE :"+key);
						countHQL.append(" r."+key+" LIKE :"+key);
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Map<String, Query> getQuery(Map<String, String> paramMap, StringBuffer hql, StringBuffer countHQL, Session session, Query query, Query countQuery) {
		Map<String, Query> queryMap = new HashMap<String, Query> ();
		try {
			if(null != session) {
				query = session.createQuery(hql.toString());
				countQuery = session.createQuery(countHQL.toString());
				if(null != paramMap) {
					Set<String> keySet = paramMap.keySet();
					Iterator<String> iter = keySet.iterator();
					while(iter.hasNext()) {
						String key = iter.next();
						if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
							query.setParameter(key, "%"+paramMap.get(key).trim()+"%");
							countQuery.setParameter(key, "%"+paramMap.get(key).trim()+"%");
						}
					}
				}
				queryMap.put("query", query);
				queryMap.put("countQuery", countQuery);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return queryMap;
	}
	
	/**
	 * 新增用户角色
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	public boolean addUserRoles(Long userId, List<Long> roleIds) throws RuntimeException {
		boolean success = false;
		String sql = "INSERT INTO sys_role_user(user_id, role_id) VALUES(:userId, :roleId)";
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			ps = conn.prepareStatement(sql);
			for(Long roleId : roleIds) {
				ps.setLong(1, userId);
				ps.setLong(2, roleId);
				ps.addBatch();
			}
			ps.executeBatch();
			success = true;
		} catch(Exception ex) {
			throw new RuntimeException("分配角色失败");
		} finally {
			try {
				if(null != ps) {
					ps.close();
					ps = null;
				}
				if(null != conn) {
					conn.close();
					conn = null;
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * 通过角色id删除该角色拥有的权限资源
	 * @param roleId
	 * @return
	 */
	public boolean delResourceByRoleId(Long roleId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_resources t WHERE t.role_id=:roleId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("roleId", roleId);
		query.executeUpdate();
		return success;
	}
	
	/**
	 * 通过用户id查询授予用户的角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByUserId(Long userId) {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT * FROM sys_role_resources t WHERE t.role_id IN (SELECT t2.role_id FROM sys_role_user t2 WHERE t2.user_id=:userId)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	/**
	 * 通过角色id集合查询角色的权限资源
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryResourcesByRoleId(Long roleId) {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT * FROM sys_role_resources t WHERE t.role_id=:roleId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("roleId", roleId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	/**
	 * 批量添加角色授权资源
	 * @param resources
	 * @return
	 * @throws RuntimeException
	 */
	public boolean addRoleResourceBatch(List<RoleResource> resources, Long roleId) throws RuntimeException {
		boolean success = false;
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement delPs = null;
		try {
			String sql = "INSERT INTO sys_role_resources(id, role_id, resources_id, resources_type, value)" +
					" VALUES(sys_role_resources_seq.nextval, ?, ?, ?, ?)";
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			if(null == resources || 0 >= resources.size()) {
				throw new Exception("授权失败，授权资源为空");
			}
			
			//授权之前先删除角色原有权限
			String delSQL = "DELETE FROM sys_role_resources t WHERE t.role_id=:roleId";
			delPs = conn.prepareStatement(delSQL);
			if(null == roleId) {
				CodeUtil.throwExcep("role's id is NULL");
			}
			delPs.setLong(1, roleId);
			delPs.executeUpdate();
			
			Iterator<RoleResource> iter = resources.iterator();
			RoleResource res = null;
			while(iter.hasNext()) {
				res = iter.next();
				if(null != res.getResourceId() && null != res.getRoleId()
						&& null != res.getResourceType() && !"".equals(res.getResourceType().trim())
						&& null != res.getValue() && !"".equals(res.getValue().trim())) {
					ps.setLong(1, res.getRoleId());
					ps.setLong(2, res.getResourceId());
					ps.setString(3, res.getResourceType().trim());
					ps.setString(4, res.getValue().trim());
					ps.addBatch();
				}
			}
			ps.executeBatch();
			conn.commit();
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("保存角色权限资源失败");
		} finally {
			try {
				if(null != ps) {
					ps.close();
					ps = null;
				}
				if(null != delPs) {
					delPs.close();
					delPs = null;
				}
				if(null != conn) {
					conn.close();
					conn = null;
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return success;
	}
}
