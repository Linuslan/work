package com.linuslan.oa.system.menu.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.system.menu.model.MenuIndex;
import com.linuslan.oa.system.menu.service.IMenuIndexService;
import com.linuslan.oa.system.menu.service.IMenuService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.TreeUtil;

public class MenuIndexAction extends BaseAction {

	@Autowired
	private IMenuIndexService menuIndexService;
	
	@Autowired
	private IMenuService menuService;
	
	private MenuIndex menuIndex;
	
	private Menu menu;
	
	private Page<MenuIndex> pageData;
	
	public void queryPageByMenuId() {
		try {
			this.pageData = this.menuIndexService.queryPageByMenuId(this.menu.getId(), paramMap, page, rows);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new DateProcessor());
			JSONObject json = JSONObject.fromObject(this.pageData, config);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String queryById() {
		try {
			this.menuIndex = this.menuIndexService.queryById(this.menuIndex.getId());
			if(null != this.menu && null != this.menu.getId()) {
				this.menu = this.menuService.queryById(this.menu.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void queryIndex() {
		try {
			Map<String, Object> map = null;
			
			List<Map<String, Object>> menuIndexes = this.menuIndexService.queryMenuIndex();
			Map<String, List<Map<String, Object>>> menuIndexMap = new LinkedHashMap<String, List<Map<String, Object>>> ();
			List<Long> ids = new ArrayList<Long> ();
			for(int i = 0; i < menuIndexes.size(); i ++) {
				map = menuIndexes.get(i);
				if(null != map) {
					if(null == menuIndexMap.get(map.get("XTYPE").toString())) {
						menuIndexMap.put(map.get("XTYPE").toString(), new ArrayList<Map<String, Object>> ());
					}
					menuIndexMap.get(map.get("XTYPE").toString()).add(map);
				}
			}
			List<Menu> children = this.menuService.queryByPid();
			List<Menu> tree = (List<Menu>) TreeUtil.buildTree(children);
			Map<Long, Menu> treeMap = (Map<Long, Menu>) TreeUtil.parseMapById(tree);
			List<Map<String, Object>> menus = this.menuService.queryMenusByXtype();
			for(int i = 0; i < menus.size(); i ++) {
				map = menus.get(i);
				if(null != map) {
					map.put("children", menuIndexMap.get(map.get("XTYPE").toString()));
					map.put("allChildren", treeMap.get(CodeUtil.parseLong(map.get("ID"))).getChildren());
				}
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"CREATE_DATE"});
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONArray json = JSONArray.fromObject(menus, jsonConfig);
			this.resultMap.put("success", true);
			this.resultMap.put("msg", json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
			this.resultMap.put("success", false);
			this.resultMap.put("msg", "首页消息获取失败");
		}
		this.printResultMap();
	}
	
	public void add() {
		try {
			if(null == this.menuIndex) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.menuIndexService.valid(this.menuIndex);
			if(this.menuIndexService.add(this.menuIndex)) {
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
			if(null == this.menuIndex || null == this.menuIndex.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			MenuIndex persist = this.menuIndexService.queryById(this.menuIndex.getId());
			BeanUtil.updateBean(persist, this.menuIndex);
			this.menuIndexService.valid(persist);
			if(this.menuIndexService.update(persist)) {
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
			if(null == this.menuIndex || null == this.menuIndex.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			MenuIndex persist = this.menuIndexService.queryById(this.menuIndex.getId());
			persist.setIsDelete(1);
			if(this.menuIndexService.update(persist)) {
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

	public MenuIndex getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(MenuIndex menuIndex) {
		this.menuIndex = menuIndex;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Page<MenuIndex> getPageData() {
		return pageData;
	}

	public void setPageData(Page<MenuIndex> pageData) {
		this.pageData = pageData;
	}
}
