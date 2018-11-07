package com.linuslan.oa.workflow.flows.sale.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="wf_special_sale_content")
public class SpecialSaleContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfSpecialSaleContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfSpecialSaleContentSeq", sequenceName="wf_special_sale_content_seq")
	private Long id;
	
	@Column(name="special_sale_id")
	private Long specialSaleId;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="sale_article_id")
	private Long saleArticleId;
	
	@Formula("(SELECT t.name FROM wf_sale_article t WHERE t.id=sale_article_id)")
	private String saleArticleName;
	
	/**
	 * 图编号
	 */
	@Column(name="picture_no")
	private String pictureNo;
	
	/**
	 * 基材
	 */
	@Column(name="material_id")
	private Long materialId;
	
	@Formula("(SELECT t.name FROM wf_material t WHERE t.id=material_id)")
	private String materialName;
	
	/**
	 * 光泽度
	 */
	@Column(name="glossiness_id")
	private Long glossinessId;
	
	@Formula("(SELECT t.name FROM wf_glossiness t WHERE t.id=glossiness_id)")
	private String glossinessName;
	
	/**
	 * 打印面
	 */
	@Column(name="face_id")
	private Long faceId;
	
	@Formula("(SELECT t.name FROM wf_face t WHERE t.id=face_id)")
	private String faceName;
	
	/**
	 * 效果
	 */
	@Column(name="effect_id")
	private Long effectId;
	
	@Formula("(SELECT t.name FROM wf_effect t WHERE t.id=effect_id)")
	private String effectName;
	
	/**
	 * 基材规格
	 */
	@Column(name="material_format_id")
	private Long materialFormatId;
	
	@Formula("(SELECT t.name FROM wf_material_format t WHERE t.id=material_format_id)")
	private String materialFormatName;
	
	@Column(name="height")
	private Double height=0d;
	
	@Column(name="width")
	private Double width=0d;
	
	@Column(name="quantity")
	private Double quantity=0d;
	
	/**
	 * 单价
	 */
	@Column(name="price")
	private BigDecimal price;
	
	/**
	 * 耗损面积金额
	 * 原耗损面积金额，根据甲方需求，2016年5月31号改版后，改为耗损基材费用
	 */
	@Column(name="loss_area_amount")
	private BigDecimal lossAreaAmount;
	
	/**
	 * 材质金额
	 * 原材质金额，根据甲方需求，2016年5月31号改版后，改为产品金额
	 */
	@Column(name="material_amount")
	private BigDecimal materialAmount;
	
	/**
	 * 相框金额
	 * 原相框金额，根据甲方需求，2016年5月31号改版后，改为配件金额
	 */
	@Column(name="photo_frame_amount")
	private BigDecimal photoFrameAmount;
	
	/**
	 * 出货日期
	 */
	@Column(name="checkout_date")
	private Date checkoutDate;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;
	
	/**
	 * 单位
	 * 根据甲方需求，2016年5月31号改版后增加字段
	 */
	@Column(name="article_unit_id")
	private Long articleUnitId;
	
	
	@Formula("(SELECT t.name FROM wf_article_unit t WHERE t.id=article_unit_id)")
	private String articleUnitName;
	
	/**
	 * 出货单的单位
	 */
	@Column(name="checkout_unit")
	private String checkoutUnit;
	
	/**
	 * 出货单的数量
	 */
	@Column(name="checkout_quantity")
	private Double checkoutQuantity=0d;
	
	/**
	 * 出货单的原材料规格
	 */
	@Column(name="original_material_format")
	private String originalMaterialFormat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Long getSaleArticleId() {
		return saleArticleId;
	}

	public void setSaleArticleId(Long saleArticleId) {
		this.saleArticleId = saleArticleId;
	}

	public String getSaleArticleName() {
		return saleArticleName;
	}

	public void setSaleArticleName(String saleArticleName) {
		this.saleArticleName = saleArticleName;
	}

	public String getPictureNo() {
		return pictureNo;
	}

	public void setPictureNo(String pictureNo) {
		this.pictureNo = pictureNo;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public Long getGlossinessId() {
		return glossinessId;
	}

	public void setGlossinessId(Long glossinessId) {
		this.glossinessId = glossinessId;
	}

	public String getGlossinessName() {
		return glossinessName;
	}

	public void setGlossinessName(String glossinessName) {
		this.glossinessName = glossinessName;
	}

	public Long getFaceId() {
		return faceId;
	}

	public void setFaceId(Long faceId) {
		this.faceId = faceId;
	}

	public String getFaceName() {
		return faceName;
	}

	public void setFaceName(String faceName) {
		this.faceName = faceName;
	}

	public Long getEffectId() {
		return effectId;
	}

	public void setEffectId(Long effectId) {
		this.effectId = effectId;
	}

	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	public Long getMaterialFormatId() {
		return materialFormatId;
	}

	public void setMaterialFormatId(Long materialFormatId) {
		this.materialFormatId = materialFormatId;
	}

	public String getMaterialFormatName() {
		return materialFormatName;
	}

	public void setMaterialFormatName(String materialFormatName) {
		this.materialFormatName = materialFormatName;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getLossAreaAmount() {
		return lossAreaAmount;
	}

	public void setLossAreaAmount(BigDecimal lossAreaAmount) {
		this.lossAreaAmount = lossAreaAmount;
	}

	public BigDecimal getMaterialAmount() {
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount) {
		this.materialAmount = materialAmount;
	}

	public BigDecimal getPhotoFrameAmount() {
		return photoFrameAmount;
	}

	public void setPhotoFrameAmount(BigDecimal photoFrameAmount) {
		this.photoFrameAmount = photoFrameAmount;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getSpecialSaleId() {
		return specialSaleId;
	}

	public void setSpecialSaleId(Long specialSaleId) {
		this.specialSaleId = specialSaleId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getArticleUnitId() {
		return articleUnitId;
	}

	public void setArticleUnitId(Long articleUnitId) {
		this.articleUnitId = articleUnitId;
	}

	public String getArticleUnitName() {
		return articleUnitName;
	}

	public void setArticleUnitName(String articleUnitName) {
		this.articleUnitName = articleUnitName;
	}

	public String getCheckoutUnit() {
		return checkoutUnit;
	}

	public void setCheckoutUnit(String checkoutUnit) {
		this.checkoutUnit = checkoutUnit;
	}

	public Double getCheckoutQuantity() {
		return checkoutQuantity;
	}

	public void setCheckoutQuantity(Double checkoutQuantity) {
		this.checkoutQuantity = checkoutQuantity;
	}

	public String getOriginalMaterialFormat() {
		return originalMaterialFormat;
	}

	public void setOriginalMaterialFormat(String originalMaterialFormat) {
		this.originalMaterialFormat = originalMaterialFormat;
	}
}
