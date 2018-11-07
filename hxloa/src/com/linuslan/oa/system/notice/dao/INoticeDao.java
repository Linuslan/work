package com.linuslan.oa.system.notice.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.notice.model.NoticeReceiver;
import com.linuslan.oa.util.Page;

public interface INoticeDao extends IBaseDao {

	public abstract Page<Notice> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);
	
	/**
	 * 查询登录用户的公告
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @param userId
	 * @return
	 */
	public Page<Map<String, Object>> queryPageByUserId(Map<String, String> paramMap,
			int currentPage, int limit, Long userId);

	public abstract List<Notice> queryByIds(List<Long> ids);
	
	public List<Notice> queryByUserId(Long userId);

	public abstract List<Notice> queryAllNotices();

	public abstract Notice queryById(Long id);
	
	/**
	 * 查询接收对象对应的id和名称
	 * @param ids
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> queryReceivers(List<Long> ids, String type);
	
	/**
	 * 通过公告id查询接收对象
	 * @param noticeId
	 * @return
	 */
	public List<NoticeReceiver> queryReceiversByNoticeId(Long noticeId);

	public abstract boolean add(Notice notice);

	public abstract boolean update(Notice notice);

	public abstract boolean del(Notice notice);
	
	/**
	 * 批量新增接收对象
	 * @param receivers
	 * @return
	 */
	public boolean addReceivers(List<NoticeReceiver> receivers);
	
	/**
	 * 删除接收对象
	 * @param noticeId
	 * @return
	 */
	public boolean delReceiversByNoticeId(Long noticeId);
	
}
