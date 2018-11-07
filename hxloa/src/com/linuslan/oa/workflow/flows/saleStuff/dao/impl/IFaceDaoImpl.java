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
import com.linuslan.oa.workflow.flows.saleStuff.dao.IFaceDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Face;

@Component("faceDao")
public class IFaceDaoImpl extends IBaseDaoImpl implements IFaceDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 新增面数
	 * @param face
	 * @return
	 */
	public boolean add(Face face) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(face);
		success = true;
		return success;
	}
	
	/**
	 * 更新面数信息
	 * @param face
	 * @return
	 */
	public boolean update(Face face) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(face);
		success = true;
		return success;
	}
	
	/**
	 * 删除面数，伪删除，将isDelete状态改为1
	 * @param face
	 * @return
	 */
	public boolean del(Face face) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		face.setIsDelete(1);
		session.update(face);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询面数
	 * @param id
	 * @return
	 */
	public Face queryById(Long id) {
		Face face = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		face = (Face) session.get(Face.class, id);
		return face;
	}
	
	/**
	 * 查询指定id的所有面数
	 * @param ids
	 * @return
	 */
	public List<Face> queryByIds(List<Long> ids) {
		List<Face> faces = new ArrayList<Face> ();
		Session session = null;
		if(null != ids && 0 < ids.size()) {
			String hql = "FROM Face r WHERE r.id IN (:ids) AND r.isDelete=0";
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setParameterList("ids", ids);
			faces.addAll(query.list());
		}
		
		return faces;
	}
	
	/**
	 * 查询面数列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Face> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		List<Face> list = null;
		Page<Face> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Face r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM Face r WHERE r.isDelete=0");
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
			page = new Page<Face>(list, totalRecord, totalPage, currentPage);
		}
		
		return page;
	}
	
	/**
	 * 查询所有的面数
	 * @param paramMap
	 * @return
	 */
	public List<Face> queryAll() {
		List<Face> faces = new ArrayList<Face>();
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Face r WHERE r.isDelete=0");
		Query query = session.createQuery(hql.toString());
		faces.addAll(query.list());
		
		return faces;
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
