package com.linuslan.oa.system.button.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.button.dao.IButtonDao;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.hibernate.BeanTransformerAdapter;

@Component("buttonDao")
public class IButtonDaoImpl extends IBaseDaoImpl implements IButtonDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过菜单id分页查询按钮
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Button> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows) {
		Page<Button> pageData = null;
		List<Button> list = new ArrayList<Button> ();
		long total = 0;
		long records = 0;
		StringBuffer hql = new StringBuffer("FROM Button b WHERE b.isDelete=0 AND b.menuId=:menuId");
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql.toString()+" ORDER BY b.menuId ASC, b.orderNo ASC");
		query.setParameter("menuId", menuId);
		query.setFirstResult((page - 1) * rows);
		query.setMaxResults(rows);
		list.addAll(query.list());
		
		Query countQuery = session.createQuery("SELECT COUNT(*) "+hql.toString());
		countQuery.setParameter("menuId", menuId);
		records = (Long) countQuery.uniqueResult();
		total = records % rows == 0 ? records / rows : records / rows + 1;
		
		pageData = new Page<Button> (list, records, total, page);
		return pageData;
	}
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Button queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Button button = (Button) session.get(Button.class, id);
		return button;
	}
	
	/**
	 * 新增按钮
	 * @param button
	 * @return
	 */
	public boolean add(Button button) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.save(button);
		success = true;
		return success;
	}
	
	/**
	 * 更新按钮
	 * @param button
	 * @return
	 */
	public boolean update(Button button) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(button);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Button b SET b.isDelete=1 WHERE b.menuId=:menuId";
		Query query = session.createQuery(hql);
		query.setParameter("menuId", menuId);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuIds(List<Long> menuIds) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "UPDATE Button b SET b.isDelete=1 WHERE b.menuId IN (:menuIds)";
		Query query = session.createQuery(hql);
		query.setParameterList("menuIds", menuIds);
		query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过菜单id查询按钮
	 */
	public List<Button> queryByMenuIds(List<Long> menuIds) {
		List<Button> list = new ArrayList<Button> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Button b WHERE b.menuId IN (:menuIds) AND b.isDelete=0 ORDER BY b.menuId ASC, b.orderNo ASC";
		Query query = session.createQuery(hql);
		query.setParameterList("menuIds", menuIds);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过用户id查询其拥有的按钮
	 * @param userId
	 * @return
	 */
	public List<Button> queryByUserId(Long userId) {
		List<Button> list = new ArrayList<Button> ();
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT m.id, m.menu_id, '' menu_name, m.name, m.value, m.memo, m.order_num, m.create_date, m.is_delete FROM sys_button m WHERE m.is_delete=0 AND m.id IN (SELECT srr.resources_id FROM sys_role_resources srr WHERE srr.resources_type='button' AND srr.role_id IN (SELECT sru.role_id FROM sys_role_user sru, sys_role sr WHERE sru.user_id=:userId AND sru.role_id=sr.id AND sr.is_delete=0))";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setResultTransformer(new BeanTransformerAdapter(Button.class));
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByButtonIds(List<Long> ids) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "DELETE FROM sys_role_resources t WHERE t.resources_type='button' AND t.resources_id IN(:ids)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameterList("ids", ids);
		query.executeUpdate();
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
		SQLQuery query = session.createSQLQuery("UPDATE sys_button t2 SET t2.is_delete=1 WHERE t2.id IN (:ids)");
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		success = true;
		return success;
	}
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Button> queryByIds(List<Long> ids) {
		List<Button> buttons = new ArrayList<Button> ();
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Button b WHERE b.isDelete=0 AND b.id IN (:ids) ORDER BY b.menuId ASC, b.orderNo ASC");
		query.setParameterList("ids", ids);
		buttons.addAll(query.list());
		return buttons;
	}
}
