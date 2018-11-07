package com.linuslan.oa.system.upload.dao;

import java.util.List;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.upload.model.UploadFile;

public interface IUploadFileDao extends IBaseDao {

	/**
	 * 通过tbId和tbName查询相应的附件
	 * @param id
	 * @param name
	 * @return
	 */
	public List<UploadFile> queryByTbIdAndTbName(Long id, String name);
	
	public UploadFile queryById(Long id);
	
	/**
	 * 批量新增上传文件对象
	 * @param files
	 * @throws RuntimeException
	 */
	public void addBatch(List<UploadFile> files) throws RuntimeException;
	
	/**
	 * 单个新增上传文件对象
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void add(UploadFile uploadFile) throws RuntimeException;
	
	/**
	 * 删除
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void del(UploadFile uploadFile) throws RuntimeException;
}
