package com.linuslan.oa.workflow.flows.saleStuff.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.model.Effect;

public interface IEffectDao extends IBaseDao {

	/**
	 * 新增效果
	 * @param effect
	 * @return
	 */
	public boolean add(Effect effect);
	
	/**
	 * 更新效果信息
	 * @param effect
	 * @return
	 */
	public boolean update(Effect effect);
	
	/**
	 * 删除效果，伪删除，将isDelete状态改为1
	 * @param effect
	 * @return
	 */
	public boolean del(Effect effect);
	
	/**
	 * 通过id查询效果
	 * @param id
	 * @return
	 */
	public Effect queryById(Long id);
	
	/**
	 * 查询指定id的所有效果
	 * @param ids
	 * @return
	 */
	public List<Effect> queryByIds(List<Long> ids);
	
	/**
	 * 查询效果列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Effect> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的效果
	 * @param paramMap
	 * @return
	 */
	public List<Effect> queryAll();
	
}
