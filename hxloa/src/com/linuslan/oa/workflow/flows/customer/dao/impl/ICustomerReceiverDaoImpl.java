package com.linuslan.oa.workflow.flows.customer.dao.impl;

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
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerReceiverDao;
import com.linuslan.oa.workflow.flows.customer.model.CustomerReceiver;

@Component("customerReceiverDao")
public class ICustomerReceiverDaoImpl extends IBaseDaoImpl implements ICustomerReceiverDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增客户
	 * @param customerReceiver
	 * @return
	 */
	public boolean add(CustomerReceiver customerReceiver) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(customerReceiver);
		success = true;
		return success;
	}
	
	/**
	 * 更新客户信息
	 * @param customerReceiver
	 * @return
	 */
	public boolean update(CustomerReceiver customerReceiver) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(customerReceiver);
		success = true;
		return success;
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param customerReceiver
	 * @return
	 */
	public boolean del(CustomerReceiver customerReceiver) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customerReceiver.setIsDelete(1);
		session.update(customerReceiver);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public CustomerReceiver queryById(Long id) {
		CustomerReceiver customerReceiver = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customerReceiver = (CustomerReceiver) session.get(CustomerReceiver.class, id);
		return customerReceiver;
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<CustomerReceiver> queryByIds(List<Long> ids) {
		List<CustomerReceiver> customerReceivers = new ArrayList<CustomerReceiver> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM CustomerReceiver r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			customerReceivers.addAll(query.list());
		}
		
		return customerReceivers;
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<CustomerReceiver> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<CustomerReceiver> list = null;
		Page<CustomerReceiver> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		
		StringBuffer hql = new StringBuffer("FROM CustomerReceiver r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM CustomerReceiver r WHERE r.isDelete = 0");
		if(null != paramMap) {
			this.getHQL(paramMap, hql, countHQL);
		}
		
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = this.getQuery(paramMap, hql, countHQL, session, query, countQuery);
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
			page = new Page<CustomerReceiver>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<CustomerReceiver> queryAll() {
		List<CustomerReceiver> customerReceivers = new ArrayList<CustomerReceiver>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM CustomerReceiver r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		customerReceivers.addAll(query.list());
		
		return customerReceivers;
	}
	
	/**
	 * 查询客户对应的接收人
	 * @param customerId
	 * @return
	 */
	public List<CustomerReceiver> queryByCustomerId(Long customerId) {
		List<CustomerReceiver> list = new ArrayList<CustomerReceiver> ();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM CustomerReceiver c WHERE c.customerId=:customerId AND c.isDelete=0";
			Query query = session.createQuery(hql);
			query.setParameter("customerId", customerId);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据公司查询客户
	 * @param companyId
	 * @return
	 */
	public List<CustomerReceiver> queryByCompanyId(Long companyId) {
		List<CustomerReceiver> list = new ArrayList<CustomerReceiver> ();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM CustomerReceiver c WHERE c.companyId = :companyId";
			Query query = session.createQuery(hql);
			query.setParameter("companyId", companyId);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
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
							if("customerId".equals(key)) {
								query.setParameter(key, Long.parseLong(paramMap.get(key).trim()));
								countQuery.setParameter(key, Long.parseLong(paramMap.get(key).trim()));
							} else {
								query.setParameter(key, "%"+paramMap.get(key).trim()+"%");
								countQuery.setParameter(key, "%"+paramMap.get(key).trim()+"%");
							}
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
}
