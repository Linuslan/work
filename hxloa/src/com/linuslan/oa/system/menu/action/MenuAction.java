package com.linuslan.oa.system.menu.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.system.button.service.IButtonService;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.system.menu.service.IMenuIndexService;
import com.linuslan.oa.system.menu.service.IMenuService;
import com.linuslan.oa.system.role.model.Role;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.TreeUtil;

@Controller
public class MenuAction extends BaseAction {
	
	private Menu menu;
	
	private Role role;
	
	private Menu parent;
	
	private List<Menu> menuList;
	
	@Autowired
	private IMenuService menuService;
	
	@Autowired
	private IButtonService buttonService;
	
	@Autowired
	private IMenuIndexService menuIndexService;
	
	public void queryTree() {
		this.resultMap.clear();
		try {
			List<Menu> list = this.menuService.queryAll();
			list = (List<Menu>) TreeUtil.buildTree(list);
			JSONArray json = JSONArray.fromObject(list);
			this.resultMap.put("success", true);
			this.resultMap.put("children", json.toString());
			this.resultMap.put("pid", "");
		} catch(Exception ex) {
			ex.printStackTrace();
			this.resultMap.put("success", false);
			this.resultMap.put("msg", ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public void queryAuthorizeTree() {
		try {
			if(null == this.role || null == this.role.getId()) {
				CodeUtil.throwExcep("请选择授权角色");
			}
			List<Menu> tree = this.menuService.queryAuthorizeTreeByRoleId(this.role.getId());
			JSONArray json = JSONArray.fromObject(tree);
			this.resultMap.put("success", true);
			this.resultMap.put("children", json.toString());
			this.resultMap.put("pid", "");
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public String index() {
		HttpServletRequest request = ServletActionContext.getRequest();
		this.menuList = new ArrayList<Menu> ();
		try {
			User user = HttpUtil.getLoginUser();
			List<Menu> menuList = null;
			List<Button> buttonList = null;
			if(1l == user.getId()) {
				menuList = this.menuService.queryIndex();
			} else {
				menuList = this.menuService.queryByUserId(user.getId());
				buttonList = this.buttonService.queryByUserId(user.getId());
			}
			request.setAttribute("menuList", menuList);
			HttpSession session = request.getSession();
			session.setAttribute(ConstantVar.AUTHORIZE_MENU, menuList);
//			String menuIdStr = BeanUtil.parseString(menuList, "id", ",");
//			List<Long> menuIds = BeanUtil.parseStringToLongList(menuIdStr, ",");
//			if(null != menuIds && 0 < menuIds.size()) {
//				List<Button> buttons = this.buttonService.queryByMenuIds(menuIds);
//				session.setAttribute(ConstantVar.AUTHORIZE_BUTTON, buttons);
//			}
			session.setAttribute(ConstantVar.AUTHORIZE_BUTTON, buttonList);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String queryById() {
		try {
			if(null != this.menu && null != this.menu.getId()) {
				this.menu = this.menuService.queryById(this.menu.getId());
				if(null != this.menu.getPid()) {
					this.parent = this.menuService.queryById(this.menu.getPid());
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.getReturnType();
	}
	
	public void add() {
		try {
			if(null == menu) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.menuService.valid(menu);
			if(this.menuService.add(menu)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void update() {
		try {
			if(null == this.menu || null == this.menu.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Menu persist = this.menuService.queryById(this.menu.getId());
			BeanUtil.updateBean(persist, this.menu);
			this.menuService.valid(persist);
			if(this.menuService.update(persist)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void del() {
		try {
			if(null == this.menu || null == this.menu.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			if(this.menuService.delById(this.menu.getId())) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getCause().toString());
		}
		this.printMap(resultMap);
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
