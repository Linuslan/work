package com.linuslan.oa.workflow.engine.flow.dao.impl;

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
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.flow.dao.IFlowDao;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;
import com.linuslan.oa.workflow.engine.flow.model.Property;

/**
 * 流程管理界面的DAO层
 * @author LinusLan
 *
 */
@Component("flowDao")
public class IFlowDaoImpl extends IBaseDaoImpl implements IFlowDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 分页查询流程
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Flow> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		Page<Flow> page = null;
		List<Flow> list =  new ArrayList<Flow> ();
		long totalRecord = 0;
		long totalPage = 0;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Flow m WHERE m.isDelete=0");
		hql.append(" ORDER BY m.type ASC, m.name DESC, m.id DESC");
		String countHQL = "SELECT COUNT(*) "+hql.toString();
		Query query = session.createQuery(hql.toString());
		Query countQuery = session.createQuery(countHQL.toString());
		query.setFirstResult((currentPage - 1) * limit)
			.setMaxResults(limit);
		list.addAll(query.list());
		totalRecord = (Long) countQuery.uniqueResult();
		totalPage = totalRecord % limit > 0 ? totalRecord / limit + 1 : totalRecord / limit;
		page = new Page<Flow> (list, totalRecord, totalPage, currentPage);
		return page;
	}
	
	/**
	 * 通过id查询流程
	 * @param id
	 * @return
	 */
	public Flow queryById(Long id) {
		Flow flow = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		flow = (Flow) session.get(Flow.class, id);
		return flow;
	}
	
	/**
	 * 新增流程
	 * @param flow
	 * @return
	 */
	public boolean add(Flow flow) {
		boolean success = false;
		Session session = null;
		session = sessionFactory.getCurrentSession();
		session.save(flow);
		success = true;
		return success;
	}
	
	/**
	 * 更新流程
	 * @param flow
	 * @return
	 */
	public boolean update(Flow flow) {
		boolean success = false;
		Session session = null;
		session = sessionFactory.getCurrentSession();
		session.merge(flow);
		success = true;
		return success;
	}
	
	/**
	 * 新增流程，包含属性
	 * @param nodes
	 * @param paths
	 * @param properties
	 * @return
	 */
	public boolean add(Flow flow, List<Node> nodes, List<Path> paths) throws RuntimeException {
		boolean success = false;
		Session session = null;
		session = sessionFactory.getCurrentSession();
		session.save(flow);
		if(null == flow.getId()) {
			CodeUtil.throwRuntimeExcep("流程主键为空");
		}
		Iterator<Node> nodeIter = nodes.iterator();
		Node node = null;
		List<Property> properties = null;
		while(nodeIter.hasNext()) {
			node = nodeIter.next();
			node.setFlowId(flow.getId());
			session.save(node);
			if(null == node.getId()) {
				CodeUtil.throwRuntimeExcep("节点主键为空");
			}
			properties = node.getProps();
			if(null != properties) {
				Iterator<Property> iter = properties.iterator();
				Property prop = null;
				while(iter.hasNext()) {
					prop = iter.next();
					prop.setPid(node.getId());
					prop.setFlowId(flow.getId());
					session.save(prop);
				}
			}
		}
		Iterator<Path> pathIter = paths.iterator();
		Path path = null;
		while(pathIter.hasNext()) {
			path = pathIter.next();
			path.setFlowId(flow.getId());
			session.save(path);
			if(null == path.getId()) {
				CodeUtil.throwRuntimeExcep("路径主键为空");
			}
			properties = path.getProps();
			if(null != properties) {
				Iterator<Property> iter = properties.iterator();
				Property prop = null;
				while(iter.hasNext()) {
					prop = iter.next();
					prop.setPid(node.getId());
					prop.setFlowId(flow.getId());
					session.save(prop);
				}
			}
		}
		success = true;
	
		return success;
	}
	
	/**
	 * 更新流程，包含属性
	 * @param nodes
	 * @param paths
	 * @param properties
	 * @return
	 */
	public boolean update(Flow flow, List<Node> nodes, List<Path> paths) throws RuntimeException {
		boolean success = false;
		Session session = null;
		session = sessionFactory.getCurrentSession();
		if(null == flow.getId()) {
			CodeUtil.throwRuntimeExcep("流程主键为空");
		}
		session.merge(flow);
		//删除旧的流程节点
		String delNode = "DELETE FROM Node n WHERE n.flowId=:flowId";
		Query query = session.createQuery(delNode);
		query.setParameter("flowId", flow.getId());
		query.executeUpdate();
		//删除旧的路径
		String delPath = "DELETE FROM Path p WHERE p.flowId=:flowId";
		query = session.createQuery(delPath);
		query.setParameter("flowId", flow.getId());
		query.executeUpdate();
		//删除旧的属性
		String delProp = "DELETE FROM Property p WHERE p.flowId=:flowId";
		query = session.createQuery(delProp);
		query.setParameter("flowId", flow.getId());
		query.executeUpdate();
		
		List<Property> properties = null;
		Iterator<Node> nodeIter = nodes.iterator();
		Node node = null;
		while(nodeIter.hasNext()) {
			node = nodeIter.next();
			node.setFlowId(flow.getId());
			session.save(node);
			if(null == node.getId()) {
				CodeUtil.throwRuntimeExcep("节点主键为空");
			}
			properties = node.getProps();
			if(null != properties) {
				Iterator<Property> iter = properties.iterator();
				Property prop = null;
				while(iter.hasNext()) {
					prop = iter.next();
					prop.setPid(node.getId());
					prop.setFlowId(flow.getId());
					session.save(prop);
				}
			}
		}
		Iterator<Path> pathIter = paths.iterator();
		Path path = null;
		while(pathIter.hasNext()) {
			path = pathIter.next();
			path.setFlowId(flow.getId());
			session.save(path);
			if(null == path.getId()) {
				CodeUtil.throwRuntimeExcep("路径主键为空");
			}
			properties = path.getProps();
			if(null != properties) {
				Iterator<Property> iter = properties.iterator();
				Property prop = null;
				while(iter.hasNext()) {
					prop = iter.next();
					prop.setPid(node.getId());
					prop.setFlowId(flow.getId());
					session.save(prop);
				}
			}
		}
		success = true;
		return success;
	}
	
	/**
	 * 查询流程节点
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNodesByFlowId(Long flowId) {
		List<Node> list = new ArrayList<Node> ();
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Node n WHERE n.flowId=:flowId");
		hql.append(" ORDER BY n.id DESC");
		Query query = session.createQuery(hql.toString());
		query.setParameter("flowId", flowId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 查询流程走向路径
	 * @param flowId
	 * @return
	 */
	public List<Path> queryPathsByFlowId(Long flowId) {
		List<Path> list = new ArrayList<Path> ();
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Path n WHERE n.flowId=:flowId");
		hql.append(" ORDER BY n.id DESC");
		Query query = session.createQuery(hql.toString());
		query.setParameter("flowId", flowId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 查询流程当前节点的下一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNextNodes(String node, Long flowId) {
		List<Node> list = new ArrayList<Node> ();
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
		hql.append(" ORDER BY p.id DESC");
		Query query = session.createQuery(hql.toString());
		query.setParameter("node", node);
		query.setParameter("flowId", flowId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 查询当前流程节点的上一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryLastNodes(String node, Long flowId) {
		List<Node> list = new ArrayList<Node> ();
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Path p WHERE p.toNode=:node AND p.flowId=:flowId");
		hql.append(" ORDER BY p.id DESC");
		Query query = session.createQuery(hql.toString());
		query.setParameter("node", node);
		query.setParameter("flowId", flowId);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过流程id删除流程路径
	 * @param id
	 * @return
	 */
	public boolean delPathByFlowId(Long id) {
		boolean success = false;
		if(null != id) {
			Session session = this.sessionFactory.getCurrentSession();
			StringBuffer hql = new StringBuffer("DELETE FROM Path p WHERE p.flowId=:id");
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			query.executeUpdate();
			success = true;
		}
		return success;
	}
	
	/**
	 * 通过流程id删除流程属性
	 * @param id
	 * @return
	 */
	public boolean delPropertyByFlowId(Long id) {
		boolean success = false;
		if(null != id) {
			Session session = this.sessionFactory.getCurrentSession();
			StringBuffer hql = new StringBuffer("DELETE FROM Property p WHERE p.flowId=:id");
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			query.executeUpdate();
			success = true;
		}
		return success;
	}
	
	/**
	 * 通过流程id删除流程节点
	 * @param id
	 * @return
	 */
	public boolean delNodeByFlowId(Long id) {
		boolean success = false;
		if(null != id) {
			Session session = this.sessionFactory.getCurrentSession();
			StringBuffer hql = new StringBuffer("DELETE FROM Node n WHERE n.flowId=:id");
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			query.executeUpdate();
			success = true;
		}
		return success;
	}
}
