package com.linuslan.oa.workflow.engine.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.system.group.dao.IGroupDao;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.ClassUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditUser;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;
import com.linuslan.oa.workflow.engine.flow.model.Property;

@Component("taskUtil")
public class TaskUtil {
	
	private static Logger logger = Logger.getLogger(TaskUtil.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IGroupDao groupDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	public void execute(Flow flow, Node node, Object object, String wfType, Map<String, Node> nodeMap, Long groupId) throws RuntimeException {
		User user = HttpUtil.getLoginUser();
		Session session = this.sessionFactory.getCurrentSession();
		try {
			List<Long> groups = HttpUtil.getLoginUserGroupIds();
			
			if(0 >= groups.size()) {
				CodeUtil.throwRuntimeExcep("用户没有审核权限！");
			}
			Long auditor = null;
			String assign = null;
			Integer status = null;
			String isJump = null;
			Group group = null;
			int passBtn = 0;
			int rejectBtn = 0;
			String auditorType="leader";
			if(!node.getType().equals("start")) {
				List<Property> props = node.getProps();
				Map<String, String> propMap = EngineUtil.convertPropertyToMap(props);
				if(null == props || 0 >= props.size()) {
					CodeUtil.throwRuntimeExcep("找不到审核人");
				}
				if(null != propMap.get("assignee") && !"".equals(propMap.get("assignee").trim())) {
					assign = propMap.get("assignee").trim();
				}
				
				if(null != propMap.get("passBtn") && !"".equals(propMap.get("passBtn").trim())) {
					try {
						passBtn = Integer.parseInt(propMap.get("passBtn"));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				if(null != propMap.get("rejectBtn") && !"".equals(propMap.get("rejectBtn").trim())) {
					try {
						rejectBtn = Integer.parseInt(propMap.get("rejectBtn"));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				if(null != propMap.get("status") && !"".equals(propMap.get("status").trim())) {
					try {
						status = Integer.parseInt(propMap.get("status"));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				if(null != propMap.get("isJump") && !"".equals(propMap.get("isJump").trim())) {
					isJump = propMap.get("isJump");
				}
				if(null != assign && !"".equals(assign.trim())) {
					if("leader".equals(assign.trim())) {
						group = (Group) session.get(Group.class, groupId);
						auditor = group.getPid();
					} else if("self".equals(assign.trim())) {
						
					} else if("manager".equals(assign.trim())) {
						
					} else {
						
					}
				}
			}
			
			
			/*
			 * 这段代码只是为了判断是否继续上级审核还是下一个节点审核
			 * 如果用户组没有上级用户组，则流程流转至下一节点
			 * 如果assign配置的不是leader，则说明不是上级审核，流程流转至下一节点
			 */
			if(node.getType().equals("start") || (null != group && null == group.getPid()) || !assign.equals("leader")) {
				StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
				nextNodeHQL.append(" ORDER BY p.id DESC");
				Query nextPathquery = session.createQuery(nextNodeHQL.toString());
				nextPathquery.setParameter("node", node.getName());
				nextPathquery.setParameter("flowId", flow.getId());
				List<Path> nextPaths = new ArrayList<Path> ();
				nextPaths.addAll(nextPathquery.list());
				Iterator<Path> nextPathIter = nextPaths.iterator();
				Path nextPath = null;
				while(nextPathIter.hasNext()) {
					nextPath = nextPathIter.next();
					String nextNodeName = nextPath.getToNode();
					if(null ==  nextNodeName || "".equals(nextNodeName.trim())) {
						CodeUtil.throwRuntimeExcep("下一审核节点不存在！");
					}
					Node nextNode = nodeMap.get(nextNodeName);
					if(null == nextNode) {
						CodeUtil.throwRuntimeExcep("下一审核节点不存在！");
					}
					this.engineUtil.executeFlow(flow.getId(), nextNode, object, wfType, nodeMap, null);
				}
			} else if(null != group && null != group.getPid()) {	//如果用户组不为空，且上级用户组不为空，则还是当前节点审核
				/*
				 * 如果当前审核用户拥有的用户组中包含了下一级审核用户组
				 * 且当前节点isJump（当遇到上述情况是的设定）=1，即跳过，则继续获取下一级用户组
				 */
				if(0 <= groups.indexOf(group.getPid()) && isJump.equals("1")) {
					this.execute(flow, node, object, wfType, nodeMap, group.getPid());
				} else {
					AuditLog newLog = new AuditLog();
					newLog.setAuditor(group.getPid());
					newLog.setCreateDate(new Date());
					newLog.setIsAudit(0);
					newLog.setLastStatus((Integer) BeanUtil.getValue(object, "status"));
					newLog.setNode(node.getName());
					newLog.setWfId((Long) BeanUtil.getValue(object, "id"));
					newLog.setWfType(wfType);
					newLog.setPassBtn(passBtn);
					newLog.setRejectBtn(rejectBtn);
					newLog.setStatus(status);
					newLog.setFlowId((Long)BeanUtil.getValue(object, "flowId"));
					session.save(newLog);
					int objStatus = (Integer) BeanUtil.getValue(object, "status");
					BeanUtil.setValue(object, "status", 3);
					BeanUtil.setValue(object, "lastStatus", objStatus);
					session.merge(object);
					
					/*
					 * 获取审核组中的审核人
					 */
					List<Long> auditors = new ArrayList<Long> ();
					auditors.add(group.getPid());
					String userSql = "SELECT id, name FROM sys_user u LEFT JOIN sys_user_group ug ON u.id = ug.user_id WHERE (u.group_id IN(:mainGroups) OR ug.group_id IN (:otherGroups)) AND u.is_delete=0 AND u.is_leave=1 GROUP BY u.id, u.name";
					SQLQuery userQuery = session.createSQLQuery(userSql);
					userQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					userQuery.setParameterList("mainGroups", auditors);
					userQuery.setParameterList("otherGroups", auditors);
					List<Map<String, Object>> users = userQuery.list();
					if(null != users && 0 < users.size()) {
						Iterator<Map<String, Object>> iter = users.iterator();
						Map<String, Object> u = null;
						while(iter.hasNext()) {
							u = iter.next();
							if(null != u.get("ID")) {
								try {
									Long uid = Long.parseLong(u.get("ID").toString());
									String name = u.get("NAME").toString();
									AuditUser au = new AuditUser();
									au.setAuditLogId(newLog.getId());
									au.setName(name);
									au.setUserId(uid);
									session.save(au);
								} catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
			} else {
				CodeUtil.throwRuntimeExcep("获取审核人异常！");
			}
		} catch(Exception ex) {
			logger.error("任务执行异常："+CodeUtil.getStackTrace(ex));
			CodeUtil.throwRuntimeExcep("任务执行异常");
		}
	}
}
