package com.linuslan.oa.workflow.flows.warehouse.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.warehouse.model.Warehouse;
import com.linuslan.oa.workflow.flows.warehouse.dao.IWarehouseDao;

@Component("warehouseDao")
public class IWarehouseDaoImpl extends IBaseDaoImpl implements IWarehouseDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增仓库
	 * @param warehouse
	 * @return
	 */
	public boolean add(Warehouse warehouse) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(warehouse);
		success = true;
		return success;
	}
	
	/**
	 * 更新仓库信息
	 * @param warehouse
	 * @return
	 */
	public boolean update(Warehouse warehouse) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(warehouse);
		success = true;
		return success;
	}
	
	/**
	 * 删除仓库，伪删除，将isDelete状态改为1
	 * @param warehouse
	 * @return
	 */
	public boolean del(Warehouse warehouse) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		warehouse.setIsDelete(1);
		session.update(warehouse);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询仓库
	 * @param id
	 * @return
	 */
	public Warehouse queryById(Long id) {
		Warehouse warehouse = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		warehouse = (Warehouse) session.get(Warehouse.class, id);
		return warehouse;
	}
	
	/**
	 * 查询指定id的所有仓库
	 * @param ids
	 * @return
	 */
	public List<Warehouse> queryByIds(List<Long> ids) {
		List<Warehouse> warehouses = new ArrayList<Warehouse> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Warehouse r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			warehouses.addAll(query.list());
		}
		
		return warehouses;
	}
	
	/**
	 * 查询仓库列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Warehouse> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Warehouse> list = null;
		Page<Warehouse> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Warehouse r");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Warehouse r");
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
			page = new Page<Warehouse>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的仓库
	 * @param paramMap
	 * @return
	 */
	public List<Warehouse> queryAll() {
		List<Warehouse> warehouses = new ArrayList<Warehouse>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Warehouse r");
		Query query = session.createQuery(hql.toString());
		warehouses.addAll(query.list());
		
		return warehouses;
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
