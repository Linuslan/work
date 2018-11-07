package com.linuslan.oa.workflow.flows.purchase.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import com.linuslan.oa.workflow.flows.purchase.model.Purchase;
import com.linuslan.oa.workflow.flows.purchase.model.PurchaseContent;
import com.linuslan.oa.workflow.flows.purchase.dao.IPurchaseDao;
import com.linuslan.oa.workflow.flows.purchase.service.IPurchaseService;

@Component("purchaseService")
@Transactional
public class IPurchaseServiceImpl extends IBaseServiceImpl implements
		IPurchaseService {
	
	private static Logger logger = Logger.getLogger(IPurchaseServiceImpl.class);
	
	@Autowired
	private IPurchaseDao purchaseDao;
	
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
	public Page<Purchase> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的采购
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Purchase> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Purchase queryById(Long id) {
		return this.purchaseDao.queryById(id);
	}
	
	/**
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<PurchaseContent> queryContentsByPurchaseId(Long id) {
		return this.purchaseDao.queryContentsByPurchaseId(id);
	}
	
	/**
	 * 新增出库
	 * @param purchase
	 * @return
	 */
	public boolean add(Purchase purchase, List<PurchaseContent> contents) {
		boolean success = false;
		try {
			if(null == purchase) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			purchase.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(purchase);
			this.purchaseDao.add(purchase);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseId", purchase.getId());
			//将出库主表的id赋值给出库项目
			BeanUtil.setValueBatch(contents, map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(purchase, CodeUtil.getClassName(Purchase.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新出库
	 * @param purchase
	 * @return
	 */
	public boolean update(Purchase purchase, List<PurchaseContent> contents) {
		boolean success = false;
		try {
			if(null == purchase || null == purchase.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			Purchase persist = this.purchaseDao.queryById(purchase.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (Purchase) BeanUtil.updateBean(persist, purchase);
			this.valid(persist);
			this.purchaseDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseId", purchase.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<PurchaseContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
				this.purchaseDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.purchaseDao.queryContentsInIds(contentIds);
				
			}
			contents = (List<PurchaseContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseDao.mergeContents(contents);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除出库，伪删除，将isDelete=0更新为isDelete=1
	 * @param purchase
	 * @return
	 */
	public boolean del(Purchase purchase) {
		boolean success = false;
		if(null == purchase || null == purchase.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Purchase persist = this.purchaseDao.queryById(purchase.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.purchaseDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param purchase
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Purchase purchase, List<PurchaseContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == purchase) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != purchase.getId()) {
			Purchase persist = this.purchaseDao.queryById(purchase.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Purchase) BeanUtil.updateBean(persist, purchase);
			purchase = persist;
			this.valid(persist);
			updateSuccess = this.purchaseDao.update(persist);
		} else {
			purchase.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(purchase);
			updateSuccess = this.purchaseDao.add(purchase);
			this.engineUtil.startFlow(purchase, CodeUtil.getClassName(Purchase.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseId", purchase.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<PurchaseContent> persists = null;
			//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
			if(0 < contentIds.size()) {
				this.purchaseDao.delContentsNotInIds(contentIds, purchase.getId());
				persists = this.purchaseDao.queryContentsInIds(contentIds);
			}
			contents = (List<PurchaseContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseDao.mergeContents(contents);
			//验证流程实例是否可以提交
			this.validFlowStatus(purchase, false);
			this.engineUtil.execute(purchase, CodeUtil.getClassName(Purchase.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param purchase
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Purchase purchase, int passType, String opinion) {
		boolean success = false;
		if(null == purchase || null == purchase.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Purchase persist = this.purchaseDao.queryById(purchase.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Purchase.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除出库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.purchaseDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param purchase
	 */
	public void valid(Purchase purchase) {
		if(null == purchase.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(PurchaseContent content, int i) {
		
		if(null == content.getPurchaseId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的出库单id位空");
		}
		
	}
	
	public void validContentBatch(List<PurchaseContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取出库项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条出库项目");
		}
		PurchaseContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
}
