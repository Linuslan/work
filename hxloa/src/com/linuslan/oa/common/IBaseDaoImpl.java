package com.linuslan.oa.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import antlr.collections.List;

import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;

public class IBaseDaoImpl implements IBaseDao {
	
	private static Logger logger = Logger.getLogger(IBaseDaoImpl.class);
	
	//从审核记录表中查询出当前登录用户所在用户组的需审核记录的流程实例id，主要用在待审核查询方法里面
	protected String auditSQL = "SELECT al.wfId FROM AuditLog al WHERE al.auditor IN (:groupIds) AND al.wfType=:wfType AND al.isAudit=0 AND al.auditorType IN ('leader', 'manager')";
	
	//从审核记录表中查询当前登录用户审核过的审核记录
	protected String auditedSQL = "SELECT al.wfId FROM AuditLog al WHERE al.auditUser=:loginUserId AND al.wfType=:wfType AND al.isAudit=1 AND al.auditorType IN ('leader', 'manager')";
	
	public String getHQL(Class<?> clz, StringBuffer hql, Map<String, ? extends Object> paramMap, String tb) {
		StringBuffer paramHQL = new StringBuffer("");
		try {
			if(null != paramMap) {
				if(null != paramMap.entrySet() && 0 < paramMap.entrySet().size()) {
					String key = null;
					Object value = null;
					String flag = null;
					String column = null;
					for(Entry<String, ? extends Object> entry : paramMap.entrySet()) {
						if(null == entry) {
							continue;
						}
						key = entry.getKey();
						if(key.indexOf("_") > 0) {
							flag = key.split("_")[1];
							column = key.split("_")[0];
						} else {
							column = key;
						}
						value = entry.getValue();
						if(CodeUtil.isEmpty(key)
								|| null == value || CodeUtil.isEmpty(value.toString())) {
							continue;
						}
						try {
							Class<?> fieldType = BeanUtil.getFieldType(clz, column);
							String operator = " LIKE #col#";
							if(fieldType == String.class) {
								
							} else if(fieldType == Long.class
									|| fieldType == Integer.class
									|| fieldType == BigDecimal.class
									|| fieldType == Double.class || fieldType == Float.class
									|| fieldType.getName().equals("int") || fieldType.getName().equals("long")
									|| fieldType.getName().equals("double") || fieldType.getName().equals("float")) {
								operator = " = #col#";
							} else if(fieldType == List.class
									|| fieldType == ArrayList.class) {
								operator = " IN (#col#)";
							} else if(fieldType == Date.class) {
								if(0 <= key.toUpperCase().indexOf("START")
										|| (CodeUtil.isNotEmpty(flag) && 0 <= flag.toUpperCase().indexOf("START"))) {
									operator = " >= TO_DATE(#col#, 'yyyy-mm-dd')";
								} else if(0 <= key.toUpperCase().indexOf("END")
										|| (CodeUtil.isNotEmpty(flag) && 0 <= flag.toUpperCase().indexOf("END"))) {
									operator = " <= TO_DATE(#col#, 'yyyy-mm-dd')";
								}
							}
							if(null != hql) {
								if(0 <= hql.indexOf("WHERE")) {
									paramHQL.append(" AND ");
								} else {
									paramHQL.append(" WHERE ");
								}
							} else {
								paramHQL.append(" WHERE ");
							}
							if(CodeUtil.isEmpty(tb)) {
								tb = "t";
							}
							String subHQL = operator.replace("#col#", ":"+key);
							paramHQL.append(tb+"."+column+subHQL);
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch(Exception ex) {
			logger.error("组装条件查询语句异常", ex);
		}
		return paramHQL.toString();
	}
	
	public Map<String, Query> buildQuery(Session session, Class<?> clz, String hql, String countHQL, Map<String, ? extends Object> paramMap) {
		Map<String, Query> queryMap = new HashMap<String, Query> ();
		Query query = null;
		Query countQuery = null;
		try {
			if(CodeUtil.isNotEmpty(hql)) {
				query = session.createQuery(hql);
			}
			if(CodeUtil.isNotEmpty(countHQL)) {
				countQuery = session.createQuery(countHQL);
			}
			if(null != paramMap) {
				if(null != paramMap.entrySet() && 0 < paramMap.entrySet().size()) {
					String key = null;
					Object value = null;
					String flag = null;
					String column = null;
					for(Entry<String, ? extends Object> entry : paramMap.entrySet()) {
						if(null == entry) {
							continue;
						}
						key = entry.getKey();
						if(key.indexOf("_") > 0) {
							flag = key.split("_")[1];
							column = key.split("_")[0];
						} else {
							column = key;
						}
						value = entry.getValue();
						if(CodeUtil.isEmpty(key)
								|| null == value || CodeUtil.isEmpty(value.toString())) {
							continue;
						}
						try {
							Class<?> fieldType = BeanUtil.getFieldType(clz, column);
							if(fieldType == String.class) {
								if(null != query) {
									query.setParameter(key, "%"+value+"%");
								}
								if(null != countQuery) {
									countQuery.setParameter(key, "%"+value+"%");
								}
							} else if(fieldType == Long.class
									|| fieldType == Integer.class
									|| fieldType == BigDecimal.class
									|| fieldType == Double.class || fieldType == Float.class
									|| fieldType.getName().equals("int") || fieldType.getName().equals("long")
									|| fieldType.getName().equals("double") || fieldType.getName().equals("float")) {
								if(null != query) {
									query.setParameter(key, CodeUtil.parseNumberic(value, fieldType));
								}
								if(null != countQuery) {
									countQuery.setParameter(key, CodeUtil.parseNumberic(value, fieldType));
								}
							} else if(fieldType == List.class
									|| fieldType == ArrayList.class) {
								if(null != query) {
									query.setParameterList(key, (Collection)value);
								}
								if(null != countQuery) {
									countQuery.setParameterList(key, (Collection)value);
								}
							} else if(fieldType == Date.class) {
								if(null != query) {
									query.setParameter(key, value);
								}
								if(null != countQuery) {
									countQuery.setParameter(key, value);
								}
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch(Exception ex) {
			logger.error("创建query异常", ex);
		} finally {
			queryMap.put(ConstantVar.QUERY, query);
			queryMap.put(ConstantVar.COUNT_QUERY, countQuery);
		}
		return queryMap;
	}
	
	public Query getQuery(Map<String, Query> queryMap, String type) {
		if(null != queryMap.get(type)) {
			return queryMap.get(type);
		} else {
			return null;
		}
	}
}
