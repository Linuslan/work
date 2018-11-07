package com.linuslan.oa.workflow.flows.article.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;
import com.linuslan.oa.workflow.flows.article.dao.IArticleDao;
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.CheckoutArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;
import com.linuslan.oa.workflow.flows.article.service.IArticleService;

@Component("articleService")
@Transactional
public class IArticleServiceImpl extends IBaseServiceImpl implements
		IArticleService {

	@Autowired
	private IArticleDao articleDao;
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckinArticle> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.articleDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<CheckinArticle> queryAll() {
		return this.articleDao.queryAll();
	}
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<CheckinArticle> queryCheckinArticlesByCompanyId(Long companyId) {
		return this.articleDao.queryCheckinArticlesByCompanyId(companyId);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckinArticle queryById(Long id) {
		return this.articleDao.queryById(id);
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean add(CheckinArticle checkinArticle) {
		return this.articleDao.add(checkinArticle);
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean update(CheckinArticle checkinArticle) {
		return this.articleDao.update(checkinArticle);
	}
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Format> queryFormatsByArticleId(Long articleId, Map<String, String> paramMap) {
		return this.articleDao.queryFormatsByArticleId(articleId, paramMap);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public Format queryFormatById(Long id) {
		return this.articleDao.queryFormatById(id);
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addFormat(Format format) {
		return this.articleDao.addFormat(format);
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateFormat(Format format) {
		return this.articleDao.updateFormat(format);
	}
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<CheckoutArticle> queryCheckoutArticlePage(Map<String, String> paramMap, int page, int rows) {
		return this.articleDao.queryCheckoutArticlePage(paramMap, page, rows);
	}
	
	/**
	 * 查询所有的出库商品
	 * @return
	 */
	public List<CheckoutArticle> queryAllCheckoutArticle() {
		return this.articleDao.queryAllCheckoutArticle();
	}
	
	/**
	 * 查询出库产品列表
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public List<CheckoutArticle> queryCheckoutArticles(Long companyId, Long customerId) {
		return this.articleDao.queryCheckoutArticles(companyId, customerId);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public CheckoutArticle queryCheckoutArticleById(Long id) {
		return this.articleDao.queryCheckoutArticleById(id);
	}
	
	/**
	 * 新增
	 * @param checkinArticle
	 * @return
	 */
	public boolean addCheckoutArticle(CheckoutArticle CheckoutArticle) {
		return this.articleDao.addCheckoutArticle(CheckoutArticle);
	}
	
	/**
	 * 更新
	 * @param checkinArticle
	 * @return
	 */
	public boolean updateCheckoutArticle(CheckoutArticle checkoutArticle) {
		if(null == checkoutArticle || null == checkoutArticle.getId()) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		CheckoutArticle persist = this.articleDao.queryCheckoutArticleById(checkoutArticle.getId());
		if(null == persist || null == persist.getId() || 1 == persist.getIsDelete()) {
			CodeUtil.throwRuntimeExcep("更新的数据不存在");
		}
		persist = (CheckoutArticle) BeanUtil.updateBean(persist, checkoutArticle);
		return this.articleDao.updateCheckoutArticle(persist);
	}
	
	/**
	 * 新增商品
	 * @param article
	 * @return
	 */
	public boolean add(CheckinArticle article, List<Format> formats) {
		boolean success = false;
		if(null == article) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		article.setUserId(HttpUtil.getLoginUser().getId());
		article.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
		//验证对象的有效性
		//this.valid(article);
		this.articleDao.add(article);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("articleId", article.getId());
		//将绩效主表的id赋值给绩效项目
		BeanUtil.setValueBatch(formats, map);
		//检查绩效项目的有效性
		//this.validContentBatch(formats);
		this.articleDao.mergeFormats(formats);
		success = true;
		return success;
	}
	
	/**
	 * 更新商品
	 * @param article
	 * @return
	 */
	public boolean update(CheckinArticle article, List<Format> formats) {
		boolean success = false;
		if(null == article || null == article.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		CheckinArticle persist = this.articleDao.queryById(article.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (CheckinArticle) BeanUtil.updateBean(persist, article);
		//this.valid(persist);
		this.articleDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("articleId", article.getId());
		//获取已保存且未被用户删除的规格
		String formatIdStr = BeanUtil.parseString(formats, "id", ",");
		List<Long> formatIds = BeanUtil.parseStringToLongList(formatIdStr, ",");
		//先删除不在formatIds中的规格，不在formatIds中，即被用户在前端界面删除了
		if(0 < formatIds.size()) {
			this.articleDao.delFormatsNotInIds(formatIds, persist.getId());
			List<Format> persists = this.articleDao.queryFormatsInIds(formatIds);
			formats = (List<Format>) BeanUtil.updateBeans(persists, formats, "id", map);
			//this.validContentBatch(formats);
			this.articleDao.mergeFormats(formats);
		}
		success = true;
		return success;
	}
	
	/**
	 * 删除商品，伪删除，将isDelete=0更新为isDelete=1
	 * @param achievement
	 * @return
	 */
	public boolean del(CheckinArticle article) {
		boolean success = false;
		if(null == article || null == article.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		CheckinArticle persist = this.articleDao.queryById(article.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.articleDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	public boolean delFormatById(Long id) {
		boolean success = false;
		Format persist = this.articleDao.queryFormatById(id);
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.articleDao.updateFormat(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除出库商品，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkoutArticle
	 * @return
	 */
	public boolean delCheckoutArticle(CheckoutArticle checkoutArticle) {
		boolean success = false;
		if(null == checkoutArticle || null == checkoutArticle.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		CheckoutArticle persist = this.articleDao.queryCheckoutArticleById(checkoutArticle.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.articleDao.updateCheckoutArticle(persist)) {
			success = true;
		}
		return success;
	}
}
