package com.linuslan.oa.system.button.action;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.button.model.Button;
import com.linuslan.oa.system.button.service.IButtonService;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.menu.model.Menu;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class ButtonAction extends BaseAction {
	
	private Button button;
	
	private Menu menu;
	
	private Page<Button> pageData;
	
	@Autowired
	private IButtonService buttonService;
	
	public void queryPageByMenuId() {
		try {
			this.pageData = this.buttonService.queryPageByMenuId(this.menu.getId(), paramMap, page, rows);
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
			this.button = this.buttonService.queryById(this.button.getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.button) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.buttonService.valid(this.button);
			if(this.buttonService.add(this.button)) {
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
			if(null == this.button || null == this.button.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Button persist = this.buttonService.queryById(this.button.getId());
			BeanUtil.updateBean(persist, this.button);
			this.buttonService.valid(persist);
			if(this.buttonService.update(persist)) {
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
			if(null == this.button || null == this.button.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			Button persist = this.buttonService.queryById(this.button.getId());
			if(this.buttonService.del(persist)) {
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

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Page<Button> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Button> pageData) {
		this.pageData = pageData;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
}
