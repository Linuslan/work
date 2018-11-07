package com.linuslan.oa.workflow.flows.checkin.service.impl;

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
import com.linuslan.oa.system.upload.dao.IUploadFileDao;
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
import com.linuslan.oa.workflow.flows.checkin.dao.ICheckinDao;
import com.linuslan.oa.workflow.flows.checkin.model.Checkin;
import com.linuslan.oa.workflow.flows.checkin.model.CheckinContent;
import com.linuslan.oa.workflow.flows.checkin.service.ICheckinService;

@Component("checkinService")
@Transactional
public class ICheckinServiceImpl extends IBaseServiceImpl implements
		ICheckinService {
	
	private Logger logger = Logger.getLogger(ICheckinServiceImpl.class);
	
	@Autowired
	private ICheckinDao checkinDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	@Autowired
	private IUploadFileDao uploadFileDao;
	
	/**
	 * 查询登陆用户的申请单列表
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkinDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkinDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的入库
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.checkinDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Checkin queryById(Long id) {
		return this.checkinDao.queryById(id);
	}
	
	/**
	 * 通过入库主表的id查询入库项目
	 * @param id
	 * @return
	 */
	public List<CheckinContent> queryContentsByCheckinId(Long id) {
		return this.checkinDao.queryContentsByCheckinId(id);
	}
	
	/**
	 * 新增入库
	 * @param checkin
	 * @return
	 */
	public boolean add(Checkin checkin, List<CheckinContent> contents, List<UploadFile> uploadFiles) {
		boolean success = false;
		try {
			if(null == checkin) {
				CodeUtil.throwRuntimeExcep("获取数据异常");
			}
			checkin.setUserId(HttpUtil.getLoginUser().getId());
			//验证对象的有效性
			this.valid(checkin);
			this.checkinDao.add(checkin);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("checkinId", checkin.getId());
			//将入库主表的id赋值给入库项目
			BeanUtil.setValueBatch(contents, map);
			//检查入库项目的有效性
			this.validContentBatch(contents);
			this.checkinDao.mergeContents(contents);
			/*
			 * 将checkin的对象Id设置到上传文件的对象中
			 */
			Map<String, Long> valueMap = new HashMap<String, Long>();
			valueMap.put("tbId", checkin.getId());
			BeanUtil.setValueBatch(uploadFiles, valueMap);
			this.uploadFileDao.addBatch(uploadFiles);
			//启动流程，生成流程实例
			this.engineUtil.startFlow(checkin, CodeUtil.getClassName(Checkin.class));
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			this.delFiles(uploadFiles);
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 更新入库
	 * @param checkin
	 * @return
	 */
	public boolean update(Checkin checkin, List<CheckinContent> contents, List<UploadFile> uploadFiles) {
		boolean success = false;
		try {
			if(null == checkin || null == checkin.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
			}
			Checkin persist = this.checkinDao.queryById(checkin.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
			}
			persist = (Checkin) BeanUtil.updateBean(persist, checkin);
			this.valid(persist);
			this.checkinDao.update(persist);
			Map<String, Long> map = new HashMap<String, Long> ();
			map.put("checkinId", checkin.getId());
			//获取已保存且未被用户删除的入库项目
			String contentIdStr = BeanUtil.parseString(contents, "id", ",");
			List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
			List<CheckinContent> persists = null;
			if(0 < contentIds.size()) {
				//先删除不在contentIds中的入库项目，不在contentIds中，即被用户在前端界面删除了
				this.checkinDao.delContentsNotInIds(contentIds, persist.getId());
				persists = this.checkinDao.queryContentsInIds(contentIds);
				
			}
			contents = (List<CheckinContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查入库项目的有效性
			this.validContentBatch(contents);
			this.checkinDao.mergeContents(contents);
			/*
			 * 将checkin的对象Id设置到上传文件的对象中
			 */
			Map<String, Long> valueMap = new HashMap<String, Long>();
			valueMap.put("tbId", persist.getId());
			BeanUtil.setValueBatch(uploadFiles, valueMap);
			this.uploadFileDao.addBatch(uploadFiles);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
			this.delFiles(uploadFiles);
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 删除入库，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkin
	 * @return
	 */
	public boolean del(Checkin checkin) {
		boolean success = false;
		if(null == checkin || null == checkin.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Checkin persist = this.checkinDao.queryById(checkin.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.checkinDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param checkin
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Checkin checkin, List<CheckinContent> contents, List<UploadFile> uploadFiles, int passType, String opinion) {
		boolean success = false;
		boolean updateSuccess = false;
		try {
			if(null == checkin) {
				CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
			}
			/*
			 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
			 */
			if(null != checkin.getId()) {
				Checkin persist = this.checkinDao.queryById(checkin.getId());
				if(null == persist || null == persist.getId()) {
					CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
				}
				persist = (Checkin) BeanUtil.updateBean(persist, checkin);
				checkin = persist;
				this.valid(persist);
				updateSuccess = this.checkinDao.update(persist);
			} else {
				checkin.setUserId(HttpUtil.getLoginUser().getId());
				this.valid(checkin);
				updateSuccess = this.checkinDao.add(checkin);
				//启动流程，生成流程实例
				this.engineUtil.startFlow(checkin, CodeUtil.getClassName(Checkin.class));
			}
			if(null != uploadFiles && 0 < uploadFiles.size()) {
				/*
				 * 将checkin的对象Id设置到上传文件的对象中
				 */
				Map<String, Long> valueMap = new HashMap<String, Long>();
				valueMap.put("tbId", checkin.getId());
				BeanUtil.setValueBatch(uploadFiles, valueMap);
				this.uploadFileDao.addBatch(uploadFiles);
			}
			if(updateSuccess) {
				Map<String, Long> map = new HashMap<String, Long> ();
				map.put("checkinId", checkin.getId());
				//获取已保存且未被用户删除的入库项目
				String contentIdStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
				//先删除不在contentIds中的入库项目，不在contentIds中，即被用户在前端界面删除了
				List<CheckinContent> persists = null;
				if(0 < contentIds.size()) {
					this.checkinDao.delContentsNotInIds(contentIds, checkin.getId());
					persists = this.checkinDao.queryContentsInIds(contentIds);
					
				}
				contents = (List<CheckinContent>) BeanUtil.updateBeans(persists, contents, "id", map);
				//检查入库项目的有效性
				this.validContentBatch(contents);
				this.checkinDao.mergeContents(contents);
				
				//验证流程实例是否可以提交
				this.validFlowStatus(checkin, false);
				this.engineUtil.execute(checkin, CodeUtil.getClassName(Checkin.class), passType);
				success = true;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.delFiles(uploadFiles);
			CodeUtil.throwRuntimeExcep(ex);
		}
		
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param checkin
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Checkin checkin, int passType, String opinion) {
		boolean success = false;
		if(null == checkin || null == checkin.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Checkin persist = this.checkinDao.queryById(checkin.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Checkin.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		success = true;
		return success;
	}
	
	/**
	 * 删除入库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.checkinDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param checkin
	 */
	public void valid(Checkin checkin) {
		if(null == checkin.getCompanyId()) {
			CodeUtil.throwRuntimeExcep("请选择归属公司");
		}
		
	}
	
	public void validContent(CheckinContent content, int i) {
		
		if(null == content.getCheckinId()) {
			CodeUtil.throwRuntimeExcep("第"+(i+1)+"项对应的入库单id位空");
		}
		
	}
	
	public void validContentBatch(List<CheckinContent> contents) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取入库项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条入库项目");
		}
		CheckinContent content = null;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
		}
	}
	
	/**
	 * 上传文件，并转换成UploadFile对象
	 * @param files
	 * @param fileNames
	 * @return
	 * @throws RuntimeException
	 */
	public List<UploadFile> getUploadFiles(List<File> files, List<String> fileNames) throws RuntimeException {
		List<UploadFile> list = new ArrayList<UploadFile> ();
		try {
			String path = PropertyUtil.getConfigPropertyValue("checkinDir");
			if(CodeUtil.isEmpty(path)) {
				CodeUtil.throwExcep("文件上传路径为空");
			}
			String realPath = HttpUtil.getRealPath(path);
			if(null != files) {
				File file = null;
				String fileName = null;
				String realName = null;
				for(int i = 0; i < files.size(); i ++) {
					file = files.get(i);
					fileName = fileNames.get(i);
					realName = SerialNoFactory.buildRandom(10) + "_" + DateUtil.parseDateToStr(new Date(), "yyyyMMddHHmmss")+ "_" + fileName;
					String filePath = realPath+"\\"+realName;
					FileOutputStream fos = new FileOutputStream(filePath);
					FileInputStream fis = new FileInputStream(file);
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = fis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	                fos.flush();
	                fis.close();
	                fos.close();
	                
	                UploadFile upFile = new UploadFile();
	                upFile.setFileName(fileName);
	                upFile.setFilePath(filePath);
	                upFile.setTbName(CodeUtil.getClassName(Checkin.class));
	                list.add(upFile);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
		return list;
	}
	
	/**
	 * 批量删除上传的文件
	 * 当保存失败或者其他情况下，需要将上传的文件删除
	 * @param uploadFiles
	 * @throws RuntimeException
	 */
	public void delFiles(List<UploadFile> uploadFiles) throws RuntimeException {
		try {
			if(null != uploadFiles) {
				Iterator<UploadFile> iter = uploadFiles.iterator();
				UploadFile file = null;
				while(iter.hasNext()) {
					file = iter.next();
					this.delFile(file);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
	}
	
	/**
	 * 删除单个文件
	 * @param uploadFile
	 */
	public void delFile(UploadFile uploadFile) {
		try {
			if(null != uploadFile) {
				String realPath = uploadFile.getFilePath();
				if(CodeUtil.isNotEmpty(realPath)) {
					File file = new File(realPath);
					if(file.exists()) {
						file.delete();
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
