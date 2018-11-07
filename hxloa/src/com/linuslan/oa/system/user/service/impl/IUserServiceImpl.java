package com.linuslan.oa.system.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.group.dao.IGroupDao;
import com.linuslan.oa.system.role.dao.IRoleDao;
import com.linuslan.oa.system.user.dao.IUserDao;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.system.userSalary.dao.IUserSalaryDao;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.MD5Util;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.ValidationUtil;

@Component("userService")
@Transactional
public class IUserServiceImpl extends IBaseServiceImpl implements IUserService {
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IUserSalaryDao userSalaryDao;
	
	@Autowired
	private IGroupDao groupDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	/**
	 * 分页查询用户
	 */
	public Page<User> queryPage(Map<String, String> paramMap, int pageNum, int pageSize) {
		return this.userDao.queryPage(paramMap, pageNum, pageSize);
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAll() {
		return this.userDao.queryAll();
	}
	
	/**
	 * 通过id查询用户
	 * @param id
	 * @return
	 */
	public User queryById(Long id) {
		return this.userDao.queryById(id);
	}
	
	/**
	 * 通过用户id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryInIds(List<Long> ids) {
		return this.userDao.queryInIds(ids);
	}
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public boolean add(User user, List<Long> roleIds, List<Long> otherGroupIds, UserSalary userSalary) throws Exception {
		boolean success = false;
		this.userDao.add(user);
		this.groupDao.addUserGroups(user.getId(), otherGroupIds);
		this.roleDao.addUserRoles(user.getId(), roleIds);
		userSalary.setCreateDate(new Date());
		userSalary.setCreateUserId(HttpUtil.getLoginUser().getId());
		userSalary.setIp(HttpUtil.getRequest().getRemoteAddr());
		userSalary.setUserId(user.getId());
		if(null == userSalary.getDepartmentId()) {
			userSalary.setDepartmentId(user.getDepartmentId());
		}
		if(null == userSalary.getPostId()) {
			userSalary.setPostId(user.getPostId());
		}
		//如果是强制生效，则将该用户的其他薪资改为不生效
		if(null != userSalary.getIsForceEffect() && 1 == userSalary.getIsForceEffect()) {
			this.userSalaryDao.updateUneffectByUserId(userSalary.getUserId());
			userSalary.setStatus(1);
		} else {
			userSalary.setStatus(0);
		}
		this.userSalaryDao.add(userSalary);
		success = true;
		return success;
	}
	
	public boolean update(User user) {
		return this.userDao.update(user);
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public boolean update(User user, List<Long> roleIds, List<Long> otherGroupIds, UserSalary userSalary) throws Exception {
		boolean success = false;
		this.userDao.update(user);
		this.groupDao.delByUserId(user.getId());
		this.groupDao.addUserGroups(user.getId(), otherGroupIds);
		this.roleDao.delByUserId(user.getId());
		this.roleDao.addUserRoles(user.getId(), roleIds);
		if(null == userSalary.getDepartmentId()) {
			userSalary.setDepartmentId(user.getDepartmentId());
		}
		if(null == userSalary.getPostId()) {
			userSalary.setPostId(user.getPostId());
		}
		//如果是强制生效，则将该用户的其他薪资改为不生效
		if(null != userSalary.getIsForceEffect() && 1 == userSalary.getIsForceEffect()) {
			this.userSalaryDao.updateUneffectByUserId(userSalary.getUserId());
			userSalary.setStatus(1);
		} else {
			userSalary.setStatus(0);
		}
		if(null == userSalary.getId()) {
			userSalary.setUserId(user.getId());
			userSalary.setCreateDate(new Date());
			userSalary.setCreateUserId(HttpUtil.getLoginUser().getId());
			userSalary.setIp(HttpUtil.getRequest().getRemoteAddr());
			this.userSalaryDao.add(userSalary);
		} else {
			UserSalary persistSalary = this.userSalaryDao.queryById(userSalary.getId());
			BeanUtil.updateBean(persistSalary, userSalary);
			this.userSalaryDao.update(persistSalary);
		}
		
		success = true;
		return success;
	}
	
	/**
	 * 删除用户，同时删除用户的其他用户组以及角色
	 * @param user
	 * @return
	 */
	public boolean del(User user) throws Exception {
		boolean success = false;
		user.setIsDelete(1);
		this.userDao.update(user);
		this.groupDao.delByUserId(user.getId());
		this.roleDao.delByUserId(user.getId());
		success = true;
		return success;
	}
	
	/**
	 * 判断是否有登陆用户
	 * @param user
	 * @return
	 */
	public User login(User user) {
		return this.userDao.login(user);
	}
	
	/**
	 * 为用户分配公司
	 * @param user
	 * @param companys
	 * @return
	 */
	public boolean assignCompany(User user, List<Company> companys) {
		return this.userDao.assignCompany(user, companys);
	}
	
	/**
	 * 验证用户的属性是否正确
	 * @param user
	 * @throws Exception
	 */
	public void valid(User user) throws Exception {
		if(null  == user.getName() || "".equals(user.getName().trim())) {
			CodeUtil.throwExcep("请输入用户名");
		}
		user.setName(user.getName().trim());
		if(CodeUtil.isEmpty(user.getEmployeeNo())) {
			CodeUtil.throwExcep("请输入工号");
		}
		user.setEmployeeNo(user.getEmployeeNo().trim());
		if(!ValidationUtil.isAlphaNumberic(user.getEmployeeNo())) {
			CodeUtil.throwExcep("工号只能包含字母，数字或下划线");
		}
		if(null == user.getLoginName() || "".equals(user.getLoginName().trim())) {
			//CodeUtil.throwExcep("请输入登录名");
			user.setLoginName(user.getEmployeeNo().trim());
		}
		user.setLoginName(user.getLoginName().trim());
		if(!ValidationUtil.isAlphaNumberic(user.getLoginName())) {
			CodeUtil.throwExcep("登录名只能包含字母，数字或下划线");
		}
		if(null == user.getPassword() || "".equals(user.getPassword().trim())) {
			CodeUtil.throwExcep("请输入密码");
		}
		user.setRealPassword(user.getPassword().trim());
		user.setPassword(MD5Util.md5(user.getPassword().trim()));
		if(!ValidationUtil.isAlphaNumberic(user.getPassword())) {
			CodeUtil.throwExcep("密码只能包含字母，数字或下划线");
		}
		if(null == user.getTelephone() || "".equals(user.getTelephone().trim())) {
			CodeUtil.throwExcep("请输入联系电话");
		}
		user.setTelephone(user.getTelephone().trim());
		if(!ValidationUtil.isNumber(user.getTelephone())) {
			CodeUtil.throwExcep("联系电话只能包含数字");
		}
		if(null == user.getDepartmentId()) {
			CodeUtil.throwExcep("请选择归属部门");
		}
		if(null == user.getGroupId()) {
			CodeUtil.throwExcep("请选择主用户组");
		}
	}
	
}
