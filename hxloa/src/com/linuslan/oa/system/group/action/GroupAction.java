package com.linuslan.oa.system.group.action;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.group.service.IGroupService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.TreeUtil;

public class GroupAction extends BaseAction {
	
	@Autowired
	private IGroupService groupService;
	
	private Group group;
	
	public void queryTree() {
		this.resultMap.clear();
		try {
			List<Group> list = this.groupService.queryAll();
			list = (List<Group>) TreeUtil.buildTree(list);
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
	
	public String queryById() {
		try {
			if(null != this.group.getId()) {
				this.group = this.groupService.queryById(this.group.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.group) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.groupService.valid(this.group);
			if(this.groupService.add(this.group)) {
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
			if(null == this.group || null == this.group.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Group persist = this.groupService.queryById(this.group.getId());
			BeanUtil.updateBean(persist, this.group);
			this.groupService.valid(persist);
			if(this.groupService.update(persist)) {
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
			if(null == this.group || null == this.group.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			if(this.groupService.delBatchByPid(this.group.getId())) {
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
