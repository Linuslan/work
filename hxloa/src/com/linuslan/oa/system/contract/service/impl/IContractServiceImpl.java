package com.linuslan.oa.system.contract.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.contract.dao.IContractDao;
import com.linuslan.oa.system.contract.model.Contract;
import com.linuslan.oa.system.contract.service.IContractService;
import com.linuslan.oa.system.upload.dao.IUploadFileDao;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.util.UploadUtil;

@Component("contractService")
@Transactional
public class IContractServiceImpl extends IBaseServiceImpl implements
		IContractService {

	private static Logger logger = Logger.getLogger(IContractServiceImpl.class);
	
	@Autowired
	private IContractDao contractDao;
	
	@Autowired
	private IUploadFileDao uploadFileDao;

	public Page<Contract> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.contractDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Contract> queryByIds(List<Long> ids) {
		return this.contractDao.queryByIds(ids);
	}

	public List<Contract> queryAllContracts() {
		return this.contractDao.queryAllContracts();
	}

	public Contract queryById(Long id) {
		return this.contractDao.queryById(id);
	}

	public boolean add(Contract contract, List<UploadFile> uploadFiles) {
		boolean success = false;
		try {
			this.contractDao.add(contract);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tbId", contract.getId());
			BeanUtil.setValueBatch(uploadFiles, map);
			this.uploadFileDao.addBatch(uploadFiles);
			success = true;
		} catch(Exception ex) {
			try {
				UploadUtil.delBatch(uploadFiles);
			} catch(Exception e) {
				logger.error("新增合同，合同附件上传失败后，批量删除失败，失败原因："+CodeUtil.getStackTrace(e));
			}
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}

	public boolean update(Contract contract, List<UploadFile> uploadFiles) {
		boolean success = false;
		try {
			Contract persist = this.contractDao.queryById(contract.getId());
			BeanUtil.updateBean(persist, contract);
			this.contractDao.update(persist);
			/*
			 * 将checkin的对象Id设置到上传文件的对象中
			 */
			Map<String, Long> valueMap = new HashMap<String, Long>();
			valueMap.put("tbId", persist.getId());
			BeanUtil.setValueBatch(uploadFiles, valueMap);
			this.uploadFileDao.addBatch(uploadFiles);
			success = true;
		} catch(Exception ex) {
			try {
				UploadUtil.delBatch(uploadFiles);
			} catch(Exception e) {
				logger.error("更新合同，合同附件上传失败后，批量删除失败，失败原因："+CodeUtil.getStackTrace(e));
			}
			CodeUtil.throwRuntimeExcep(ex);
		}
		
		return success;
	}

	public boolean del(Contract contract) {
		return this.contractDao.del(contract);
	}
	
}
