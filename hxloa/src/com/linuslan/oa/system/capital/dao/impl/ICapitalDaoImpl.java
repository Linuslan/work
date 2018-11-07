package com.linuslan.oa.system.capital.dao.impl;

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
import com.linuslan.oa.system.capital.dao.ICapitalDao;
import com.linuslan.oa.system.capital.model.Capital;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("capitalDao")
public class ICapitalDaoImpl extends IBaseDaoImpl implements ICapitalDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 分页查询固资
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Capital> queryPage(Map<String, String> paramMap,int year, int month, int currentPage, int limit) {
		Page<Capital> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			String where = " WHERE c.year=:year AND c.month=:month";
			StringBuffer hql = new StringBuffer("FROM Capital c");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Capital c");
			hql.append(where);
			countHQL.append(where);
			if(null != paramMap) {
				String subHQL = this.getHQL(Capital.class, hql, paramMap, "c");
				hql.append(subHQL);
				countHQL.append(subHQL);
			}
			hql.append(" ORDER BY c.id DESC");
			Map<String, Query> queryMap = this.buildQuery(session, Capital.class, hql.toString(), countHQL.toString(), paramMap);
			Query query = queryMap.get(ConstantVar.QUERY);
			Query countQuery = queryMap.get(ConstantVar.COUNT_QUERY);
			query.setParameter("year", year);
			query.setParameter("month", month);
			countQuery.setParameter("year", year);
			countQuery.setParameter("month", month);
			query.setFirstResult((currentPage - 1) * limit)
				.setMaxResults(limit);
			try {
				query.list();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			long totalRecord = (Long) countQuery.uniqueResult();
			long totalPage = totalRecord % limit > 0 ? totalRecord / limit + 1 : totalRecord / limit;
			page = new Page<Capital> (query.list(), totalRecord, totalPage, currentPage);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return page;
	}
	
	
	/**
	 * 不分页查询固资
	 * @param paramMap
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Capital> queryCapitals(Map<String, String> paramMap,int year, int month) {
		List<Capital> list = new ArrayList<Capital> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			String where = " WHERE c.year=:year AND c.month=:month";
			StringBuffer hql = new StringBuffer("FROM Capital c");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Capital c");
			hql.append(where);
			countHQL.append(where);
			
			String param = this.getParamHQL(paramMap);
			hql.append(param);
			countHQL.append(param);
			hql.append(" ORDER BY c.id DESC");
			Map<String, Query> queryMap = this.getQueryMap(paramMap, session, hql, countHQL);
			Query query = queryMap.get("query");
			Query countQuery = queryMap.get("countQuery");
			query.setParameter("year", year);
			query.setParameter("month", month);
			countQuery.setParameter("year", year);
			countQuery.setParameter("month", month);
			try {
				list.addAll(query.list());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 得到查询条件Map里的查询条件拼凑后的hql
	 * @param paramMap
	 * @return
	 */
	public String getParamHQL(Map<String, String> paramMap) {
		StringBuffer paramHQL = new StringBuffer();
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
					if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
						if(key.equals("serial") || key.equals("name") || key.equals("model")) {
							paramHQL.append(" AND c."+key+" LIKE :"+key);
						} else {
							paramHQL.append(" AND c."+key+"=:"+key);
						}
					}
				}
			}
		}
		return paramHQL.toString();
	}
	
	public String getParamHQLWithWhere(Map<String, String> paramMap) {
		StringBuffer paramHQL = new StringBuffer();
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
					if(paramHQL.indexOf("WHERE") > 0) {
						paramHQL.append(" AND");
					} else {
						paramHQL.append(" WHERE");
					}
					paramHQL.append(" c."+key+"=:"+key);
				}
			}
		}
		return paramHQL.toString();
	}
	
	/**
	 * 将存放在map里的查询条件的值设置到hql中
	 * @param paramMap
	 * @param session
	 * @param hql
	 * @param countHQL
	 * @return
	 */
	public Map<String, Query> getQueryMap(Map<String, String> paramMap, Session session, StringBuffer hql, StringBuffer countHQL) {
		Map<String, Query> queryMap = new HashMap<String, Query>();
		Query query = session.createQuery(hql.toString());
		Query countQuery = session.createQuery(countHQL.toString());
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).toString().trim())) {
					if(key.equals("state") || key.equals("userId")) {
						try {
							query.setParameter(key, Long.parseLong(paramMap.get(key)));
							countQuery.setParameter(key, Long.parseLong(paramMap.get(key)));
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					} else if(key.equals("serial") || key.equals("name") || key.equals("model")) {
						query.setParameter(key, "%"+paramMap.get(key)+"%");
						countQuery.setParameter(key, "%"+paramMap.get(key)+"%");
					} else {
						query.setParameter(key, paramMap.get(key));
						countQuery.setParameter(key, paramMap.get(key));
					}
				}
			}
		}
		queryMap.put("query", query);
		queryMap.put("countQuery", countQuery);
		
		return queryMap;
	}
	
	/**
	 * 查询类别
	 * @return
	 */
	public List<Map<String, Object>> queryClassName() {
		List<Map<String, Object>> classNameList = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.class_name id, c.class_name text FROM sys_capital c GROUP BY c.class_name ORDER BY c.class_name");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			classNameList.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return classNameList;
	}
	
	/**
	 * 查询厂家
	 * @return
	 */
	public List<Map<String, Object>> queryShopName() {
		List<Map<String, Object>> classNameList = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.shop_name id, c.shop_name text FROM sys_capital c GROUP BY c.shop_name ORDER BY c.shop_name");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			classNameList.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return classNameList;
	}
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryAddress() {
		List<Map<String, Object>> classNameList = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.address id, c.address text FROM sys_capital c GROUP BY c.address ORDER BY c.address");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			classNameList.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return classNameList;
	}
	
	/**
	 * 查询存放地
	 * @return
	 */
	public List<Map<String, Object>> queryUser() {
		List<Map<String, Object>> classNameList = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.borrow_user id, c.borrow_user text FROM sys_capital c GROUP BY c.borrow_user ORDER BY c.borrow_user");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			classNameList.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return classNameList;
	}
	
	public List<Map<String, Object>> queryDepartment() {
		List<Map<String, Object>> departments = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.department id, c.department text FROM sys_capital c GROUP BY c.department ORDER BY c.department");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			departments.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return departments;
	}
	
	public List<Map<String, Object>> queryBorrowDepartment() {
		List<Map<String, Object>> departments = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT c.borrow_department id, c.borrow_department text FROM sys_capital c GROUP BY c.borrow_department ORDER BY c.borrow_department");
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			departments.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return departments;
	}
	
	/**
	 * 通过id查询固资
	 * @param id
	 * @return
	 */
	public Capital queryById(Long id) {
		Capital capital = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			capital = (Capital) session.get(Capital.class, id);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		if(null == capital) {
			capital = new Capital();
		}
		return capital;
	}
	
	/**
	 * 新增固资
	 * @param pettyCash
	 * @return
	 */
	public Capital add(Capital capital) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			session.save(capital);
		} catch(Exception ex) {
			ex.printStackTrace();
			capital = null;
		}
		return capital;
	}
	
	/**
	 * 更新固资
	 * @param pettyCash
	 * @return
	 */
	public Capital update(Capital capital) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Capital persist = (Capital) session.get(Capital.class, capital.getId());
			capital.setMonth(persist.getMonth());
			capital.setYear(persist.getYear());
			persist = (Capital) BeanUtil.updateBean(persist, capital);
			session.merge(persist);
		} catch(Exception ex) {
			ex.printStackTrace();
			capital = null;
		}
		return capital;
	}
	
	/**
	 * 通过id删除固资
	 * @param id
	 * @return
	 */
	public boolean delById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Capital capital = (Capital) session.get(Capital.class, id);
			session.delete(capital);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
}
