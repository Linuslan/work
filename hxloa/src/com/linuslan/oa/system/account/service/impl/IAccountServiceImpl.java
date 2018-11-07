package com.linuslan.oa.system.account.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.account.service.IAccountService;
import com.linuslan.oa.system.account.dao.IAccountDao;
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("accountService")
@Transactional
public class IAccountServiceImpl extends IBaseServiceImpl implements
		IAccountService {

	@Autowired
	private IAccountDao accountDao;

	public Page<Account> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.accountDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Account> queryByIds(List<Long> ids) {
		return this.accountDao.queryByIds(ids);
	}
	
	public List<Account> queryByUserId(Long userId) {
		return this.accountDao.queryByUserId(userId);
	}

	public List<Account> queryAllAccounts() {
		return this.accountDao.queryAllAccounts();
	}
	
	/**
	 * 查询类型为type值的账号
	 * @param type
	 * @return
	 */
	public List<Account> queryByType(int type) {
		return this.accountDao.queryByType(type);
	}

	public Account queryById(Long id) {
		return this.accountDao.queryById(id);
	}

	public boolean add(Account account) {
		return this.accountDao.add(account);
	}

	public boolean update(Account account) {
		Account persist = this.accountDao.queryById(account.getId());
		BeanUtil.updateBean(persist, account);
		return this.accountDao.update(persist);
	}

	public boolean del(Account account) {
		return this.accountDao.del(account);
	}
	
}
