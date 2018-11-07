package com.linuslan.oa.workflow.flows.saleArticle.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleArticle.dao.ISaleArticleDao;
import com.linuslan.oa.workflow.flows.saleArticle.model.Material;
import com.linuslan.oa.workflow.flows.saleArticle.model.MaterialFormat;
import com.linuslan.oa.workflow.flows.saleArticle.model.SaleArticle;

@Component("saleArticleDao")
public class ISaleArticleDaoImpl extends IBaseDaoImpl implements ISaleArticleDao {
	
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
	public Page<SaleArticle> queryPage(Map<String, String> paramMap, int page, int rows) {
		Page<SaleArticle> pageData = null;
		List<SaleArticle> list = new ArrayList<SaleArticle> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM SaleArticle b WHERE b.isDelete=0");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.orderNo ASC, b.id ASC");
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<SaleArticle> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<SaleArticle> queryAll() {
		List<SaleArticle> list = new ArrayList<SaleArticle> ();
		try {
			StringBuffer hql = new StringBuffer("FROM SaleArticle ca WHERE ca.isDelete=0 ORDER BY ca.orderNo ASC, ca.id ASC");
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
	public SaleArticle queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SaleArticle saleArticle = (SaleArticle) session.get(SaleArticle.class, id);
		return saleArticle;
	}
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<SaleArticle> querySaleArticlesByCompanyId(Long companyId) {
		List<SaleArticle> list = new ArrayList<SaleArticle> ();
		if(null != companyId) {
			String hql = "FROM SaleArticle cia WHERE cia.companyId=:companyId";
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql);
			query.setParameter("companyId", companyId);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean add(SaleArticle saleArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(saleArticle);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean update(SaleArticle saleArticle) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(saleArticle);
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
	public List<MaterialFormat> queryMaterialFormatsByArticleId(Long articleId, Map<String, String> paramMap) {
		List<MaterialFormat> list = new ArrayList<MaterialFormat> ();
		StringBuffer hql = new StringBuffer("FROM MaterialFormat b WHERE b.articleId=:articleId AND b.isDelete=0");
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
	public List<MaterialFormat> queryMaterialFormatsInIds(List<Long> ids) {
		List<MaterialFormat> contents = new ArrayList<MaterialFormat> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM MaterialFormat rc WHERE rc.id IN (:ids)";
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
	public MaterialFormat queryMaterialFormatById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		MaterialFormat materialFormat = (MaterialFormat) session.get(MaterialFormat.class, id);
		return materialFormat;
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterialFormat(MaterialFormat materialFormat) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(materialFormat);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterialFormat(MaterialFormat materialFormat) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(materialFormat);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新规格，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterialFormats(List<MaterialFormat> materialFormats) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<MaterialFormat> iter = materialFormats.iterator();
		MaterialFormat materialFormat = null;
		while(iter.hasNext()) {
			materialFormat = iter.next();
			session.merge(materialFormat);
		}
		success = true;
		return success;
	}
	
	/**
	 * 删除规格的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialFormatsNotInIds(List<Long> ids, Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE MaterialFormat rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.articleId=:id";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过id查询材质
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Material> queryMaterialsByArticleId(Long articleId, Map<String, String> paramMap) {
		List<Material> list = new ArrayList<Material> ();
		StringBuffer hql = new StringBuffer("FROM Material b WHERE b.articleId=:articleId AND b.isDelete=0");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.orderNo ASC, b.id ASC");
		query.setParameter("articleId", articleId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过id查询材质
	 * @param ids
	 * @return
	 */
	public List<Material> queryMaterialsInIds(List<Long> ids) {
		List<Material> contents = new ArrayList<Material> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Material rc WHERE rc.id IN (:ids)";
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
	public Material queryMaterialById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Material material = (Material) session.get(Material.class, id);
		return material;
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterial(Material material) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(material);
		success = true;
		return success;
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterial(Material material) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(material);
		success = true;
		return success;
	}
	
	/**
	 * 批量更新材质，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterials(List<Material> materials) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		Iterator<Material> iter = materials.iterator();
		Material material = null;
		while(iter.hasNext()) {
			material = iter.next();
			session.merge(material);
		}
		success = true;
		return success;
	}
	
	/**
	 * 删除材质的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialsNotInIds(List<Long> ids, Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Material rc SET rc.isDelete=1 WHERE rc.id NOT IN (:ids) AND rc.articleId=:id";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		query.setParameter("id", id);
		query.executeUpdate();
		success = true;
		return success;
	}
}
