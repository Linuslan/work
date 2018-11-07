package com.linuslan.oa.system.upload.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.upload.dao.IUploadFileDao;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.system.upload.service.IUploadFileService;
import com.linuslan.oa.util.CodeUtil;

@Component("uploadFileService")
@Transactional
public class IUploadFileServiceImpl extends IBaseServiceImpl implements
		IUploadFileService {

	@Autowired
	private IUploadFileDao uploadFileDao;
	
	/**
	 * 通过tbId和tbName查询相应的附件
	 * @param id
	 * @param name
	 * @return
	 */
	public List<UploadFile> queryByTbIdAndTbName(Long id, String name) {
		return this.uploadFileDao.queryByTbIdAndTbName(id, name);
	}
	
	public UploadFile queryById(Long id) {
		return this.uploadFileDao.queryById(id);
	}
	
	/**
	 * 删除
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void del(UploadFile uploadFile) throws RuntimeException {
		try {
			UploadFile persist = this.uploadFileDao.queryById(uploadFile.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("文件不存在");
			}
			String realPath = persist.getFilePath();
			this.uploadFileDao.del(persist);
			File file = new File(realPath);
			if(file.exists()) {
				file.delete();
			}
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
	}
}
