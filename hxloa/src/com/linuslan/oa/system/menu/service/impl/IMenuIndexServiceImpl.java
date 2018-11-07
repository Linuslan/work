package com.linuslan.oa.system.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.menu.dao.IMenuIndexDao;
import com.linuslan.oa.system.menu.model.MenuIndex;
import com.linuslan.oa.system.menu.service.IMenuIndexService;
import com.linuslan.oa.util.Page;

@Component("menuIndexService")
@Transactional
public class IMenuIndexServiceImpl extends IBaseServiceImpl implements
		IMenuIndexService {
	
	@Autowired
	private IMenuIndexDao menuIndexDao;
	
	/**
	 * 通过菜单id分页查询首页索引
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<MenuIndex> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows) {
		return this.menuIndexDao.queryPageByMenuId(menuId, paramMap, page, rows);
	}
	
	/**
	 * 通过id查询首页索引
	 * @param id
	 * @return
	 */
	public MenuIndex queryById(Long id){
		return this.menuIndexDao.queryById(id);
	}
	
	/**
	 * 新增首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean add(MenuIndex menuIndex) {
		return this.menuIndexDao.add(menuIndex);
	}
	
	/**
	 * 更新首页索引
	 * @param menuIndex
	 * @return
	 */
	public boolean update(MenuIndex menuIndex) {
		return this.menuIndexDao.update(menuIndex);
	}
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId) {
		return this.menuIndexDao.delByMenuId(menuId);
	}
	
	/**
	 * 批量删除首页索引（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuIds(List<Long> menuIds) {
		return this.menuIndexDao.delByMenuIds(menuIds);
	}
	
	/**
	 * 通过菜单id查询首页索引
	 */
	public List<MenuIndex> queryByMenuIds(List<Long> menuIds) {
		return this.menuIndexDao.queryByMenuIds(menuIds);
	}
	
	/**
	 * 查询登录用户首页展示的需办理的消息提醒列表
	 * @return
	 */
	public List<Map<String, Object>> queryMenuIndex() {
		return this.menuIndexDao.queryMenuIndex();
	}
}
