package com.linuslan.oa.workflow.flows.sale.service.impl;

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
import com.linuslan.oa.workflow.flows.sale.dao.ISaleDao;
import com.linuslan.oa.workflow.flows.sale.model.Sale;
import com.linuslan.oa.workflow.flows.sale.model.SaleContent;
import com.linuslan.oa.workflow.flows.sale.service.impl.ISaleServiceImpl;
import com.linuslan.oa.workflow.flows.sale.service.ISaleService;

@Component("saleService")
@Transactional
public class ISaleServiceImpl extends IBaseServiceImpl implements ISaleService {
	private Logger logger = Logger.getLogger(ISaleServiceImpl.class);
	
	@Autowired
	private ISaleDao saleDao;
	
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
	public Page<Sale> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.saleDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Sale> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.saleDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的销售订单
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Sale> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.saleDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Sale queryById(Long id) {
		return this.saleDao.queryById(id);
	}
	
	/**
	 * 通过销售主表的id查询销售项目
	 * @param id
	 * @return
	 */
	public List<SaleContent> queryContentsBySaleId(Long id) {
		return this.saleDao.queryContentsBySaleId(id);
	}
	
	/**
	 * 新增销售
	 * @param sale
	 * @return
	 */
	public boolean add(Sale sale, List<SaleContent> contents) {
		boolean success = false;
		try {
			if(null == sale) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			sale.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(sale);
			this.saleDao.add(sale);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("saleId", sale.getId());
			//将销售主表的id赋值给销售项目
			BeanUtil.setValueBatch(contents, map);
			//检查销售项目的有效性
			this.validContentBatch(contents);
			this.saleDao.mergeContents(contents);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(sale, CodeUtil.getClassName(Sale.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新销售
	 * @param sale
	 * @return
	 */
	public boolean update(Sale sale, List<SaleContent> contents) {
		boolean success = false;
		try {
			if(null == sale || null == sale.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			Sale persist = this.saleDao.queryById(sale.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (Sale) BeanUtil.updateBean(persist, sale);
			this.valid(persist);
			this.saleDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("saleId", sale.getId());
			//获取已保存且未被用户删除的销售项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<SaleContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的销售项目，不在contentIds中，即被用户在前端界面删除了
				this.saleDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.saleDao.queryContentsInIds(contentIds);
				
			}
			contents = (List<SaleContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查销售项目的有效性
			this.validContentBatch(contents);
			this.saleDao.mergeContents(contents);
			
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除销售，伪删除，将isDelete=0更新为isDelete=1
	 * @param sale
	 * @return
	 */
	public boolean del(Sale sale) {
		boolean success = false;
		if(null == sale || null == sale.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Sale persist = this.saleDao.queryById(sale.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.saleDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param sale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Sale sale, List<SaleContent> contents, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == sale) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != sale.getId()) {
			Sale persist = this.saleDao.queryById(sale.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Sale) BeanUtil.updateBean(persist, sale);
			sale = persist;
			this.valid(persist);
			updateSuccess = this.saleDao.update(persist);
		} else {
			sale.setUserId(HttpUtil.getLoginUser().getId());
			this.valid(sale);
			updateSuccess = this.saleDao.add(sale);
			this.engineUtil.startFlow(sale, CodeUtil.getClassName(Sale.class));
		}
		if(updateSuccess) {
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("saleId", sale.getId());
			//获取已保存且未被用户删除的销售项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			//先删除不在contentIds中的销售项目，不在contentIds中，即被用户在前端界面删除了
			List<SaleContent> persists = null;
			if(0 < contentIds.size()) {
				this.saleDao.delContentsNotInIds(contentIds, sale.getId());
				persists = this.saleDao.queryContentsInIds(contentIds);
			}
			contents = (List<SaleContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查销售项目的有效性
			this.validContentBatch(contents);
			this.saleDao.mergeContents(contents);
			
			//验证流程实例是否可以提交
			this.validFlowStatus(sale, false);
			this.engineUtil.execute(sale, CodeUtil.getClassName(Sale.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param sale
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Sale sale, List<SaleContent> contents, int passType, String opinion) {
		boolean success = false;
		if(null == sale || null == sale.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Sale persist = this.saleDao.queryById(sale.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		BeanUtil.updateBean(persist, sale);
		this.saleDao.update(persist);
		//获取已保存且未被用户删除的销售项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		//先删除不在contentIds中的销售项目，不在contentIds中，即被用户在前端界面删除了
		if(0 < contentIds.size()) {
			List<SaleContent> persists = this.saleDao.queryContentsInIds(contentIds);
			contents = (List<SaleContent>) BeanUtil.updateBeans(persists, contents, "id", null);
			//检查销售项目的有效性
			this.validContentBatch(contents);
			this.saleDao.mergeContents(contents);
		}
		this.engineUtil.execute(persist, CodeUtil.getClassName(Sale.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除销售项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.saleDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param sale
	 */
	public void valid(Sale sale) {
		if(null == sale.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(SaleContent content, int i) {
		
		if(null == content.getSaleId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的销售单id位空");
		}
		
	}
	
	public void validContentBatch(List<SaleContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取销售项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条销售项目");
		}
		SaleContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
}
