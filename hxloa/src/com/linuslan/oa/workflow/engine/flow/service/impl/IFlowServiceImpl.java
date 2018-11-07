package com.linuslan.oa.workflow.engine.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.flow.dao.IFlowDao;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;
import com.linuslan.oa.workflow.engine.flow.model.Property;
import com.linuslan.oa.workflow.engine.flow.service.IFlowService;

@Component("flowService")
@Transactional
public class IFlowServiceImpl extends IBaseServiceImpl implements IFlowService {
	@Autowired
	private IFlowDao flowDao;
	/**
	 * 分页查询流程
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Flow> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.flowDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 通过id查询流程
	 * @param id
	 * @return
	 */
	public Flow queryById(Long id) {
		return this.flowDao.queryById(id);
	}
	
	/**
	 * 新增流程
	 * @param flow
	 * @return
	 */
	public boolean add(Flow flow) {
		return this.flowDao.add(flow);
	}
	
	/**
	 * 更新流程
	 * @param flow
	 * @return
	 */
	public boolean update(Flow flow) {
		return this.flowDao.update(flow);
	}
	
	/**
	 * 新增流程，包含属性
	 * @param nodes
	 * @param paths
	 * @param properties
	 * @return
	 */
	public boolean add(Flow flow, List<Node> nodes, List<Path> paths) throws RuntimeException {
		return this.flowDao.add(flow, nodes, paths);
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
		Flow persist = this.flowDao.queryById(flow.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("");
		}
		//如果当前有正在运行为完成的流程实例，则保留当前流程，再新增一条流程
		if(0 < persist.getInstanceCount()) {
			persist.setIsHistory(1);
			flow.setId(null);
			List<Company> companys = new ArrayList<Company> ();
			companys.addAll(persist.getCompanys());
			flow.setCompanys(companys);
			this.flowDao.update(persist);
			List<Node> newNodes = new ArrayList<Node> ();
			for(int i = 0; i < nodes.size(); i ++) {
				Node node = nodes.get(i);
				node.setId(null);
				newNodes.add(node);
			}
			List<Path> newPaths = new ArrayList<Path> ();
			for(int i = 0; i < paths.size(); i ++) {
				Path path = paths.get(i);
				path.setId(null);
				newPaths.add(path);
			}
			this.flowDao.add(flow, newNodes, newPaths);
			success = true;
		} else {
			flow.setCreateDate(persist.getCreateDate());
			try {
				persist = (Flow) BeanUtil.updateBean(persist, flow);
			} catch(Exception ex) {
				CodeUtil.throwRuntimeExcep("更新数据异常");
			}
			success = this.flowDao.update(persist, nodes, paths);
		}
		return success;
	}
	
	/**
	 * 查询流程节点
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNodesByFlowId(Long flowId) {
		return this.flowDao.queryNodesByFlowId(flowId);
	}
	
	/**
	 * 查询流程走向路径
	 * @param flowId
	 * @return
	 */
	public List<Path> queryPathsByFlowId(Long flowId) {
		return this.flowDao.queryPathsByFlowId(flowId);
	}
	
	/**
	 * 查询流程当前节点的下一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNextNodes(String node, Long flowId) {
		return this.flowDao.queryNextNodes(node, flowId);
	}
	
	/**
	 * 查询当前流程节点的上一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryLastNodes(String node, Long flowId) {
		return this.flowDao.queryLastNodes(node, flowId);
	}
	
	/**
	 * 将前端传来的json转换成Flow对象
	 * @param json
	 * @param user 当前登录用户
	 * @return
	 */
	public Flow parseFlow(JSONObject json, User user) throws Exception {
		JSONObject flow = (JSONObject) json.get("props");
		JSONObject flowInfo = (JSONObject) flow.get("props");
		JSONObject nameInfo = flowInfo.getJSONObject("name");
		String name = nameInfo.getString("value");
		if(null == name || "".equals(name.trim())) {
			throw new Exception("请输入流程名称！");
		}
		String type = flowInfo.getJSONObject("key").getString("value");
		if(null == type || "".equals(type.trim())) {
			throw new Exception("请输入流程类型！");
		}
		String memo = flowInfo.getJSONObject("desc").getString("value");
		Flow f = new Flow();
		f.setCreateDate(new Date());
		f.setIsDelete(0);
		f.setIsUse(0);
		f.setMemo(memo);
		f.setName(name);
		f.setType(type);
		//f.setUserId(user.getId());
		return f;
	}
	
	/**
	 * 将前端传来的Json解析成流程节点
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<Node> parseNodes(JSONObject json) throws Exception {
		List<Node> list = new ArrayList<Node> ();
		JSONObject states = json.getJSONObject("states");
		Iterator<String> keysIter = states.keys();
		String key = null;
		Node node = null;
		while(keysIter.hasNext()) {
			key = keysIter.next();
			if(null == key || "".equals(key.trim())) {
				throw new Exception("流程节点获取异常！");
			}
			node = new Node();
			node.setName(key.trim());
			JSONObject nodeJson = states.getJSONObject(key);
			String type = nodeJson.getString("type");
			if(null == type || "".equals(type.trim())) {
				throw new Exception("节点类型获取异常！");
			}
			node.setType(type.trim());
			JSONObject textJson = nodeJson.getJSONObject("text");
			String text = textJson.getString("text");
			if(null == text || "".equals(text.trim())) {
				throw new Exception("请输入节点名称！");
			}
			node.setText(text.trim());
			
			JSONObject attrJson = nodeJson.getJSONObject("attr");
			String attr = attrJson.toString();
			node.setAttr(attr);
			JSONObject propsJson = nodeJson.getJSONObject("props");
			Iterator<String> propKeyIter = propsJson.keys();
			String propKey = null;
			List<Property> props = new ArrayList<Property> ();
			while(propKeyIter.hasNext()) {
				propKey = propKeyIter.next();
				JSONObject jsonProp = propsJson.getJSONObject(propKey);
				Property prop = new Property();
				prop.setName(propKey);
				prop.setPropType("node");
				String editor = jsonProp.getString("editor");
				prop.setEditor(jsonProp.getString("editor"));
				prop.setLabel(jsonProp.getString("label"));
				prop.setText(propKey);
				String value = "";
				if(propKey.equals("passBtn") || propKey.equals("rejectBtn")) {
					//默认是允许通过或者退回的
					if(null == jsonProp.getString("value") || "".equals(jsonProp.getString("value"))) {
						value = "1";
					} else {
						value = jsonProp.getString("value");
					}
				} else if("isCountersign".equals(propKey) || "isParallel".equals(propKey)) {
					//默认是允许通过或者退回的
					if(null == jsonProp.getString("value") || "".equals(jsonProp.getString("value"))) {
						value = "0";
					} else {
						value = jsonProp.getString("value");
					}
				} else if("status".equals(propKey)) {
					if(null == jsonProp.getString("value") || "".equals(jsonProp.getString("value"))) {
						//默认task的status为3，end的status为4
						if(node.getType().equals("task")) {
							value = "3";
						} else if(node.getType().equals("end")) {
							value = "4";
						}
					} else {
						value = jsonProp.getString("value");
					}
				} else if("isJump".equals(propKey)) {	//是否可以跳过，默认为1，可以跳过
					if(null == jsonProp.getString("value") || "".equals(jsonProp.getString("value"))) {
						value = "1";
					} else {
						value = jsonProp.getString("value");
					}
				} else {
					value = jsonProp.getString("value");
				}
				prop.setValue(value);
				props.add(prop);
			}
			node.setProps(props);
			list.add(node);
		}
		return list;
	}
	
	/**
	 * 将前端传来的Json解析成流程节点的路径
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<Path> parsePaths(JSONObject json) throws Exception {
		List<Path> list = new ArrayList<Path> ();
		JSONObject paths = json.getJSONObject("paths");
		Iterator<String> keysIter = paths.keys();
		String key = null;
		Path path = null;
		while(keysIter.hasNext()) {
			key = keysIter.next();
			if(null == key || "".equals(key.trim())) {
				throw new Exception("流程节点获取异常！");
			}
			path = new Path();
			path.setName(key.trim());
			path.setText(key.trim());
			JSONObject nodeJson = paths.getJSONObject(key);
			String fromNode = nodeJson.getString("from");
			if(null == fromNode || "".equals(fromNode.trim())) {
				throw new Exception("路径的上一节点获取异常！");
			}
			path.setFromNode(fromNode);
			String toNode = nodeJson.getString("to");
			if(null == toNode || "".equals(toNode.trim())) {
				throw new Exception("路径的下一节点获取异常");
			}
			path.setToNode(toNode);
			String dots = nodeJson.getString("dots");
			path.setDots(dots);
			String textPos = nodeJson.getString("textPos");
			path.setTextPos(textPos);
			JSONObject textJson = nodeJson.getJSONObject("text");
			String text = textJson.getString("text");
			path.setText(text);
			
			JSONObject attrJson = nodeJson.getJSONObject("attr");
			String attr = attrJson.toString();
			//path.setAttr(attr);
			JSONObject propsJson = nodeJson.getJSONObject("props");
			Iterator<String> propKeyIter = propsJson.keys();
			String propKey = null;
			List<Property> props = new ArrayList<Property> ();
			while(propKeyIter.hasNext()) {
				propKey = propKeyIter.next();
				JSONObject jsonProp = propsJson.getJSONObject(propKey);
				Property prop = new Property();
				prop.setName(propKey);
				prop.setPropType("path");
				prop.setEditor(jsonProp.getString("editor"));
				prop.setLabel(jsonProp.getString("label"));
				prop.setText(propKey);
				prop.setValue(jsonProp.getString("value"));
				props.add(prop);
			}
			path.setProps(props);
			list.add(path);
		}
		return list;
	}
	
}
