package com.linuslan.oa.system.userSalary.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.department.service.IDepartmentService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.system.post.service.IPostService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.system.userSalary.service.IUserSalaryService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class UserSalaryAction extends BaseAction {

	@Autowired
	private IUserSalaryService userSalaryService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDepartmentService departmentService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IPostService postService;
	
	private UserSalary userSalary;
	
	private User user;
	
	private Department department;
	
	private Post post;
	
	private List<Post> posts;
	
	private List<Dictionary> types;
	
	private Page<UserSalary> pageData;
	
	public void queryPageByUserId() {
		try {
			this.pageData = this.userSalaryService.queryPageByUserId(this.user.getId(), paramMap, page, rows);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, config);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.userSalary = this.userSalaryService.queryById(this.userSalary.getId());
			this.user = new User();
			this.user.setId(this.userSalary.getUserId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.types = this.dictionaryService.queryByPid(41l);
			this.user = this.userService.queryById(this.user.getId());
			List<Long> list = new ArrayList<Long> ();
			list.add(this.user.getDepartmentId());
			this.posts = this.postService.queryByDepartmentIds(list);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.userSalary) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.userSalaryService.valid(this.userSalary);
			if(this.userSalaryService.add(this.userSalary)) {
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
			if(null == this.userSalary || null == this.userSalary.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			UserSalary persist = this.userSalaryService.queryById(this.userSalary.getId());
			BeanUtil.updateBean(persist, this.userSalary);
			this.userSalaryService.valid(persist);
			if(this.userSalaryService.update(persist)) {
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
			if(null == this.userSalary || null == this.userSalary.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			UserSalary persist = this.userSalaryService.queryById(this.userSalary.getId());
			if(this.userSalaryService.del(persist)) {
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

	public UserSalary getUserSalary() {
		return userSalary;
	}

	public void setUserSalary(UserSalary userSalary) {
		this.userSalary = userSalary;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Page<UserSalary> getPageData() {
		return pageData;
	}

	public void setPageData(Page<UserSalary> pageData) {
		this.pageData = pageData;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Dictionary> getTypes() {
		return types;
	}

	public void setTypes(List<Dictionary> types) {
		this.types = types;
	}
	
}
