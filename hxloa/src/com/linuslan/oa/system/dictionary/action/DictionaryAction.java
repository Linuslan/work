package com.linuslan.oa.system.dictionary.action;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.TreeUtil;

@Controller
public class DictionaryAction extends BaseAction {
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	private Dictionary dictionary;
	
	public void queryTree() {
		this.resultMap.clear();
		try {
			List<Dictionary> list = this.dictionaryService.queryAll();
			list = (List<Dictionary>) TreeUtil.buildTree(list);
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
			if(null != this.dictionary.getId()) {
				this.dictionary = this.dictionaryService.queryById(this.dictionary.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.dictionary) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.dictionaryService.valid(this.dictionary);
			if(this.dictionaryService.add(this.dictionary)) {
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
			if(null == this.dictionary || null == this.dictionary.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Dictionary persist = this.dictionaryService.queryById(this.dictionary.getId());
			BeanUtil.updateBean(persist, this.dictionary);
			this.dictionaryService.valid(persist);
			if(this.dictionaryService.update(persist)) {
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
			if(null == this.dictionary || null == this.dictionary.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			if(this.dictionaryService.delBatchByPid(this.dictionary.getId())) {
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

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
}
