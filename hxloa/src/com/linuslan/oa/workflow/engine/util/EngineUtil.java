package com.linuslan.oa.workflow.engine.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.group.dao.IGroupDao;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.ClassUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditUser;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;
import com.linuslan.oa.workflow.engine.flow.model.Property;

@Component("engineUtil")
@Transactional
public class EngineUtil {
	
	private static Logger logger = Logger.getLogger(EngineUtil.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IGroupDao groupDao;
	
	@Autowired
	private TaskUtil taskUtil;
	
	public void executeFlow(Long flowId, Node nextNode, Object obj, String wfType, Map<String, Node> nodeMap, Long gId) throws RuntimeException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = HttpUtil.getLoginUser();
			//String nodeName = oldLog.getNode();
			List<Long> groups = HttpUtil.getLoginUserGroupIds();
			Group group = null;
			if(null == gId) {
				group = HttpUtil.getLoginUserMainGroup();
			} else {
				group = (Group) session.get(Group.class, gId);
			}
			//Group group = HttpUtil.getLoginUserMainGroup();
			if(0 >= groups.size()) {
				CodeUtil.throwRuntimeExcep("用户没有审核权限！");
			}
			switch(NodeType.getType(nextNode.getType())) {
				case START: {
					
				} break;
				case END: {
					int status = (Integer) BeanUtil.getValue(obj, "status");
					BeanUtil.setValue(obj, "status", 4);
					BeanUtil.setValue(obj, "lastStatus", status);
					
					session.merge(obj);
					
					String userGroup = "SELECT t.group_id FROM sys_user t WHERE t.id=:userId";
					SQLQuery sqlQuery = session.createSQLQuery(userGroup);
					Long userId = null;
					try {
						userId = (Long) BeanUtil.getValue(obj, "userId");
					} catch(Exception ex) {
						try {
							userId = ((User)BeanUtil.getValue(obj, "user")).getId();
						} catch(Exception e) {
							try {
								userId = ((User)BeanUtil.getValue(obj, "createUser")).getId();
							} catch(Exception e1) {
								CodeUtil.throwRuntimeExcep("未找到申请人");
							}
						}
					}
					if(null == userId) {
						CodeUtil.throwRuntimeExcep("未找到申请人");
					}
					sqlQuery.setParameter("userId", userId);
					Long groupId = Long.parseLong(sqlQuery.list().get(0).toString());
					AuditLog newLog = new AuditLog();
					newLog.setAuditor(groupId);
					newLog.setCreateDate(new Date());
					newLog.setIsAudit(1);
					newLog.setLastStatus((Integer) BeanUtil.getValue(obj, "lastStatus"));
					newLog.setNode(nextNode.getName());
					newLog.setWfId((Long) BeanUtil.getValue(obj, "id"));
					newLog.setWfType(wfType);
					newLog.setPassBtn(0);
					newLog.setRejectBtn(0);
					newLog.setStatus((Integer) BeanUtil.getValue(obj, "status"));
					newLog.setFlowId((Long)BeanUtil.getValue(obj, "flowId"));
					session.save(newLog);
					
					/*
					 * 查询申请人信息
					 */
					String userInfoSql = "SELECT id, name FROM sys_user su WHERE su.id=:userId";
					SQLQuery userInfoQuery = session.createSQLQuery(userInfoSql);
					userInfoQuery.setParameter("userId", userId);
					userInfoQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List<Map<String, Object>> userInfo = userInfoQuery.list();
					if(null == userInfo || 0 >= userInfo.size()) {
						CodeUtil.throwRuntimeExcep("申请人不存在！");
					}
					Map<String, Object> u = userInfo.get(0);
					AuditUser auditor = new AuditUser();
					auditor.setAuditLogId(newLog.getId());
					auditor.setName(u.get("NAME").toString());
					auditor.setUserId(userId);
					session.save(auditor);
					
				} break;
				case TASK: {
					Long auditor = null;
					int passBtn = 0;
					int rejectBtn = 0;
					Integer status = null;
					int isJump = 0;
					//审核人的类型，只有两种，一种是leader，一种是self
					//领导审核都是leader，如果是自己审核（例如绩效到自评阶段），则auditorType=self
					String auditorType = "leader";
					String assignee = "";
					int isPrint = 0;
					List<Property> props = nextNode.getProps();
					Map<String, String> propMap = EngineUtil.convertPropertyToMap(props);
					if(null == props || 0 >= props.size()) {
						CodeUtil.throwRuntimeExcep("找不到审核人");
					}
					
					/*
					 * 判断该节点属性配置的审核人的类型
					 */
					if(null != propMap.get("assignee") && !"".equals(propMap.get("assignee").trim())) {
						
						assignee = propMap.get("assignee").trim();
						//如果值==leader，则说明是上级领导审核
						//则获取登录用户的主用户组的上级用户组
						if("leader".equals(propMap.get("assignee").trim())) {
							//获取当前登录用户的主用户组
							auditor = group.getPid();
						} else if("self".equals(propMap.get("assignee").trim())) {
							//如果是自己审核
							Long userId = (Long) BeanUtil.getValue(obj, "userId");
							if(null != userId) {
								//获取申请人，从而得到主用户组
								User u = (User) session.get(User.class, userId);
								auditor = u.getGroupId();
							}
							auditorType = "self";
						} else if("manager".equals(propMap.get("assignee").trim())) {
							/*
							 * 如果值==manager，则说明是组织架构中各个分支的最高领导人审核
							 * 则从Property对象中name="propertName"中获取该组织架构的最高领导人
							 * propertyName对应的value应该是这样的格式
							 * 对象对应的属性名=属性值对应的类名，该类应该和库中的表对应
							 */
							if(null == propMap.get("propertyName") || "".equals(propMap.get("propertyName").trim())) {
								CodeUtil.throwRuntimeExcep("获取分管领导用户组异常，请设置属性名和对应的类");
							}
							String value = propMap.get("propertyName");
							
							//用“=”分割值形成字符串数组，第一个值为流程实例的属性名，第二个值为对应的类名
							String[] valueArr = value.split("=");
							if(null == valueArr[0] || "".equals(valueArr[0].trim())) {
								CodeUtil.throwRuntimeExcep("获取分管领导用户组异常，请设置属性名");
							}
							if(null == valueArr[1] || "".equals(valueArr[1].trim())) {
								CodeUtil.throwRuntimeExcep("获取分管领导用户组异常，请设置对应的类");
							}
							//通过反射获取到属性的类型
							Class fieldType = BeanUtil.getFieldType(obj, valueArr[0].trim());
							Object propObj = null;
							if(fieldType == String.class) {
								String id = BeanUtil.getValue(obj, valueArr[0].trim()).toString();
								//得到的属性值即费valueArr[1]对应的类在数据库中的表的主键id
								//通过id获取到对象
								propObj = session.get(ClassUtil.getClassByName("com.linuslan.oa", valueArr[1].trim()), id);
							} else if(fieldType == Long.class || fieldType == Integer.class) {
								Long id = (Long) BeanUtil.getValue(obj, valueArr[0].trim());
								//得到的属性值即费valueArr[1]对应的类在数据库中的表的主键id
								//通过id获取到对象
								propObj = session.get(ClassUtil.getClassByName("com.linuslan.oa", valueArr[1].trim()), id);
							}
							if(null == propObj || null == BeanUtil.getValue(propObj, "id")
									|| null == BeanUtil.getValue(propObj, "groupId")) {
								CodeUtil.throwRuntimeExcep("获取分管领导用户组异常");
							}
							auditor = (Long) BeanUtil.getValue(propObj, "groupId");
						} else {
							try {
								auditor = Long.parseLong(propMap.get("assignee").trim());
							} catch(Exception ex) {
								ex.printStackTrace();
								logger.error("获取审核人异常："+ex);
							}
						}
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
						isJump = CodeUtil.parseInt(propMap.get("isJump"));
					}
					if(CodeUtil.isNotEmpty(propMap.get("isPrint"))) {
						isPrint = CodeUtil.parseInt(propMap.get("isPrint"));
					}
					//如果审核人为空，且不能直接跳过，则报错
					//此处判断节点是否为上级领导审核，如果是上级领导审核
					//当上一节点为某个顶级用户组审核完之后，当前节点为上级领导审核
					//则此时的审核人有可能为空
					if(!"leader".equals(assignee) && null == auditor) {
						CodeUtil.throwRuntimeExcep("找不到审核人");
					}
					
					//如果当前节点为上级领导审核
					if("leader".equals(assignee)) {
						//如果auditor(审核人)为空，则说明当前登录用户的主用户组为顶级用户组
						//没有上级领导，则流程流转至下一节点
						//如果auditor(审核人)不为空，则还是当前节点审核，继续上级领导审核
						if(null == auditor) {
							StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
							nextNodeHQL.append(" ORDER BY p.id DESC");
							Query nextPathquery = session.createQuery(nextNodeHQL.toString());
							nextPathquery.setParameter("node", nextNode.getName());
							nextPathquery.setParameter("flowId", flowId);
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
								Node forwardNode = nodeMap.get(nextNodeName);
								if(null == forwardNode) {
									CodeUtil.throwRuntimeExcep("下一审核节点不存在！");
								}
								this.executeFlow(flowId, forwardNode, obj, wfType, nodeMap, null);
							}
						} else {
							/*
							 * 如果当前审核用户拥有的用户组包含了下一级审核用户组
							 * 且当前节点的isJump=1，即跳过，则获取下一个用户组
							 */
							if(0 <= groups.indexOf(auditor) && "1".equals(isJump)) {
								this.executeFlow(flowId, nextNode, obj, wfType, nodeMap, auditor);
							} else {
								AuditLog newLog = this.addAuditLog(obj, auditor, nextNode, passBtn, rejectBtn, status, auditorType, isPrint);
								this.addAuditUser(auditor, newLog);
							}
						}
						
					} else {
						//如果不是上级领导审核且允许同组跳过，如果用户拥有的用户组中包含了审核用户组
						//则直接跳到下一个节点，否则，当前用户审核
						if(0 <= groups.indexOf(auditor) && "1".equals(isJump)) {
							StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
							nextNodeHQL.append(" ORDER BY p.id DESC");
							Query nextPathquery = session.createQuery(nextNodeHQL.toString());
							nextPathquery.setParameter("node", nextNode.getName());
							nextPathquery.setParameter("flowId", flowId);
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
								Node forwardNode = nodeMap.get(nextNodeName);
								if(null == forwardNode) {
									CodeUtil.throwRuntimeExcep("下一审核节点不存在！");
								}
								this.executeFlow(flowId, forwardNode, obj, wfType, nodeMap, null);
							}
						} else {
							//如果不是上级领导审核，则直接下一节点审核
							AuditLog newLog = this.addAuditLog(obj, auditor, nextNode, passBtn, rejectBtn, status, auditorType, isPrint);
							this.addAuditUser(auditor, newLog);
						}
					}
				} break;
				case JOIN: {
					int isCountersign = 0;
					List<Property> props = nextNode.getProps();
					if(null == props || 0 >= props.size()) {
						CodeUtil.throwRuntimeExcep("合并处理异常");
					}
					Iterator<Property> iter = props.iterator();
					Property prop = null;
					boolean hasCountersign = false;
					while(iter.hasNext()) {
						prop = iter.next();
						if("isCountersign".equals(prop.getName().trim())) {
							try {
								isCountersign = Integer.parseInt(prop.getValue());
								hasCountersign = true;
							} catch(Exception ex) {
								CodeUtil.throwRuntimeExcep("处理合并属性：\"是否会签\"异常！");
							}
							break;
						}
					}
					if(!hasCountersign) {
						CodeUtil.throwRuntimeExcep("处理合并节点异常，无法获悉该合并节点的类型！");
					}
					switch(isCountersign) {
						//为会签
						case 1: {
							//查询所有的路径，存放在Map中，用于后续查询Task节点
							String allPathHQL = "FROM Path p WHERE p.flowId=:flowId";
							Query allPathQuery = session.createQuery(allPathHQL);
							allPathQuery.setParameter("flowId", flowId);
							List<Path> allPath = allPathQuery.list();
							Iterator<Path> allPathIter = allPath.iterator();
							//存放key为toNode的Path
							Map<String, Path> toPathMap = new HashMap<String, Path> ();
							Path p = null;
							while(allPathIter.hasNext()) {
								p = allPathIter.next();
								toPathMap.put(p.getToNode(), p);
							}
							
							//获取到达当前会签节点的路径，以获取会签节点的前节点
							//将前节点存放到Map中，用来和auditlog里面的数据进行比较，只要有一个不相同，则判断为会签未结束
							
							String pathHQL = "FROM Path p WHERE p.toNode=:node AND p.flowId=:flowId";
							Query pathQuery = session.createQuery(pathHQL.toString());
							pathQuery.setParameter("node", nextNode.getName());
							pathQuery.setParameter("flowId", flowId);
							List<Path> paths = new ArrayList<Path> ();
							paths.addAll(pathQuery.list());
							if(null == paths || 0 >= paths.size()) {
								throw new Exception("");
							}
							List<String> nodeNames = new ArrayList<String> ();
							Iterator<Path> pathIter = paths.iterator();
							Path path = null;
							while(pathIter.hasNext()) {
								path = pathIter.next();
								String node = path.getFromNode();
								nodeNames.add(EngineUtil.getTaskNodeName(session, node, nodeMap, toPathMap));
							}
							
							//获取会签之前的节点未审核的日志，判断该日志有多少条，如果大于1条，则说明会签未结束
							String logHQL = "FROM AuditLog al WHERE al.wfId=:wfId AND al.wfType=:wfType AND al.isAudit=0 AND al.node IN (:nodes) ORDER BY al.id ASC";
							Query logQuery = session.createQuery(logHQL);
							logQuery.setParameter("wfId", (Long)BeanUtil.getValue(obj, "id"));
							logQuery.setParameter("wfType", wfType);
							logQuery.setParameterList("nodes", nodeNames);
							List<AuditLog> logs = logQuery.list();
							if(null == logs || 0 >= logs.size()) {
								CodeUtil.throwRuntimeExcep("获取审核日志异常");
							}
							Iterator<AuditLog> logIter = logs.iterator();
							AuditLog log = null;
							AuditLog userLog = null;
							while(logIter.hasNext()) {
								log = logIter.next();
								Long groupId = log.getAuditor();
								//如果用户的审核用户组中有该组id，则说明用户有审核权限
								//只获取一条审核节点即可
								//会签未结束，则更新用户审核的记录
								if(0 <= groups.indexOf(groupId)) {
									userLog = log;
									break;
								}
							}
							
							String allLogHQL = "FROM AuditLog al WHERE al.wfId=:wfId AND al.wfType=:wfType AND al.node IN (:nodes) ORDER BY al.id ASC";
							Query allLogQuery = session.createQuery(allLogHQL);
							allLogQuery.setParameter("wfId", (Long)BeanUtil.getValue(obj, "id"));
							allLogQuery.setParameter("wfType", wfType);
							allLogQuery.setParameterList("nodes", nodeNames);
							List<AuditLog> allLogs = allLogQuery.list();
							Map<String, AuditLog> auditLogMap = new HashMap<String, AuditLog> ();
							Iterator<AuditLog> auditLogIter = allLogs.iterator();
							AuditLog al = null;
							while(auditLogIter.hasNext()) {
								al = auditLogIter.next();
								if(null != al && null != al.getNode() && !"".equals(al.getNode().trim())) {
									auditLogMap.put(al.getNode(), al);
								}
							}
							
							//如果审核的记录数小于节点的数量，则说明有的节点可能还没到会签节点的前节点，则说明会签未结束
							if(auditLogMap.size() < nodeNames.size()) {
								if(null == userLog) {
									CodeUtil.throwRuntimeExcep("审核异常，用户没有审核权限");
								}
								userLog.setAuditDate(new Date());
								userLog.setAuditUser(user.getId());
								userLog.setIsAudit(1);
								userLog.setIsRead(1);
								session.merge(userLog);
							} else {
								//说明会签未结束，则获取审核人的那个审核记录
								if(1 < logs.size()) {
									if(null == userLog) {
										CodeUtil.throwRuntimeExcep("审核异常，用户没有审核权限");
									}
									userLog.setAuditDate(new Date());
									userLog.setAuditUser(user.getId());
									userLog.setIsAudit(1);
									userLog.setIsRead(1);
									session.merge(userLog);
								} else if(1 == logs.size()) {	//说明会签即将结束
									log = logs.get(0);
									log.setAuditDate(new Date());
									log.setAuditUser(user.getId());
									log.setIsAudit(1);
									log.setIsRead(1);
									session.merge(log);
									String hql = "FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId";
									Query query = session.createQuery(hql);
									//会签结束，获取的应该是会签节点的下一个节点
									query.setParameter("node", nextNode.getName());
									query.setParameter("flowId", flowId);
									List<Path> nextPaths =  query.list();
									if(null == nextPaths || 0 >= nextPaths.size()) {
										CodeUtil.throwRuntimeExcep("审核异常，会签结束获取下一节点异常");
									}
									Iterator<Path> nextPathIter = nextPaths.iterator();
									Path nextPath = null;
									while(nextPathIter.hasNext()) {
										nextPath = nextPathIter.next();
										String nextNodeName = nextPath.getToNode();
										if(null == nextNodeName || "".equals(nextNodeName.trim())) {
											continue;
										}
										Node afterNode = nodeMap.get(nextNodeName);
										if(null == afterNode) {
											continue;
										}
										executeFlow(flowId, afterNode, obj, wfType, nodeMap, null);
									}
								} else {
									CodeUtil.throwRuntimeExcep("会签审核异常！");
								}
							}
							
						} break;
						//非会签
						case 0: {
							
						}
						default: {
							StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
							nextNodeHQL.append(" ORDER BY p.id DESC");
							Query nextPathquery = session.createQuery(nextNodeHQL.toString());
							nextPathquery.setParameter("node", nextNode.getName());
							nextPathquery.setParameter("flowId", flowId);
							List<Path> paths = new ArrayList<Path> ();
							paths.addAll(nextPathquery.list());
							Iterator<Path> pathIter = paths.iterator();
							Path path = null;
							Node node = null;
							while(pathIter.hasNext()) {
								path = pathIter.next();
								String toNodeName = path.getToNode();
								if(null == toNodeName || "".equals(toNodeName.trim())) {
									CodeUtil.throwRuntimeExcep("未找到审核节点！");
								}
								node = nodeMap.get(toNodeName);
								if(null == node) {
									CodeUtil.throwRuntimeExcep("未找到审核节点！");
								}
								executeFlow(flowId, node, obj, wfType, nodeMap, null);
							}
						} break;
					}
				} break;
				case FORK: {
					//是否并行处理，默认为否
					int isParallel = 0;
					
					List<Property> props = nextNode.getProps();
					if(null == props || 0 >= props.size()) {
						CodeUtil.throwRuntimeExcep("分支处理异常");
					}
					Iterator<Property> iter = props.iterator();
					Property prop = null;
					boolean hasParallel = false;
					while(iter.hasNext()) {
						prop = iter.next();
						if("isParallel".equals(prop.getName().trim())) {
							try {
								isParallel = Integer.parseInt(prop.getValue());
								hasParallel = true;
							} catch(Exception ex) {
								CodeUtil.throwRuntimeExcep("处理分支属性: \"是否并行\"异常！");
							}
							break;
						}
					}
					if(!hasParallel) {
						CodeUtil.throwRuntimeExcep("处理分支节点异常，无法获悉该分支节点的类型！");
					}
					switch(isParallel) {
						//并行
						case 1: {
							StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
							nextNodeHQL.append(" ORDER BY p.id DESC");
							Query nextPathquery = session.createQuery(nextNodeHQL.toString());
							nextPathquery.setParameter("node", nextNode.getName());
							nextPathquery.setParameter("flowId", flowId);
							List<Path> paths = new ArrayList<Path> ();
							paths.addAll(nextPathquery.list());
							Iterator<Path> pathIter = paths.iterator();
							Path path = null;
							Node node = null;
							while(pathIter.hasNext()) {
								path = pathIter.next();
								String nextNodeName = path.getToNode();
								if(null ==  nextNodeName || "".equals(nextNodeName.trim())) {
									CodeUtil.throwRuntimeExcep("路径获取的节点为空");
								}
								node = nodeMap.get(nextNodeName);
								if(null == node) {
									CodeUtil.throwRuntimeExcep("路径获取的节点为空");
								}
								executeFlow(flowId, node, obj, wfType, nodeMap, null);
							}
						} break;
						//非并行和default默认都按照非并行的类型处理
						case 0: {
							
						}
						default: {
							String expression = "";
							StringBuffer nextNodeHQL = new StringBuffer("FROM Path p WHERE p.fromNode=:node AND p.flowId=:flowId");
							nextNodeHQL.append(" ORDER BY p.id DESC");
							Query nextPathquery = session.createQuery(nextNodeHQL.toString());
							nextPathquery.setParameter("node", nextNode.getName());
							nextPathquery.setParameter("flowId", flowId);
							List<Path> paths = new ArrayList<Path> ();
							paths.addAll(nextPathquery.list());
							Iterator<Path> pathIter = paths.iterator();
							Path path = null;
							Node node = null;
							while(pathIter.hasNext()) {
								path = pathIter.next();
								List<Property> pathProps = path.getProps();
								if(null == pathProps || 0 >= pathProps.size()) {
									CodeUtil.throwRuntimeExcep("获取路径判断表达式异常");
								}
								Iterator<Property> nextPathIter = pathProps.iterator();
								Property nextPathProp = null;
								while(nextPathIter.hasNext()) {
									nextPathProp = nextPathIter.next();
									if("expression".equals(nextPathProp.getName().trim())) {
										expression = nextPathProp.getValue();
										break;
									}
								}
								try {
									// Create or retrieve a JexlEngine
									JexlEngine jexl = new JexlEngine();
									//特殊字符"$"要写在[]里面才不会报错
									Pattern pattern = Pattern.compile("[${}]");
									Matcher matcher = pattern.matcher(expression);
									String newExpre = matcher.replaceAll("");
									// Create an expression object
									Expression e = jexl.createExpression(newExpre);
									// Create a context and add data
									JexlContext jc = new MapContext();
									List<String> columns = EngineUtil.getColumnNames(expression);
									for(String column : columns) {
										jc.set(column, BeanUtil.getValue(obj, column));
									}
									
									// Now evaluate the expression, getting the result
									Boolean o = (Boolean) e.evaluate(jc);
									if(o) {
										String nextNodeName = path.getToNode();
										if(null ==  nextNodeName || "".equals(nextNodeName.trim())) {
											CodeUtil.throwRuntimeExcep("表达式符合的节点为空");
										}
										node = nodeMap.get(nextNodeName);
										if(null == node) {
											CodeUtil.throwRuntimeExcep("表达式符合的节点为空");
										}
										break;
									}
								} catch(Exception ex) {
									ex.printStackTrace();
									CodeUtil.throwRuntimeExcep("表达式判断异常！");
								}
								
							}
							//如果未获取到节点，则默认走第一个节点
							if(null == node) {
								path = paths.get(0);
								String nextNodeName = path.getToNode();
								if(null ==  nextNodeName || "".equals(nextNodeName.trim())) {
									CodeUtil.throwRuntimeExcep("表达式符合的节点为空");
								}
								node = nodeMap.get(nextNodeName);
								if(null == node) {
									CodeUtil.throwRuntimeExcep("表达式符合的节点为空");
								}
							}
							executeFlow(flowId, node, obj, wfType, nodeMap, null);
						} break;
					}
				} break;
				case STATE: {
					
				} break;
				default: {
					CodeUtil.throwRuntimeExcep("未找到节点类型！");
				}
			}
		} catch(Exception ex) {
			logger.error("审核失败："+ex);
			CodeUtil.throwRuntimeExcep("审核失败，"+CodeUtil.getStackTrace(ex));
		}
		
	}
	
	/**
	 * 退回
	 * @param session
	 * @param user 审核人
	 * @param object 申请单实例
	 * @param wfType
	 * @throws Exception
	 */
	public void rejectFlow(Object object, String wfType) throws RuntimeException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			User user = HttpUtil.getLoginUser();
			String updateHQL = "UPDATE AuditLog al SET al.isAudit=1, al.auditDate=SYSDATE, al.operate=1, al.auditUser=:auditUser WHERE al.wfId=:wfId AND al.wfType=:wfType AND al.isAudit=0";
			Query query = session.createQuery(updateHQL);
			query.setParameter("wfId", BeanUtil.getValue(object, "id"));
			query.setParameter("wfType", wfType);
			query.setParameter("auditUser", user.getId());
			query.executeUpdate();
			//String queryStartNode = "FROM Node n WHERE n.type='start' AND n.flowId=(SELECT f.id FROM Flow f LEFT JOIN f.companys c WHERE f.type=:type AND f.isUse=0 AND f.isDelete=0 AND c.id=:companyId)";
			String queryStartNode = "FROM Node n WHERE n.type='start' AND n.flowId=:flowId";
			query = session.createQuery(queryStartNode);
			//query.setParameter("type", wfType);
			query.setParameter("flowId", BeanUtil.getValue(object, "flowId"));
			//query.setParameter("companyId", BeanUtil.getValue(object, "companyId"));
			List<Node> nodes = query.list();
			if(null == nodes || 0 >= nodes.size()) {
				CodeUtil.throwRuntimeExcep("退回失败，开始节点不存在！");
			}
			Node node = nodes.get(0);
			String userGroup = "SELECT t.group_id FROM sys_user t WHERE t.id=:userId";
			SQLQuery sqlQuery = session.createSQLQuery(userGroup);
			Long userId = null;
			try {
				userId = (Long) BeanUtil.getValue(object, "userId");
			} catch(Exception ex) {
				try {
					userId = ((User)BeanUtil.getValue(object, "user")).getId();
				} catch(Exception e) {
					try {
						userId = ((User)BeanUtil.getValue(object, "createUser")).getId();
					} catch(Exception e1) {
						CodeUtil.throwRuntimeExcep("未找到申请人");
					}
				}
			}
			if(null == userId) {
				CodeUtil.throwRuntimeExcep("未找到申请人");
			}
			sqlQuery.setParameter("userId", userId);
			Long groupId = Long.parseLong(sqlQuery.list().get(0).toString());
			AuditLog log = new AuditLog();
			log.setCreateDate(new Date());
			log.setIsAudit(0);
			log.setLastStatus((Integer) BeanUtil.getValue(object, "status"));
			log.setStatus(1);
			log.setAuditor(groupId);
			log.setNode(node.getName());
			log.setWfId((Long)BeanUtil.getValue(object, "id"));
			log.setWfType(wfType);
			log.setFlowId((Long)BeanUtil.getValue(object, "flowId"));
			session.save(log);
			/*
			 * 查询申请人信息
			 */
			String userInfoSql = "SELECT id, name FROM sys_user su WHERE su.id=:userId";
			SQLQuery userInfoQuery = session.createSQLQuery(userInfoSql);
			userInfoQuery.setParameter("userId", userId);
			userInfoQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<Map<String, Object>> userInfo = userInfoQuery.list();
			if(null == userInfo || 0 >= userInfo.size()) {
				CodeUtil.throwRuntimeExcep("申请人不存在！");
			}
			Map<String, Object> u = userInfo.get(0);
			AuditUser auditor = new AuditUser();
			auditor.setAuditLogId(log.getId());
			auditor.setName(u.get("NAME").toString());
			auditor.setUserId(userId);
			session.save(auditor);
			BeanUtil.setValue(object, "lastStatus", BeanUtil.getValue(object, "status"));
			BeanUtil.setValue(object, "status", 1);
			session.merge(object);
		} catch(Exception ex) {
			logger.error("退回流程失败："+ex);
			CodeUtil.throwRuntimeExcep("退回流程失败");
		}
		
	}
	
	public void execute(Object object, String wfType, int pass) throws RuntimeException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			User user = HttpUtil.getLoginUser();
			List<Long> groups = HttpUtil.getLoginUserGroupIds();
			if(0 >= groups.size()) {
				CodeUtil.throwRuntimeExcep("用户没有审核权限");
			}
			Long flowId = (Long) BeanUtil.getValue(object, "flowId");
			if(null == flowId) {
				CodeUtil.throwRuntimeExcep("获取流程异常，流程ID为空");
			}
			Flow flow = (Flow) session.get(Flow.class, flowId);
			if(null == flow || null == flow.getId()) {
				CodeUtil.throwRuntimeExcep("获取流程异常，流程不存在");
			}
			Map<String, Node> nodeMap = new HashMap<String, Node> ();
			
			String nodeHql = "FROM Node n WHERE n.flowId=:flowId";
			Query nodeQuery = session.createQuery(nodeHql);
			nodeQuery.setParameter("flowId", flow.getId());
			List<Node> nodes = nodeQuery.list();
			if(null == nodes || 0 >= nodes.size()) {
				CodeUtil.throwRuntimeExcep("该流程没有审核节点");
			}
			Iterator<Node> nodeIter = nodes.iterator();
			Node node = null;
			while(nodeIter.hasNext()) {
				node = nodeIter.next();
				nodeMap.put(node.getName(), node);
			}
			
			switch(pass) {
				//通过
				case 0: {
					//有用户审核的节点必然只可能是TASK节点
					String hql = "FROM AuditLog al WHERE al.isAudit=0 AND al.wfId=:wfId AND al.wfType=:wfType AND al.auditor IN (:groups) ORDER BY al.id ASC";
					Query query = session.createQuery(hql);
					query.setParameter("wfId", BeanUtil.getValue(object, "id"));
					query.setParameter("wfType", wfType);
					query.setParameterList("groups", groups);
					List<AuditLog> logs = query.list();
					if(null == logs || 0 >= logs.size()) {
						CodeUtil.throwRuntimeExcep("当前用户没有待审的申请");
					}
					//如果用户有多个审核节点，则只取一个进行审核
					AuditLog log = logs.get(0);
					String nodeName = log.getNode();
					node = nodeMap.get(nodeName);
					if(null == node) {
						CodeUtil.throwRuntimeExcep("主键为"+log.getWfId()+"的"+log.getWfType()+"流程找不到"+nodeName+"节点");
					}
					switch(NodeType.getType(node.getType())) {
						case START: {
							
						}
						case TASK: {
							this.taskUtil.execute(flow, node, object, wfType, nodeMap, log.getAuditor());
							log.setAuditDate(new Date());
							log.setAuditUser(user.getId());
							log.setIsAudit(1);
							log.setOperate(0);
							log.setIsRead(1);
							session.merge(log);
						} break;
						case JOIN: {
							
						} break;
						case FORK: {
							
						} break;
						case STATE: {
							
						} break;
						case END: {
							
						}
						default: {
							CodeUtil.throwRuntimeExcep("节点类型无法识别！");
						}
					}
				} break;
				case 1: {
					this.rejectFlow(object, wfType);
				} break;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
	}
	
	/**
	 * 启动流程
	 * @param session
	 * @param user 申请人
	 * @param object 表单实体
	 * @param wfType 流程类型
	 * @throws Exception
	 */
	public void startFlow(Object object, String wfType) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			User user = HttpUtil.getLoginUser();
			String hql = "SELECT f FROM Flow f JOIN f.companys c WHERE f.isDelete=0 AND f.type=:wfType AND f.isUse=0 AND c.id=:companyId AND f.isHistory=0";
			Query query = session.createQuery(hql);
			query.setParameter("wfType", wfType);
			query.setParameter("companyId", BeanUtil.getValue(object, "companyId"));
			List<Flow> list = query.list();
			if(null != list && 0 < list.size()) {
				Flow flow = list.get(0);
				if(null == flow || null == flow.getId()) {
					CodeUtil.throwRuntimeExcep("没有可用的流程");
				}
				hql = "FROM Node n WHERE n.flowId=:flowId AND n.type='start'";
				query = session.createQuery(hql);
				query.setParameter("flowId", flow.getId());
				List<Node> nodes = query.list();
				if(null == nodes || 0 >= nodes.size()) {
					CodeUtil.throwRuntimeExcep("流程节点为空");
				}
				Node startNode = nodes.get(0);
				if(null == startNode.getName() || "".equals(startNode.getName().trim())) {
					CodeUtil.throwRuntimeExcep("流程开始节点为空");
				}
				AuditLog auditlog = new AuditLog();
				auditlog.setCreateDate(new Date());
				auditlog.setIsAudit(0);
				auditlog.setNode(startNode.getName());
				auditlog.setStatus(0);
				auditlog.setWfId((Long) BeanUtil.getValue(object, "id"));
				auditlog.setWfType(wfType);
				auditlog.setAuditor(user.getGroupId());
				auditlog.setFlowId(flow.getId());
				session.save(auditlog);
				AuditUser auditor = new AuditUser();
				auditor.setAuditLogId(auditlog.getId());
				auditor.setName(user.getName());
				auditor.setUserId(user.getId());
				session.save(auditor);
				BeanUtil.setValue(object, "flowId", flow.getId());
				BeanUtil.setValue(object, "status", 0);
				session.merge(object);
			} else {
				CodeUtil.throwRuntimeExcep("没有可用的流程");
			}
		} catch(Exception ex) {
			logger.error("启动流程异常，异常原因："+CodeUtil.getStackTrace(ex));
			CodeUtil.throwRuntimeExcep("启动流程异常");
		}
	}
	
	/**
	 * 判断当前节点的退回和通过的权限
	 * @param auditLogs
	 * @return
	 */
	public static Map<String, Boolean> checkAuditBtn(List<AuditLog> auditLogs) {
		Map<String, Boolean> map = new HashMap<String, Boolean> ();
		//先初始化不能打印
		map.put("print", false);
		try {
			Iterator<AuditLog> iter = auditLogs.iterator();
			AuditLog log = null;
			AuditLog temp = null;
			while(iter.hasNext()) {
				temp = iter.next();
				if(temp.getIsAudit() == 0) {
					if(null == log) {
						log = temp;
						//只要有一个节点允许打印，则可以打印
						if(null != log && log.getIsPrint() == 1) {
							map.put("print", true);
						}
					} else {
						if(temp.getId() < log.getId()) {
							log = temp;
						}
					}
				}
			}
			if(null != log && log.getPassBtn() == 1) {
				map.put("passBtn", true);
			} else {
				map.put("passBtn", false);
			}
			
			if(null != log && log.getRejectBtn() == 1) {
				map.put("rejectBtn", true);
			} else {
				map.put("rejectBtn", false);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			map.put("passBtn", false);
			map.put("rejectBtn", false);
			map.put("print", false);
		}
		return map;
	}
	
	/**
	 * 得到当前审核节点的流程状态
	 * @param auditLogs
	 * @return
	 */
	public static int getStatus(List<AuditLog> auditLogs) {
		int status = 0;
		try {
			List<Long> groups = HttpUtil.getLoginUserGroupIds();
			Iterator<AuditLog> iter = auditLogs.iterator();
			AuditLog log = null;
			AuditLog temp = null;
			while(iter.hasNext()) {
				temp = iter.next();
				/*
				 * 流程状态同一条流程实例，在并行的情况下，可能有多条流程状态对应不同的审核人
				 */
				if(temp.getIsAudit() == 0 && 0 <= groups.indexOf(temp.getAuditor())) {
					if(null == log) {
						log = temp;
					} else {
						if(temp.getId() < log.getId()) {
							log = temp;
						}
					}
				}
			}
			status = log.getStatus();
		} catch(Exception ex) {
			status = 4;
		}
		return status;
	}
	
	/**
	 * 得到所有的流程状态
	 * @param auditLogs
	 * @return
	 */
	public static String getAllStatus(List<AuditLog> auditLogs) {
		String status = "4";
		try {
			List<Integer> statusList = new ArrayList<Integer> ();
			Iterator<AuditLog> iter = auditLogs.iterator();
			AuditLog temp = null;
			while(iter.hasNext()) {
				temp = iter.next();
				/*
				 * 流程状态同一条流程实例，在并行的情况下，可能有多条流程状态对应不同的审核人
				 */
				if(temp.getIsAudit() == 0) {
					statusList.add(temp.getStatus());
				}
			}
			status = BeanUtil.parseIntegerListToString(statusList, ",");
		} catch(Exception ex) {
			status = "4";
		}
		return status;
	}
	
	public static String getAuditors(List<AuditLog> auditLogs) {
		String auditor = "";
		try {
			Iterator<AuditLog> iter = auditLogs.iterator();
			AuditLog log = null;
			while(iter.hasNext()) {
				log = iter.next();
				if(log.getStatus()!=0 && log.getStatus()!=1 && log.getStatus()!=2 && log.getStatus() != 4) {
					auditor += log.getAuditorName();
					if(iter.hasNext()) {
						auditor += "、";
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			auditor = "";
		}
		return auditor;
	}
	
	/**
	 * pathMap: key=toNode
	 * @param session
	 * @param name
	 * @param nodeMap
	 * @param pathMap
	 * @return
	 */
	public static String getTaskNodeName(Session session, String name, Map<String, Node> nodeMap, Map<String, Path> pathMap) {
		String nodeName = "";
		Node node = nodeMap.get(name);
		if(node.getType().equals("task")) {
			nodeName = node.getName();
		} else {
			Path path = pathMap.get(name);
			nodeName = path.getFromNode();
			EngineUtil.getTaskNodeName(session, nodeName, nodeMap, pathMap);
		}
		return nodeName;
	}
	
	/**
	 * 将节点的属性转换成map
	 * key为属性名，value为属性的值
	 * @param list
	 * @return
	 */
	public static Map<String, String> convertPropertyToMap(List<Property> list) {
		Map<String, String> propMap = new HashMap<String, String> ();
		try {
			Iterator<Property> iter = list.iterator();
			Property prop = null;
			while(iter.hasNext()) {
				prop = iter.next();
				propMap.put(prop.getName(), prop.getValue());
			}
		} catch(Exception ex) {
			logger.error("转换流程属性异常，异常原因："+CodeUtil.getStackTrace(ex));
			CodeUtil.throwRuntimeExcep("转换流程属性异常");
		}
		return propMap;
	}
	
	/**
	 * 获取表达式中"${}"表达式之间的属性名
	 * @param expression
	 * @return
	 */
	public static List<String> getColumnNames(String expression) {
		List<String> list = new ArrayList<String> ();
		try {
			int start = 0;
			int end = 0;
			String columnName = null;
			
			//方法indexOf("", int start);开始检索的位置包括start
			while(0 <= expression.indexOf("${", start)) {
				start = expression.indexOf("${", start);
				
				end = expression.indexOf("}", start);
				columnName = expression.substring(start+2, end);
				start = end;
				if(CodeUtil.isNotEmpty(columnName)) {
					list.add(columnName.trim());
				}
			}
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep("解析表达式异常");
		}
		return list;
	}
	
	/**
	 * 生成审核意见
	 * @param opinion
	 * @param passType
	 * @param object
	 * @return
	 */
	public static AuditorOpinion generateOpinion(String opinion, int passType, Object object) {
		AuditorOpinion obj = new AuditorOpinion();
		obj.setAuditDate(new Date());
		obj.setAuditor(HttpUtil.getLoginUser().getId());
		obj.setIsDelete(0);
		obj.setOpinion(opinion);
		obj.setPassType(passType);
		obj.setWfId((Long) BeanUtil.getValue(object, "id"));
		obj.setWfType(CodeUtil.getClassName(object.getClass()));
		return obj;
	}
	
	/**
	 * 添加审核记录
	 * @param object
	 * @param auditor
	 * @param node
	 * @param passBtn
	 * @param rejectBtn
	 * @param status
	 * @param auditorType
	 * @param isPrint 是否打印
	 */
	public AuditLog addAuditLog(Object object, Long auditor, Node node, int passBtn, int rejectBtn, int status, String auditorType, int isPrint) {
		Session session = this.sessionFactory.getCurrentSession();
		AuditLog newLog = new AuditLog();
		newLog.setAuditor(auditor);
		newLog.setCreateDate(new Date());
		newLog.setIsAudit(0);
		newLog.setLastStatus((Integer) BeanUtil.getValue(object, "status"));
		newLog.setNode(node.getName());
		newLog.setWfId((Long) BeanUtil.getValue(object, "id"));
		newLog.setWfType(CodeUtil.getClassName(object.getClass()));
		newLog.setPassBtn(passBtn);
		newLog.setRejectBtn(rejectBtn);
		newLog.setStatus(status);
		newLog.setFlowId((Long)BeanUtil.getValue(object, "flowId"));
		newLog.setAuditorType(auditorType);
		newLog.setIsPrint(isPrint);
		session.save(newLog);
		int objStatus = (Integer) BeanUtil.getValue(object, "status");
		BeanUtil.setValue(object, "status", 3);
		BeanUtil.setValue(object, "lastStatus", objStatus);
		session.merge(object);
		return newLog;
	}
	
	/**
	 * 新增审核记录对应的审核人
	 * @param auditor
	 * @param newLog
	 */
	public void addAuditUser(Long auditor, AuditLog newLog) {
		Session session = this.sessionFactory.getCurrentSession();
		/*
		 * 获取审核组中的审核人
		 */
		List<Long> auditors = new ArrayList<Long> ();
		auditors.add(auditor);
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
	
	public static void main(String[] args) {
		//String expression = "${userDeptId}.equals(${payDeptId})==${abcdefg}";
		//System.out.println(expression.replaceAll("[${}]", ""));
		
		//Pattern p = Pattern.compile("[${}]");
		//Matcher m = p.matcher(expression);
		//System.out.println(m.replaceAll(""));
		
		/*List<String> columns = EngineUtil.getColumnNames(expression);
		for(String column : columns) {
			System.out.println(column);
		}*/
		System.out.println(ClassUtil.getClassByName("com.linuslan.oa", "Department").getName());
	}
}
