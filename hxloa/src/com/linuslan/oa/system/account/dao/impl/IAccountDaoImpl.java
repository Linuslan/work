package com.linuslan.oa.system.account.dao.impl;

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
import com.linuslan.oa.system.account.dao.IAccountDao;
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("accountDao")
public class IAccountDaoImpl extends IBaseDaoImpl implements IAccountDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Page<Account> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Account> list = null;
		Page<Account> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Account r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Account r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSql = this.getHQL(Account.class, hql, paramMap, "r");
			hql.append(subSql);
			countHQL.append(subSql);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Account.class, hql.toString(), countHQL.toString(), paramMap);
		if (null != queryMap.get(ConstantVar.QUERY)) {
			query = (Query) queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (null != queryMap.get(ConstantVar.COUNT_QUERY)) {
			countQuery = (Query) queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Account>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Account> queryByUserId(Long userId) {
		List<Account> accounts = new ArrayList<Account>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Account c WHERE c.isDelete=0 AND c.userId=:userId";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		accounts.addAll(query.list());
		return accounts;
	}

	public List<Account> queryByIds(List<Long> ids) {
		List<Account> accounts = new ArrayList<Account>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Account c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		accounts.addAll(query.list());
		return accounts;
	}

	public List<Account> queryAllAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Account c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		accounts.addAll(query.list());
		return accounts;
	}
	
	/**
	 * 查询类型为type值的账号
	 * @param type
	 * @return
	 */
	public List<Account> queryByType(int type) {
		List<Account> accounts = new ArrayList<Account>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Account c WHERE c.isDelete=0 AND c.type=:type";
		Query query = session.createQuery(hql);
		query.setParameter("type", type);
		accounts.addAll(query.list());
		return accounts;
	}

	public Account queryById(Long id) {
		Account account = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		account = (Account) session.get(Account.class, id);
		return account;
	}

	/*private void getHQL(Map<String, String> paramMap, StringBuffer hql,
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

						if ("userId".equals(key)) {
							hql.append(" r." + key + " = :" + key);
							countHQL.append(" r." + key + " = :" + key);
						} else {
							hql.append(" r." + key + " LIKE :" + key);
							countHQL.append(" r." + key + " LIKE :" + key);
						}
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
							if ("userId".equals(key)) {
								query.setParameter(key, Long.parseLong(paramMap.get(key)));
								countQuery.setParameter(key, Long.parseLong(paramMap.get(key)));
							} else {
								query.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
								countQuery.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
							}
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
	}*/

	public boolean add(Account account) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(account);
		success = true;
		return success;
	}

	public boolean update(Account account) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(account);
		success = true;
		return success;
	}

	public boolean del(Account account) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		account.setIsDelete(Integer.valueOf(1));
		session.update(account);
		success = true;
		return success;
	}
}
