package com.linuslan.oa.workflow.flows.article.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;

public interface IArticleDao extends IBaseDao {

	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckinArticle> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<CheckinArticle> queryAll();
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckinArticle queryById(Long id);
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<CheckinArticle> queryCheckinArticlesByCompanyId(Long companyId);
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean add(CheckinArticle checkinArticle);
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean update(CheckinArticle checkinArticle);
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Format> queryFormatsByArticleId(Long articleId, Map<String, String> paramMap);
	
	/**
	 * 通过id查询规格
	 * @param ids
	 * @return
	 */
	public List<Format> queryFormatsInIds(List<Long> ids);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public Format queryFormatById(Long id);
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addFormat(Format format);
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateFormat(Format format);
	
	/**
	 * 批量更新规格，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeFormats(List<Format> formats);
	
	/**
	 * 删除规格的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delFormatsNotInIds(List<Long> ids, Long articleId);
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckoutArticle> queryCheckoutArticlePage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询出库产品列表
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public List<CheckoutArticle> queryCheckoutArticles(Long companyId, Long customerId);
	
	/**
	 * 查询所有的出库商品
	 * @return
	 */
	public List<CheckoutArticle> queryAllCheckoutArticle();
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckoutArticle queryCheckoutArticleById(Long id);
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addCheckoutArticle(CheckoutArticle CheckoutArticle);
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateCheckoutArticle(CheckoutArticle checkoutArticle);
	
}
