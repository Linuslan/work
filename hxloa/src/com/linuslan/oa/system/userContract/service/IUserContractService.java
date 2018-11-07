package com.linuslan.oa.system.userContract.service;

import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.userContract.model.UserContract;
import com.linuslan.oa.util.Page;

public interface IUserContractService extends IBaseService {

	/**
	 * 查询用户的合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryPage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询即将到期的用户合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryWillExpirePage(Map<String, String> paramMap, int currentPage, int limit);
	
	/**
	 * 查询用户合同即将到期，提醒行政的条数
	 * @return
	 */
	//public NoteVo queryWillExpireUserContractMsg();
	
	/**
	 * 通过id查询用户合同
	 * @param id
	 * @return
	 */
	public UserContract queryById(Long id);
	
	/**
	 * 新增用户合同
	 * @param pettyCash
	 * @return
	 */
	public UserContract add(UserContract userContract);
	
	/**
	 * 更新用户合同
	 * @param userContract
	 * @return
	 */
	public UserContract update(UserContract userContract);
	
	/**
	 * 通过id删除用户合同
	 * @param id
	 * @return
	 */
	public boolean delById(Long id);
	
}
