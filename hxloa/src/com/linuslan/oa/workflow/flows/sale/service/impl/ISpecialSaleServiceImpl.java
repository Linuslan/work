package com.linuslan.oa.workflow.flows.sale.service.impl;

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
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.sale.dao.ISpecialSaleDao;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSale;
import com.linuslan.oa.workflow.flows.sale.model.SpecialSaleContent;
import com.linuslan.oa.workflow.flows.sale.service.ISpecialSaleService;

@Component("specialSaleService")
@Transactional
public class ISpecialSaleServiceImpl extends IBaseServiceImpl implements
		ISpecialSaleService {
	@Autowired
	private ISpecialSaleDao specialSaleDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	/**
	 * 查询登陆用户的申请单列表
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.specialSaleDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.specialSaleDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的特殊销售
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SpecialSale> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.specialSaleDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public SpecialSale queryById(Long id) {
		return this.specialSaleDao.queryById(id);
	}
	
	/**
	 * 通过华夏蓝销售订单主表的id查询华夏蓝销售订单项目
	 * @param id
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsBySpecialSaleId(Long id) {
		return this.specialSaleDao.queryContentsBySpecialSaleId(id);
	}
	
	/**
	 * 新增华夏蓝销售订单
	 * @param specialSale
	 * @return
	 */
	public boolean add(SpecialSale specialSale, List<SpecialSaleContent> contents) {
		boolean success = false;
		try {
			if(null == specialSale) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			specialSale.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(specialSale);
			this.specialSaleDao.add(specialSale);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("specialSaleId", specialSale.getId());
			//将华夏蓝销售订单主表的id赋值给华夏蓝销售订单项目
			BeanUtil.setValueBatch(contents, map);
			//检查华夏蓝销售订单项目的有效性
			this.validContentBatch(contents);
			this.specialSaleDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(specialSale, CodeUtil.getClassName(SpecialSale.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新华夏蓝销售订单
	 * @param specialSale
	 * @return
	 */
	public boolean update(SpecialSale specialSale, List<SpecialSaleContent> contents) {
		boolean success = false;
		try {
			if(null == specialSale || null == specialSale.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			SpecialSale persist = this.specialSaleDao.queryById(specialSale.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (SpecialSale) BeanUtil.updateBean(persist, specialSale);
			this.valid(persist);
			this.specialSaleDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("specialSaleId", specialSale.getId());
			//获取已保存且未被用户删除的华夏蓝销售订单项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<SpecialSaleContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的华夏蓝销售订单项目，不在contentIds中，即被用户在前端界面删除了
				this.specialSaleDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.specialSaleDao.queryContentsInIds(contentIds);
				
			}
			contents = (List<SpecialSaleContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查华夏蓝销售订单项目的有效性
			this.validContentBatch(contents);
			this.specialSaleDao.mergeContents(contents);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	public boolean update(SpecialSale specialSale) {
		boolean success = false;
		if(this.specialSaleDao.update(specialSale)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 删除华夏蓝销售订单，伪删除，将isDelete=0更新为isDelete=1
	 * @param specialSale
	 * @return
	 */
	public boolean del(SpecialSale specialSale) {
		boolean success = false;
		if(null == specialSale || null == specialSale.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		SpecialSale persist = this.specialSaleDao.queryById(specialSale.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.specialSaleDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param specialSale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(SpecialSale specialSale, List<SpecialSaleContent> contents, int passType, String opinion, boolean isUpdate) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == specialSale) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != specialSale.getId()) {
			SpecialSale persist = this.specialSaleDao.queryById(specialSale.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (SpecialSale) BeanUtil.updateBean(persist, specialSale);
			specialSale = persist;
			this.valid(persist);
			updateSuccess = this.specialSaleDao.update(persist);
		} else {
			specialSale.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(specialSale);
			updateSuccess = this.specialSaleDao.add(specialSale);
			this.engineUtil.startFlow(specialSale, CodeUtil.getClassName(SpecialSale.class));
		}
		if(updateSuccess) {
			if(isUpdate) {
				Map<String, Long> map = new HashMap<String, Long> ();
				map.put("specialSaleId", specialSale.getId());
				//获取已保存且未被用户删除的华夏蓝销售订单项目
				String contentIdStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
				//先删除不在contentIds中的华夏蓝销售订单项目，不在contentIds中，即被用户在前端界面删除了
				List<SpecialSaleContent> persists = null;
				if(0 < contentIds.size()) {
					this.specialSaleDao.delContentsNotInIds(contentIds, specialSale.getId());
					persists = this.specialSaleDao.queryContentsInIds(contentIds);
					
				}
				contents = (List<SpecialSaleContent>) BeanUtil.updateBeans(persists, contents, "id", map);
				//检查华夏蓝销售订单项目的有效性
				this.validContentBatch(contents);
				this.specialSaleDao.mergeContents(contents);
			}
			
			//验证流程实例是否可以提交
			this.validFlowStatus(specialSale, false);
			this.engineUtil.execute(specialSale, CodeUtil.getClassName(SpecialSale.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param specialSale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(SpecialSale specialSale, int passType, String opinion) {
		boolean success = false;
		if(null == specialSale || null == specialSale.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		SpecialSale persist = this.specialSaleDao.queryById(specialSale.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		BeanUtil.updateBean(persist, specialSale);
		this.specialSaleDao.update(persist);
		this.engineUtil.execute(persist, CodeUtil.getClassName(SpecialSale.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除华夏蓝销售订单项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.specialSaleDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param specialSale
	 */
	public void valid(SpecialSale specialSale) {
		if(null == specialSale.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(SpecialSaleContent content, int i) {
		
		if(null == content.getSpecialSaleId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的销售订单id位空");
		}
		
	}
	
	public void validContentBatch(List<SpecialSaleContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取销售订单项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条销售订单项目");
		}
		SpecialSaleContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
	
	/**
	 * 通过id查询华夏蓝销售订单项目
	 * @param ids
	 * @return
	 */
	public List<SpecialSaleContent> queryContentsInIds(List<Long> ids) {
		return this.specialSaleDao.queryContentsInIds(ids);
	}
	
	/**
	 * 批量更新华夏蓝销售订单项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<SpecialSaleContent> contents) {
		return this.specialSaleDao.mergeContents(contents);
	}
}
