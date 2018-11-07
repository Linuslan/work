package com.linuslan.oa.system.upload.service;

import java.util.List;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.upload.model.UploadFile;

public interface IUploadFileService extends IBaseService {

	public UploadFile queryById(Long id);
	
	/**
	 * 通过tbId和tbName查询相应的附件
	 * @param id
	 * @param name
	 * @return
	 */
	public List<UploadFile> queryByTbIdAndTbName(Long id, String name);
	
	/**
	 * 删除
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void del(UploadFile uploadFile) throws RuntimeException;
	
}
