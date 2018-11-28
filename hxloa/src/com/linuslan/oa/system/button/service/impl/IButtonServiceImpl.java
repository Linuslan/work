package com.linuslan.oa.system.button.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.button.dao.IButtonDao;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.system.button.service.IButtonService;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.TreeUtil;

@Component("buttonService")
@Transactional
public class IButtonServiceImpl extends IBaseServiceImpl implements IButtonService {
	
	@Autowired
	private IButtonDao buttonDao;
	
	/**
	 * 通过菜单id分页查询按钮
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Button> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows) {
		return this.buttonDao.queryPageByMenuId(menuId, paramMap, page, rows);
	}
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Button queryById(Long id) {
		return this.buttonDao.queryById(id);
	}
	
	/**
	 * 新增按钮
	 * @param button
	 * @return
	 */
	public boolean add(Button button) throws Exception {
		return this.buttonDao.add(button);
	}
	
	/**
	 * 更新按钮
	 * @param button
	 * @return
	 */
	public boolean update(Button button) throws Exception {
		return this.buttonDao.update(button);
	}
	
	/**
	 * 伪删除按钮同时真删除角色的按钮资源
	 * @param button
	 * @return
	 * @throws Exception
	 */
	public boolean del(Button button) throws Exception {
		boolean success = false;
		List<Button> list = new ArrayList<Button> ();
		list.add(button);
		String idStr = BeanUtil.parseString(list, "id", ",");
		List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
		this.buttonDao.delResourcesByButtonIds(ids);
		this.buttonDao.delBatchByIds(ids);
		success = true;
		return success;
	}
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId) throws Exception {
		return this.buttonDao.delByMenuId(menuId);
	}
	
	public List<Button> queryByMenuIds(List<Long> menuIds) {
		return this.buttonDao.queryByMenuIds(menuIds);
	}
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByButtonIds(List<Long> ids) throws Exception {
		return this.buttonDao.delResourcesByButtonIds(ids);
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception {
		return this.buttonDao.delBatchByIds(ids);
	}
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Button> queryByIds(List<Long> ids) {
		return this.buttonDao.queryByIds(ids);
	}

	public List<Button> queryByUserId(Long userId) {
		return this.buttonDao.queryByUserId(userId);
	}
	
}
