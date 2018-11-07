package com.linuslan.oa.system.user.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.util.Page;

public interface IUserService extends IBaseService {

	/**
	 * 分页查询用户
	 * @param paramMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<User> queryPage(Map<String, String> paramMap, int pageNum, int pageSize);
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAll();
	
	/**
	 * 通过id查询用户
	 * @param id
	 * @return
	 */
	public User queryById(Long id);
	
	/**
	 * 通过用户id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryInIds(List<Long> ids);
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public boolean add(User user, List<Long> roleIds, List<Long> otherGroupIds, UserSalary userSalary) throws Exception;
	
	public boolean update(User user);
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public boolean update(User user, List<Long> roleIds, List<Long> otherGroupIds, UserSalary userSalary) throws Exception;
	
	/**
	 * 删除用户，同时删除用户的其他用户组以及角色
	 * @param user
	 * @return
	 */
	public boolean del(User user) throws Exception;
	
	/**
	 * 判断是否有登陆用户
	 * @param user
	 * @return
	 */
	public User login(User user);
	
	/**
	 * 为用户分配公司
	 * @param user
	 * @param companys
	 * @return
	 */
	public boolean assignCompany(User user, List<Company> companys);
	
	public void valid(User user) throws Exception;
}
