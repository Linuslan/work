package com.linuslan.oa.system.user.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.Page;

public interface IUserDao extends IBaseDao {
	/**
	 * 分页查询用户
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
	 * 通过部门id集合查询归属人员
	 * @param ids
	 * @return
	 */
	public List<User> queryByDepartmentIds(List<Long> ids);
	
	/**
	 * 通过用户id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryInIds(List<Long> ids);
	
	/**
	 * 通过用户组id集合查询用户
	 * @param ids
	 * @return
	 */
	public List<User> queryByGroupIds(List<Long> ids);
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public boolean add(User user);
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public boolean update(User user);
	
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
}
