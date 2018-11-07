package com.linuslan.oa.system.menu.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.menu.model.MenuIndex;
import com.linuslan.oa.util.Page;

public interface IMenuIndexService extends IBaseService {
	/**
	 * 通过菜单id分页查询首页索引
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<MenuIndex> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询首页索引
	 * @param id
	 * @return
	 */
	public MenuIndex queryById(Long id);
	
	/**
	 * 新增首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean add(MenuIndex menuIndex);
	
	/**
	 * 更新首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean update(MenuIndex menuIndex);
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId);
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuIds(List<Long> menuIds);
	
	/**
	 * 通过菜单id查询首页索引
	 */
	public List<MenuIndex> queryByMenuIds(List<Long> menuIds);
	
	/**
	 * 查询登录用户首页展示的需办理的消息提醒列表
	 * @return
	 */
	public List<Map<String, Object>> queryMenuIndex();
}
