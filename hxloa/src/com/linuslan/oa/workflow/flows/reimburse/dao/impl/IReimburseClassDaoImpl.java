package com.linuslan.oa.workflow.flows.reimburse.dao.impl;

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
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.reimburse.dao.IReimburseClassDao;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;

@Component("reimburseClassDao")
public class IReimburseClassDaoImpl extends IBaseDaoImpl implements
		IReimburseClassDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public Page<ReimburseClass> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<ReimburseClass> list = null;
		Page<ReimburseClass> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM ReimburseClass r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM ReimburseClass r WHERE r.isDelete=0");
		if (paramMap != null) {
			getHQL(paramMap, hql, countHQL);
		}

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
			page = new Page<ReimburseClass>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}
	
	/**
	 * sql查询报销类别页面
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Map<String, Object>> queryPageBySql(Map<String, String> paramMap, int currentPage, int limit) {
		String sql = "SELECT sd.id department_id, sd.name department_name, rc.* FROM wf_reimburse_class rc LEFT JOIN wf_reimburse_class_department rcd ON rcd.reimburse_class_id=rc.id LEFT JOIN sys_department sd ON sd.id=rcd.department_id";
		sql = "SELECT * FROM ("+sql+") WHERE is_delete=0";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Page<Map<String, Object>> page = null;
		long totalRecord = 0;
		long totalPage = 0;
		try {
			if(null != paramMap) {
				if(null != paramMap.get("departmentId")
						&& !"".equals(paramMap.get("departmentId").trim())) {
					sql += " AND department_id=:departmentId";
				}
			}
			String countSQL = "SELECT COUNT(*) FROM ("+sql+")";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			SQLQuery countQuery = session.createSQLQuery(countSQL);
			if(null != paramMap) {
				if(null != paramMap.get("departmentId")
						&& !"".equals(paramMap.get("departmentId").trim())) {
					query.setParameter("departmentId", Long.parseLong(paramMap.get("departmentId")));
					countQuery.setParameter("departmentId", Long.parseLong(paramMap.get("departmentId")));
				}
			}
			totalRecord = Long.parseLong(countQuery.uniqueResult().toString());
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			page = new Page<Map<String, Object>> (list, totalRecord, totalPage, currentPage);
			
		}
		return page;
	}

	public List<ReimburseClass> queryByIds(List<Long> ids) {
		List<ReimburseClass> reimburseClasss = new ArrayList<ReimburseClass>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM ReimburseClass c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		reimburseClasss.addAll(query.list());
		return reimburseClasss;
	}

	public List<ReimburseClass> queryAllReimburseClasss() {
		List<ReimburseClass> reimburseClasss = new ArrayList<ReimburseClass>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM ReimburseClass c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		reimburseClasss.addAll(query.list());
		return reimburseClasss;
	}

	public ReimburseClass queryById(Long id) {
		ReimburseClass reimburseClass = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		reimburseClass = (ReimburseClass) session.get(ReimburseClass.class, id);
		return reimburseClass;
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

	public boolean add(ReimburseClass reimburseClass) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(reimburseClass);
		success = true;
		return success;
	}

	public boolean update(ReimburseClass reimburseClass) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(reimburseClass);
		success = true;
		return success;
	}

	public boolean del(ReimburseClass reimburseClass) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		reimburseClass.setIsDelete(Integer.valueOf(1));
		session.update(reimburseClass);
		success = true;
		return success;
	}
}
