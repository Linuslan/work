package com.linuslan.oa.workflow.flows.saleStuff.dao.impl;

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
import com.linuslan.oa.workflow.flows.saleStuff.dao.IGlossinessDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Glossiness;

@Component("glossinessDao")
public class IGlossinessDaoImpl extends IBaseDaoImpl implements IGlossinessDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增光泽度
	 * @param glossiness
	 * @return
	 */
	public boolean add(Glossiness glossiness) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(glossiness);
		success = true;
		return success;
	}
	
	/**
	 * 更新光泽度信息
	 * @param glossiness
	 * @return
	 */
	public boolean update(Glossiness glossiness) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(glossiness);
		success = true;
		return success;
	}
	
	/**
	 * 删除光泽度，伪删除，将isDelete状态改为1
	 * @param glossiness
	 * @return
	 */
	public boolean del(Glossiness glossiness) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		glossiness.setIsDelete(1);
		session.update(glossiness);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询光泽度
	 * @param id
	 * @return
	 */
	public Glossiness queryById(Long id) {
		Glossiness glossiness = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		glossiness = (Glossiness) session.get(Glossiness.class, id);
		return glossiness;
	}
	
	/**
	 * 查询指定id的所有光泽度
	 * @param ids
	 * @return
	 */
	public List<Glossiness> queryByIds(List<Long> ids) {
		List<Glossiness> glossinesss = new ArrayList<Glossiness> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Glossiness r WHERE r.id IN (:ids) AND r.isDelete=0";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			glossinesss.addAll(query.list());
		}
		
		return glossinesss;
	}
	
	/**
	 * 查询光泽度列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Glossiness> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Glossiness> list = null;
		Page<Glossiness> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Glossiness r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Glossiness r WHERE r.isDelete=0");
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
			page = new Page<Glossiness>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的光泽度
	 * @param paramMap
	 * @return
	 */
	public List<Glossiness> queryAll() {
		List<Glossiness> glossinesss = new ArrayList<Glossiness>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Glossiness r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		glossinesss.addAll(query.list());
		
		return glossinesss;
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
