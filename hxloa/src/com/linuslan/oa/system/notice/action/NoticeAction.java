package com.linuslan.oa.system.notice.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.department.service.IDepartmentService;
import com.linuslan.oa.system.group.model.Group;
import com.linuslan.oa.system.group.service.IGroupService;
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.notice.model.NoticeReceiver;
import com.linuslan.oa.system.notice.service.INoticeService;
import com.linuslan.oa.system.notice.service.impl.INoticeServiceImpl;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Controller
public class NoticeAction extends BaseAction {
	private static Logger logger = Logger.getLogger(NoticeAction.class);

	@Autowired
	private INoticeService noticeService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDepartmentService departmentService;
	
	@Autowired
	private IGroupService groupService;
	
	private Page<Notice> pageData;
	private Notice notice;
	private List<Notice> notices;
	
	private List<User> users;
	
	private List<Long> userIds;
	
	private List<Long> groupIds;
	
	private List<Long> departmentIds;
	
	private String operate = "";
	
	private List<User> selectedUsers;
	
	private List<Department> selectedDepartments;
	
	private List<Group> selectedGroups;
	
	private int selectedAll;

	public void queryPage() {
		try {
			if(null == this.paramMap) {
				this.paramMap = new HashMap<String, String> ();
			}
			this.pageData = this.noticeService.queryPage(this.paramMap,
					this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void queryPageByUserId() {
		try {
			if(null == this.paramMap) {
				this.paramMap = new HashMap<String, String> ();
			}
			Page<Map<String, Object>> userPage = this.noticeService.queryPageByUserId(this.paramMap,
					this.page, this.rows, HttpUtil.getLoginUser().getId());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"CREATE_DATE"});
			JSONObject json = JSONObject.fromObject(userPage);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAll() {
		try {
			List<Notice> allNotices = this.noticeService.queryAllNotices();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allNotices, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.notice = this.noticeService.queryById(this.notice.getId());
			List<NoticeReceiver> list = this.noticeService.queryReceiversByNoticeId(this.notice.getId());
			departmentIds = new ArrayList<Long> ();
			userIds = new ArrayList<Long> ();
			groupIds = new ArrayList<Long> ();
			
			Iterator<NoticeReceiver> iter = list.iterator();
			NoticeReceiver receiver = null;
			while(iter.hasNext()) {
				receiver = iter.next();
				if(null != receiver.getType()
						&& null != receiver.getTypeId()
						&& null != receiver.getNoticeId()
						&& receiver.getNoticeId() == notice.getId()) {
					if(INoticeServiceImpl.USER_TYPE.equals(receiver.getType())) {
						userIds.add(receiver.getTypeId());
					} else if(INoticeServiceImpl.DEPARTMENT_TYPE.equals(receiver.getType())) {
						departmentIds.add(receiver.getTypeId());
					} else if(INoticeServiceImpl.GROUP_TYPE.equals(receiver.getType())) {
						groupIds.add(receiver.getTypeId());
					} else if(INoticeServiceImpl.ALL_TYPE.equals(receiver.getType())) {
						this.selectedAll = 1;
					}
				}
			}
			
			if(0 < departmentIds.size()) {
				List<Department> departments = this.departmentService.queryInIds(departmentIds);
				if(0 < departments.size()) {
					String idStr = BeanUtil.parseString(departments, "id", ",");
					String names = BeanUtil.parseString(departments, "name", ",");
					HttpUtil.getRequest().setAttribute("departmentIdList", idStr);
					HttpUtil.getRequest().setAttribute("departmentNameList", names);
				}
			}
			
			if(0 < userIds.size()) {
				this.selectedUsers = this.userService.queryInIds(this.userIds);
			}
			
			if(0 < groupIds.size()) {
				List<Group> groups = this.groupService.queryInIds(groupIds);
				if(0 < groups.size()) {
					String idStr = BeanUtil.parseString(groups, "id", ",");
					String names = BeanUtil.parseString(groups, "text", ",");
					HttpUtil.getRequest().setAttribute("groupIdList", idStr);
					HttpUtil.getRequest().setAttribute("groupNameList", names);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			this.users = this.userService.queryAll();
		} catch(Exception ex) {
			
		}
		return this.returnType;
	}
	
	public String queryByUserId() {
		try {
			this.notices = this.noticeService.queryByUserId(HttpUtil.getLoginUser().getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "select";
	}

	public void add() {
		try {
			this.notice.setUserId(HttpUtil.getLoginUser().getId());
			this.noticeService.valid(this.notice);
			if (this.noticeService.add(this.notice, this.userIds, this.departmentIds, this.groupIds, this.selectedAll))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep("保存失败！");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void update() {
		try {
			this.noticeService.valid(this.notice);
			if (this.noticeService.update(this.notice, this.userIds, this.departmentIds, this.groupIds, this.selectedAll))
				setupSimpleSuccessMap();
			else
				CodeUtil.throwExcep(this.failureMsg);
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public void del() {
		try {
			if ((this.notice == null) || (this.notice.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Notice persist = this.noticeService.queryById(this.notice
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.noticeService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}
	
	/**
	 * 发送公告
	 */
	public void send() {
		try {
			this.noticeService.valid(this.notice);
			int count = this.noticeService.send(this.notice, this.userIds, this.departmentIds, this.groupIds, this.selectedAll, this.operate);
			if(0 < count) {
				this.setupSuccessMap("成功发送<font color='red'>"+count+"</font>份公告");
			} else {
				CodeUtil.throwExcep("发送失败，请检查接收人是否为空");
			}
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Notice> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Notice> pageData) {
		this.pageData = pageData;
	}

	public Notice getNotice() {
		return this.notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<Group> getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(List<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public List<Department> getSelectedDepartments() {
		return selectedDepartments;
	}

	public void setSelectedDepartments(List<Department> selectedDepartments) {
		this.selectedDepartments = selectedDepartments;
	}

	public int getSelectedAll() {
		return selectedAll;
	}

	public void setSelectedAll(int selectedAll) {
		this.selectedAll = selectedAll;
	}
}
