package com.linuslan.oa.system.dictionary.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.dictionary.model.Dictionary;

public interface IDictionaryDao extends IBaseDao {

	/**
	 * 查询所有的字典
	 * @return
	 */
	public List<Dictionary> queryAll();
	
	/**
	 * 通过父id递归查询子节点
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryChildrenByPid(Long id);
	
	/**
	 * 通过父节点，只查询下级子节点，且是启用状态的
	 * @param id
	 * @return
	 */
	public List<Dictionary> queryByPid(Long id);
	
	/**
	 * 通过id查询字典
	 * @param id
	 * @return
	 */
	public Dictionary queryById(Long id);
	
	/**
	 * 新增字典
	 * @param dictionary
	 * @return
	 */
	public boolean add(Dictionary dictionary);
	
	/**
	 * 更新字典
	 * @param dictionary
	 * @return
	 */
	public boolean update(Dictionary dictionary);
	
	/**
	 * 通过父id递归伪删除子节点，将is_delete设置为1
	 * @param pid
	 * @return
	 */
	public boolean delBatchByPid(Long pid);
}
