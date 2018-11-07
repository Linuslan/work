package com.linuslan.oa.workflow.flows.customer.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ISpecialCustomerDao;
import com.linuslan.oa.workflow.flows.customer.model.SpecialCustomer;

@Component("specialCustomerDao")
public class ISpecialCustomerDaoImpl extends IBaseDaoImpl implements
		ISpecialCustomerDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增客户
	 * @param specialCustomer
	 * @return
	 */
	public boolean add(SpecialCustomer specialCustomer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(specialCustomer);
		success = true;
		return success;
	}
	
	/**
	 * 更新客户信息
	 * @param specialCustomer
	 * @return
	 */
	public boolean update(SpecialCustomer specialCustomer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(specialCustomer);
		success = true;
		return success;
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param specialCustomer
	 * @return
	 */
	public boolean del(SpecialCustomer specialCustomer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		specialCustomer.setIsDelete(1);
		session.update(specialCustomer);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public SpecialCustomer queryById(Long id) {
		SpecialCustomer specialCustomer = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		specialCustomer = (SpecialCustomer) session.get(SpecialCustomer.class, id);
		return specialCustomer;
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<SpecialCustomer> queryByIds(List<Long> ids) {
		List<SpecialCustomer> specialCustomers = new ArrayList<SpecialCustomer> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM SpecialCustomer r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			specialCustomers.addAll(query.list());
		}
		
		return specialCustomers;
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<SpecialCustomer> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<SpecialCustomer> list = null;
		Page<SpecialCustomer> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM SpecialCustomer r");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM SpecialCustomer r");
		if(null != paramMap) {
			this.getHQL(SpecialCustomer.class, hql, paramMap, "r");
		}
		
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = this.buildQuery(session, SpecialCustomer.class, hql.toString(), countHQL.toString(), paramMap);
		if(null != queryMap.get("query")) {
			query = queryMap.get("query");
			query.setFirstResult((currentPage - 1)*limit).setMaxResults(limit);
			list = query.list();
		}
		
		long totalRecord = 0;
		long totalPage = 0;
		if(null != queryMap.get("countQuery")) {
			countQuery = queryMap.get("countQuery");
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = totalRecord%limit > 0 ? totalRecord/limit + 1 : totalRecord/limit;
		}
		if(null != list) {
			page = new Page<SpecialCustomer>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<SpecialCustomer> queryAll() {
		List<SpecialCustomer> specialCustomers = new ArrayList<SpecialCustomer>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM SpecialCustomer r");
		Query query = session.createQuery(hql.toString());
		specialCustomers.addAll(query.list());
		
		return specialCustomers;
	}
}
