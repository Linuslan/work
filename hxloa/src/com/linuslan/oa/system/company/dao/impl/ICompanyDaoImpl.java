package com.linuslan.oa.system.company.dao.impl;

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
import com.linuslan.oa.system.company.dao.ICompanyDao;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("companyDao")
public class ICompanyDaoImpl extends IBaseDaoImpl implements ICompanyDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Page<Company> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Company> list = null;
		Page<Company> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Company r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Company r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSql = this.getHQL(Company.class, hql, paramMap, "r");
			hql.append(subSql);
			countHQL.append(subSql);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Company.class, hql.toString(), countHQL.toString(), paramMap);
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
			page = new Page<Company>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Company> queryByIds(List<Long> ids) {
		List<Company> companys = new ArrayList<Company>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Company c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		companys.addAll(query.list());
		return companys;
	}

	public List<Company> queryAllCompanys() {
		List<Company> companys = new ArrayList<Company>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Company c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		companys.addAll(query.list());
		return companys;
	}

	public Company queryById(Long id) {
		Company company = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		company = (Company) session.get(Company.class, id);
		return company;
	}
	
	/**
	 * 查询分配给用户的公司
	 * @param userId
	 * @return
	 */
	public List<Company> queryByUserId(Long userId) {
		List<Company> list = new ArrayList<Company> ();
		try {
			String sql = "SELECT r.* FROM sys_company r INNER JOIN sys_user_company ru ON r.id=ru.company_id WHERE ru.user_id=:userId AND r.is_delete=0";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(new BeanTransformerAdapter(Company.class));
			query.setParameter("userId", userId);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
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

	public boolean add(Company company) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(company);
		success = true;
		return success;
	}

	public boolean update(Company company) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(company);
		success = true;
		return success;
	}

	public boolean del(Company company) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		company.setIsDelete(Integer.valueOf(1));
		session.update(company);
		success = true;
		return success;
	}
}