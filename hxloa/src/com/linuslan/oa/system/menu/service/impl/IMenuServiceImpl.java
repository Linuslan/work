package com.linuslan.oa.system.menu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.button.dao.IButtonDao;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.system.menu.dao.IMenuDao;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.system.menu.service.IMenuService;
import com.linuslan.oa.system.role.dao.IRoleDao;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.TreeUtil;
import com.linuslan.oa.util.ValidationUtil;

@Component("menuService")
@Transactional
public class IMenuServiceImpl extends IBaseServiceImpl implements IMenuService {
	
	@Autowired
	private IMenuDao menuDao;
	
	@Autowired
	private IButtonDao buttonDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	/**
	 * 新增菜单
	 * @param menu
	 * @return
	 */
	public boolean add(Menu menu) {
		return this.menuDao.add(menu);
	}
	
	/**
	 * 更新菜单
	 * @param menu
	 * @return
	 */
	public boolean update(Menu menu) {
		return this.menuDao.update(menu);
	}
	
	/**
	 * 批量删除菜单，同时删除菜单所有的按钮，伪删除，设置isDelete=1
	 * @param ids
	 * @return
	 */
	public boolean batchDel(String ids) {
		return this.menuDao.batchDel(ids);
	}
	
	/**
	 * 通过id查询菜单
	 * @param id
	 * @return
	 */
	public Menu queryById(Long id) {
		return this.menuDao.queryById(id);
	}
	
	public List<Menu> queryAll() {
		return this.menuDao.queryAll();
	}
	
	/**
	 * 查询所有的菜单资源和按钮资源，组装成树
	 * 并通过用户id查询用户的权限资源，将对应的资源设置为选中状态
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> queryAuthorizeTreeByRoleId(Long roleId) throws Exception {
		List<Menu> menus = this.menuDao.queryAll();
		Map<Long, Menu> menuMap = (Map<Long, Menu>) TreeUtil.parseMapById(menus);
		
		//存放用户的菜单资源
		Map<String, Map<String, Object>> menuRes = new HashMap<String, Map<String, Object>> ();
		//存放用户的按钮资源
		Map<String, Map<String, Object>> buttonRes = new HashMap<String, Map<String, Object>>();
		
		//获取授权给用户的角色资源
		List<Map<String, Object>> resources = this.roleDao.queryResourcesByRoleId(roleId);
		for(Map<String, Object> map : resources) {
			String id = map.get("RESOURCES_ID").toString();
			String type = map.get("RESOURCES_TYPE").toString();
			String value = map.get("VALUE").toString();
			if(null != id && !"".equals(id.trim())
					&& null != type && !"".equals(type.trim())
					&& null != value && !"".equals(value.trim())) {
				if("menu".equals(type.trim())) {
					menuRes.put(id, map);
					//如果所有菜单里有这个菜单，则说明这个菜单被授权给了用户，则将checked设置为true
					if(null != menuMap.get(Long.parseLong(id.trim()))) {
						menuMap.get(Long.parseLong(id.trim())).setChecked(true);
					}
				} else if("button".equals(type.trim())) {
					buttonRes.put(id, map);
				}
			}
		}
		
		String menuIdStr = BeanUtil.parseString(menus, "id", ",");
		List<Long> menuIds = BeanUtil.parseStringToLongList(menuIdStr, ",");
		List<Button> buttons = this.buttonDao.queryByMenuIds(menuIds);
		for(Button button : buttons) {
			if(null != button && null != button.getId()
					&& null != button.getValue() && !"".equals(button.getValue().trim())
					&& null != button.getMenuId()) {
				//如果用户权限资源菜单里面有这个按钮，则将该按钮的checked设置为true
				if(null != buttonRes.get(button.getId().toString())) {
					button.setChecked(true);
				}
				menuMap.get(button.getMenuId()).getButtons().add(button);
			}
		}
		
		List<Menu> tree = (List<Menu>) TreeUtil.buildTree(menus);
		return tree;
	}
	
	public List<Menu> queryIndex() throws Exception {
		List<Menu> allMenus = this.menuDao.queryAll();
		List<Menu> tree = (List<Menu>) TreeUtil.buildTree(allMenus);
		return tree;
	}
	
	public boolean isMultipleValue(Menu menu) {
		return this.menuDao.isMultipleValue(menu);
	}
	
	public List<Menu> queryMenuListByIds(List<Long> idList) {
		return this.menuDao.queryMenuListByIds(idList);
	}
	
	/**
	 * 删除菜单，伪删除菜单和菜单归属的按钮
	 * 真删除角色资源里面对应的资源
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delById(Long id) throws Exception {
		boolean success = false;
		List<Menu> menus = this.menuDao.queryByPid(id);
		String menuIdStr = BeanUtil.parseString(menus, "id", ",");
		List<Long> menuIds = BeanUtil.parseStringToLongList(menuIdStr, ",");
		
		List<Button> buttons = this.buttonDao.queryByMenuIds(menuIds);
		if(null != buttons && 0 < buttons.size()) {
			String buttonIdStr = BeanUtil.parseString(buttons, "id", ",");
			List<Long> buttonIds = BeanUtil.parseStringToLongList(buttonIdStr, ",");
			
			//先删除角色拥有的按钮资源
			this.buttonDao.delResourcesByButtonIds(buttonIds);
			//删除按钮
			this.buttonDao.delBatchByIds(buttonIds);
		}
		
		//删除角色拥有的菜单资源
		this.menuDao.delResourcesByMenuIds(menuIds);
		
		//删除菜单
		this.menuDao.delBatchByIds(menuIds);
		success = true;
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) throws Exception {
		return this.menuDao.delBatchByPid(pid);
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param pid
	 * @return
	 */
	public List<Menu> queryByPid(Long pid) {
		return this.menuDao.queryByPid(pid);
	}
	
	/**
	 * 通过批量的父节点查询归属的子节点
	 * @param ids
	 * @return
	 */
	public List<Menu> queryByPid() {
		return this.menuDao.queryByPid();
	}
	
	/**
	 * 通过用户id查询其拥有的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> queryByUserId(Long userId) {
		List<Menu> list = this.menuDao.queryByUserId(userId);
		List<Menu> tree = (List<Menu>) TreeUtil.buildTree(list);
		return tree;
	}
	
	public List<Menu> queryByUser(User user) {
		List<Menu> list = this.menuDao.queryByUserId(user.getId());
		return list;
	}
	
	public void valid(Menu menu) throws Exception {
		if(null == menu.getText() || "".equals(menu.getText().trim())) {
			CodeUtil.throwExcep("请输入菜单名");
		}
		menu.setText(menu.getText().trim());
		if(null == menu.getValue() || "".equals(menu.getValue().trim())) {
			CodeUtil.throwExcep("请输入菜单值");
		}
		menu.setValue(menu.getValue().trim());
		if(!ValidationUtil.isAlphaNumberic(menu.getValue())) {
			CodeUtil.throwExcep("菜单值格式不正确，只能包含字母，数字或下划线");
		}
		if(!this.menuDao.isMultipleValue(menu)) {
			CodeUtil.throwExcep("菜单值已存在");
		}
		if(null != menu.getUrl() && !"".equals(menu.getUrl().trim())) {
			menu.setUrl(menu.getUrl().trim());
			if(!ValidationUtil.isURI(menu.getUrl())) {
				CodeUtil.throwExcep("菜单地址格式不正确");
			}
		}
		if(null != menu.getIcon() && !"".equals(menu.getIcon().trim())) {
			menu.setIcon(menu.getIcon().trim());
			if(!ValidationUtil.isURI(menu.getIcon())) {
				CodeUtil.throwExcep("菜单图标格式不正确");
			}
		}
		if(null == menu.getOrderNo()) {
			menu.setOrderNo(0);
		}
	}
	
	/**
	 * 通过菜单id集合，删除角色资源里面的菜单资源
	 * @param ids
	 * @return
	 */
	public boolean delResourcesByMenuIds(List<Long> ids) throws Exception {
		return this.menuDao.delResourcesByMenuIds(ids);
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByIds(List<Long> ids) throws Exception {
		return this.menuDao.delBatchByIds(ids);
	}
	
	/**
	 * 查询登录用户拥有的菜单权限
	 * 且xtype不为空，不为空表示首页可以显示
	 * 且url为空，即查询类似于我的报销，待审报销这样菜单的父菜单
	 * 该方法用于首页显示菜单
	 * @return
	 */
	public List<Map<String, Object>> queryMenusByXtype() {
		return this.menuDao.queryMenusByXtype();
	}
}
