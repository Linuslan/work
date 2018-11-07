package com.linuslan.oa.system.certificate.dao.impl;

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
import com.linuslan.oa.system.cellphone.model.Cellphone;
import com.linuslan.oa.system.certificate.dao.ICertificateDao;
import com.linuslan.oa.system.certificate.model.Certificate;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("certificateDao")
public class ICertificateDaoImpl extends IBaseDaoImpl implements
		ICertificateDao {
	@Autowired
	private SessionFactory sessionFactory;

	public Page<Certificate> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Certificate> list = null;
		Page<Certificate> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Certificate r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Certificate r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSql = this.getHQL(Certificate.class, hql, paramMap, "r");
			if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateStart"))) {
				subSql += " AND r.inspectionDate >= TO_DATE(:inspectionDateStart, 'yyyy-mm-dd')";
			}
			if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateEnd"))) {
				subSql += " AND r.inspectionDate <= TO_DATE(:inspectionDateEnd, 'yyyy-mm-dd')";
			}
			hql.append(subSql);
			countHQL.append(subSql);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Certificate.class, hql.toString(), countHQL.toString(), paramMap);
		if (queryMap.get(ConstantVar.QUERY) != null) {
			query = (Query) queryMap.get(ConstantVar.QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateStart"))) {
					query.setParameter("inspectionDateStart", paramMap.get("inspectionDateStart"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateEnd"))) {
					query.setParameter("inspectionDateEnd", paramMap.get("inspectionDateEnd"));
				}
			}
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (queryMap.get(ConstantVar.COUNT_QUERY) != null) {
			countQuery = (Query) queryMap.get(ConstantVar.COUNT_QUERY);
			if(null != paramMap) {
				if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateStart"))) {
					countQuery.setParameter("inspectionDateStart", paramMap.get("inspectionDateStart"));
				}
				if(CodeUtil.isNotEmpty(paramMap.get("inspectionDateEnd"))) {
					countQuery.setParameter("inspectionDateEnd", paramMap.get("inspectionDateEnd"));
				}
			}
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Certificate>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<Certificate> queryByIds(List<Long> ids) {
		List<Certificate> certificates = new ArrayList<Certificate>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Certificate c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		certificates.addAll(query.list());
		return certificates;
	}

	public List<Certificate> queryAllCertificates() {
		List<Certificate> certificates = new ArrayList<Certificate>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Certificate c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		certificates.addAll(query.list());
		return certificates;
	}

	public Certificate queryById(Long id) {
		Certificate certificate = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		certificate = (Certificate) session.get(Certificate.class, id);
		return certificate;
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

	public boolean add(Certificate certificate) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(certificate);
		success = true;
		return success;
	}

	public boolean update(Certificate certificate) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(certificate);
		success = true;
		return success;
	}

	public boolean del(Certificate certificate) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		certificate.setIsDelete(Integer.valueOf(1));
		session.update(certificate);
		success = true;
		return success;
	}
}
