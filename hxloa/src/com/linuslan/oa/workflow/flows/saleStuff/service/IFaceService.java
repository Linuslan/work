package com.linuslan.oa.workflow.flows.saleStuff.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.model.Face;

public interface IFaceService extends IBaseService {

	/**
	 * 新增面数
	 * @param face
	 * @return
	 */
	public boolean add(Face face);
	
	/**
	 * 更新面数信息
	 * @param face
	 * @return
	 */
	public boolean update(Face face);
	
	/**
	 * 删除面数，伪删除，将isDelete状态改为1
	 * @param face
	 * @return
	 */
	public boolean del(Face face);
	
	/**
	 * 通过id查询面数
	 * @param id
	 * @return
	 */
	public Face queryById(Long id);
	
	/**
	 * 查询指定id的所有面数
	 * @param ids
	 * @return
	 */
	public List<Face> queryByIds(List<Long> ids);
	
	/**
	 * 查询面数列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Face> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的面数
	 * @param paramMap
	 * @return
	 */
	public List<Face> queryAll();
	
	/**
	 * 检测有效值
	 * @param face
	 * @throws Exception
	 */
	public void valid(Face face) throws Exception;
}
