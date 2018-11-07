package com.linuslan.oa.system.cellphone.dao.impl;

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
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.system.cellphone.dao.ICellphoneDao;
import com.linuslan.oa.system.cellphone.model.Cellphone;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("cellphoneDao")
public class ICellphoneDaoImpl extends IBaseDaoImpl implements ICellphoneDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Page<Cellphone> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Cellphone> list = null;
		Page<Cellphone> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Cellphone r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Cellphone r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSql = this.getHQL(Cellphone.class, hql, paramMap, "r");
			hql.append(subSql);
			countHQL.append(subSql);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Cellphone.class, hql.toString(), countHQL.toString(), paramMap);
		if (queryMap.get(ConstantVar.QUERY) != null) {
			query = (Query) queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (queryMap.get(ConstantVar.COUNT_QUERY) != null) {
			countQuery = (Query) queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Cellphone>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Cellphone> queryByIds(List<Long> ids) {
		List<Cellphone> cellphones = new ArrayList<Cellphone>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Cellphone c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		cellphones.addAll(query.list());
		return cellphones;
	}

	public List<Cellphone> queryAllCellphones() {
		List<Cellphone> cellphones = new ArrayList<Cellphone>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Cellphone c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		cellphones.addAll(query.list());
		return cellphones;
	}

	public Cellphone queryById(Long id) {
		Cellphone cellphone = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		cellphone = (Cellphone) session.get(Cellphone.class, id);
		return cellphone;
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

	public boolean add(Cellphone cellphone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(cellphone);
		success = true;
		return success;
	}

	public boolean update(Cellphone cellphone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(cellphone);
		success = true;
		return success;
	}

	public boolean del(Cellphone cellphone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		cellphone.setIsDelete(Integer.valueOf(1));
		session.update(cellphone);
		success = true;
		return success;
	}
	
}
