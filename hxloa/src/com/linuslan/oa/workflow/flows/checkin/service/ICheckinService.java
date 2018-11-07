package com.linuslan.oa.workflow.flows.checkin.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.checkin.model.Checkin;
import com.linuslan.oa.workflow.flows.checkin.model.CheckinContent;

public interface ICheckinService extends IBaseService {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的入库
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Checkin> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Checkin queryById(Long id);
	
	/**
	 * 通过入库主表的id查询入库项目
	 * @param id
	 * @return
	 */
	public List<CheckinContent> queryContentsByCheckinId(Long id);

	/**
	 * 新增申请单
	 * @param checkin
	 * @return
	 */
	public boolean add(Checkin checkin, List<CheckinContent> contents, List<UploadFile> uploadFiles);
	
	/**
	 * 更新申请单
	 * @param checkin
	 * @return
	 */
	public boolean update(Checkin checkin, List<CheckinContent> contents, List<UploadFile> files);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param checkin
	 * @return
	 */
	public boolean del(Checkin checkin);
	
	/**
	 * 申请人提交申请
	 * @param checkin
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Checkin checkin, List<CheckinContent> contents, List<UploadFile> uploadFiles, int passType, String opinion);
	
	/**
	 * 审核申请单
	 * @param checkin
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Checkin checkin, int passType, String opinion);
	
	/**
	 * 删除入库项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param checkin
	 */
	public void valid(Checkin checkin);
	
	/**
	 * 上传文件，并转换成UploadFile对象
	 * @param files
	 * @param fileNames
	 * @return
	 * @throws RuntimeException
	 */
	public List<UploadFile> getUploadFiles(List<File> files, List<String> fileNames) throws RuntimeException;
	
	/**
	 * 批量删除上传的文件
	 * @param uploadFiles
	 * @throws RuntimeException
	 */
	public void delFiles(List<UploadFile> uploadFiles) throws RuntimeException;
	
	/**
	 * 删除单个文件
	 * @param uploadFile
	 */
	public void delFile(UploadFile uploadFile);
	
}
