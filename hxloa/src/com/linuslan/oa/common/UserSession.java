package com.linuslan.oa.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆有用户的session信息
 * @author LinusLan
 *
 */
public class UserSession {
	
	/**
	 * 登陆用户的主键
	 */
	private Long userId;
	
	/**
	 * 对应创建的sessionid
	 */
	private String sessionId;
	
	/**
	 * 消息推送连接成功之后生成的connectId
	 */
	private String connectId;
	
	private Map<String, Object> messageCache = new HashMap<String, Object> ();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public Map<String, Object> getMessageCache() {
		return messageCache;
	}

	public void setMessageCache(Map<String, Object> messageCache) {
		this.messageCache = messageCache;
	}
}
