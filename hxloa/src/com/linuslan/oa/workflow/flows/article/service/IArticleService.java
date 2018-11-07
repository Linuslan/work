package com.linuslan.oa.workflow.flows.article.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;

public interface IArticleService extends IBaseService {

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
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<CheckinArticle> queryCheckinArticlesByCompanyId(Long companyId);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckinArticle queryById(Long id);
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean add(CheckinArticle checkinArticle);
	
	/**
	 * 新增商品
	 * @param article
	 * @return
	 */
	public boolean add(CheckinArticle article, List<Format> formats);
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean update(CheckinArticle checkinArticle);
	
	/**
	 * 更新商品
	 * @param article
	 * @return
	 */
	public boolean update(CheckinArticle article, List<Format> formats);
	
	/**
	 * 删除商品，伪删除，将isDelete=0更新为isDelete=1
	 * @param achievement
	 * @return
	 */
	public boolean del(CheckinArticle article);
	
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
	
	public boolean delFormatById(Long id);
	
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
	 * 查询所有的出库商品
	 * @return
	 */
	public List<CheckoutArticle> queryAllCheckoutArticle();
	
	/**
	 * 查询出库产品列表
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public List<CheckoutArticle> queryCheckoutArticles(Long companyId, Long customerId);
	
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
	
	/**
	 * 删除出库商品，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkoutArticle
	 * @return
	 */
	public boolean delCheckoutArticle(CheckoutArticle checkoutArticle);
	
}
