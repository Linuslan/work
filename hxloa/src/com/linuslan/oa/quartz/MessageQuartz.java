package com.linuslan.oa.quartz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.linuslan.oa.common.UserSession;
import com.linuslan.oa.listener.SessionListener;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.PropertyUtil;

public class MessageQuartz implements Runnable {

	private static int count;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(10000);
			} catch(Exception ex) {
				
			}
			try {
				
				//参数的意思：通过什么频道（text）发送什么数据，前台可用频道的值text来获取某频道发送的数据
				//engine.sendToAll("text1", "hellow everyone ,i am the new guy: "+count++);
				Map<String, UserSession> loginUsers = SessionListener.getSessionMap();
				Set<Entry<String, UserSession>> set = loginUsers.entrySet();
				Entry<String, UserSession> entry = null;
				String sessionId = null;
				UserSession session = null;
				Iterator<Entry<String, UserSession>> iter = set.iterator();
				//存放key = 消息推送连接的connectId， value = 登陆用户的id
				Map<String, String> connectIdMap = new HashMap<String, String> ();
				List<Long> userIds = new ArrayList<Long> ();
				List<String> userIdStr = new ArrayList<String> ();
				Map<String, UserSession> sessionMap = new HashMap<String, UserSession> ();
				while(iter.hasNext()) {
					entry = iter.next();
					sessionId = entry.getKey();
					session = entry.getValue();
					if(CodeUtil.isNotEmpty(sessionId)
							&& CodeUtil.isNotEmpty(session.getConnectId())
							&& null != session.getUserId() && CodeUtil.isNotEmpty(session.getSessionId())) {
						userIds.add(session.getUserId());
						userIdStr.add(session.getUserId().toString());
						connectIdMap.put(session.getUserId().toString(), session.getConnectId());
						sessionMap.put(session.getUserId().toString(), session);
					}
				}
				Map<String, List<Map<String, Object>>> userMsgMap = this.executeJobs(userIdStr, sessionMap);
				this.sendMsg(userMsgMap, connectIdMap, sessionMap);
				
				//发送公告
				Map<String, List<Map<String, Object>>> userNoticeMap = this.executeNoticeJob(userIds, sessionMap);
				this.sendNotice(userNoticeMap, connectIdMap, sessionMap);
			} catch(Exception ex) {
				
			}
		}
			
	}
	
	public void sendNotice(Map<String, List<Map<String, Object>>> userMsgMap,
			Map<String, String> connectIdMap, Map<String, UserSession> sessionMap) {
		try {
			//引擎，负责管理和维持连接，并能够必要的发送服务
			CometEngine engine = CometContext.getInstance().getEngine();
			Set<String> keySet = userMsgMap.keySet();
			if(null != keySet && 0 < keySet.size()) {
				Iterator<String> keyIter = keySet.iterator();
				List<Map<String, Object>> msgs = null;
				String key = null;
				List<Long> userIds = new ArrayList<Long> ();
				while(keyIter.hasNext()) {
					key = keyIter.next();
					msgs = userMsgMap.get(key);
					if(null != msgs && 0 < msgs.size() && CodeUtil.isNotEmpty(connectIdMap.get(key))) {
						String connectId = connectIdMap.get(key);
						System.out.println(connectId);
						JSONArray json = JSONArray.fromObject(msgs);
						engine.sendTo("unReadNotice", engine.getConnection(connectId), json.toString());
						userIds.add(CodeUtil.parseLong(key));
					}
				}
				if(0 < userIds.size()) {
					updateNoticeInUserIds(userIds);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Map<String, List<Map<String, Object>>> executeNoticeJob(List<Long> userIds, Map<String, UserSession> sessionMap) {
		Map<String, List<Map<String, Object>>> userMsgMap = new HashMap<String, List<Map<String, Object>>> ();
		try {
			//查询公告消息
			if(null != userIds && 0 < userIds.size()) {
				List<Map<String, Object>> list = this.queryNoticeInUserIds(userIds);
				this.parseMsg(userMsgMap, list);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return userMsgMap;
	}
	
	//向对应的连接id发送消息
	public void sendMsg(Map<String, List<Map<String, Object>>> userMsgMap,
			Map<String, String> connectIdMap, Map<String, UserSession> sessionMap) {
		try {
			//引擎，负责管理和维持连接，并能够必要的发送服务
			CometEngine engine = CometContext.getInstance().getEngine();
			Set<String> keySet = userMsgMap.keySet();
			if(null != keySet && 0 < keySet.size()) {
				Iterator<String> keyIter = keySet.iterator();
				List<Map<String, Object>> msgs = null;
				String key = null;
				while(keyIter.hasNext()) {
					key = keyIter.next();
					msgs = userMsgMap.get(key);
					if(null != msgs && 0 < msgs.size() && CodeUtil.isNotEmpty(connectIdMap.get(key))) {
						String connectId = connectIdMap.get(key);
						System.out.println(connectId);
						JSONArray json = JSONArray.fromObject(msgs);
						engine.sendTo("unRead", engine.getConnection(connectId), json.toString());
					}
					sessionMap.get(key).getMessageCache().put("lastConnectId", sessionMap.get(key).getConnectId());
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	/**
	 * 查询各个方法，获取消息map，转换为用户对应的消息对象
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> executeJobs(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		Map<String, List<Map<String, Object>>> userMsgMap = new HashMap<String, List<Map<String, Object>>> ();
		try {
			//查询合同消息
			List<Map<String, Object>> list = queryContractMsg(userIdStr, sessionMap);
			this.parseMsg(userMsgMap, list);
			
			//查询用户合同消息
			list = this.queryUserContractMsg(userIdStr, sessionMap);
			this.parseMsg(userMsgMap, list);
			
			//查询电话套餐消息
			list = this.queryPhoneMsg(userIdStr, sessionMap);
			this.parseMsg(userMsgMap, list);
			
			//查询手机套餐
			list = this.queryCellphoneMsg(userIdStr, sessionMap);
			this.parseMsg(userMsgMap, list);
			
			//查询证件
			list = this.queryCertificateMsg(userIdStr, sessionMap);
			this.parseMsg(userMsgMap, list);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return userMsgMap;
	}
	
	/**
	 * 将消息map转换成登陆用户对应的消息对象
	 * @param userMsgMap
	 * @param list
	 */
	public void parseMsg(Map<String, List<Map<String, Object>>> userMsgMap, List<Map<String, Object>> list) {
		if(null != list && 0 < list.size()) {
			Iterator<Map<String, Object>> iter = list.iterator();
			Map<String, Object> map = null;
			while(iter.hasNext()) {
				map = iter.next();
				if(CodeUtil.isNotEmpty(CodeUtil.parseString(map.get("USER_ID")))) {
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					if(null == userMsgMap.get(userId)) {
						List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
						userMsgMap.put(userId, msgs);
					}
					userMsgMap.get(userId).add(map);
				}
			}
		}
	}
	
	/**
	 * 获取合同信息
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryContractMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expired_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_contract' AND t2.value IN (:ids)";
			
			//查询即将到期的合同
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_contract t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND  t.end_date >= TRUNC(SYSDATE) AND t.out_of_date=2 AND ROUND(TO_NUMBER(t.end_date - SYSDATE)) <= "+expireTime;
			
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_contract t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.end_date < TRUNC(SYSDATE) AND t.out_of_date=3 AND ROUND(TO_NUMBER(SYSDATE - t.end_date)) <= " + expiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					/*
					 * 如果上次连接的id和本次连接的id不同，则说明是刷新，要重新提醒用户
					 */
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 查询用户合同的提醒消息
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryUserContractMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("userContract_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("userContract_expired_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("contract_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_user_contract' AND t2.value IN (:ids)";
			
			//查询即将到期的合同
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_user_contract t WHERE t.is_remind = 0 AND t.is_effective = 0 AND t.is_delete = 0" +
					" AND  t.end_date >= TRUNC(SYSDATE) AND t.out_of_date=2 AND ROUND(TO_NUMBER(t.end_date - SYSDATE)) <= "+expireTime;
			
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_user_contract t WHERE t.is_remind = 0 AND t.is_effective = 0 AND t.is_delete = 0" +
					" AND t.end_date < TRUNC(SYSDATE) AND t.out_of_date=3 AND ROUND(TO_NUMBER(SYSDATE - t.end_date)) <= " + expiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 固话消息查询
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryPhoneMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("phone_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("phone_expire_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("phone_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("phone_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_phone' AND t2.value IN (:ids)";
			
			//查询即将到期的合同
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_phone t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date >= TRUNC(SYSDATE) AND t.out_of_date=2 AND ROUND(TO_NUMBER(t.package_end_date - SYSDATE)) <= "+expireTime;
			
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_phone t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date < TRUNC(SYSDATE) AND t.out_of_date=3 AND ROUND(TO_NUMBER(SYSDATE - t.package_end_date)) <= " + expiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 手机消息查询
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryCellphoneMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("cellphone_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("cellphone_expire_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("cellphone_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("cellphone_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_cellphone' AND t2.value IN (:ids)";
			
			//查询即将到期的合同
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_cellphone t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date >= TRUNC(SYSDATE) AND t.out_of_date=2 AND ROUND(TO_NUMBER(t.package_end_date - SYSDATE)) <= "+expireTime;
			
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_cellphone t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date < TRUNC(SYSDATE) AND t.out_of_date=3 AND ROUND(TO_NUMBER(SYSDATE - t.package_end_date)) <= " + expiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 宽带消息查询
	 * 暂时未启用
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryBroadBandMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("broadBand_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("broadBand_expire_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("broadBand_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("broadBand_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_broad_band' AND t2.value IN (:ids)";
			
			//查询即将到期的合同
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_broad_band t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date >= TRUNC(SYSDATE) AND t.out_of_date=2 AND ROUND(TO_NUMBER(t.package_end_date - SYSDATE)) <= "+expireTime;
			
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_broad_band t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.package_end_date < TRUNC(SYSDATE) AND t.out_of_date=3 AND ROUND(TO_NUMBER(SYSDATE - t.package_end_date)) <= " + expiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 企业证件消息查询
	 * @param userIdStr
	 * @param sessionMap
	 * @return
	 */
	public List<Map<String, Object>> queryCertificateMsg(List<String> userIdStr, Map<String, UserSession> sessionMap) {
		if(null != userIdStr && 0 < userIdStr.size()) {
			//设置即将到期提醒时间，默认设置为31，即即将到期时间为31天时提醒
			int expireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_expire_day"))) {
				expireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_expire_day"));
			}
			
			//设置已经过期仍提醒的时间，默认设置为10，即已经过期10天，但仍需提醒
			int expiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_expired_day"))) {
				expiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_expired_day"));
			}
			
			//设置年检即将到期时间，默认为31
			int annualExpireTime = 31;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_annual_expire_day"))) {
				annualExpireTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_annual_expire_day"));
			}
			
			//设置年检已过期
			int annualExpiredTime = 10;
			if(null != CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_annual_expired_day"))) {
				annualExpiredTime = CodeUtil.parseInt(PropertyUtil.getConfigPropertyValue("certificate_annual_expired_day"));
			}
			
			String sql1 = "SELECT t.value, t.memo, t2.value user_id FROM sys_dictionary t INNER JOIN sys_dictionary t2 ON t2.pid = t.id WHERE t.value = 'sys_certificate' AND t2.value IN (:ids)";
			
			//查询证件即将到期
			String sql2 = "SELECT NVL(COUNT(*), 0) count FROM  sys_certificate t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.end_date >= TRUNC(SYSDATE) AND t.status=0 AND ROUND(TO_NUMBER(t.end_date - SYSDATE)) <= "+expireTime;
			
			//查询新过期
			String sql3 = "SELECT NVL(COUNT(*), 0) expired_count  FROM sys_certificate t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.end_date < TRUNC(SYSDATE) AND t.status=3 AND ROUND(TO_NUMBER(SYSDATE - t.end_date)) <= " + expiredTime;
			
			//查询年检即将到期
			String sql4 = "SELECT NVL(COUNT(*), 0) annual_count  FROM sys_certificate t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.inspection_date >= TRUNC(SYSDATE) AND t.inspection_status=0 AND ROUND(TO_NUMBER(t.inspection_date - SYSDATE)) <= " + annualExpireTime;
			
			//查询年检新过期
			String sql5 = "SELECT NVL(COUNT(*), 0) annual_expired_count  FROM sys_certificate t WHERE t.is_remind = 0 AND t.is_delete = 0" +
					" AND t.inspection_date < TRUNC(SYSDATE) AND t.inspection_status=3 AND ROUND(TO_NUMBER(SYSDATE - t.inspection_date)) <= " + annualExpiredTime;
			
			String sql = "SELECT * FROM ("+sql1+") t1, ("+sql2+") t2, ("+sql3+") t3, ("+sql4+") t4, ("+sql5+") t5";
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("ids", userIdStr);
			List<Map<String, Object>> list = this.query(sql, paramMap);
			List<Map<String, Object>> msgs = new ArrayList<Map<String, Object>> ();
			if(null != list && 0 < list.size()) {
				Map<String, Object> map = null;
				Iterator<Map<String, Object>> iter = list.iterator();
				while(iter.hasNext()) {
					map = iter.next();
					String userId = CodeUtil.parseString(map.get("USER_ID"));
					String memo = CodeUtil.parseString(map.get("MEMO"));
					String count = CodeUtil.parseString(map.get("COUNT"));
					String expiredCount = CodeUtil.parseString(map.get("EXPIRED_COUNT"));
					String annualCount = CodeUtil.parseString(map.get("ANNUAL_COUNT"));
					String annualExpiredCount = CodeUtil.parseString(map.get("ANNUAL_EXPIRED_COUNT"));
					if(CodeUtil.isNotEmpty(count) && Integer.parseInt(count) > 0) {
						count = "<font color='red'>" + count + "</font>";
					}
					if(CodeUtil.isNotEmpty(expiredCount) && Integer.parseInt(expiredCount) > 0) {
						expiredCount = "<font color='red'>"+expiredCount+"</font>";
					}
					if(CodeUtil.isNotEmpty(annualCount) && Integer.parseInt(annualCount) > 0) {
						annualCount = "<font color='red'>" + annualCount + "</font>";
					}
					if(CodeUtil.isNotEmpty(annualExpiredCount) && Integer.parseInt(annualExpiredCount) > 0) {
						annualExpiredCount = "<font color='red'>"+annualExpiredCount+"</font>";
					}
					memo = memo.replace("${count}", count).replace("${expiredCount}", expiredCount).replace("${annualCount}", annualCount)
							.replace("${annualExpiredCount}", annualExpiredCount);
					map.put("MEMO", memo);
					//取出用户对应的上一次的消息记录
					Map<String, Object> cache = sessionMap.get(userId).getMessageCache();
					String lastConnectId = CodeUtil.parseString(cache.get("lastConnectId"));
					String msgType = CodeUtil.parseString(map.get("VALUE"));
					String msg = CodeUtil.parseString(cache.get(msgType));
					if(CodeUtil.isNotEmpty(msg)
							&& lastConnectId.equals(sessionMap.get(userId).getConnectId())) {
						if(!msg.equals(memo)) {
							msg = memo;
							msgs.add(map);
						}
					} else {
						msg = memo;
						msgs.add(map);
					}
					cache.put(msgType, msg);
				}
				return msgs;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public List<Map<String, Object>> queryNoticeInUserIds(List<Long> ids) {
		String sql = "SELECT t.tb_id, t.tb_name, t.user_id FROM sys_message t WHERE t.user_id IN (:ids)" +
				" AND t.is_delete = 0 AND (t.is_read=0 OR t.is_deal = 0) ORDER BY t.user_id ASC, t.id DESC";
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("ids", ids);
		return this.query(sql, paramMap);
	}
	
	public void updateNoticeInUserIds(List<Long> ids) {
		String sql = "UPDATE sys_message t SET t.is_read=1 WHERE t.user_id IN (:ids)" +
				" AND t.is_delete = 0 AND (t.is_read=0 OR t.is_deal = 0)";
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("ids", ids);
		this.update(sql, paramMap);
	}
	
	public List<Map<String, Object>> query(String sql, Map<String, Object> paramMap) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Session session = null;
		try {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory = (SessionFactory) wac.getBean("sessionFactory");
			session = sessionFactory.openSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql);
			if(null != paramMap) {
				Set<String> keySet = paramMap.keySet();
				Iterator<String> iter = keySet.iterator();
				String key = null;
				while(iter.hasNext()) {
					key = iter.next();
					if(null != paramMap.get(key)) {
						Object obj = paramMap.get(key);
						if(obj instanceof Collection) {
							query.setParameterList(key, (Collection)obj);
						} else {
							query.setParameter(key, paramMap.get(key));
						}
					}
				}
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list.addAll(query.list());
			session.getTransaction().commit();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(null != session) {
				session.close();
			}
		}
		return list;
	}
	
	public void update(String sql, Map<String, Object> paramMap) {
		Session session = null;
		try {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory = (SessionFactory) wac.getBean("sessionFactory");
			session = sessionFactory.openSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql);
			if(null != paramMap) {
				Set<String> keySet = paramMap.keySet();
				Iterator<String> iter = keySet.iterator();
				String key = null;
				while(iter.hasNext()) {
					key = iter.next();
					if(null != paramMap.get(key)) {
						Object obj = paramMap.get(key);
						if(obj instanceof Collection) {
							query.setParameterList(key, (Collection)obj);
						} else {
							query.setParameter(key, paramMap.get(key));
						}
					}
				}
			}
			query.executeUpdate();
			session.getTransaction().commit();
		} catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}finally {
			if(null != session) {
				session.close();
			}
		}
	}
}
