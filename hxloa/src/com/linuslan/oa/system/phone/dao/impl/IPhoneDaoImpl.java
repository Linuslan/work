package com.linuslan.oa.system.phone.dao.impl;

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
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.phone.model.Phone;
import com.linuslan.oa.system.phone.dao.IPhoneDao;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("phoneDao")
public class IPhoneDaoImpl extends IBaseDaoImpl implements IPhoneDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Page<Phone> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Phone> list = null;
		Page<Phone> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Phone r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Phone r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSQL = this.getHQL(Phone.class, hql, paramMap, "r");
			hql.append(subSQL);
			countHQL.append(subSQL);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Phone.class, hql.toString(), countHQL.toString(), paramMap);
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
			page = new Page<Phone>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Phone> queryByIds(List<Long> ids) {
		List<Phone> phones = new ArrayList<Phone>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Phone c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		phones.addAll(query.list());
		return phones;
	}

	public List<Phone> queryAllPhones() {
		List<Phone> phones = new ArrayList<Phone>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Phone c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		phones.addAll(query.list());
		return phones;
	}

	public Phone queryById(Long id) {
		Phone phone = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		phone = (Phone) session.get(Phone.class, id);
		return phone;
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

	public boolean add(Phone phone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(phone);
		success = true;
		return success;
	}

	public boolean update(Phone phone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(phone);
		success = true;
		return success;
	}

	public boolean del(Phone phone) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		phone.setIsDelete(Integer.valueOf(1));
		session.update(phone);
		success = true;
		return success;
	}
	
}
