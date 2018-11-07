package com.linuslan.oa.system.userSalary.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.userSalary.dao.IUserSalaryDao;
import com.linuslan.oa.system.userSalary.model.UserSalary;
import com.linuslan.oa.system.userSalary.service.IUserSalaryService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.salary.model.Salary;

@Component("userSalaryService")
@Transactional
public class IUserSalaryServiceImpl extends IBaseServiceImpl implements
		IUserSalaryService {

	@Autowired
	private IUserSalaryDao userSalaryDao;
	
	/**
	 * 通过菜单id分页查询员工薪资
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<UserSalary> queryPageByUserId(Long userId, Map<String, String> paramMap, int page, int rows) {
		return this.userSalaryDao.queryPageByUserId(userId, paramMap, page, rows);
	}
	
	/**
	 * 查询生效的工资，同时查询year年month月对应的员工的绩效
	 * @return
	 */
	public List<Map<String, Object>> queryAllUserSalarys(int year, int month) {
		return this.userSalaryDao.queryAllUserSalarys(year, month);
	}
	
	/**
	 * 通过id查询员工薪资
	 * @param id
	 * @return
	 */
	public UserSalary queryById(Long id) {
		return this.userSalaryDao.queryById(id);
	}
	
	/**
	 * 通过用户id查询其生效的工资
	 * @param userId
	 * @return
	 */
	public UserSalary queryEffectSalaryByUserId(Long userId) {
		return this.userSalaryDao.queryEffectSalaryByUserId(userId);
	}
	
	/**
	 * 如果没有生效工资，则查询即将生效的工资，即生效时间在当前时间之后，且离当前时间最近的工资
	 * @param userId
	 * @return
	 */
	public UserSalary queryImmediatelyEffectSalaryByUserId(Long userId) {
		return this.userSalaryDao.queryImmediatelyEffectSalaryByUserId(userId);
	}
	
	/**
	 * 查询用户的生效工资，如果没有生效工资，则查询即将生效的工资，即生效时间在当前时间之后，且离当前时间最近的工资
	 * @param userId
	 * @return
	 */
	public UserSalary querySalaryByUserId(Long userId) {
		UserSalary salary = this.userSalaryDao.queryEffectSalaryByUserId(userId);
		if(null == salary) {
			salary = this.userSalaryDao.queryImmediatelyEffectSalaryByUserId(userId);
		}
		if(null == salary) {
			salary = this.userSalaryDao.queryLastSalaryByUserId(userId);
		}
		return salary;
	}
	
	/**
	 * 新增员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean add(UserSalary userSalary) throws Exception {
		userSalary.setCreateDate(new Date());
		userSalary.setCreateUserId(HttpUtil.getLoginUser().getId());
		userSalary.setIp(HttpUtil.getRequest().getRemoteAddr());
		
		//如果是强制生效，则将该用户的其他薪资改为不生效
		if(null != userSalary.getIsForceEffect() && 1 == userSalary.getIsForceEffect()) {
			this.userSalaryDao.updateUneffectByUserId(userSalary.getUserId());
			userSalary.setStatus(1);
		} else {
			userSalary.setStatus(0);
		}
		return this.userSalaryDao.add(userSalary);
	}
	
	/**
	 * 更新员工薪资
	 * @param userSalary
	 * @return
	 */
	public boolean update(UserSalary userSalary) throws Exception {
		//如果是强制生效，则将该用户的其他薪资改为不生效
		if(null != userSalary.getIsForceEffect() && 1 == userSalary.getIsForceEffect()) {
			this.userSalaryDao.updateUneffectByUserId(userSalary.getUserId());
			userSalary.setStatus(1);
		} else {
			userSalary.setStatus(0);
		}
		return this.userSalaryDao.update(userSalary);
	}
	
	/**
	 * 将用户的其他工资更新为不生效
	 * @param userId
	 * @return
	 */
	public boolean updateUneffectByUserId(Long userId) {
		return this.userSalaryDao.updateUneffectByUserId(userId);
	}
	
	/**
	 * 伪删除员工薪资同时真删除角色的员工薪资资源
	 * @param userSalary
	 * @return
	 * @throws Exception
	 */
	public boolean del(UserSalary userSalary) throws Exception {
		boolean success = false;
		List<UserSalary> list = new ArrayList<UserSalary> ();
		list.add(userSalary);
		String idStr = BeanUtil.parseString(list, "id", ",");
		List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
		this.userSalaryDao.delResourcesByUserSalaryIds(ids);
		this.userSalaryDao.delBatchByIds(ids);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除员工薪资（伪删除，将isDelete更新为1）
	 * @param departmentId
	 * @return
	 */
	public boolean delByDepartmentId(Long departmentId) throws Exception {
		return this.userSalaryDao.delByDepartmentId(departmentId);
	}
	
	public List<UserSalary> queryByDepartmentIds(List<Long> departmentIds) {
		return this.userSalaryDao.queryByDepartmentIds(departmentIds);
	}
	
	/**
	 * 通过员工薪资id集合，删除角色资源里面的员工薪资资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByUserSalaryIds(List<Long> ids) throws Exception {
		return this.userSalaryDao.delResourcesByUserSalaryIds(ids);
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception {
		return this.userSalaryDao.delBatchByIds(ids);
	}
	
	/**
	 * 通过id集合查询对应的员工薪资集合
	 * @param ids
	 * @return
	 */
	public List<UserSalary> queryByIds(List<Long> ids) {
		return this.userSalaryDao.queryByIds(ids);
	}
}
