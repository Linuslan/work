package com.linuslan.oa.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.user.model.User;
import com.opensymphony.xwork2.ActionContext;

public class HttpUtil {
	public static User getLoginUser() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		User user = (User) request.getSession().getAttribute(ConstantVar.LOGINUSER);
		return user;
	}
	
	public static HttpServletRequest getRequest() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}
	
	/**
	 * 获取当前登录用户的所有用户组
	 * @return
	 */
	public static List<Group> getLoginUserGroups() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		List<Group> groups = (List<Group>) request.getSession().getAttribute(ConstantVar.LOGINUSERGROUPS);
		if(null == groups) {
			groups = new ArrayList<Group> ();
		}
		return groups;
	}
	
	/**
	 * 获取当前登录用户的所有用户组的Id集合
	 * @return
	 */
	public static List<Long> getLoginUserGroupIds() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		List<Group> groups = (List<Group>) request.getSession().getAttribute(ConstantVar.LOGINUSERGROUPS);
		if(null == groups) {
			groups = new ArrayList<Group> ();
		}
		String ids = BeanUtil.parseString(groups, "id", ",");
		List<Long> idList = BeanUtil.parseStringToLongList(ids, ",");
		return idList;
	}
	
	/**
	 * 获取当前登录用户的主用户组
	 * @return
	 */
	public static Group getLoginUserMainGroup() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		Group group = (Group) request.getSession().getAttribute(ConstantVar.LOGINUSER_MAINGROUP);
		return group;
	}
	
	public static String getRealPath(String path) {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		String realpath = request.getServletContext().getRealPath(path);
		return realpath;
	}
	
	public static String getAppName() {
		ActionContext ac = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		String appName = request.getContextPath();
		return appName;
	}
}
