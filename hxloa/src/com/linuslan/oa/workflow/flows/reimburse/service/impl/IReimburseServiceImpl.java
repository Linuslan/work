package com.linuslan.oa.workflow.flows.reimburse.service.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import com.linuslan.oa.workflow.flows.reimburse.dao.IReimburseDao;
import com.linuslan.oa.workflow.flows.reimburse.model.Reimburse;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseContent;
import com.linuslan.oa.workflow.flows.reimburse.service.IReimburseService;

@Component("reimburseService")
@Transactional
public class IReimburseServiceImpl extends IBaseServiceImpl implements
		IReimburseService {

	private Logger logger = Logger.getLogger(IReimburseServiceImpl.class);
	
	@Autowired
	private IReimburseDao reimburseDao;
	
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
	public Page<Reimburse> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.reimburseDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.reimburseDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的报销
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.reimburseDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询报销汇总
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Reimburse> queryReportPage(Map<String, String> paramMap, int page, int rows) {
		return this.reimburseDao.queryReportPage(paramMap, page, rows);
	}
	
	/**
	 * 统计汇总页面的待审总额，待打款总额，已打款总额
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> sumReportPage(Map<String, String> paramMap) {
		return this.reimburseDao.sumReportPage(paramMap);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Reimburse queryById(Long id) {
		return this.reimburseDao.queryById(id);
	}
	
	/**
	 * 通过报销主表的id查询报销项目
	 * @param id
	 * @return
	 */
	public List<ReimburseContent> queryContentsByReimburseId(Long id) {
		return this.reimburseDao.queryContentsByReimburseId(id);
	}
	
	public List<Reimburse> queryInIds(List<Long> ids) {
		return this.reimburseDao.queryInIds(ids);
	}
	
	public List<ReimburseContent> queryContentsByReimburseIds(List<Long> ids) {
		return this.reimburseDao.queryContentsByReimburseIds(ids);
	}
	
	/**
	 * 通过部门id查询归属的报销类别
	 * @param departmentId
	 * @return
	 */
	public List<ReimburseClass> queryReimburseClassesByDepartmentId(Long departmentId) {
		return this.reimburseDao.queryReimburseClassesByDepartmentId(departmentId);
	}
	
	/**
	 * 新增企业付款
	 * @param reimburse
	 * @return
	 */
	public boolean add(Reimburse reimburse, List<ReimburseContent> contents) {
		boolean success = false;
		if(null == reimburse) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		reimburse.setUserId(HttpUtil.getLoginUser().getId());
		reimburse.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
		//验证对象的有效性
		this.valid(reimburse);
		this.reimburseDao.add(reimburse);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("reimburseId", reimburse.getId());
		//将报销主表的id赋值给报销项目
		BeanUtil.setValueBatch(contents, map);
		//检查报销项目的有效性
		this.validContentBatch(contents);
		this.reimburseDao.mergeContents(contents);
		//启动流程，生成流程实例
		this.engineUtil.startFlow(reimburse, CodeUtil.getClassName(Reimburse.class));
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param reimburse
	 * @return
	 */
	public boolean update(Reimburse reimburse, List<ReimburseContent> contents) {
		boolean success = false;
		if(null == reimburse || null == reimburse.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		Reimburse persist = this.reimburseDao.queryById(reimburse.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (Reimburse) BeanUtil.updateBean(persist, reimburse);
		this.valid(persist);
		this.reimburseDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("reimburseId", reimburse.getId());
		//获取已保存且未被用户删除的报销项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		List<ReimburseContent> persists = null;
		if(0 < contentIds.size()) {
			//先删除不在contentIds中的报销项目，不在contentIds中，即被用户在前端界面删除了
			this.reimburseDao.delContentsNotInIds(contentIds, persist.getId());
			persists = this.reimburseDao.queryContentsInIds(contentIds);
			
		}
		contents = (List<ReimburseContent>) BeanUtil.updateBeans(persists, contents, "id", map);
		//检查报销项目的有效性
		this.validContentBatch(contents);
		this.reimburseDao.mergeContents(contents);
		
		success = true;
		return success;
	}
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param reimburse
	 * @return
	 */
	public boolean del(Reimburse reimburse) {
		boolean success = false;
		if(null == reimburse || null == reimburse.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Reimburse persist = this.reimburseDao.queryById(reimburse.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.reimburseDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param reimburse
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Reimburse reimburse, List<ReimburseContent> contents, int passType, String opinion, boolean isUpdate) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == reimburse) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != reimburse.getId()) {
			Reimburse persist = this.reimburseDao.queryById(reimburse.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Reimburse) BeanUtil.updateBean(persist, reimburse);
			reimburse = persist;
			this.valid(persist);
			updateSuccess = this.reimburseDao.update(persist);
		} else {
			reimburse.setUserId(HttpUtil.getLoginUser().getId());
			reimburse.setUserDeptId(HttpUtil.getLoginUser().getDepartmentId());
			this.valid(reimburse);
			updateSuccess = this.reimburseDao.add(reimburse);
			this.engineUtil.startFlow(reimburse, CodeUtil.getClassName(Reimburse.class));
		}
		if(updateSuccess) {
			if(isUpdate) {
				Map<String, Long> map = new HashMap<String, Long> ();
				map.put("reimburseId", reimburse.getId());
				//获取已保存且未被用户删除的报销项目
				String contentIdStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
				//先删除不在contentIds中的报销项目，不在contentIds中，即被用户在前端界面删除了
				List<ReimburseContent> persists = null;
				if(0 < contentIds.size()) {
					this.reimburseDao.delContentsNotInIds(contentIds, reimburse.getId());
					persists = this.reimburseDao.queryContentsInIds(contentIds);
					
				}
				contents = (List<ReimburseContent>) BeanUtil.updateBeans(persists, contents, "id", map);
				//检查报销项目的有效性
				this.validContentBatch(contents);
				this.reimburseDao.mergeContents(contents);
			}
			
			//验证流程实例是否可以提交
			this.validFlowStatus(reimburse, false);
			this.engineUtil.execute(reimburse, CodeUtil.getClassName(Reimburse.class), passType);
			success = true;
		}
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param reimburse
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Reimburse reimburse, int passType, String opinion) {
		boolean success = false;
		if(null == reimburse || null == reimburse.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Reimburse persist = this.reimburseDao.queryById(reimburse.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Reimburse.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除报销项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.reimburseDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param reimburse
	 */
	public void valid(Reimburse reimburse) {
		if(null == reimburse.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		if(null == reimburse.getReimburseDeptId()) {
			CodeUtil.throwRuntimeExcep("请选择报销部门");
		}
		if(CodeUtil.isEmpty(reimburse.getReceiver())) {
			CodeUtil.throwRuntimeExcep("请输入收款方名称");
		}
		if(CodeUtil.isEmpty(reimburse.getBank())) {
			CodeUtil.throwRuntimeExcep("请输入收款方开户行");
		}
		if(CodeUtil.isEmpty(reimburse.getBankNo())) {
			CodeUtil.throwRuntimeExcep("请输入收款方银行账号");
		}
	}
	
	public void validContent(ReimburseContent content, int i) {
		if(null == content.getMoney()) {
			CodeUtil.throwRuntimeExcep("请输入第"+(i+1)+"项的金额");
		}
		if(null == content.getReimburseClassId()) {
			CodeUtil.throwRuntimeExcep("请选择第"+(i+1)+"项的报销类别");
		}
		if(null == content.getReimburseId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的报销单id位空");
		}
		if(null == content.getRemittanceDate()) {
			CodeUtil.throwRuntimeExcep("请选择第"+(i+1)+"项的费用产生时间");
		}
		if(null == content.getContent() || "".equals(content.getContent().trim())) {
			CodeUtil.throwRuntimeExcep("请输入第"+(i+1)+"项的报销项目");
		}
	}
	
	public void validContentBatch(List<ReimburseContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取报销项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条报销项目");
		}
		ReimburseContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
}
