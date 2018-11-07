package com.linuslan.oa.system.notice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.department.dao.IDepartmentDao;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.group.dao.IGroupDao;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.message.dao.IMessageDao;
import com.linuslan.oa.system.message.model.Message;
import com.linuslan.oa.system.notice.dao.INoticeDao;
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.notice.model.NoticeReceiver;
import com.linuslan.oa.system.notice.service.INoticeService;
import com.linuslan.oa.system.user.dao.IUserDao;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Component("noticeService")
@Transactional
public class INoticeServiceImpl extends IBaseServiceImpl implements
		INoticeService {

	public static String USER_TYPE = "sys_user";
	
	public static String DEPARTMENT_TYPE = "sys_department";
	
	public static String GROUP_TYPE = "sys_group";
	
	public static String ALL_TYPE = "select_all";
	
	@Autowired
	private INoticeDao noticeDao;
	
	@Autowired
	private IDepartmentDao departmentDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IGroupDao groupDao;
	
	@Autowired
	private IMessageDao messageDao;

	public Page<Notice> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.noticeDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询登录用户的公告
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @param userId
	 * @return
	 */
	public Page<Map<String, Object>> queryPageByUserId(Map<String, String> paramMap,
			int currentPage, int limit, Long userId) {
		return this.noticeDao.queryPageByUserId(paramMap, currentPage, limit, userId);
	}

	public List<Notice> queryByIds(List<Long> ids) {
		return this.noticeDao.queryByIds(ids);
	}
	
	public List<Notice> queryByUserId(Long userId) {
		return this.noticeDao.queryByUserId(userId);
	}

	public List<Notice> queryAllNotices() {
		return this.noticeDao.queryAllNotices();
	}

	public Notice queryById(Long id) {
		return this.noticeDao.queryById(id);
	}
	
	public List<NoticeReceiver> queryReceiversByNoticeId(Long noticeId) {
		return this.noticeDao.queryReceiversByNoticeId(noticeId);
	}

	public boolean add(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll) {
		boolean success = false;
		try {
			this.noticeDao.add(notice);
			List<NoticeReceiver> list = new ArrayList<NoticeReceiver> ();
			if(null != userIds && 0 < userIds.size()) {
				list.addAll(this.parseReceivers(userIds, USER_TYPE, notice.getId()));
			}
			if(null != departmentIds && 0 < departmentIds.size()) {
				list.addAll(this.parseReceivers(departmentIds, DEPARTMENT_TYPE, notice.getId()));
			}
			if(null != groupIds && 0 < groupIds.size()) {
				list.addAll(this.parseReceivers(groupIds, GROUP_TYPE, notice.getId()));
			}
			if(1 == selectedAll) {
				list.add(this.parseAllReceiver(ALL_TYPE, notice.getId()));
			}
			this.noticeDao.addReceivers(list);
			success = true;
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}

	public boolean update(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll) {
		boolean success = false;
		try {
			Notice persist = this.noticeDao.queryById(notice.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所更新的数据不存在");
			}
			if(1 == persist.getIsSend()) {
				CodeUtil.throwExcep("该公告已发布，不能变更");
			}
			BeanUtil.updateBean(persist, notice);
			this.noticeDao.update(persist);
			this.noticeDao.delReceiversByNoticeId(persist.getId());
			List<NoticeReceiver> list = new ArrayList<NoticeReceiver> ();
			if(null != userIds && 0 < userIds.size()) {
				list.addAll(this.parseReceivers(userIds, USER_TYPE, persist.getId()));
			}
			if(null != departmentIds && 0 < departmentIds.size()) {
				list.addAll(this.parseReceivers(departmentIds, DEPARTMENT_TYPE, persist.getId()));
			}
			if(null != groupIds && 0 < groupIds.size()) {
				list.addAll(this.parseReceivers(groupIds, GROUP_TYPE, persist.getId()));
			}
			if(1 == selectedAll) {
				list.add(this.parseAllReceiver(ALL_TYPE, notice.getId()));
			}
			this.noticeDao.addReceivers(list);
			success = true;
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	/**
	 * 发送公告
	 * @param notice
	 * @param userIds
	 * @param departmentIds
	 * @param groupIds
	 * @param operate
	 * @return
	 */
	public int send(Notice notice, List<Long> userIds, List<Long> departmentIds, List<Long> groupIds, int selectedAll, String operate) {
		int count = 0;
		try {
			List<NoticeReceiver> list = null;
			if("update".equals(operate)) {
				if(null == notice.getId()) {
					this.noticeDao.add(notice);
				} else {
					Notice persist = this.noticeDao.queryById(notice.getId());
					if(null == persist || null == persist.getId()) {
						CodeUtil.throwExcep("您所更新的数据不存在");
					}
					if(1 == persist.getIsSend()) {
						CodeUtil.throwExcep("该公告已发布，不能重复发布");
					}
					BeanUtil.updateBean(persist, notice);
					this.noticeDao.update(persist);
					notice = persist;
				}
				this.noticeDao.delReceiversByNoticeId(notice.getId());
				list = new ArrayList<NoticeReceiver> ();
				if(null != userIds && 0 < userIds.size()) {
					list.addAll(this.parseReceivers(userIds, USER_TYPE, notice.getId()));
				}
				if(null != departmentIds && 0 < departmentIds.size()) {
					list.addAll(this.parseReceivers(departmentIds, DEPARTMENT_TYPE, notice.getId()));
				}
				if(null != groupIds && 0 < groupIds.size()) {
					list.addAll(this.parseReceivers(groupIds, GROUP_TYPE, notice.getId()));
				}
				if(1 == selectedAll) {
					list.add(this.parseAllReceiver(ALL_TYPE, notice.getId()));
				}
				this.noticeDao.addReceivers(list);
			} else {
				list = this.noticeDao.queryReceiversByNoticeId(notice.getId());
			}
			if(null != list && 0 < list.size()) {
				departmentIds = new ArrayList<Long> ();
				userIds = new ArrayList<Long> ();
				groupIds = new ArrayList<Long> ();
				int allSel = 0;
				Iterator<NoticeReceiver> iter = list.iterator();
				NoticeReceiver receiver = null;
				while(iter.hasNext()) {
					receiver = iter.next();
					if(null != receiver.getType()
							&& null != receiver.getTypeId()
							&& null != receiver.getNoticeId()
							&& receiver.getNoticeId() == notice.getId()) {
						if(USER_TYPE.equals(receiver.getType())) {
							userIds.add(receiver.getTypeId());
						} else if(DEPARTMENT_TYPE.equals(receiver.getType())) {
							departmentIds.add(receiver.getTypeId());
						} else if(GROUP_TYPE.equals(receiver.getType())) {
							groupIds.add(receiver.getTypeId());
						} else if(ALL_TYPE.equals(receiver.getType())) {
							allSel = 1;
						}
					}
				}
				List<User> users = new ArrayList<User> ();
				if(0 < departmentIds.size()) {
					List<Department> departments = this.departmentDao.queryInIds(departmentIds);
					if(0 < departments.size()) {
						String idStr = BeanUtil.parseString(departments, "id", ",");
						List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
						users.addAll(this.userDao.queryByDepartmentIds(ids));
					}
				}
				if(0 < userIds.size()) {
					users.addAll(this.userDao.queryInIds(userIds));
				}
				if(0 < groupIds.size()) {
					List<Group> groups = this.groupDao.queryInIds(groupIds);
					String idStr = BeanUtil.parseString(groups, "id", ",");
					List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
					users.addAll(this.userDao.queryByGroupIds(ids));
				}
				/*
				 * 全选用户
				 */
				if(1 == allSel) {
					users.addAll(this.userDao.queryAll());
				}
				if(0 < users.size()) {
					Iterator<User> userIter = users.iterator();
					User user = null;
					
					Map<Long, User> userMap = new HashMap<Long, User> ();
					
					while(userIter.hasNext()) {
						user = userIter.next();
						if(null != user && null != user.getId()) {
							userMap.put(user.getId(), user);
						}
					}
					List<Message> messages = new ArrayList<Message> ();
					Message message = null;
					if(0 < userMap.size()) {
						Set<Long> keySet = userMap.keySet();
						Iterator<Long> keyIter = keySet.iterator();
						Long id = null;
						User u = null;
						while(keyIter.hasNext()) {
							id = keyIter.next();
							if(null != id) {
								u = userMap.get(id);
								message = new Message();
								message.setIsDeal(1);
								message.setIsDelete(0);
								message.setIsRead(0);
								message.setSendDate(new Date());
								message.setShowType(1);
								message.setTbId(notice.getId());
								message.setTbName("sys_notice");
								message.setUserId(u.getId());
								message.setUserName(u.getName());
								messages.add(message);
							}
						}
					}
					if(0 < messages.size()) {
						count = this.messageDao.addBatch(messages);
						if(0 < count) {
							notice.setIsSend(1);
							notice.setSendDate(new Date());
							notice.setSender(HttpUtil.getLoginUser().getId());
							notice.setSenderName(HttpUtil.getLoginUser().getName());
							this.noticeDao.update(notice);
						}
					}
				}
			}
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
		}
		return count;
	}

	public boolean del(Notice notice) {
		boolean success = false;
		try {
			Notice persist = this.noticeDao.queryById(notice.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if(persist.getIsSend() == 1) {
				CodeUtil.throwExcep("该公告已发布，不能删除");
			}
			this.noticeDao.del(persist);
			this.noticeDao.delReceiversByNoticeId(persist.getId());
			success = true;
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex);
		}
		return success;
	}
	
	public List<NoticeReceiver> parseReceivers(List<Long> ids, String type, Long noticeId) {
		List<NoticeReceiver> list = new ArrayList<NoticeReceiver> ();
		if(null != ids && 0 < ids.size()) {
			List<Long> nullList = new ArrayList<Long> ();
			nullList.add(null);
			ids.removeAll(nullList);
			if(null != ids && 0 < ids.size()) {
				List<Map<String, Object>> objects = this.noticeDao.queryReceivers(ids, type);
				if(0 < objects.size()) {
					Map<String, Object> map = null;
					Iterator<Map<String, Object>> iter = objects.iterator();
					while(iter.hasNext()) {
						map = iter.next();
						NoticeReceiver receiver = this.parseReceiver(map, type, noticeId);
						if(null != receiver) {
							list.add(receiver);
						}
					}
				}
			}
		}
		return list;
	}
	
	public NoticeReceiver parseReceiver(Map<String, Object> map, String type, Long noticeId) {
		try {
			if(null != map && CodeUtil.isNotEmpty(CodeUtil.parseString(map.get("ID")))
					&& CodeUtil.isNotEmpty(type) && null != noticeId) {
				NoticeReceiver receiver = new NoticeReceiver();
				receiver.setCreateDate(new Date());
				receiver.setType(type);
				receiver.setTypeId(CodeUtil.parseLong(map.get("ID")));
				receiver.setTypeName(CodeUtil.parseString(map.get("NAME")));
				receiver.setNoticeId(noticeId);
				return receiver;
			} else {
				return null;
			}
		} catch(Exception ex) {
			return null;
		}
		
	}
	
	public NoticeReceiver parseAllReceiver(String type, Long noticeId) {
		NoticeReceiver receiver = new NoticeReceiver();
		receiver.setCreateDate(new Date());
		receiver.setType(type);
		receiver.setTypeId(1l);
		receiver.setTypeName("全选");
		receiver.setNoticeId(noticeId);
		return receiver;
	}
	
}
