package com.linuslan.oa.system.userContract.action;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.system.userContract.model.UserContract;
import com.linuslan.oa.system.userContract.service.IUserContractService;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.Page;

@Controller
public class UserContractAction extends BaseAction {

	@Autowired
	private IUserContractService userContractService;
	
	@Autowired
	private IUserService userService;
	
	private Page<UserContract> pageData;
	
	private UserContract userContract;
	
	private User user;
	
	/**
	 * 查询用户合同
	 */
	public void queryPage() {
		try {
			if(returnType.indexOf(",") > 0) {
				returnType = returnType.substring(0, returnType.indexOf(","));
			}
			if(returnType.equals("expire")) {
				this.pageData = this.userContractService.queryWillExpirePage(paramMap, this.page, this.rows);
			} else {
				this.pageData = this.userContractService.queryPage(paramMap, this.page, this.rows);
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 查询行政需要知道的即将到期的合同数
	 */
	/*public void queryWillExpireUserContractMsg() {
		NoteVo vo = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(UserInfo.USER);
		try {
			if((long)user.getId() == 6l) {
				vo = this.userContractService.queryWillExpireUserContractMsg();
			} else {
				vo = new NoteVo();
			}
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			JSONObject json = JSONObject.fromObject(vo, jsonConfig);
			response.getWriter().print(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}*/
	
	/**
	 * 新增或修改时的初始化内容
	 * @return
	 */
	public String initContent() {
		try {
			if(null != this.userContract) {
				if(null != this.userContract.getId()) {
					this.userContract = this.userContractService.queryById(this.userContract.getId());
				}
			}
			this.user = this.userService.queryById(this.user.getId());
		} catch(Exception ex) {
			//ex.printStackTrace();
		}
		
		return returnType;
	}
	
	/**
	 * 新增备用金申请
	 */
	public void add() {
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			this.userContract = this.userContractService.add(userContract);
			if(null != this.userContract) {
				result.append(true);
				result.append(", 'id': "+this.userContract.getId());
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 更新备用金
	 */
	public void update() {
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			this.userContract = this.userContractService.update(userContract);
			if(null != this.userContract) {
				result.append(true);
				result.append(", 'id': "+this.userContract.getId());
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 通过id删除开票
	 */
	public void delById() {
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			if(this.userContractService.delById(this.userContract.getId())) {
				result.append(true);
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Page<UserContract> getPageData() {
		return pageData;
	}

	public void setPageData(Page<UserContract> pageData) {
		this.pageData = pageData;
	}

	public UserContract getUserContract() {
		return userContract;
	}

	public void setUserContract(UserContract userContract) {
		this.userContract = userContract;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
