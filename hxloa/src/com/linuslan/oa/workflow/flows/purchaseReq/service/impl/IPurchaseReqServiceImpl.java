package com.linuslan.oa.workflow.flows.purchaseReq.service.impl;

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
import com.linuslan.oa.workflow.flows.purchase.dao.IPurchaseDao;
import com.linuslan.oa.workflow.flows.purchase.model.Purchase;
import com.linuslan.oa.workflow.flows.purchase.model.PurchaseContent;
import com.linuslan.oa.workflow.flows.purchase.service.impl.IPurchaseServiceImpl;
import com.linuslan.oa.workflow.flows.purchaseReq.dao.IPurchaseReqDao;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReq;
import com.linuslan.oa.workflow.flows.purchaseReq.model.PurchaseReqContent;
import com.linuslan.oa.workflow.flows.purchaseReq.service.IPurchaseReqService;

@Component("purchaseReqService")
@Transactional
public class IPurchaseReqServiceImpl extends IBaseServiceImpl implements
		IPurchaseReqService {
	private static Logger logger = Logger.getLogger(IPurchaseReqServiceImpl.class);
	
	@Autowired
	private IPurchaseReqDao purchaseReqDao;
	
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
	public Page<PurchaseReq> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseReqDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<PurchaseReq> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseReqDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的采购申请
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<PurchaseReq> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.purchaseReqDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public PurchaseReq queryById(Long id) {
		return this.purchaseReqDao.queryById(id);
	}
	
	/**
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<PurchaseReqContent> queryContentsByPurchaseReqId(Long id) {
		return this.purchaseReqDao.queryContentsByPurchaseReqId(id);
	}
	
	/**
	 * 新增出库
	 * @param purchaseReq
	 * @return
	 */
	public boolean add(PurchaseReq purchaseReq, List<PurchaseReqContent> contents) {
		boolean success = false;
		try {
			if(null == purchaseReq) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			purchaseReq.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(purchaseReq);
			this.purchaseReqDao.add(purchaseReq);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseReqId", purchaseReq.getId());
			//将出库主表的id赋值给出库项目
			BeanUtil.setValueBatch(contents, map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseReqDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(purchaseReq, CodeUtil.getClassName(PurchaseReq.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新出库
	 * @param purchaseReq
	 * @return
	 */
	public boolean update(PurchaseReq purchaseReq, List<PurchaseReqContent> contents) {
		boolean success = false;
		try {
			if(null == purchaseReq || null == purchaseReq.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			PurchaseReq persist = this.purchaseReqDao.queryById(purchaseReq.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (PurchaseReq) BeanUtil.updateBean(persist, purchaseReq);
			this.valid(persist);
			this.purchaseReqDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseReqId", purchaseReq.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<PurchaseReqContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
				this.purchaseReqDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.purchaseReqDao.queryContentsInIds(contentIds);
				
			}
			contents = (List<PurchaseReqContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseReqDao.mergeContents(contents);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除出库，伪删除，将isDelete=0更新为isDelete=1
	 * @param purchaseReq
	 * @return
	 */
	public boolean del(PurchaseReq purchaseReq) {
		boolean success = false;
		if(null == purchaseReq || null == purchaseReq.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		PurchaseReq persist = this.purchaseReqDao.queryById(purchaseReq.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.purchaseReqDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param purchaseReq
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(PurchaseReq purchaseReq, List<PurchaseReqContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == purchaseReq) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != purchaseReq.getId()) {
			PurchaseReq persist = this.purchaseReqDao.queryById(purchaseReq.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (PurchaseReq) BeanUtil.updateBean(persist, purchaseReq);
			purchaseReq = persist;
			this.valid(persist);
			updateSuccess = this.purchaseReqDao.update(persist);
		} else {
			purchaseReq.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(purchaseReq);
			updateSuccess = this.purchaseReqDao.add(purchaseReq);
			this.engineUtil.startFlow(purchaseReq, CodeUtil.getClassName(PurchaseReq.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("purchaseReqId", purchaseReq.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
			List<PurchaseReqContent> persists = null;
			if(0 < contentIds.size()) {
				this.purchaseReqDao.delContentsNotInIds(contentIds, purchaseReq.getId());
				persists = this.purchaseReqDao.queryContentsInIds(contentIds);
			}
			contents = (List<PurchaseReqContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.purchaseReqDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(purchaseReq, false);
			this.engineUtil.execute(purchaseReq, CodeUtil.getClassName(PurchaseReq.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param purchaseReq
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(PurchaseReq purchaseReq, int passType, String opinion) {
		boolean success = false;
		if(null == purchaseReq || null == purchaseReq.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		PurchaseReq persist = this.purchaseReqDao.queryById(purchaseReq.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(PurchaseReq.class), passType);
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
		return this.purchaseReqDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param purchaseReq
	 */
	public void valid(PurchaseReq purchaseReq) {
		if(null == purchaseReq.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(PurchaseReqContent content, int i) {
		
		if(null == content.getPurchaseReqId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的出库单id位空");
		}
		
	}
	
	public void validContentBatch(List<PurchaseReqContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取出库项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条出库项目");
		}
		PurchaseReqContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
}
