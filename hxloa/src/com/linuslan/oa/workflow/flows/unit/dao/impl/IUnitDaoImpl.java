package com.linuslan.oa.workflow.flows.unit.dao.impl;

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
import com.linuslan.oa.workflow.flows.unit.model.Unit;
import com.linuslan.oa.workflow.flows.unit.dao.IUnitDao;

@Component("unitDao")
public class IUnitDaoImpl extends IBaseDaoImpl implements IUnitDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增单位
	 * @param unit
	 * @return
	 */
	public boolean add(Unit unit) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(unit);
		success = true;
		return success;
	}
	
	/**
	 * 更新单位信息
	 * @param unit
	 * @return
	 */
	public boolean update(Unit unit) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(unit);
		success = true;
		return success;
	}
	
	/**
	 * 删除单位，伪删除，将isDelete状态改为1
	 * @param unit
	 * @return
	 */
	public boolean del(Unit unit) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		unit.setIsDelete(1);
		session.update(unit);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询单位
	 * @param id
	 * @return
	 */
	public Unit queryById(Long id) {
		Unit unit = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		unit = (Unit) session.get(Unit.class, id);
		return unit;
	}
	
	/**
	 * 查询指定id的所有单位
	 * @param ids
	 * @return
	 */
	public List<Unit> queryByIds(List<Long> ids) {
		List<Unit> units = new ArrayList<Unit> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Unit r WHERE r.id IN (:ids)";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			units.addAll(query.list());
		}
		
		return units;
	}
	
	/**
	 * 查询单位列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Unit> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Unit> list = null;
		Page<Unit> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Unit r");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Unit r");
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
			page = new Page<Unit>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的单位
	 * @param paramMap
	 * @return
	 */
	public List<Unit> queryAll() {
		List<Unit> units = new ArrayList<Unit>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Unit r");
		Query query = session.createQuery(hql.toString());
		units.addAll(query.list());
		
		return units;
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
