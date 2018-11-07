package com.linuslan.oa.system.menu.dao.impl;

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
import com.linuslan.oa.system.menu.dao.IMenuDao;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("menuDao")
public class IMenuDaoImpl extends IBaseDaoImpl implements IMenuDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public boolean add(Menu menu) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(menu);
		success = true;
		return success;
	}
	
	public boolean update(Menu menu) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.merge(menu);
		success = true;
		return success;
	}
	
	
	public boolean batchDel(String ids) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("UPDATE Menu m SET m.isDelete = 1 WHERE m.id IN ("+ids+")");
		StringBuffer buttonHQL = new StringBuffer("UPDATE Button b SET b.isDelete=1 WHERE b.menuId IN ("+ids+")");
		Query query = session.createQuery(hql.toString());
		Query btnQuery = session.createQuery(buttonHQL.toString());
		query.executeUpdate();
		btnQuery.executeUpdate();
		success = true;
		
		return success;
	}
	
	public List<Menu> queryAll() {
		List<Menu> list = new ArrayList<Menu>();
		Session session = null;
		String hql = "FROM Menu m WHERE m.isDelete=0";
		session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		list.addAll(query.list());
		return list;
	}
	
	public Menu queryById(Long id) {
		Menu menu = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		menu = (Menu) session.get(Menu.class, id);
		return menu;
	}
	
	public boolean isMultipleValue(Menu menu) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		String hql = "SELECT COUNT(*) FROM Menu m WHERE m.value='"+menu.getValue()+"'";
		if(null != menu.getId()) {
			hql += " AND m.id!="+menu.getId();
		}
		Query query = session.createQuery(hql);
		long count = (Long) query.uniqueResult();
		if(count == 0) {
			success = true;
		}
		
		return success;
	}
	
	public List<Menu> queryMenuListByIds(List<Long> idList) {
		Session session = null;
		List<Menu> list = new ArrayList<Menu> ();
		StringBuffer hql = new StringBuffer("FROM Menu m WHERE m.id IN (:ids)");
		if(null != idList && 0 < idList.size()) {
			session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameterList("ids", idList);
			list.addAll(query.list());
		}
		return list;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("UPDATE sys_menu t2 SET t2.is_delete=1 WHERE t2.id IN (SELECT t.id FROM sys_menu t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.parent_id)");
		query.setParameter("id", pid);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("UPDATE sys_menu t2 SET t2.is_delete=1 WHERE t2.id IN (:ids)");
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param pid
	 * @return
	 */
	public List<Menu> queryByPid(Long pid) {
		List<Menu> menus = new ArrayList<Menu> ();
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT t2.* FROM sys_menu t2 WHERE t2.is_delete=0 AND t2.id IN (SELECT t.id FROM sys_menu t WHERE t.is_delete=0 START WITH t.id=:id CONNECT BY PRIOR t.id=t.parent_id)");
		query.setParameter("id", pid);
		query.setResultTransformer(new BeanTransformerAdapter(Menu.class));
		menus.addAll(query.list());
		return menus;
	}
	
	/**
	 * 查询登录用户拥有的首页显示的菜单的所有子菜单
	 * @param ids
	 * @return
	 */
	public List<Menu> queryByPid() {
		List<Menu> menus = new ArrayList<Menu> ();
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM (SELECT t.id, t.text, t.value, t.order_num orderNo, t.url, t.img_url icon, t.parent_id pid, t.xtype, t.is_delete isDelete, t.content FROM sys_menu t WHERE t.is_delete=0 START WITH t.id IN (SELECT t.id FROM sys_menu t WHERE t.xtype IS NOT NULL AND t.url IS NULL) CONNECT BY PRIOR t.id=t.parent_id) t WHERE t.id IN (SELECT t7.resources_id FROM sys_role_resources t7 WHERE t7.resources_type='menu' AND t7.role_id IN (SELECT t8.role_id FROM sys_role_user t8 WHERE t8.user_id=:userId))");
		query.setParameter("userId", HttpUtil.getLoginUser().getId());
		query.setResultTransformer(new BeanTransformerAdapter(Menu.class));
		menus.addAll(query.list());
		return menus;
	}
	
	/**
	 * 通过菜单id集合，删除角色资源里面的菜单资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByMenuIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_resources t WHERE t.resources_type='menu' AND t.resources_id IN(:ids)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameterList("ids", ids);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过用户id查询其拥有的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> queryByUserId(Long userId) {
		List<Menu> list = new ArrayList<Menu> ();
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT m.id, m.text, m.value, m.order_num orderNo, m.url, m.img_url icon, m.parent_id pid, m.xtype, m.create_date createDate, m.is_delete isDelete, m.content FROM sys_menu m WHERE m.is_delete=0 AND m.id IN (SELECT srr.resources_id FROM sys_role_resources srr WHERE srr.resources_type='menu' AND srr.role_id IN (SELECT sru.role_id FROM sys_role_user sru, sys_role sr WHERE sru.user_id=:userId AND sru.role_id=sr.id AND sr.is_delete=0))";
		//String sql = "SELECT m.* FROM sys_menu m WHERE m.is_delete=0 AND m.id IN (SELECT srr.resources_id FROM sys_role_resources srr WHERE srr.resources_type='menu' AND srr.role_id IN (SELECT sru.role_id FROM sys_role_user sru, sys_role sr WHERE sru.user_id=:userId AND sru.role_id=sr.id AND sr.is_delete=0))";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		//query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		//List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>> ();
		//mapList.addAll(query.list());
		query.setResultTransformer(new BeanTransformerAdapter(Menu.class));
		list.addAll(query.list());
		//list.addAll(this.parseMenus(mapList));
		return list;
	}
	
	/**
	 * 查询登录用户拥有的菜单权限
	 * 且xtype不为空，不为空表示首页可以显示
	 * 且url为空，即查询类似于我的报销，待审报销这样菜单的父菜单
	 * 该方法用于首页显示菜单
	 * @return
	 */
	public List<Map<String, Object>> queryMenusByXtype() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT t.* FROM sys_menu t WHERE t.xtype IS NOT NULL AND t.url IS NULL AND t.id IN (SELECT t7.resources_id FROM sys_role_resources t7 WHERE t7.resources_type='menu' AND t7.role_id IN (SELECT t8.role_id FROM sys_role_user t8 WHERE t8.user_id=:userId)) ORDER BY t.order_num ASC";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter("userId", HttpUtil.getLoginUser().getId());
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(sqlQuery.list());
		return list;
	}
	
	public List<Menu> parseMenus(List<Map<String, Object>> mapList) {
		List<Menu> menus = new ArrayList<Menu> ();
		Iterator<Map<String, Object>> iter = mapList.iterator();
		Map<String, Object> map = null;
		Menu menu = null;
		while(iter.hasNext()) {
			map = iter.next();
			menu = this.parseMenu(map);
			menus.add(menu);
		}
		return menus;
	}
	
	public Menu parseMenu(Map<String, Object> map) {
		Menu menu = new Menu();
		menu.setId(CodeUtil.parseLong(map.get("ID")));
		menu.setContent(CodeUtil.parseString(map.get("CONTENT")));
		menu.setIcon(CodeUtil.parseString(map.get("IMG_URL")));
		menu.setIndexCss(CodeUtil.parseString(map.get("INDEX_CSS")));
		menu.setIndexIcon(CodeUtil.parseString(map.get("INDEX_ICON")));
		menu.setIsDelete(CodeUtil.parseInt(map.get("IS_DELETE")));
		menu.setOrderNo(CodeUtil.parseInt(map.get("ORDER_NUM")));
		menu.setPid(CodeUtil.parseLong(map.get("PARENT_ID")));
		menu.setText(CodeUtil.parseString(map.get("TEXT")));
		menu.setUrl(CodeUtil.parseString(map.get("URL")));
		menu.setValue(CodeUtil.parseString(map.get("VALUE")));
		menu.setXtype(CodeUtil.parseString(map.get("XTYPE")));
		return menu;
	}
}
