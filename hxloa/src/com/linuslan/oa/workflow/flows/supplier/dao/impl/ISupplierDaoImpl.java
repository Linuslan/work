package com.linuslan.oa.workflow.flows.supplier.dao.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.supplier.dao.ISupplierDao;
import com.linuslan.oa.workflow.flows.supplier.model.Supplier;

@Component("supplierDao")
public class ISupplierDaoImpl extends IBaseDaoImpl implements ISupplierDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增供货商
	 * @param supplier
	 * @return
	 */
	public boolean add(Supplier supplier) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(supplier);
		success = true;
		return success;
	}
	
	/**
	 * 更新供货商信息
	 * @param supplier
	 * @return
	 */
	public boolean update(Supplier supplier) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(supplier);
		success = true;
		return success;
	}
	
	/**
	 * 删除供货商，伪删除，将isDelete状态改为1
	 * @param supplier
	 * @return
	 */
	public boolean del(Supplier supplier) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		supplier.setIsDelete(1);
		session.update(supplier);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询供货商
	 * @param id
	 * @return
	 */
	public Supplier queryById(Long id) {
		Supplier supplier = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		supplier = (Supplier) session.get(Supplier.class, id);
		return supplier;
	}
	
	/**
	 * 查询指定id的所有供货商
	 * @param ids
	 * @return
	 */
	public List<Supplier> queryByIds(List<Long> ids) {
		List<Supplier> suppliers = new ArrayList<Supplier> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Supplier r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			suppliers.addAll(query.list());
		}
		
		return suppliers;
	}
	
	/**
	 * 查询供货商列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Supplier> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Supplier> list = null;
		Page<Supplier> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Supplier r");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Supplier r");
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
			page = new Page<Supplier>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的供货商
	 * @param paramMap
	 * @return
	 */
	public List<Supplier> queryAll() {
		List<Supplier> suppliers = new ArrayList<Supplier>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Supplier r");
		Query query = session.createQuery(hql.toString());
		suppliers.addAll(query.list());
		
		return suppliers;
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
