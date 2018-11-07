package com.linuslan.oa.workflow.flows.saleStuff.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.model.ArticleUnit;

public interface IArticleUnitService extends IBaseService {

	/**
	 * 新增效果
	 * @param articleUnit
	 * @return
	 */
	public boolean add(ArticleUnit articleUnit);
	
	/**
	 * 更新效果信息
	 * @param articleUnit
	 * @return
	 */
	public boolean update(ArticleUnit articleUnit);
	
	/**
	 * 删除效果，伪删除，将isDelete状态改为1
	 * @param articleUnit
	 * @return
	 */
	public boolean del(ArticleUnit articleUnit);
	
	/**
	 * 通过id查询效果
	 * @param id
	 * @return
	 */
	public ArticleUnit queryById(Long id);
	
	/**
	 * 查询指定id的所有效果
	 * @param ids
	 * @return
	 */
	public List<ArticleUnit> queryByIds(List<Long> ids);
	
	/**
	 * 查询效果列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<ArticleUnit> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询所有的效果
	 * @param paramMap
	 * @return
	 */
	public List<ArticleUnit> queryAll();
	
	/**
	 * 检测有效值
	 * @param articleUnit
	 * @throws Exception
	 */
	public void valid(ArticleUnit articleUnit) throws Exception;
}
