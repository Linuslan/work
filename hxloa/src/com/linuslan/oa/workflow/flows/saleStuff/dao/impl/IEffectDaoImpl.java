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
import com.linuslan.oa.workflow.flows.saleStuff.dao.IEffectDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Effect;

@Component("effectDao")
public class IEffectDaoImpl extends IBaseDaoImpl implements IEffectDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增效果
	 * @param effect
	 * @return
	 */
	public boolean add(Effect effect) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(effect);
		success = true;
		return success;
	}
	
	/**
	 * 更新效果信息
	 * @param effect
	 * @return
	 */
	public boolean update(Effect effect) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(effect);
		success = true;
		return success;
	}
	
	/**
	 * 删除效果，伪删除，将isDelete状态改为1
	 * @param effect
	 * @return
	 */
	public boolean del(Effect effect) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		effect.setIsDelete(1);
		session.update(effect);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询效果
	 * @param id
	 * @return
	 */
	public Effect queryById(Long id) {
		Effect effect = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		effect = (Effect) session.get(Effect.class, id);
		return effect;
	}
	
	/**
	 * 查询指定id的所有效果
	 * @param ids
	 * @return
	 */
	public List<Effect> queryByIds(List<Long> ids) {
		List<Effect> effects = new ArrayList<Effect> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Effect r WHERE r.id IN (:ids) AND r.isDelete=0";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			effects.addAll(query.list());
		}
		
		return effects;
	}
	
	/**
	 * 查询效果列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Effect> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Effect> list = null;
		Page<Effect> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Effect r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Effect r WHERE r.isDelete=0");
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
			page = new Page<Effect>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的效果
	 * @param paramMap
	 * @return
	 */
	public List<Effect> queryAll() {
		List<Effect> effects = new ArrayList<Effect>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Effect r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		effects.addAll(query.list());
		
		return effects;
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
