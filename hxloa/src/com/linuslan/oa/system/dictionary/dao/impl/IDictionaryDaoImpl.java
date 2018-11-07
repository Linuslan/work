package com.linuslan.oa.system.dictionary.dao.impl;

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
import com.linuslan.oa.system.dictionary.dao.IDictionaryDao;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("dictionaryDao")
public class IDictionaryDaoImpl extends IBaseDaoImpl implements IDictionaryDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Dictionary> queryAll() {
		List<Dictionary> list = new ArrayList<Dictionary> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Dictionary g WHERE g.isDelete=0");
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
		SQLQuery query = session.createSQLQuery("SELECT t.*, (SELECT t2.text FROM sys_dictionary t2 WHERE t2.id=t.pid) as pname FROM sys_dictionary t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid");
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过父节点，只查询下级子节点，且是启用状态的
	 * @param id
	 * @return
	 */
	public List<Dictionary> queryByPid(Long id) {
		List<Dictionary> list = new ArrayList<Dictionary> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Dictionary d WHERE d.isDelete=0 AND d.isUse=0 AND d.pid=:id");
		query.setParameter("id", id);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询字典
	 * @param id
	 * @return
	 */
	public Dictionary queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Dictionary)session.get(Dictionary.class, id);
	}
	
	/**
	 * 新增字典
	 * @param dictionary
	 * @return
	 */
	public boolean add(Dictionary dictionary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(dictionary);
		success = true;
		return success;
	}
	
	/**
	 * 更新字典
	 * @param dictionary
	 * @return
	 */
	public boolean update(Dictionary dictionary) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(dictionary);
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
		SQLQuery query = session.createSQLQuery("UPDATE sys_dictionary t2 SET t2.is_delete=1 WHERE t2.id IN (SELECT t.id FROM sys_dictionary t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.pid)");
		query.setParameter("id", pid);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
}
