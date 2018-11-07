package com.linuslan.oa.system.contract.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.contract.model.Contract;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.Page;

public interface IContractService extends IBaseService {

	public abstract Page<Contract> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Contract> queryByIds(List<Long> paramList);

	public abstract List<Contract> queryAllContracts();

	public abstract Contract queryById(Long paramLong);

	public abstract boolean add(Contract paramContract, List<UploadFile> uploadFiles);

	public abstract boolean update(Contract paramContract, List<UploadFile> uploadFiles);

	public abstract boolean del(Contract paramContract);
	
}
