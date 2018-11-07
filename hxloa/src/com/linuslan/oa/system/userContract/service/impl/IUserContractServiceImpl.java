package com.linuslan.oa.system.userContract.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.userContract.dao.IUserContractDao;
import com.linuslan.oa.system.userContract.model.UserContract;
import com.linuslan.oa.system.userContract.service.IUserContractService;
import com.linuslan.oa.util.Page;

@Component("userContractService")
@Transactional
public class IUserContractServiceImpl extends IBaseServiceImpl implements
		IUserContractService {

	@Autowired
	private IUserContractDao userContractDao;

	/**
	 * 查询用户的合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.userContractDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询即将到期的用户合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryWillExpirePage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.userContractDao.queryWillExpirePage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询用户合同即将到期，提醒行政的条数
	 * @return
	 */
	/*public NoteVo queryWillExpireUserContractMsg() {
		return this.userContractDao.queryWillExpireUserContractMsg();
	}*/
	
	/**
	 * 通过id查询用户合同
	 * @param id
	 * @return
	 */
	public UserContract queryById(Long id) {
		return this.userContractDao.queryById(id);
	}
	
	/**
	 * 新增用户合同
	 * @param pettyCash
	 * @return
	 */
	public UserContract add(UserContract userContract) {
		return this.userContractDao.add(userContract);
	}
	
	/**
	 * 更新用户合同
	 * @param userContract
	 * @return
	 */
	public UserContract update(UserContract userContract) {
		return this.userContractDao.update(userContract);
	}
	
	/**
	 * 通过id删除用户合同
	 * @param id
	 * @return
	 */
	public boolean delById(Long id) {
		return this.userContractDao.delById(id);
	}
	
}
