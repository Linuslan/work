package com.linuslan.oa.workflow.flows.saleStuff.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.dao.IArticleUnitDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.ArticleUnit;
import com.linuslan.oa.workflow.flows.saleStuff.service.IArticleUnitService;

@Component("articleUnitService")
@Transactional
public class IArticleUnitServiceImpl extends IBaseServiceImpl implements
		IArticleUnitService {
	@Autowired
	private IArticleUnitDao articleUnitDao;
	
	/**
	 * 新增单位
	 * @param articleUnit
	 * @return
	 */
	public boolean add(ArticleUnit articleUnit) {
		return this.articleUnitDao.add(articleUnit);
	}
	
	/**
	 * 更新单位信息
	 * @param articleUnit
	 * @return
	 */
	public boolean update(ArticleUnit articleUnit) {
		return this.articleUnitDao.update(articleUnit);
	}
	
	/**
	 * 删除单位，伪删除，将isDelete状态改为1
	 * @param articleUnit
	 * @return
	 */
	public boolean del(ArticleUnit articleUnit) {
		return this.articleUnitDao.del(articleUnit);
	}
	
	/**
	 * 通过id查询单位
	 * @param id
	 * @return
	 */
	public ArticleUnit queryById(Long id) {
		return this.articleUnitDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有单位
	 * @param ids
	 * @return
	 */
	public List<ArticleUnit> queryByIds(List<Long> ids) {
		return this.articleUnitDao.queryByIds(ids);
	}
	
	/**
	 * 查询单位列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<ArticleUnit> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.articleUnitDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的单位
	 * @param paramMap
	 * @return
	 */
	public List<ArticleUnit> queryAll() {
		return this.articleUnitDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param articleUnit
	 * @throws Exception
	 */
	public void valid(ArticleUnit articleUnit) throws Exception {
		if(null == articleUnit.getName() || "".equals(articleUnit.getName().trim())) {
			CodeUtil.throwExcep("单位名称不能为空");
		}
	}
}
