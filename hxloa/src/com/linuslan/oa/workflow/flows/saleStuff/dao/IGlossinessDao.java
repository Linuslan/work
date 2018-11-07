package com.linuslan.oa.workflow.flows.saleStuff.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.model.Glossiness;


public interface IGlossinessDao extends IBaseDao {

	/**
	 * 新增光泽度
	 * @param glossiness
	 * @return
	 */
	public boolean add(Glossiness glossiness);
	
	/**
	 * 更新光泽度信息
	 * @param glossiness
	 * @return
	 */
	public boolean update(Glossiness glossiness);
	
	/**
	 * 删除光泽度，伪删除，将isDelete状态改为1
	 * @param glossiness
	 * @return
	 */
	public boolean del(Glossiness glossiness);
	
	/**
	 * 通过id查询光泽度
	 * @param id
	 * @return
	 */
	public Glossiness queryById(Long id);
	
	/**
	 * 查询指定id的所有光泽度
	 * @param ids
	 * @return
	 */
	public List<Glossiness> queryByIds(List<Long> ids);
	
	/**
	 * 查询光泽度列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Glossiness> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的光泽度
	 * @param paramMap
	 * @return
	 */
	public List<Glossiness> queryAll();
}
