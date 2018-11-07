package com.linuslan.oa.system.user.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.servlet.RandomValidateCode;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.group.service.IGroupService;
import com.linuslan.oa.system.post.model.Post;
import com.linuslan.oa.system.post.service.IPostService;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.role.service.IRoleService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.system.userSalary.service.IUserSalaryService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.MD5Util;
import com.linuslan.oa.util.Page;

@Controller
public class UserAction extends BaseAction {
	
	private Page<User> pageData;
	
	private User user;
	
	private List<User> users;
	
	private List<Long> roleIds;
	
	private List<Role> roles;
	
	/**
	 * 用户选中的角色
	 */
	private List<Role> userRoles;
	
	/**
	 * 用户选中的角色id
	 */
	private String userRoleIds;
	
	/**
	 * 用户选中的其他用户组
	 */
	private List<Group> userGroups;
	
	/**
	 * 存放前端传入的用户组id
	 */
	private List<Long> groupIds;
	
	/**
	 * 用户选中的其他用户组的id
	 */
	private String userGroupIds;
	
	/**
	 * 用户选中的其他用户组的组名
	 */
	private String userGroupTexts;
	
	private UserSalary userSalary;
	
	private List<Post> posts;
	
	private List<Company> companys;
	
	private List<Long> companyIds;
	
	private List<Company> userCompanys;
	
	/**
	 * 用户选中的公司id
	 */
	private String userCompanyIds;
	
	private List<Dictionary> types;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IPostService postService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IUserSalaryService userSalaryService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IGroupService groupService;
	
	/**
	 * 分页查询用户
	 */
	public void queryPage() {
		try {
			if(null == this.paramMap) {
				this.paramMap = new HashMap<String, String> ();
			}
			if(CodeUtil.isEmpty(this.paramMap.get("isLeave"))) {
				this.paramMap.put("isLeave", "1");
			}
			this.pageData = this.userService.queryPage(paramMap, page, rows);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, config);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initSelect() {
		try {
			List<Company> company = this.companyService.queryAllCompanys();
			Map<String, List<? extends Object>> maps = new HashMap<String, List<? extends Object>> ();
			maps.put("companyId", company);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(maps, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * 通过id查询用户，返回各个页面
	 * @return
	 */
	public String queryById() {
		try {
			if(null != this.user && null != this.user.getId()) {
				this.user = this.userService.queryById(this.user.getId());
				this.userRoles = this.roleService.queryByUserId(this.user.getId());
				this.userGroups = this.groupService.queryByUserId(this.user.getId());
				this.userGroupIds = BeanUtil.parseString(userGroups, "id", ",", null);
				this.userGroupTexts = BeanUtil.parseString(userGroups, "text", ",", null);
				this.userSalary = this.userSalaryService.querySalaryByUserId(this.user.getId());
				List<Long> list = new ArrayList<Long> ();
				list.add(this.user.getDepartmentId());
				this.posts = this.postService.queryByDepartmentIds(list);
				this.userCompanys = this.companyService.queryByUserId(user.getId());
			}
			this.roles = this.roleService.queryAll();
			this.users = this.userService.queryAll();
			this.companys = this.companyService.queryAllCompanys();
			this.types = this.dictionaryService.queryByPid(41l);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.user) {
				CodeUtil.throwExcep("获取用户数据异常");
			}
			this.userService.valid(this.user);
			/**
			 * 如果没有选择角色，则默认授予普通管理员角色
			 */
			if(null == this.roleIds || 0 >= this.roleIds.size()) {
				this.roleIds = new ArrayList<Long> ();
				this.roleIds.add(2l);
			}
			this.groupIds = BeanUtil.parseStringToLongList(this.userGroupIds, ",");
			if(this.userService.add(this.user, roleIds, this.groupIds, this.userSalary)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void update() {
		try {
			if(null == this.user || null == user.getId()) {
				CodeUtil.throwExcep("获取用户数据异常");
			}
			User persist = this.userService.queryById(this.user.getId());
			BeanUtil.updateBean(persist, this.user);
			this.userService.valid(persist);
			/**
			 * 如果没有选择角色，则默认授予普通管理员角色
			 */
			if(null == this.roleIds || 0 >= this.roleIds.size()) {
				this.roleIds = new ArrayList<Long> ();
				this.roleIds.add(2l);
			}
			this.groupIds = BeanUtil.parseStringToLongList(this.userGroupIds, ",");
			if(this.userService.update(persist, roleIds, this.groupIds, this.userSalary)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void del() {
		try {
			if(null == this.user || null == this.user.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			User persist = this.userService.queryById(this.user.getId());
			if(null == persist) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.userService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void login() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		StringBuffer results = new StringBuffer("{\"success\": ");
		String validateCode = (String) request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY);
		String validateNum = request.getParameter("validateNum");
		try {
			if(null == this.user) {
				CodeUtil.throwExcep("登陆失败，获取用户数据异常");
			}
			if(CodeUtil.isEmpty(this.user.getLoginName())) {
				CodeUtil.throwExcep("登陆失败，请输入用户名");
			}
			if(CodeUtil.isEmpty(this.user.getPassword())) {
				CodeUtil.throwExcep("登陆失败，请输入密码");
			}
			if(CodeUtil.isEmpty(validateNum)) {
				CodeUtil.throwExcep("登陆失败，请输入验证码");
			}
			this.user.setLoginName(this.user.getLoginName().trim());
			this.user.setPassword(MD5Util.md5(this.user.getPassword().trim()));
			if(!validateNum.equals(validateCode)) {
				CodeUtil.throwExcep("登陆失败，验证码不正确");
			}
			this.user = this.userService.login(user);
			if(null != user) {
				HttpSession session = request.getSession();
				session.setAttribute(ConstantVar.LOGINUSER, user);
				
				//查询用户的其他用户组
				List<Group> groups = this.groupService.queryByUserId(user.getId());
				
				//查询用户的主用户组对象
				Group mainGroup = this.groupService.queryById(user.getGroupId());
				
				//添加到groups中，此时groups存放的是当前登录用户的所有用户组
				groups.add(mainGroup);
				
				//将当前登录用户的所有用户组添加到session中，方便流程获取
				session.setAttribute(ConstantVar.LOGINUSERGROUPS, groups);
				session.setAttribute(ConstantVar.LOGINUSER_MAINGROUP, mainGroup);
				this.setupSuccessMap("登陆成功");
			} else {
				CodeUtil.throwExcep("登陆失败，用户名或密码不正确");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void logout() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			session.removeAttribute(ConstantVar.LOGINUSER);
			session.removeAttribute(ConstantVar.LOGINUSER_MAINGROUP);
			session.removeAttribute(ConstantVar.LOGINUSERGROUPS);
			session.invalidate();
			this.setupSuccessMap("退出成功");
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap("系统异常，请联系管理员");
		}
		this.printResultMap();
	}
	
	/**
	 * 修改密码
	 */
	public void updatePassword() {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			User user = HttpUtil.getLoginUser();
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String checkPassword = request.getParameter("checkPassword");
			if(CodeUtil.isEmpty(oldPassword)) {
				CodeUtil.throwExcep("请输入旧密码");
			}
			oldPassword = MD5Util.md5(oldPassword.trim());
			if(!oldPassword.equals(user.getPassword().trim())) {
				CodeUtil.throwExcep("旧密码输入错误");
			}
			if(CodeUtil.isEmpty(newPassword)) {
				CodeUtil.throwExcep("请输入新密码");
			}
			if(CodeUtil.isEmpty(checkPassword)) {
				CodeUtil.throwExcep("请输入确认密码");
			}
			if(!newPassword.equals(checkPassword)) {
				CodeUtil.throwExcep("两次输入的新密码不同");
			}
			
			User persist = this.userService.queryById(user.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您的账号异常，请联系管理员");
			}
			persist.setRealPassword(newPassword.trim());
			persist.setPassword(MD5Util.md5(newPassword.trim()));
			if(this.userService.update(persist)) {
				this.setupSuccessMap("密码修改成功，重新登录生效");
			} else {
				CodeUtil.throwExcep("密码修改失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void assignCompany() {
		try {
			if(null == this.companyIds || 0 >= this.companyIds.size()) {
				CodeUtil.throwExcep("请至少选择一家公司");
			}
			List<Company> companys = this.companyService.queryByIds(companyIds);
			if(null == companys || 0 >= companys.size()) {
				CodeUtil.throwExcep("您所选择的公司不存在");
			}
			if(null == this.user || null == this.user.getId()) {
				CodeUtil.throwExcep("请选择人员");
			}
			this.user = this.userService.queryById(this.user.getId());
			if(null == this.user || null == this.user.getId()) {
				CodeUtil.throwExcep("您所选择的人员不存在");
			}
			if(this.userService.assignCompany(user, companys)) {
				this.setupSuccessMap("公司分配成功");
			} else {
				CodeUtil.throwExcep("分配公司保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void fire() {
		try {
			if(null == this.user || null == this.user.getId()) {
				CodeUtil.throwExcep("您所选择的对象为空");
			}
			User persist = this.userService.queryById(this.user.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所选择的对象不存在");
			}
			if(CodeUtil.isEmpty(this.user.getLeaveMemo())) {
				CodeUtil.throwExcep("请输入离职原因");
			}
			persist.setLeaveMemo(this.user.getLeaveMemo());
			if(null == this.user.getLeaveDate()) {
				CodeUtil.throwExcep("请选择离职时间");
			}
			persist.setLeaveDate(this.user.getLeaveDate());
			persist.setIsLeave(0);
			if(this.userService.update(persist)) {
				setupSuccessMap("离职办理成功");
			} else {
				CodeUtil.throwExcep("离职办理数据更新失败");
			}
		} catch(Exception ex) {
			setupFailureMap("离职失败，失败原因："+ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void resume() {
		try {
			if(null == this.user || null == this.user.getId()) {
				CodeUtil.throwExcep("您所选择的对象为空");
			}
			User persist = this.userService.queryById(this.user.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所选择的对象不存在");
			}
			persist.setIsLeave(1);
			if(this.userService.update(persist)) {
				setupSuccessMap("复职办理成功");
			} else {
				CodeUtil.throwExcep("复职办理数据更新失败");
			}
		} catch(Exception ex) {
			setupFailureMap("复职失败，失败原因："+ex.getMessage());
		}
		this.printResultMap();
	}

	public Page<User> getPageData() {
		return pageData;
	}

	public void setPageData(Page<User> pageData) {
		this.pageData = pageData;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<Role> userRoles) {
		this.userRoles = userRoles;
	}

	public List<Group> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<Group> userGroups) {
		this.userGroups = userGroups;
	}

	public String getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(String userRoleIds) {
		this.userRoleIds = userRoleIds;
	}

	public String getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(String userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

	public String getUserGroupTexts() {
		return userGroupTexts;
	}

	public void setUserGroupTexts(String userGroupTexts) {
		this.userGroupTexts = userGroupTexts;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public UserSalary getUserSalary() {
		return userSalary;
	}

	public void setUserSalary(UserSalary userSalary) {
		this.userSalary = userSalary;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<Dictionary> getTypes() {
		return types;
	}

	public void setTypes(List<Dictionary> types) {
		this.types = types;
	}

	public List<Long> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Long> companyIds) {
		this.companyIds = companyIds;
	}

	public List<Company> getUserCompanys() {
		return userCompanys;
	}

	public void setUserCompanys(List<Company> userCompanys) {
		this.userCompanys = userCompanys;
	}

	public String getUserCompanyIds() {
		return userCompanyIds;
	}

	public void setUserCompanyIds(String userCompanyIds) {
		this.userCompanyIds = userCompanyIds;
	}
	
}
