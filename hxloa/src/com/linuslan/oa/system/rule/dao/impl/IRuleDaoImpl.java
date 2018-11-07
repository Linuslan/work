package com.linuslan.oa.system.rule.dao.impl;

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
import com.linuslan.oa.system.rule.model.Rule;
import com.linuslan.oa.system.rule.dao.IRuleDao;
import com.linuslan.oa.util.Page;

@Component("ruleDao")
public class IRuleDaoImpl extends IBaseDaoImpl implements IRuleDao {
	@Autowired
	private SessionFactory sessionFactory;

	public Page<Rule> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Rule> list = null;
		Page<Rule> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Rule r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Rule r WHERE r.isDelete=0");
		if (paramMap != null) {
			getHQL(paramMap, hql, countHQL);
		}
		hql.append(" ORDER BY r.orderNo ASC, r.id DESC");
		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = getQuery(paramMap, hql, countHQL, session, query,
				countQuery);
		if (queryMap.get("query") != null) {
			query = (Query) queryMap.get("query");
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (queryMap.get("countQuery") != null) {
			countQuery = (Query) queryMap.get("countQuery");
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Rule>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Rule> queryByIds(List<Long> ids) {
		List<Rule> rules = new ArrayList<Rule>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Rule c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		rules.addAll(query.list());
		return rules;
	}

	public List<Rule> queryAllRules() {
		List<Rule> rules = new ArrayList<Rule>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Rule c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		rules.addAll(query.list());
		return rules;
	}

	public Rule queryById(Long id) {
		Rule rule = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		rule = (Rule) session.get(Rule.class, id);
		return rule;
	}

	private void getHQL(Map<String, String> paramMap, StringBuffer hql,
			StringBuffer countHQL) {
		try {
			if (paramMap != null) {
				Set<String> keySet = paramMap.keySet();
				Iterator<String> iter = keySet.iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					if ((paramMap.get(key) != null)
							&& (!"".equals(((String) paramMap.get(key)).trim()))) {
						if (hql.indexOf("WHERE") > 0)
							hql.append(" AND");
						else {
							hql.append(" WHERE");
						}

						if (countHQL.indexOf("WHERE") > 0)
							countHQL.append(" AND");
						else {
							countHQL.append(" WHERE");
						}

						hql.append(" r." + key + " LIKE :" + key);
						countHQL.append(" r." + key + " LIKE :" + key);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Map<String, Query> getQuery(Map<String, String> paramMap,
			StringBuffer hql, StringBuffer countHQL, Session session,
			Query query, Query countQuery) {
		Map<String, Query> queryMap = new HashMap<String, Query>();
		try {
			if (session != null) {
				query = session.createQuery(hql.toString());
				countQuery = session.createQuery(countHQL.toString());
				if (paramMap != null) {
					Set<String> keySet = paramMap.keySet();
					Iterator<String> iter = keySet.iterator();
					while (iter.hasNext()) {
						String key = (String) iter.next();
						if ((paramMap.get(key) != null)
								&& (!"".equals(((String) paramMap.get(key))
										.trim()))) {
							query.setParameter(key,
									"%" + ((String) paramMap.get(key)).trim()
											+ "%");
							countQuery
									.setParameter(
											key,
											"%"
													+ ((String) paramMap
															.get(key)).trim()
													+ "%");
						}
					}
				}
				queryMap.put("query", query);
				queryMap.put("countQuery", countQuery);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return queryMap;
	}
	
	/**
	 * 检查value值是否唯一
	 * @param rule
	 * @return true: 唯一; false: 重复
	 */
	public boolean checkUniqueValue(Rule rule) {
		boolean isUnique = true;
		try {
			String hql = "SELECT COUNT(*) FROM Rule r WHERE r.value=:value";
			if(null != rule.getId()) {
				hql += " AND r.id <> :id";
			}
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("value", rule.getValue());
			if(null != rule.getId()) {
				query.setParameter("id", rule.getId());
			}
			long total = (Long) query.uniqueResult();
			if(total > 0) {
				isUnique = false;
			}
		} catch(Exception ex) {
			isUnique = false;
		}
		return isUnique;
	}

	public boolean add(Rule rule) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(rule);
		success = true;
		return success;
	}

	public boolean update(Rule rule) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(rule);
		success = true;
		return success;
	}

	public boolean del(Rule rule) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		rule.setIsDelete(Integer.valueOf(1));
		session.update(rule);
		success = true;
		return success;
	}
}
