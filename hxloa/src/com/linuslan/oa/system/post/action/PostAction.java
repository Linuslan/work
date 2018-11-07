package com.linuslan.oa.system.post.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.system.post.service.IPostService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

public class PostAction extends BaseAction {

	@Autowired
	private IPostService postService;
	
	private Post post;
	
	private Department department;
	
	private Page<Post> pageData;
	
	public void queryPageByDepartmentId() {
		try {
			this.pageData = this.postService.queryPageByDepartmentId(this.department.getId(), paramMap, page, rows);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, config);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryByDepartmentId() {
		try {
			Long departmentId = CodeUtil.parseLong(HttpUtil.getRequest().getParameter("departmentId"));
			if(null != departmentId) {
				List<Post> posts = this.postService.queryByDepartmentId(departmentId);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
				JSONArray json = JSONArray.fromObject(posts, jsonConfig);
				this.resultMap.put("posts", json.toString());
				this.resultMap.put("success", true);
			} else {
				this.setupFailureMap("部门id为空");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		this.printResultMap();
	}
	
	public String queryById() {
		try {
			this.post = this.postService.queryById(this.post.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.post) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.postService.valid(this.post);
			if(this.postService.add(this.post)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void update() {
		try {
			if(null == this.post || null == this.post.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Post persist = this.postService.queryById(this.post.getId());
			BeanUtil.updateBean(persist, this.post);
			this.postService.valid(persist);
			if(this.postService.update(persist)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void del() {
		try {
			if(null == this.post || null == this.post.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			Post persist = this.postService.queryById(this.post.getId());
			if(this.postService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getCause().toString());
		}
		this.printMap(resultMap);
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Page<Post> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Post> pageData) {
		this.pageData = pageData;
	}
}
