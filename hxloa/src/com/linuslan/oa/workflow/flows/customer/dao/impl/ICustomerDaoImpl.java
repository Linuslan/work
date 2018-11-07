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
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerDao;
import com.linuslan.oa.workflow.flows.customer.model.Customer;

@Component("customerDao")
public class ICustomerDaoImpl extends IBaseDaoImpl implements ICustomerDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增客户
	 * @param customer
	 * @return
	 */
	public boolean add(Customer customer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(customer);
		success = true;
		return success;
	}
	
	/**
	 * 更新客户信息
	 * @param customer
	 * @return
	 */
	public boolean update(Customer customer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(customer);
		success = true;
		return success;
	}
	
	/**
	 * 删除客户，伪删除，将isDelete状态改为1
	 * @param customer
	 * @return
	 */
	public boolean del(Customer customer) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customer.setIsDelete(1);
		session.update(customer);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询客户
	 * @param id
	 * @return
	 */
	public Customer queryById(Long id) {
		Customer customer = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customer = (Customer) session.get(Customer.class, id);
		return customer;
	}
	
	/**
	 * 查询指定id的所有客户
	 * @param ids
	 * @return
	 */
	public List<Customer> queryByIds(List<Long> ids) {
		List<Customer> customers = new ArrayList<Customer> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Customer r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			customers.addAll(query.list());
		}
		
		return customers;
	}
	
	/**
	 * 查询客户列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Customer> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Customer> list = null;
		Page<Customer> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT t.company_id FROM sys_user_company t WHERE t.user_id=:userId";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter("userId", HttpUtil.getLoginUser().getId());
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> mapList = sqlQuery.list();
		List<Long> companyIds = new ArrayList<Long> ();
		if(null != mapList) {
			Iterator<Map<String, Object>> iter = mapList.iterator();
			Map<String, Object> map = null;
			while(iter.hasNext()) {
				map = iter.next();
				try {
					companyIds.add(Long.parseLong(map.get("COMPANY_ID").toString()));
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		StringBuffer hql = new StringBuffer("FROM Customer r WHERE r.companyId IN (:companyIds)");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Customer r WHERE r.companyId IN (:companyIds)");
		if(null != paramMap) {
			this.getHQL(paramMap, hql, countHQL);
		}
		
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = this.getQuery(paramMap, hql, countHQL, session, query, countQuery);
		if(null != queryMap.get("query")) {
			query = queryMap.get("query");
			query.setParameterList("companyIds", companyIds);
			query.setFirstResult((currentPage - 1)*limit).setMaxResults(limit);
			list = query.list();
		}
		
		long totalRecord = 0;
		long totalPage = 0;
		if(null != queryMap.get("countQuery")) {
			countQuery = queryMap.get("countQuery");
			countQuery.setParameterList("companyIds", companyIds);
			totalRecord = (Long) countQuery.uniqueResult();
			totalPage = totalRecord%limit > 0 ? totalRecord/limit + 1 : totalRecord/limit;
		}
		if(null != list) {
			page = new Page<Customer>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的客户
	 * @param paramMap
	 * @return
	 */
	public List<Customer> queryAll() {
		List<Customer> customers = new ArrayList<Customer>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Customer r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		customers.addAll(query.list());
		
		return customers;
	}
	
	/**
	 * 查询登录用户分配的公司对应的客户
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public List<Customer> queryByUser(Long userId) {
		List<Customer> list = new ArrayList<Customer> ();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String sql = "SELECT t.company_id FROM sys_user_company t WHERE t.user_id=:userId";
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter("userId", userId);
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<Map<String, Object>> mapList = sqlQuery.list();
			List<Long> companyIds = new ArrayList<Long> ();
			if(null != mapList) {
				Iterator<Map<String, Object>> iter = mapList.iterator();
				Map<String, Object> map = null;
				while(iter.hasNext()) {
					map = iter.next();
					try {
						companyIds.add(Long.parseLong(map.get("COMPANY_ID").toString()));
					} catch(Exception ex) {
						
					}
				}
			}
			if(companyIds.size() > 0) {
				String hql = "FROM Customer c WHERE c.companyId IN (:companyIds)";
				Query query = session.createQuery(hql);
				query.setParameterList("companyIds", companyIds);
				list.addAll(query.list());
			}
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
	public List<Customer> queryByCompanyId(Long companyId) {
		List<Customer> list = new ArrayList<Customer> ();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM Customer c WHERE c.companyId = :companyId";
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
}
