package com.linuslan.oa.system.account.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.system.account.service.IAccountService;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Controller
public class AccountAction extends BaseAction {

	private static Logger logger = Logger.getLogger(AccountAction.class);

	@Autowired
	private IAccountService accountService;
	private Page<Account> pageData;
	private Account account;
	private List<Account> accounts;

	public void queryPage() {
		try {
			if(null == this.paramMap) {
				this.paramMap = new HashMap<String, String> ();
			}
			this.paramMap.put("userId", HttpUtil.getLoginUser().getId().toString());
			this.pageData = this.accountService.queryPage(this.paramMap,
					this.page, this.rows);
			JSONObject json = JSONObject.fromObject(this.pageData);
			printResult(json.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAll() {
		try {
			List allAccounts = this.accountService.queryAllAccounts();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateProcessor());
			JSONArray json = JSONArray.fromObject(allAccounts, jsonConfig);
			printResult(json.toString());
		} catch (Exception ex) {
			logger.error(CodeUtil.getStackTrace(ex));
		}
	}

	public String queryById() {
		try {
			this.account = this.accountService.queryById(this.account.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public String queryByUserId() {
		try {
			this.accounts = this.accountService.queryByUserId(HttpUtil.getLoginUser().getId());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "select";
	}

	public void add() {
		try {
			this.account.setUserId(HttpUtil.getLoginUser().getId());
			this.accountService.valid(this.account);
			if (this.accountService.add(this.account))
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
			this.accountService.valid(this.account);
			if (this.accountService.update(this.account))
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
			if ((this.account == null) || (this.account.getId() == null)) {
				CodeUtil.throwExcep("请至少选择一条数据");
			}
			Account persist = this.accountService.queryById(this.account
					.getId());
			if ((persist == null) || (persist.getId() == null)) {
				CodeUtil.throwExcep("您所删除的数据不存在");
			}
			if (this.accountService.del(persist))
				setupSuccessMap("删除成功");
			else
				CodeUtil.throwExcep("删除失败");
		} catch (Exception ex) {
			setupFailureMap(ex.getMessage());
		}
		printResultMap();
	}

	public Page<Account> getPageData() {
		return this.pageData;
	}

	public void setPageData(Page<Account> pageData) {
		this.pageData = pageData;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
}
