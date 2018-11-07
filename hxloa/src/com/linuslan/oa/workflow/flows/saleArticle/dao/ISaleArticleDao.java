package com.linuslan.oa.workflow.flows.saleArticle.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleArticle.model.Material;
import com.linuslan.oa.workflow.flows.saleArticle.model.MaterialFormat;
import com.linuslan.oa.workflow.flows.saleArticle.model.SaleArticle;

public interface ISaleArticleDao extends IBaseDao {

	/**
	 * 通过id分页查询
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SaleArticle> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询所有的入库商品
	 * @return
	 */
	public List<SaleArticle> queryAll();
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public SaleArticle queryById(Long id);
	
	/**
	 * 通过公司id查询入库商品
	 * @param companyId
	 * @return
	 */
	public List<SaleArticle> querySaleArticlesByCompanyId(Long companyId);
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean add(SaleArticle saleArticle);
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean update(SaleArticle saleArticle);
	
	/**
	 * 通过id查询规格
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<MaterialFormat> queryMaterialFormatsByArticleId(Long articleId, Map<String, String> paramMap);
	
	/**
	 * 通过id查询规格
	 * @param ids
	 * @return
	 */
	public List<MaterialFormat> queryMaterialFormatsInIds(List<Long> ids);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public MaterialFormat queryMaterialFormatById(Long id);
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterialFormat(MaterialFormat materialFormat);
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterialFormat(MaterialFormat materialFormat);
	
	/**
	 * 批量更新规格，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterialFormats(List<MaterialFormat> materialFormats);
	
	/**
	 * 删除规格的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialFormatsNotInIds(List<Long> ids, Long id);
	
	/**
	 * 通过id查询材质
	 * @param departmentId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Material> queryMaterialsByArticleId(Long articleId, Map<String, String> paramMap);
	
	/**
	 * 通过id查询材质
	 * @param ids
	 * @return
	 */
	public List<Material> queryMaterialsInIds(List<Long> ids);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public Material queryMaterialById(Long id);
	
	/**
	 * 新增
	 * @param saleArticle
	 * @return
	 */
	public boolean addMaterial(Material material);
	
	/**
	 * 更新
	 * @param saleArticle
	 * @return
	 */
	public boolean updateMaterial(Material material);
	
	/**
	 * 批量更新材质，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeMaterials(List<Material> materials);
	
	/**
	 * 删除材质的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delMaterialsNotInIds(List<Long> ids, Long id);
	
}
