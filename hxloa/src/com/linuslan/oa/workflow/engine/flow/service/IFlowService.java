package com.linuslan.oa.workflow.engine.flow.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;

public interface IFlowService extends IBaseService {

	/**
	 * 分页查询流程
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Flow> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 通过id查询流程
	 * @param id
	 * @return
	 */
	public Flow queryById(Long id);
	
	/**
	 * 新增流程
	 * @param flow
	 * @return
	 */
	public boolean add(Flow flow);
	
	/**
	 * 更新流程
	 * @param flow
	 * @return
	 */
	public boolean update(Flow flow);
	
	/**
	 * 新增流程，包含属性
	 * @param nodes
	 * @param paths
	 * @param properties
	 * @return
	 */
	public boolean add(Flow flow, List<Node> nodes, List<Path> paths) throws RuntimeException;
	
	/**
	 * 更新流程，包含属性
	 * @param nodes
	 * @param paths
	 * @param properties
	 * @return
	 */
	public boolean update(Flow flow, List<Node> nodes, List<Path> paths) throws RuntimeException;
	
	/**
	 * 查询流程节点
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNodesByFlowId(Long flowId);
	
	/**
	 * 查询流程走向路径
	 * @param flowId
	 * @return
	 */
	public List<Path> queryPathsByFlowId(Long flowId);
	
	/**
	 * 查询流程当前节点的下一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryNextNodes(String node, Long flowId);
	
	/**
	 * 查询当前流程节点的上一节点
	 * @param node
	 * @param flowId
	 * @return
	 */
	public List<Node> queryLastNodes(String node, Long flowId);
	
	/**
	 * 将前端传来的json转换成Flow对象
	 * @param json
	 * @param user 当前登录用户
	 * @return
	 */
	public Flow parseFlow(JSONObject json, User user) throws Exception;
	
	/**
	 * 将前端传来的Json解析成流程节点
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<Node> parseNodes(JSONObject json) throws Exception;
	
	/**
	 * 将前端传来的Json解析成流程节点的路径
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<Path> parsePaths(JSONObject json) throws Exception;
	
}
