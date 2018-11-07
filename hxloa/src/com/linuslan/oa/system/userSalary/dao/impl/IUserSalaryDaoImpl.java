package com.linuslan.oa.system.userSalary.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.system.userSalary.dao.IUserSalaryDao;
import com.linuslan.oa.util.Page;

@Component("userSalaryDao")
public class IUserSalaryDaoImpl extends IBaseDaoImpl implements IUserSalaryDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过菜单id分页查询员工薪资
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<UserSalary> queryPageByUserId(Long userId, Map<String, String> paramMap, int page, int rows) {
		Page<UserSalary> pageData = null;
		List<UserSalary> list = new ArrayList<UserSalary> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM UserSalary b WHERE b.isDelete=0 AND b.userId=:userId");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.createDate DESC, b.id DESC");
		query.setParameter("userId", userId);
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		countQuery.setParameter("userId", userId);
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<UserSalary> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 查询生效的工资，同时查询year年month月对应的员工的绩效
	 * @return
	 */
	public List<Map<String, Object>> queryAllUserSalarys(int year, int month) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		//String sql = "SELECT t.id, t.name, t3.id department_id, t3.name department_name, t3.company_id, t2.* FROM sys_user t LEFT JOIN sys_department t3 ON t3.id=t.department_id LEFT JOIN sys_user_salary t2 ON t2.user_id=t.id AND t2.is_delete=0 AND t2.status=1 WHERE t.is_delete=0 AND t.is_leave=1";
		//String sql = "SELECT t2.company_id, t.* FROM sys_user_salary t LEFT JOIN sys_department t2 ON t2.id=t.department_id WHERE t.status=1 AND t.is_delete=0 AND t.user_id IN (SELECT t3.id FROM sys_user t3 WHERE t3.is_delete=0 AND t3.is_leave=1)";
		//增加了绩效
		String sql = "SELECT t2.company_id, t3.name user_name, t4.leader_score, t4.add_score, t4.add_money, t.* FROM sys_user_salary t LEFT JOIN sys_department t2 ON t2.id=t.department_id LEFT JOIN sys_user t3 ON t3.id=t.user_id LEFT JOIN wf_achievement t4 ON t4.year=:year AND t4.month=:month AND t4.status=4 AND t4.is_delete=0 AND t4.user_id=t.user_id WHERE t.last_status=1 AND t.is_delete=0 AND t.user_id IN (SELECT t3.id FROM sys_user t3 WHERE t3.is_delete=0 AND t3.is_leave=1)";
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter("year", year);
		sqlQuery.setParameter("month", month);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(sqlQuery.list());
		return list;
	}
	
	/**
	 * 通过id查询员工薪资
	 * @param id
	 * @return
	 */
	public UserSalary queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		UserSalary userSalary = (UserSalary) session.get(UserSalary.class, id);
		return userSalary;
	}
	
	/**
	 * 通过用户id查询其生效的工资
	 * @param userId
	 * @return
	 */
	public UserSalary queryEffectSalaryByUserId(Long userId) {
		List<UserSalary> list = new ArrayList<UserSalary> ();
		Session session = this.sessionFactory.getCurrentSession();
		//假如有多条生效的工资，则取最新一条生效的工资
		String hql = "FROM UserSalary us WHERE us.status=1 AND us.isDelete=0 AND us.userId=:userId ORDER BY us.effectDate DESC, us.id DESC";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		list.addAll(query.list());
		if(0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 如果没有生效工资，则查询即将生效的工资，即生效时间在当前时间之后，且离当前时间最近的工资
	 * @param userId
	 * @return
	 */
	public UserSalary queryImmediatelyEffectSalaryByUserId(Long userId) {
		List<UserSalary> list = new ArrayList<UserSalary> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM UserSalary us WHERE us.isDelete=0 AND us.effectDate >= SYSDATE AND us.userId=:userId ORDER BY us.effectDate ASC, us.id ASC";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		list.addAll(query.list());
		if(0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 如果没有生效工资，也没有即将生效的工资，则查询用户在当前时间之前，且离当前时间最近的一条工资
	 * @param userId
	 * @return
	 */
	public UserSalary queryLastSalaryByUserId(Long userId) {
		List<UserSalary> list = new ArrayList<UserSalary> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM UserSalary us WHERE us.isDelete=0 AND us.effectDate <= SYSDATE AND us.userId=:userId ORDER BY us.effectDate DESC, us.id DESC";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		list.addAll(query.list());
		if(0 < list.size()) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 新增员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean add(UserSalary userSalary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(userSalary);
		success = true;
		return success;
	}
	
	/**
	 * 更新员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean update(UserSalary userSalary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(userSalary);
		success = true;
		return success;
	}
	
	/**
	 * 将用户的其他工资更新为不生效
	 * @param userId
	 * @return
	 */
	public boolean updateUneffectByUserId(Long userId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE UserSalary b SET b.status=0, b.isForceEffect=0 WHERE b.userId=:userId AND b.isDelete=0";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量删除员工薪资（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE UserSalary b SET b.isDelete=1 WHERE b.departmentId=:departmentId";
		Query query = session.createQuery(hql);
		query.setParameter("departmentId", departmentId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量删除员工薪资（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentIds(List<Long> departmentIds) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE UserSalary b SET b.isDelete=1 WHERE b.departmentId IN (:departmentIds)";
		Query query = session.createQuery(hql);
		query.setParameterList("departmentIds", departmentIds);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过菜单id查询员工薪资
	 */
	public List<UserSalary> queryByDepartmentIds(List<Long> departmentIds) {
		List<UserSalary> list = new ArrayList<UserSalary> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM UserSalary b WHERE b.departmentId IN (:departmentIds) AND b.isDelete=0 ORDER BY b.departmentId ASC, b.orderNo ASC";
		Query query = session.createQuery(hql);
		query.setParameterList("departmentIds", departmentIds);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过员工薪资id集合，删除角色资源里面的员工薪资资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByUserSalaryIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_resources t WHERE t.resources_type='userSalary' AND t.resources_id IN(:ids)";
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
		SQLQuery query = session.createSQLQuery("UPDATE sys_userSalary t2 SET t2.is_delete=1 WHERE t2.id IN (:ids)");
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过id集合查询对应的员工薪资集合
	 * @param ids
	 * @return
	 */
	public List<UserSalary> queryByIds(List<Long> ids) {
		List<UserSalary> userSalarys = new ArrayList<UserSalary> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM UserSalary b WHERE b.isDelete=0 AND b.id IN (:ids) ORDER BY b.departmentId ASC, b.orderNo ASC");
		query.setParameterList("ids", ids);
		userSalarys.addAll(query.list());
		return userSalarys;
	}
}
