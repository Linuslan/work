package com.linuslan.oa.system.contract.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.contract.model.Contract;
import com.linuslan.oa.util.Page;

public interface IContractDao extends IBaseDao {

	public abstract Page<Contract> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Contract> queryByIds(List<Long> paramList);

	public abstract List<Contract> queryAllContracts();

	public abstract Contract queryById(Long paramLong);

	public abstract boolean add(Contract paramContract);

	public abstract boolean update(Contract paramContract);

	public abstract boolean del(Contract paramContract);
	
}
