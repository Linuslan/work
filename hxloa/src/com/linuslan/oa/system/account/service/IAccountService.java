package com.linuslan.oa.system.account.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.util.Page;

public interface IAccountService extends IBaseService {

	public abstract Page<Account> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Account> queryByIds(List<Long> paramList);
	
	public List<Account> queryByUserId(Long userId);

	public abstract List<Account> queryAllAccounts();
	
	/**
	 * 查询类型为type值的账号
	 * @param type
	 * @return
	 */
	public List<Account> queryByType(int type);

	public abstract Account queryById(Long paramLong);

	public abstract boolean add(Account paramAccount);

	public abstract boolean update(Account paramAccount);

	public abstract boolean del(Account paramAccount);
	
}
