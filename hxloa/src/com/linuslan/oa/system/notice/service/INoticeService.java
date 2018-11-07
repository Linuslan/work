package com.linuslan.oa.system.notice.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.notice.model.NoticeReceiver;
import com.linuslan.oa.util.Page;

public interface INoticeService extends IBaseService {

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
	
	public List<NoticeReceiver> queryReceiversByNoticeId(Long noticeId);

	public abstract boolean add(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll);

	public abstract boolean update(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll);

	public abstract boolean del(Notice notice);
	
	/**
	 * 发送公告
	 * @param notice
	 * @param userIds
	 * @param departmentIds
	 * @param groupIds
	 * @param operate
	 * @return
	 */
	public int send(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll, String operate);
	
}
