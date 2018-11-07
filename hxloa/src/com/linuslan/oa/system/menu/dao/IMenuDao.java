package com.linuslan.oa.system.menu.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.menu.model.Menu;

public interface IMenuDao extends IBaseDao {
	
	public boolean add(Menu menu);
	
	
	public boolean update(Menu menu);
	
	
	public boolean batchDel(String ids);
	
	
	public Menu queryById(Long id);
	
	/**
	 * 查询所有菜单
	 * @return
	 */
	public List<Menu> queryAll();
	
	public boolean isMultipleValue(Menu menu);
	
	
	public List<Menu> queryMenuListByIds(List<Long> idList);
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid);
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids);
	
	/**
	 * 通过父id递归查询子节点
	 * @param pid
	 * @return
	 */
	public List<Menu> queryByPid(Long pid);
	
	/**
	 * 通过批量的父节点查询归属的子节点
	 * @param ids
	 * @return
	 */
	public List<Menu> queryByPid();
	
	/**
	 * 通过菜单id集合，删除角色资源里面的菜单资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByMenuIds(List<Long> ids);
	
	/**
	 * 通过用户id查询其拥有的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> queryByUserId(Long userId);
	
	/**
	 * 查询登录用户拥有的菜单权限
	 * 且xtype不为空，不为空表示首页可以显示
	 * 且url为空，即查询类似于我的报销，待审报销这样菜单的父菜单
	 * 该方法用于首页显示菜单
	 * @return
	 */
	public List<Map<String, Object>> queryMenusByXtype();
}
