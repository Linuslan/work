package com.linuslan.oa.system.menu.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.system.menu.vo.MenuView;
import com.linuslan.oa.system.menu.vo.MenuVo;

public interface IMenuService extends IBaseService {
	/**
	 * 新增菜单
	 * @param menu
	 * @return
	 */
	public boolean add(Menu menu);
	
	/**
	 * 更新菜单
	 * @param menu
	 * @return
	 */
	public boolean update(Menu menu);
	
	/**
	 * 删除菜单，伪删除菜单和菜单归属的按钮
	 * 真删除角色资源里面对应的资源
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delById(Long id) throws Exception;
	
	/**
	 * 批量删除菜单，同时删除菜单所有的按钮，伪删除，设置isDelete=1
	 * @param ids
	 * @return
	 */
	public boolean batchDel(String ids);
	
	/**
	 * 通过id查询菜单
	 * @param id
	 * @return
	 */
	public Menu queryById(Long id);
	
	public List<Menu> queryAll();
	
	/**
	 * 通过用户id查询其拥有的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> queryByUserId(Long userId);
	
	/**
	 * 查询所有的菜单资源和按钮资源，组装成树
	 * 并通过角色id查询角色的权限资源，将对应的资源设置为选中状态
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> queryAuthorizeTreeByRoleId(Long roleId) throws Exception;
	
	public List<Menu> queryIndex() throws Exception;
	
	public boolean isMultipleValue(Menu menu);
	
	public List<Menu> queryMenuListByIds(List<Long> idList);
	
	public void valid(Menu menu) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) throws Exception;
	
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
	public boolean delResourcesByMenuIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception;
	
	/**
	 * 查询登录用户拥有的菜单权限
	 * 且xtype不为空，不为空表示首页可以显示
	 * 且url为空，即查询类似于我的报销，待审报销这样菜单的父菜单
	 * 该方法用于首页显示菜单
	 * @return
	 */
	public List<Map<String, Object>> queryMenusByXtype();
}
