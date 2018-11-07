package com.linuslan.oa.system.menu.dao.impl;

import java.util.ArrayList;
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
import com.linuslan.oa.system.menu.dao.IMenuIndexDao;
import com.linuslan.oa.system.menu.model.MenuIndex;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Component("menuIndeDao")
public class IMenuIndexDaoImpl extends IBaseDaoImpl implements IMenuIndexDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过菜单id分页查询首页索引
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<MenuIndex> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows) {
		Page<MenuIndex> pageData = null;
		List<MenuIndex> list = new ArrayList<MenuIndex> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM MenuIndex b WHERE b.isDelete=0 AND b.menuId=:menuId");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.menuId ASC, b.id ASC");
		query.setParameter("menuId", menuId);
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		countQuery.setParameter("menuId", menuId);
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<MenuIndex> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 通过id查询首页索引
	 * @param id
	 * @return
	 */
	public MenuIndex queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		MenuIndex menuIndex = (MenuIndex) session.get(MenuIndex.class, id);
		return menuIndex;
	}
	
	/**
	 * 新增首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean add(MenuIndex menuIndex) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(menuIndex);
		success = true;
		return success;
	}
	
	/**
	 * 更新首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean update(MenuIndex menuIndex) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(menuIndex);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE MenuIndex b SET b.isDelete=1 WHERE b.menuId=:menuId";
		Query query = session.createQuery(hql);
		query.setParameter("menuId", menuId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuIds(List<Long> menuIds) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE MenuIndex b SET b.isDelete=1 WHERE b.menuId IN (:menuIds)";
		Query query = session.createQuery(hql);
		query.setParameterList("menuIds", menuIds);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过菜单id查询首页索引
	 */
	public List<MenuIndex> queryByMenuIds(List<Long> menuIds) {
		List<MenuIndex> list = new ArrayList<MenuIndex> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM MenuIndex b WHERE b.menuId IN (:menuIds) AND b.isDelete=0 ORDER BY b.menuId ASC, b.id ASC";
		Query query = session.createQuery(hql);
		query.setParameterList("menuIds", menuIds);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 查询登录用户首页展示的需办理的消息提醒列表
	 * SELECT text, xtype, value, url, index_name, NVL(SUM(total), 0) total
  FROM (SELECT t1.*, t2.name index_name, t2.value status
          FROM sys_menu t1
          LEFT JOIN sys_menu_index t2
            ON t2.menu_id = t1.id
           AND t2.is_delete = 0
         WHERE t1.is_delete = 0
           AND t1.xtype IS NOT NULL
           AND t1.url IS NOT NULL
           AND t1.id IN (SELECT t7.resources_id
                           FROM sys_role_resources t7
                          WHERE t7.resources_type = 'menu'
                            AND t7.role_id IN
                                (SELECT t8.role_id
                                   FROM sys_role_user t8
                                  WHERE t8.user_id = 94))) t5
  LEFT JOIN (SELECT t3.wf_type, t3.status, NVL(count(*), 0) total
               FROM workflow_audit_log t3, workflow_audit_user t4
              WHERE t3.id = t4.audit_log_id
                AND t4.user_id = 94
                AND t3.auditor IN (93)
                AND t3.status > 0
                AND ((t3.is_audit = 0 AND t3.status > 2) OR t3.is_read = 0)
              GROUP BY t3.wf_type, t3.status) t6
    ON t5.status = t6.status
   AND t5.xtype = t6.wf_type
 GROUP BY text, xtype, value, url, index_name
	 * @return
	 */
	public List<Map<String, Object>> queryMenuIndex() {
		String sql = "SELECT text, xtype, value, url, index_name, NVL(SUM(total), 0) total FROM (SELECT t1.*, t2.name index_name, t2.value status " +
				"FROM sys_menu t1 LEFT JOIN sys_menu_index t2 ON t2.menu_id = t1.id AND t2.is_delete = 0 WHERE t1.is_delete = 0" +
				" AND t1.xtype IS NOT NULL AND t1.url IS NOT NULL AND t1.id IN (SELECT t7.resources_id FROM sys_role_resources t7" +
				" WHERE t7.resources_type='menu' AND t7.role_id IN (SELECT t8.role_id FROM sys_role_user t8 WHERE t8.user_id=:userId))) t5" +
				" LEFT JOIN (SELECT t3.wf_type, t3.status, NVL(count(*), 0) total FROM workflow_audit_log t3, workflow_audit_user t4" +
				" WHERE t3.id = t4.audit_log_id AND t4.user_id = :userId AND t3.auditor IN (:groups) AND t3.status > 0 AND ((t3.is_audit = 0 AND t3.status > 2) OR t3.is_read = 0)" +
				" GROUP BY t3.wf_type, t3.status) t6 ON t5.status = t6.status AND t5.xtype = t6.wf_type GROUP BY text, xtype, value, url, index_name";
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter("userId", HttpUtil.getLoginUser().getId());
		sqlQuery.setParameterList("groups", HttpUtil.getLoginUserGroupIds());
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
}
