package com.linuslan.oa.workflow.flows.checkout.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.PropertyUtil;
import com.linuslan.oa.util.SerialNoFactory;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.checkout.model.Checkout;
import com.linuslan.oa.workflow.flows.checkout.model.CheckoutContent;
import com.linuslan.oa.workflow.flows.checkout.service.impl.ICheckoutServiceImpl;
import com.linuslan.oa.workflow.flows.checkout.dao.ICheckoutDao;
import com.linuslan.oa.workflow.flows.checkout.service.ICheckoutService;

@Component("checkoutService")
@Transactional
public class ICheckoutServiceImpl extends IBaseServiceImpl implements
		ICheckoutService {
	private Logger logger = Logger.getLogger(ICheckoutServiceImpl.class);
	
	@Autowired
	private ICheckoutDao checkoutDao;
	
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
	public Page<Checkout> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkoutDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkout> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkoutDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的出库
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkout> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkoutDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Checkout queryById(Long id) {
		return this.checkoutDao.queryById(id);
	}
	
	/**
	 * 通过出库主表的id查询出库项目
	 * @param id
	 * @return
	 */
	public List<CheckoutContent> queryContentsByCheckoutId(Long id) {
		return this.checkoutDao.queryContentsByCheckoutId(id);
	}
	
	/**
	 * 新增出库
	 * @param checkout
	 * @return
	 */
	public boolean add(Checkout checkout, List<CheckoutContent> contents) {
		boolean success = false;
		try {
			if(null == checkout) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			checkout.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(checkout);
			this.checkoutDao.add(checkout);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("checkoutId", checkout.getId());
			//将出库主表的id赋值给出库项目
			BeanUtil.setValueBatch(contents, map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.checkoutDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(checkout, CodeUtil.getClassName(Checkout.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新出库
	 * @param checkout
	 * @return
	 */
	public boolean update(Checkout checkout, List<CheckoutContent> contents) {
		boolean success = false;
		try {
			if(null == checkout || null == checkout.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			Checkout persist = this.checkoutDao.queryById(checkout.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (Checkout) BeanUtil.updateBean(persist, checkout);
			this.valid(persist);
			this.checkoutDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("checkoutId", checkout.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<CheckoutContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
				this.checkoutDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.checkoutDao.queryContentsInIds(contentIds);
			}
			contents = (List<CheckoutContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.checkoutDao.mergeContents(contents);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除出库，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkout
	 * @return
	 */
	public boolean del(Checkout checkout) {
		boolean success = false;
		if(null == checkout || null == checkout.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Checkout persist = this.checkoutDao.queryById(checkout.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.checkoutDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param checkout
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Checkout checkout, List<CheckoutContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == checkout) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != checkout.getId()) {
			Checkout persist = this.checkoutDao.queryById(checkout.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Checkout) BeanUtil.updateBean(persist, checkout);
			checkout = persist;
			this.valid(persist);
			updateSuccess = this.checkoutDao.update(persist);
		} else {
			checkout.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(checkout);
			updateSuccess = this.checkoutDao.add(checkout);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(checkout, CodeUtil.getClassName(Checkout.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("checkoutId", checkout.getId());
			//获取已保存且未被用户删除的出库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			//先删除不在contentIds中的出库项目，不在contentIds中，即被用户在前端界面删除了
			List<CheckoutContent> persists = null;
			if(0 < contentIds.size()) {
				this.checkoutDao.delContentsNotInIds(contentIds, checkout.getId());
				persists = this.checkoutDao.queryContentsInIds(contentIds);
			}
			contents = (List<CheckoutContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查出库项目的有效性
			this.validContentBatch(contents);
			this.checkoutDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(checkout, false);
			this.engineUtil.execute(checkout, CodeUtil.getClassName(Checkout.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param checkout
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Checkout checkout, int passType, String opinion) {
		boolean success = false;
		if(null == checkout || null == checkout.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Checkout persist = this.checkoutDao.queryById(checkout.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Checkout.class), passType);
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
		return this.checkoutDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param checkout
	 */
	public void valid(Checkout checkout) {
		if(null == checkout.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(CheckoutContent content, int i) {
		
		if(null == content.getCheckoutId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的出库单id位空");
		}
		
	}
	
	public void validContentBatch(List<CheckoutContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取出库项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条出库项目");
		}
		CheckoutContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
}
