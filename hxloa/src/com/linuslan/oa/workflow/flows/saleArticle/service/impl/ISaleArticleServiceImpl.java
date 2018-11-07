package com.linuslan.oa.workflow.flows.saleArticle.service.impl;

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
import com.linuslan.oa.workflow.flows.article.model.CheckinArticle;
import com.linuslan.oa.workflow.flows.article.model.Format;
import com.linuslan.oa.workflow.flows.saleArticle.dao.ISaleArticleDao;
import com.linuslan.oa.workflow.flows.saleArticle.model.Material;
import com.linuslan.oa.workflow.flows.saleArticle.model.MaterialFormat;
import com.linuslan.oa.workflow.flows.saleArticle.model.SaleArticle;
import com.linuslan.oa.workflow.flows.saleArticle.service.ISaleArticleService;

@Component("saleArticleService")
@Transactional
public class ISaleArticleServiceImpl extends IBaseServiceImpl implements
		ISaleArticleService {
	
	@Autowired
	private ISaleArticleDao saleArticleDao;
	
	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SaleArticle> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.saleArticleDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<SaleArticle> queryAll() {
		return this.saleArticleDao.queryAll();
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public SaleArticle queryById(Long id) {
		return this.saleArticleDao.queryById(id);
	}
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<SaleArticle> querySaleArticlesByCompanyId(Long companyId) {
		return this.saleArticleDao.querySaleArticlesByCompanyId(companyId);
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean add(SaleArticle saleArticle) {
		return this.saleArticleDao.add(saleArticle);
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean update(SaleArticle saleArticle) {
		return this.saleArticleDao.update(saleArticle);
	}
	
	/**
	 * 通过id查询规格
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<MaterialFormat> queryMaterialFormatsByArticleId(Long articleId, Map<String, String> paramMap) {
		return this.saleArticleDao.queryMaterialFormatsByArticleId(articleId, paramMap);
	}
	
	/**
	 * 通过id查询规格
	 * @param ids
	 * @return
	 */
	public List<MaterialFormat> queryMaterialFormatsInIds(List<Long> ids) {
		return this.saleArticleDao.queryMaterialFormatsInIds(ids);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public MaterialFormat queryMaterialFormatById(Long id) {
		return this.saleArticleDao.queryMaterialFormatById(id);
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterialFormat(MaterialFormat materialFormat) {
		return this.saleArticleDao.addMaterialFormat(materialFormat);
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterialFormat(MaterialFormat materialFormat) {
		return this.saleArticleDao.updateMaterialFormat(materialFormat);
	}
	
	/**
	 * 批量更新规格，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterialFormats(List<MaterialFormat> materialFormats) {
		return this.saleArticleDao.mergeMaterialFormats(materialFormats);
	}
	
	/**
	 * 删除规格的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialFormatsNotInIds(List<Long> ids, Long id) {
		return this.saleArticleDao.delMaterialFormatsNotInIds(ids, id);
	}
	
	/**
	 * 通过id查询材质
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Material> queryMaterialsByArticleId(Long articleId, Map<String, String> paramMap) {
		return this.saleArticleDao.queryMaterialsByArticleId(articleId, paramMap);
	}
	
	/**
	 * 通过id查询材质
	 * @param ids
	 * @return
	 */
	public List<Material> queryMaterialsInIds(List<Long> ids) {
		return this.saleArticleDao.queryMaterialsInIds(ids);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public Material queryMaterialById(Long id) {
		return this.saleArticleDao.queryMaterialById(id);
	}
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterial(Material material) {
		return this.saleArticleDao.addMaterial(material);
	}
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterial(Material material) {
		return this.saleArticleDao.updateMaterial(material);
	}
	
	/**
	 * 批量更新材质，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterials(List<Material> materials) {
		return this.saleArticleDao.mergeMaterials(materials);
	}
	
	/**
	 * 删除材质的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialsNotInIds(List<Long> ids, Long id) {
		return this.saleArticleDao.delMaterialsNotInIds(ids, id);
	}
	
	/**
	 * 新增商品
	 * @param article
	 * @return
	 */
	public boolean add(SaleArticle article, List<MaterialFormat> formats, List<Material> materials) {
		boolean success = false;
		if(null == article) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		article.setCreateUserId(HttpUtil.getLoginUser().getId());
		//验证对象的有效性
		//this.valid(article);
		this.saleArticleDao.add(article);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("articleId", article.getId());
		map.put("createUserId", HttpUtil.getLoginUser().getId());
		if(null != formats && 0 < formats.size()) {
			//将绩效主表的id赋值给绩效项目
			BeanUtil.setValueBatch(formats, map);
			//检查绩效项目的有效性
			//this.validContentBatch(formats);
			this.saleArticleDao.mergeMaterialFormats(formats);
		}
		if(null != materials && 0 < materials.size()) {
			BeanUtil.setValueBatch(materials, map);
			this.saleArticleDao.mergeMaterials(materials);
		}
		success = true;
		return success;
	}
	
	/**
	 * 更新商品
	 * @param article
	 * @return
	 */
	public boolean update(SaleArticle article, List<MaterialFormat> formats, List<Material> materials) {
		boolean success = false;
		if(null == article || null == article.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		SaleArticle persist = this.saleArticleDao.queryById(article.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (SaleArticle) BeanUtil.updateBean(persist, article);
		//this.valid(persist);
		this.saleArticleDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("articleId", article.getId());
		map.put("createUserId", HttpUtil.getLoginUser().getId());
		//获取已保存且未被用户删除的规格
		String formatIdStr = BeanUtil.parseString(formats, "id", ",");
		List<Long> formatIds = BeanUtil.parseStringToLongList(formatIdStr, ",");
		if(0 < formatIds.size()) {
			//先删除不在formatIds中的规格，不在formatIds中，即被用户在前端界面删除了
			this.saleArticleDao.delMaterialFormatsNotInIds(formatIds, persist.getId());
			List<MaterialFormat> persists = this.saleArticleDao.queryMaterialFormatsInIds(formatIds);
			formats = (List<MaterialFormat>) BeanUtil.updateBeans(persists, formats, "id", map);
			//this.validContentBatch(formats);
			this.saleArticleDao.mergeMaterialFormats(formats);
		}
		
		String materialIdStr = BeanUtil.parseString(materials, "id", ",");
		List<Long> materialIds = BeanUtil.parseStringToLongList(materialIdStr, ",");
		if(0 < materialIds.size()) {
			this.saleArticleDao.delMaterialsNotInIds(materialIds, persist.getId());
			List<Material> persistMaterials = this.saleArticleDao.queryMaterialsInIds(materialIds);
			materials = (List<Material>) BeanUtil.updateBeans(persistMaterials, materials, "id", map);
			this.saleArticleDao.mergeMaterials(materials);
		}
		
		success = true;
		return success;
	}
	
	/**
	 * 删除商品，伪删除，将isDelete=0更新为isDelete=1
	 * @param achievement
	 * @return
	 */
	public boolean del(SaleArticle article) {
		boolean success = false;
		if(null == article || null == article.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		SaleArticle persist = this.saleArticleDao.queryById(article.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.saleArticleDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	public boolean delMaterialFormatById(Long id) {
		boolean success = false;
		MaterialFormat persist = this.saleArticleDao.queryMaterialFormatById(id);
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.saleArticleDao.updateMaterialFormat(persist)) {
			success = true;
		}
		return success;
	}
	
	public boolean delMaterialById(Long id) {
		boolean success = false;
		Material persist = this.saleArticleDao.queryMaterialById(id);
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.saleArticleDao.updateMaterial(persist)) {
			success = true;
		}
		return success;
	}
}
