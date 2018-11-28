package com.linuslan.oa.system.button.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.util.Page;

public interface IButtonService extends IBaseService {
	
	/**
	 * 通过菜单id分页查询按钮
	 * @param menuId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Button> queryPageByMenuId(Long menuId, Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询按钮
	 * @param id
	 * @return
	 */
	public Button queryById(Long id);
	
	/**
	 * 新增按钮
	 * @param button
	 * @return
	 */
	public boolean add(Button button) throws Exception;
	
	/**
	 * 更新按钮
	 * @param button
	 * @return
	 */
	public boolean update(Button button) throws Exception;
	
	/**
	 * 伪删除按钮同时真删除角色的按钮资源
	 * @param button
	 * @return
	 * @throws Exception
	 */
	public boolean del(Button button) throws Exception;
	
	/**
	 * 批量删除按钮（伪删除，将isDelete更新为1）
	 * @param menuId
	 * @return
	 */
	public boolean delByMenuId(Long menuId) throws Exception;
	
	public List<Button> queryByMenuIds(List<Long> menuIds);
	
	/**
	 * 通过用户id查询其拥有的按钮
	 * @param userId
	 * @return
	 */
	public List<Button> queryByUserId(Long userId);
	
	/**
	 * 通过按钮id集合，删除角色资源里面的按钮资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByButtonIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception;
	
	/**
	 * 通过id集合查询对应的按钮集合
	 * @param ids
	 * @return
	 */
	public List<Button> queryByIds(List<Long> ids);
}
