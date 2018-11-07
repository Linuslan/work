package com.linuslan.oa.system.department.dao.impl;

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
import com.linuslan.oa.system.department.dao.IDepartmentDao;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;

@Component("departmentDao")
public class IDepartmentDaoImpl extends IBaseDaoImpl implements IDepartmentDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Department> queryAll() {
		List<Department> list = new ArrayList<Department> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Department d WHERE d.isDelete=0");
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
		SQLQuery query = session.createSQLQuery("SELECT t.*, (SELECT t2.name FROM sys_department t2 WHERE t2.id=t.pid) as pname FROM sys_department t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid");
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Department queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Department)session.get(Department.class, id);
	}
	
	/**
	 * 通过id集合查询部门集合
	 * @param ids
	 * @return
	 */
	public List<Department> queryInIds(List<Long> ids) {
		List<Department> list = new ArrayList<Department> ();
		if(null != ids && 0 < ids.size()) {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM Department d WHERE d.id IN (:ids) AND d.isDelete=0 ORDER BY d.orderNo";
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 新增部门
	 * @param department
	 * @return
	 */
	public boolean add(Department department) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(department);
		success = true;
		return success;
	}
	
	/**
	 * 更新部门
	 * @param department
	 * @return
	 */
	public boolean update(Department department) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(department);
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
		SQLQuery query = session.createSQLQuery("UPDATE sys_department t2 SET t2.is_delete=1 WHERE t2.id IN (SELECT t.id FROM sys_department t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid)");
		query.setParameter("id", pid);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	public boolean addClass(Long departmentId, Long reimburseClassId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("INSERT INTO WF_REIMBURSE_CLASS_DEPARTMENT(REIMBURSE_CLASS_ID, DEPARTMENT_ID) VALUES(:classId, :departmentId)");
		query.setParameter("classId", reimburseClassId);
		query.setParameter("departmentId", departmentId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	public boolean delClass(Long departmentId, Long reimburseClassId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM wf_reimburse_class_department WHERE reimburse_class_id=:classId AND department_id=:departmentId");
		query.setParameter("classId", reimburseClassId);
		query.setParameter("departmentId", departmentId);
		query.executeUpdate();
		success = true;
		return success;
	}
}
