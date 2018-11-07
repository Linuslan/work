package com.linuslan.oa.workflow.engine.flow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.flow.model.Flow;
import com.linuslan.oa.workflow.engine.flow.model.Node;
import com.linuslan.oa.workflow.engine.flow.model.Path;
import com.linuslan.oa.workflow.engine.flow.model.Property;
import com.linuslan.oa.workflow.engine.flow.service.IFlowService;

/**
 * 流程引擎管理界面的action
 * @author LinusLan
 *
 */
@Controller
public class FlowAction extends BaseAction {
	
	@Autowired
	private IFlowService flowService;
	
	@Autowired
	private ICompanyService companyService;
	
	private Flow flow;
	
	private Page<Flow> pageData;
	
	private List<Company> companys;
	
	private List<Long> companyIds;
	
	public String queryPage() {
		try {
			pageData = this.flowService.queryPage(paramMap, this.page, this.rows);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, config);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String queryById() {
		try {
			this.flow = this.flowService.queryById(this.flow.getId());
			this.companys = this.companyService.queryAllCompanys();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void init() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			StringBuffer results = new StringBuffer("{");
			if(null == this.flow || null == this.flow.getId()) {
				throw new Exception("获取流程ID异常！");
			}
			this.flow = this.flowService.queryById(this.flow.getId());
			if(null == flow || null == flow.getId()) {
				throw new Exception("流程不存在！");
			}
			results.append("props:{props:{");
			results.append("name:{value: \""+flow.getName()+"\"}, ");
			results.append("key:{value: \""+flow.getType()+"\"}, ");
			results.append("desc:{value: \""+(flow.getMemo()==null ? "":flow.getMemo())+"\"}");
			results.append("}}");
			results.append(", states: {");
			List<Node> nodes = this.flowService.queryNodesByFlowId(this.flow.getId());
			List<Path> paths = this.flowService.queryPathsByFlowId(this.flow.getId());
			Node node = null;
			Iterator<Node> iter = nodes.iterator();
			while(iter.hasNext()) {
				node = iter.next();
				results.append(node.getName()+":{rectName:\""+node.getName()+"\",");
				results.append("type: \""+node.getType()+"\", ");
				results.append("text: {text: \""+node.getText()+"\"}, ");
				results.append("attr: "+node.getAttr()+", ");
				results.append("props: {");
				List<Property> props = node.getProps();
				Iterator<Property> propIter = props.iterator();
				Property prop = null;
				while(propIter.hasNext()) {
					prop = propIter.next();
					results.append(prop.getName()+":{label: \""+prop.getLabel()+"\", ");
					results.append("name: \""+prop.getName()+"\", ");
					results.append("value: \""+(prop.getValue() == null ? "" : prop.getValue())+"\", ");
					results.append("editor: "+prop.getEditor());
					results.append("}");
					if(propIter.hasNext()) {
						results.append(", ");
					}
				}
				results.append("}");	//props end
				results.append("}"); //node end
				if(iter.hasNext()) {
					results.append(", ");
				}
			}
			results.append("}, ");	//end states
			results.append("paths: {");
			Iterator<Path> pathIter = paths.iterator();
			Path path = null;
			while(pathIter.hasNext()) {
				path = pathIter.next();
				results.append(path.getName()+": {pathName:\""+path.getName()+"\",");
				results.append("from: \""+path.getFromNode()+"\", ");
				results.append("to: \""+path.getToNode()+"\", ");
				results.append("dots: "+path.getDots()+", ");
				results.append("text: {text: \""+path.getText()+"\"}, ");
				results.append("textPos: "+path.getTextPos()+", ");
				results.append("props: {");
				List<Property> props = path.getProps();
				Iterator<Property> propIter = props.iterator();
				Property prop = null;
				while(propIter.hasNext()) {
					prop = propIter.next();
					results.append(prop.getName()+":{label: \""+prop.getLabel()+"\", ");
					results.append("name: \""+prop.getName()+"\", ");
					results.append("value: \""+(prop.getValue() == null ? "" : prop.getValue())+"\", ");
					results.append("editor: "+prop.getEditor());
					results.append("}");
					if(propIter.hasNext()) {
						results.append(", ");
					}
				}
				results.append("}");	//end props
				results.append("}"); //path end
				if(pathIter.hasNext()) {
					results.append(", ");
				}
			}
			results.append("}");	//end paths
			results.append("}");
			response.getWriter().print(results.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void add() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		StringBuffer results = new StringBuffer("{\"success\": ");
		User user = (User) session.getAttribute("user");
		try {
			String data = request.getParameter("data");
			JSONObject json = JSONObject.fromObject(data);
			Flow flow = this.flowService.parseFlow(json, user);
			List<Node> nodes = this.flowService.parseNodes(json);
			List<Path> paths = this.flowService.parsePaths(json);
			if(this.flowService.add(flow, nodes, paths)) {
				results.append(true+", \"msg\": \"保存成功！\"");
			} else {
				throw new Exception("保存失败！");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			results.append(false+", \"msg\": \"保存失败\"");
		}
		results.append("}");
		try {
			response.getWriter().print(results.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void update() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		StringBuffer results = new StringBuffer("{\"success\": ");
		try {
			String data = request.getParameter("data");
			JSONObject json = JSONObject.fromObject(data);
			if(null == flow || null == this.flow.getId()) {
				throw new Exception("获取数据异常！");
			}
			Flow tempFlow = this.flowService.parseFlow(json, user);
			tempFlow.setId(this.flow.getId());
			List<Node> nodes = this.flowService.parseNodes(json);
			List<Path> paths = this.flowService.parsePaths(json);
			if(this.flowService.update(tempFlow, nodes, paths)) {
				results.append(true+", \"msg\": \"保存成功！\"");
			} else {
				throw new Exception("保存失败！");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			results.append(false+", \"msg\": \""+ex.getMessage()+"\"");
		}
		results.append("}");
		try {
			response.getWriter().print(results.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 分配流程归属的公司
	 */
	public void assignCompany() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		StringBuffer results = new StringBuffer("{\"success\": ");
		try {
			if(null == this.companyIds || 0 >= this.companyIds.size()) {
				CodeUtil.throwExcep("请至少选择一家公司");
			}
			String flowIdStr = request.getParameter("flowId");
			if(null == flowIdStr || "".equals(flowIdStr.trim())) {
				throw new Exception("获取流程ID异常！");
			}
			Long id = null;
			try {
				id = Long.parseLong(flowIdStr.trim());
			} catch(Exception ex) {
				throw new Exception("流程ID解析异常！");
			}
			Flow persist = this.flowService.queryById(id);
			if(null == persist || null == persist.getId()) {
				throw new Exception("您所更新的流程不存在！");
			}
			List<Company> companys = this.companyService.queryByIds(companyIds);
			if(0 >= companys.size()) {
				throw new Exception("您所选择的公司都不存在！");
			}
			persist.setCompanys(companys);
			if(this.flowService.update(persist)) {
				results.append(true+", \"msg\": \"保存成功！\"");
			} else {
				throw new Exception("保存失败！");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			results.append(false+", \"msg\": \""+ex.getMessage()+"\"");
		}
		results.append("}");
		try {
			response.getWriter().print(results.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void delById() {
		try {
			if(null == this.flow || null == this.flow.getId()) {
				CodeUtil.throwExcep("获取流程数据异常");
			}
			Flow persist = this.flowService.queryById(this.flow.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的流程不存在");
			}
			if(0 < persist.getInstanceCount()) {
				CodeUtil.throwExcep("删除失败，当前流程有正在运行的流程实例");
			}
			persist.setIsDelete(1);
			if(this.flowService.update(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwRuntimeExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public Page<Flow> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Flow> pageData) {
		this.pageData = pageData;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Long> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Long> companyIds) {
		this.companyIds = companyIds;
	}
	
}
