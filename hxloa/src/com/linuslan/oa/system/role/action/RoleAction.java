package com.linuslan.oa.system.role.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.system.button.service.IButtonService;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.system.menu.service.IMenuService;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.role.model.RoleResource;
import com.linuslan.oa.system.role.service.IRoleService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;

@Controller
public class RoleAction extends BaseAction {
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IMenuService menuService;
	
	@Autowired
	private IButtonService buttonService;
	
	private String menuIds;
	
	private String buttonIds;
	
	private Role role;
	
	private Page<Role> pageData;
	
	public void queryPage() {
		try {
			this.pageData = this.roleService.queryPage(paramMap, page, rows);
			JSONObject json = JSONObject.fromObject(pageData);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.role = this.roleService.queryById(this.role.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			this.roleService.valid(this.role);
			if(this.roleService.add(this.role)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败！");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void update() {
		try {
			this.roleService.valid(this.role);
			if(this.roleService.update(role)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep(this.failureMsg);
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void del() {
		try {
			if(null == this.role || null == this.role.getId()) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Role persist = this.roleService.queryById(this.role.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(this.roleService.del(persist)) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void authorize() {
		try {
			if(null == this.role || null == this.role.getId()) {
				CodeUtil.throwExcep("授权失败，角色id获取异常");
			}
			if(null == this.menuIds || "".equals(this.menuIds.trim())) {
				CodeUtil.throwExcep("授权失败，请至少选择一条菜单资源");
			}
			/*if(null == this.buttonIds || "".equals(this.buttonIds.trim())) {
				CodeUtil.throwExcep("授权失败，请至少选择一条按钮资源");
			}*/
			List<RoleResource> resList = new ArrayList<RoleResource> ();
			try {
				List<Long> buttonIdList = BeanUtil.parseStringToLongList(this.buttonIds, ",");
				List<Button> buttons = this.buttonService.queryByIds(buttonIdList);
				this.roleService.parseToResource(resList, role, buttons);
			} catch(Exception ex) {
				
			}
			List<Long> menuIdList = BeanUtil.parseStringToLongList(this.menuIds, ",");
			List<Menu> menus = this.menuService.queryMenuListByIds(menuIdList);
			this.roleService.parseToResource(resList, role, menus);
			if(this.roleService.addRoleResourceBatch(resList, this.role.getId())) {
				this.setupSuccessMap("授权成功");
			} else {
				CodeUtil.throwExcep("授权失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getButtonIds() {
		return buttonIds;
	}

	public void setButtonIds(String buttonIds) {
		this.buttonIds = buttonIds;
	}

	public Page<Role> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Role> pageData) {
		this.pageData = pageData;
	}
}
