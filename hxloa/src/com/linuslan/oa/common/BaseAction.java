package com.linuslan.oa.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	public int page=1;
	
	public int rows=30;
	
	public Map<String, String> paramMap;
	
	public Map<String, Object> resultMap = new HashMap<String, Object> ();
	
	/**
	 * 返回类型，目前用于struts返回的页面类型
	 */
	public String returnType="success";
	
	/**
	 * 返回的消息字段，用于json返回到前端
	 */
	public final String returnMsg="msg";
	
	/**
	 * 返回的类型字段，用于json返回到前端
	 */
	public final String returnFlag = "success";
	
	/**
	 * 操作成功的初始提示信息
	 */
	public String successMsg = "保存成功";
	
	/**
	 * 操作失败的初始提示信息
	 */
	public String failureMsg = "保存失败";
	
	/**
	 * 审核的操作类型
	 * 0：通过
	 * 1：退回
	 */
	public int passType = 0;
	
	/**
	 * 审核意见
	 */
	public String opinion;
	
	public Page<Object> auditedPage;
	
	/**
	 * 打印消息到前端
	 * @param msg
	 * @throws Exception
	 */
	public void printResult(String msg) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(msg);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	/**
	 * 将Map转化成json打印到前端
	 * @param map
	 */
	public void printMap(Map<String, Object> map) {
		try {
			JSONObject json = JSONObject.fromObject(map);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 将resultMap转换成json打印到前端
	 */
	public void printResultMap() {
		try {
			JSONObject json = JSONObject.fromObject(this.resultMap);
			this.printResult(json.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 设置简单的返回到前端的成功的消息map值
	 * 即返回到前端的map只有success: true, msg: 保存成功
	 */
	public void setupSimpleSuccessMap() {
		this.resultMap.clear();
		this.resultMap.put(this.returnFlag, true);
		this.resultMap.put(this.returnMsg, this.successMsg);
	}
	
	/**
	 * 设置简单的返回到前端的成功的消息map值
	 * 即返回到前端的map只有success: false, msg: 保存失败
	 */
	public void setupSimpleFailureMap() {
		this.resultMap.clear();
		this.resultMap.put(this.returnFlag, false);
		this.resultMap.put(this.returnMsg, this.failureMsg);
	}
	
	/**
	 * 自定义设置成功的消息
	 * @param msg
	 */
	public void setupSuccessMap(String msg) {
		this.resultMap.clear();
		this.resultMap.put(this.returnFlag, true);
		this.resultMap.put(this.returnMsg, msg);
	}
	
	/**
	 * 自定义设置失败的消息
	 * @param msg
	 */
	public void setupFailureMap(String msg) {
		this.resultMap.clear();
		this.resultMap.put(this.returnFlag, false);
		this.resultMap.put(this.returnMsg, msg);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public int getPassType() {
		return passType;
	}

	public void setPassType(int passType) {
		this.passType = passType;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
}
