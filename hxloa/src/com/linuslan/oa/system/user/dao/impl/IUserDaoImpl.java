package com.linuslan.oa.system.user.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.user.dao.IUserDao;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("userDao")
public class IUserDaoImpl extends IBaseDaoImpl implements IUserDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 分页查询用户
	 */
	public Page<User> queryPage(Map<String, String> paramMap, int pageNum, int pageSize) {
		Page<User> page = null;
		long totalRecord = 0;
		long totalPage = 0;
		List<User> list = new ArrayList<User> ();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM User r WHERE r.isDelete=0");
		if(null != paramMap) {
			String subHQL = this.getHQL(User.class, hql, paramMap, "r");
			hql.append(subHQL);
		}
		String countHQL = "SELECT COUNT(*) "+hql;
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = this.buildQuery(session, User.class, hql.toString(), countHQL, paramMap);
		if(null != queryMap.get(ConstantVar.QUERY)) {
			query = queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((pageNum - 1)*pageSize);
			query.setMaxResults(pageSize);
			list.addAll(query.list());
		}
		if(null != queryMap.get(ConstantVar.COUNT_QUERY)) {
			countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
		}
		page = new Page<User>(list, totalRecord, totalPage, pageNum);
		return page;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAll() {
		List<User> list = new ArrayList<User> ();
		String hql = "FROM User u WHERE u.isDelete=0 AND u.isLeave=1";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询用户
	 * @param id
	 * @return
	 */
	public User queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (User) session.get(User.class, id);
	}
	
	/**
	 * 通过部门id集合查询归属人员
	 * @param ids
	 * @return
	 */
	public List<User> queryByDepartmentIds(List<Long> ids) {
		List<User> users = new ArrayList<User> ();
		Session session = this.sessionFactory.getCurrentSession();
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM User u WHERE u.departmentId IN (:ids) AND u.isDelete=0 AND u.isLeave=1";
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			users.addAll(query.list());
		}
		return users;
	}
	
	/**
	 * 通过用户id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryInIds(List<Long> ids) {
		List<User> users = new ArrayList<User> ();
		Session session = this.sessionFactory.getCurrentSession();
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM User u WHERE u.id IN (:ids) AND u.isDelete=0 AND u.isLeave=1";
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			users.addAll(query.list());
		}
		return users;
	}
	
	/**
	 * 通过用户组id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryByGroupIds(List<Long> ids) {
		List<User> users = new ArrayList<User> ();
		Session session = this.sessionFactory.getCurrentSession();
		if(null != ids && 0 < ids.size()) {
			String sql = "SELECT * FROM sys_user t WHERE (t.group_id IN (:ids)" +
					" OR (t.id IN (SELECT t1.user_id FROM sys_user_group t1 WHERE t1.group_id IN (:ids))))" +
					" AND t.is_delete=0 AND t.is_leave = 1";
			Query query = session.createQuery(sql);
			query.setParameter("ids", ids);
			query.setResultTransformer(new BeanTransformerAdapter(User.class));
			users.addAll(query.list());
		}
		return users;
	}
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public boolean add(User user) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(user);
		success = true;
		return success;
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		boolean success = true;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(user);
		success = true;
		return success;
	}
	
	/**
	 * 判断是否有登陆用户
	 * @param user
	 * @return
	 */
	public User login(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM User u WHERE (u.loginName=:loginName OR u.employeeNo=:employeeNo) AND u.password=:password AND u.isDelete=0 AND u.isLeave=1";
		Query query = session.createQuery(hql);
		query.setParameter("loginName", user.getLoginName());
		query.setParameter("employeeNo", user.getLoginName());
		query.setParameter("password", user.getPassword());
		List<User> users = new ArrayList<User> ();
		users.addAll(query.list());
		if(0 < users.size()) {
			user = users.get(0);
		} else {
			user = null;
		}
		return user;
	}
	
	/**
	 * 为用户分配公司
	 * @param user
	 * @param companys
	 * @return
	 */
	public boolean assignCompany(User user, List<Company> companys) {
		boolean success = true;
		String delSQL = "DELETE FROM sys_user_company WHERE user_id=:userId";
		String sql = "INSERT INTO sys_user_company(user_id, company_id) VALUES(:userId, :companyId)";
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			ps = conn.prepareStatement(delSQL);
			ps.setLong(1, user.getId());
			ps.execute();
			ps = conn.prepareStatement(sql);
			for(Company company : companys) {
				ps.setLong(1, user.getId());
				ps.setLong(2, company.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			success = true;
		} catch(Exception ex) {
			throw new RuntimeException("分配公司失败");
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
}
