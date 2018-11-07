package com.linuslan.oa.system.group.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.group.dao.impl.IGroupDaoImpl;
import com.linuslan.oa.system.group.service.IGroupService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ValidationUtil;

@Component("groupService")
@Transactional
public class IGroupServiceImpl extends IBaseServiceImpl implements
		IGroupService {
	@Autowired
	private IGroupDaoImpl groupDao;
	
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Group> queryAll() throws Exception {
		return this.groupDao.queryAll();
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id) throws Exception {
		return this.groupDao.queryChildrenByPid(id);
	}
	
	/**
	 * 通过用户id查询用户拥有的其他用户组
	 * @param userId
	 * @return
	 */
	public List<Group> queryByUserId(Long userId) {
		return this.groupDao.queryByUserId(userId);
	}
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Group queryById(Long id) throws Exception {
		return this.groupDao.queryById(id);
	}
	
	/**
	 * 通过id集合查询用户集合
	 * @param ids
	 * @return
	 */
	public List<Group> queryInIds(List<Long> ids) {
		return this.groupDao.queryInIds(ids);
	}
	
	/**
	 * 新增部门
	 * @param group
	 * @return
	 */
	public boolean add(Group group) throws Exception {
		boolean success = this.groupDao.add(group);
		return success;
	}
	
	/**
	 * 更新部门
	 * @param group
	 * @return
	 */
	public boolean update(Group group) throws Exception {
		boolean success = this.groupDao.update(group);
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) {
		boolean success = this.groupDao.delBatchByPid(pid);
		return success;
	}
	
	public void valid(Group group) throws Exception {
		if(null == group.getText() || "".equals(group.getText().trim())) {
			CodeUtil.throwExcep("请输入用户组名称");
		}
		group.setText(group.getText().trim());
		if(null == group.getGroupId() || "".equals(group.getGroupId().trim())) {
			CodeUtil.throwExcep("请输入用户组ID");
		}
		group.setGroupId(group.getGroupId().trim());
		if(!ValidationUtil.isAlphaNumberic(group.getGroupId())) {
			CodeUtil.throwExcep("用户组ID格式不正确，只允许字母，数字或者下划线");
		}
		if(!this.groupDao.checkeUniqueGroupId(group.getGroupId(), group.getId())) {
			CodeUtil.throwExcep("用户组ID不唯一");
		}
		if(null == group.getDepartmentId()) {
			CodeUtil.throwExcep("请选择归属部门");
		}
	}
}
