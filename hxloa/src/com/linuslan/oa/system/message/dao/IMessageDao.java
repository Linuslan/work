package com.linuslan.oa.system.message.dao;

import java.util.List;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.message.model.Message;

public interface IMessageDao extends IBaseDao {

	/**
	 * 批量新增
	 * 返回成功数
	 * @param list
	 * @return
	 */
	public int addBatch(List<Message> list);
	
}
