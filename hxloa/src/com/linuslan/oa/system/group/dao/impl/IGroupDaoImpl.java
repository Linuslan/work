package com.linuslan.oa.system.group.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.group.dao.IGroupDao;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("groupDao")
public class IGroupDaoImpl extends IBaseDaoImpl implements IGroupDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Group> queryAll() {
		List<Group> list = new ArrayList<Group> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Group g WHERE g.isDelete=0");
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT t.*, (SELECT t2.text FROM sys_group t2 WHERE t2.id=t.pid) as pname FROM sys_group t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid");
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过用户id查询用户拥有的其他用户组
	 * @param userId
	 * @return
	 */
	public List<Group> queryByUserId(Long userId) {
		List<Group> groups = new ArrayList<Group> ();
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT t.* FROM sys_group t WHERE t.id IN (SELECT t2.group_id FROM sys_user_group t2 WHERE t2.user_id=:userId)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setResultTransformer(new BeanTransformerAdapter(Group.class));
		groups.addAll(query.list());
		return groups;
	}
	
	/**
	 * 通过id查询用户组
	 * @param id
	 * @return
	 */
	public Group queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Group)session.get(Group.class, id);
	}
	
	/**
	 * 通过id集合查询用户集合
	 * @param ids
	 * @return
	 */
	public List<Group> queryInIds(List<Long> ids) {
		List<Group> list = new ArrayList<Group> ();
		if(null != ids && 0 < ids.size()) {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM Group d WHERE d.id IN (:ids) AND d.isDelete=0";
			Query query = session.createQuery(hql);
			query.setParameter("ids", ids);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 新增用户组
	 * @param group
	 * @return
	 */
	public boolean add(Group group) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(group);
		success = true;
		return success;
	}
	
	/**
	 * 更新用户组
	 * @param group
	 * @return
	 */
	public boolean update(Group group) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(group);
		success = true;
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("UPDATE sys_group t2 SET t2.is_delete=1 WHERE t2.id IN (SELECT t.id FROM sys_group t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid)");
		query.setParameter("id", pid);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 新增用户的用户组
	 * @param userId
	 * @param groupIds
	 * @return
	 * @throws SQLException 
	 */
	public boolean addUserGroups(Long userId, List<Long> groupIds) throws RuntimeException {
		boolean success = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			String sql = "INSERT INTO sys_user_group(user_id, group_id) VALUES(:userId, :groupId)";
			ps = conn.prepareStatement(sql);
			for(Long groupId : groupIds) {
				ps.setLong(1, userId);
				ps.setLong(2, groupId);
				ps.addBatch();
			}
			ps.executeBatch();
			success = true;
		} catch(Exception ex) {
			throw new RuntimeException("分配用户组失败");
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
	 * 检查groupId是否唯一
	 * @param groupId
	 * @param id
	 * @return
	 */
	public boolean checkeUniqueGroupId(String groupId, Long id) {
		boolean isUnique = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "SELECT COUNT(*) FROM Group g WHERE g.groupId=:groupId";
		if(null != id) {
			hql += " AND g.id<>:id";
		}
		Query query = session.createQuery(hql);
		query.setParameter("groupId", groupId);
		if(null != id) {
			query.setParameter("id", id);
		}
		long count = (Long) query.uniqueResult();
		if(0 >= count) {
			isUnique = true;
		}
		return isUnique;
	}
	
	/**
	 * 通过用户id删除用户组
	 * @param userId
	 * @return
	 */
	public boolean delByUserId(Long userId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_user_group t WHERE t.user_id=:userId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.executeUpdate();
		return success;
	}
}
