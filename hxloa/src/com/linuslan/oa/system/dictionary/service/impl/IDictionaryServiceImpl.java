package com.linuslan.oa.system.dictionary.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.dictionary.dao.IDictionaryDao;
import com.linuslan.oa.system.dictionary.service.IDictionaryService;
import com.linuslan.oa.system.dictionary.model.Dictionary;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ValidationUtil;

@Component("dictionaryService")
@Transactional
public class IDictionaryServiceImpl extends IBaseServiceImpl implements
		IDictionaryService {
	
	@Autowired
	private IDictionaryDao dictionaryDao;
	
	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Dictionary> queryAll() throws Exception {
		return this.dictionaryDao.queryAll();
	}
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id) throws Exception {
		return this.dictionaryDao.queryChildrenByPid(id);
	}
	
	/**
	 * 通过父节点，只查询下级子节点，且是启用状态的
	 * @param id
	 * @return
	 */
	public List<Dictionary> queryByPid(Long id) {
		return this.dictionaryDao.queryByPid(id);
	}
	
	/**
	 * 通过id查询部门
	 * @param id
	 * @return
	 */
	public Dictionary queryById(Long id) throws Exception {
		return this.dictionaryDao.queryById(id);
	}
	
	/**
	 * 新增部门
	 * @param dictionary
	 * @return
	 */
	public boolean add(Dictionary dictionary) throws Exception {
		boolean success = this.dictionaryDao.add(dictionary);
		return success;
	}
	
	/**
	 * 更新部门
	 * @param dictionary
	 * @return
	 */
	public boolean update(Dictionary dictionary) throws Exception {
		boolean success = this.dictionaryDao.update(dictionary);
		return success;
	}
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid) {
		boolean success = this.dictionaryDao.delBatchByPid(pid);
		return success;
	}
	
	public void valid(Dictionary dictionary) throws Exception {
		if(null == dictionary.getText() || "".equals(dictionary.getText().trim())) {
			CodeUtil.throwExcep("请输入字典名称");
		}
	}
}
