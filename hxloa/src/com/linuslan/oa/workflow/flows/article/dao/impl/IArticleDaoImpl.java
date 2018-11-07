package com.linuslan.oa.workflow.flows.article.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.dao.IArticleDao;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;

@Component("articleDao")
public class IArticleDaoImpl extends IBaseDaoImpl implements IArticleDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckinArticle> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<CheckinArticle> pageData = null;
		List<CheckinArticle> list = new ArrayList<CheckinArticle> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM CheckinArticle b WHERE b.isDelete=0");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.orderNo ASC, b.id ASC");
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<CheckinArticle> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<CheckinArticle> queryAll() {
		List<CheckinArticle> list = new ArrayList<CheckinArticle> ();
		try {
			StringBuffer hql = new StringBuffer("FROM CheckinArticle ca WHERE ca.isDelete=0 ORDER BY ca.orderNo ASC, ca.id ASC");
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckinArticle queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		CheckinArticle checkinArticle = (CheckinArticle) session.get(CheckinArticle.class, id);
		return checkinArticle;
	}
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<CheckinArticle> queryCheckinArticlesByCompanyId(Long companyId) {
		List<CheckinArticle> list = new ArrayList<CheckinArticle> ();
		if(null != companyId) {
			String hql = "FROM CheckinArticle cia WHERE cia.companyId=:companyId";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("companyId", companyId);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean add(CheckinArticle checkinArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(checkinArticle);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean update(CheckinArticle checkinArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(checkinArticle);
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询规格
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Format> queryFormatsByArticleId(Long articleId, Map<String, String> paramMap) {
		List<Format> list = new ArrayList<Format> ();
		StringBuffer hql = new StringBuffer("FROM Format b WHERE b.articleId=:articleId AND b.isDelete=0");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.orderNo ASC, b.id ASC");
		query.setParameter("articleId", articleId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询规格
	 * @param ids
	 * @return
	 */
	public List<Format> queryFormatsInIds(List<Long> ids) {
		List<Format> contents = new ArrayList<Format> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Format rc WHERE rc.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		contents.addAll(query.list());
		return contents;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public Format queryFormatById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Format format = (Format) session.get(Format.class, id);
		return format;
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addFormat(Format format) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(format);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateFormat(Format format) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(format);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新规格，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeFormats(List<Format> formats) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<Format> iter = formats.iterator();
		Format format = null;
		while(iter.hasNext()) {
			format = iter.next();
			session.merge(format);
		}
		success = true;
		return success;
	}
	
	/**
	 * 删除规格的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delFormatsNotInIds(List<Long> ids, Long articleId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Format rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.articleId=:articleId";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("articleId", articleId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 分页查询出库商品列表
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckoutArticle> queryCheckoutArticlePage(Map<String, String> paramMap, int page, int rows) {
		Page<CheckoutArticle> pageData = null;
		List<CheckoutArticle> list = new ArrayList<CheckoutArticle> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM CheckoutArticle b WHERE b.isDelete=0");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.orderNo ASC, b.id ASC");
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<CheckoutArticle> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 查询所有的出库商品
	 * @return
	 */
	public List<CheckoutArticle> queryAllCheckoutArticle() {
		List<CheckoutArticle> list = new ArrayList<CheckoutArticle> ();
		try {
			StringBuffer hql = new StringBuffer("FROM CheckoutArticle ca WHERE ca.isDelete=0 ORDER BY ca.orderNo ASC, ca.id ASC");
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询出库产品列表
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public List<CheckoutArticle> queryCheckoutArticles(Long companyId, Long customerId) {
		List<CheckoutArticle> list = new ArrayList<CheckoutArticle> ();
		try {
			StringBuffer sql = new StringBuffer("SELECT wca.id FROM wf_checkin_article wca WHERE wca.is_delete=0 AND wca.company_id=:companyId");
			StringBuffer hql = new StringBuffer("FROM CheckoutArticle ca WHERE ca.isDelete=0 AND ca.customerId=:customerId AND ca.checkinArticleId IN(:checkinArticleIds) ORDER BY ca.orderNo ASC, ca.id ASC");
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
			sqlQuery.setParameter("companyId", companyId);
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<Map<String, Object>> mapList = sqlQuery.list();
			List<Long> checkinArticleIds = new ArrayList<Long> ();
			if(null != mapList) {
				Iterator<Map<String, Object>> iter = mapList.iterator();
				Map<String, Object> map = null;
				while(iter.hasNext()) {
					map = iter.next();
					try {
						checkinArticleIds.add(Long.parseLong(map.get("ID").toString()));
					} catch(Exception ex) {
						
					}
				}
			}
			Query query = session.createQuery(hql.toString());
			query.setParameterList("checkinArticleIds", checkinArticleIds);
			query.setParameter("customerId", customerId);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckoutArticle queryCheckoutArticleById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		CheckoutArticle checkoutArticle = (CheckoutArticle) session.get(CheckoutArticle.class, id);
		return checkoutArticle;
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addCheckoutArticle(CheckoutArticle CheckoutArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(CheckoutArticle);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateCheckoutArticle(CheckoutArticle checkoutArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(checkoutArticle);
		success = true;
		return success;
	}
}
