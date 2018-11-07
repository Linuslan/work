package com.linuslan.oa.system.userContract.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.userContract.dao.IUserContractDao;
import com.linuslan.oa.system.userContract.model.UserContract;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("userContractDao")
public class IUserContractDaoImpl extends IBaseDaoImpl implements
		IUserContractDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 查询用户的合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		Page<UserContract> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("FROM UserContract uc");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM UserContract uc");
			String param = this.getParamHQLWithWhere(paramMap);
			hql.append(param);
			countHQL.append(param);
			hql.append(" AND uc.isDelete=0");
			countHQL.append(" AND uc.isDelete=0");
			hql.append(" ORDER BY uc.isEffective ASC, uc.isRemind ASC, uc.id DESC");
			Map<String, Query> queryMap = this.getQueryMap(paramMap, session, hql, countHQL);
			Query query = queryMap.get("query");
			Query countQuery = queryMap.get("countQuery");
			query.setFirstResult((currentPage - 1) * limit)
				.setMaxResults(limit);
			long totalRecord = (Long) countQuery.uniqueResult();
			long totalPage = totalRecord % limit > 0 ? totalRecord / limit + 1 : totalRecord / limit;
			page = new Page<UserContract> (query.list(), totalRecord, totalPage, currentPage);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return page;
	}
	
	/**
	 * 查询即将到期的用户合同列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<UserContract> queryWillExpirePage(Map<String, String> paramMap, int currentPage, int limit) {
		Page<UserContract> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			StringBuffer hql = new StringBuffer("FROM UserContract uc WHERE uc.id IN (SELECT crm.typeId FROM ContractRemindMsg crm WHERE crm.isDelete=0 AND crm.status=0 AND crm.type='user_contract' AND crm.readUser.id=6)");
			StringBuffer countHQL = new StringBuffer("SELECT COUNT(*) FROM UserContract uc WHERE uc.id IN (SELECT crm.typeId FROM ContractRemindMsg crm WHERE crm.isDelete=0 AND crm.status=0 AND crm.type='user_contract' AND crm.readUser.id=6)");
			String param = this.getParamHQL(paramMap);
			hql.append(param);
			countHQL.append(param);
			hql.append(" ORDER BY uc.user.orderNum, uc.isEffective ASC, uc.isRemind ASC, uc.id DESC");
			Map<String, Query> queryMap = this.getQueryMap(paramMap, session, hql, countHQL);
			Query query = queryMap.get("query");
			Query countQuery = queryMap.get("countQuery");
			query.setFirstResult((currentPage - 1) * limit)
				.setMaxResults(limit);
			long totalRecord = (Long) countQuery.uniqueResult();
			long totalPage = totalRecord % limit > 0 ? totalRecord / limit + 1 : totalRecord / limit;
			page = new Page<UserContract> (query.list(), totalRecord, totalPage, currentPage);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return page;
	}
	
	/**
	 * 查询用户合同即将到期，提醒行政的条数
	 * @return
	 */
	/*public NoteVo queryWillExpireUserContractMsg() {
		NoteVo vo = new NoteVo();
		String sql = "SELECT COUNT(*) FROM contract_remind_msg crm WHERE crm.is_delete=0 AND crm.status=0 AND crm.type='user_contract' AND crm.read_id=6";
		Connection conn = OracleUtil.getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				vo.setCount(rs.getInt(1));
				vo.setUrl("userContract/queryUserContractPage.action?returnType=expire");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			OracleUtil.closeDB(conn, ps, rs);
		}
		return vo;
	}*/
	
	/**
	 * 得到查询条件Map里的查询条件拼凑后的hql
	 * @param paramMap
	 * @return
	 */
	public String getParamHQL(Map<String, String> paramMap) {
		StringBuffer paramHQL = new StringBuffer();
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
					paramHQL.append(" AND uc."+key+"=:"+key);
				}
			}
		}
		return paramHQL.toString();
	}
	
	/**
	 * 得到查询条件Map里的查询条件拼凑后的hql
	 * @param paramMap
	 * @return
	 */
	public String getParamHQLWithWhere(Map<String, String> paramMap) {
		StringBuffer paramHQL = new StringBuffer();
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
					if(paramHQL.indexOf("WHERE") > 0) {
						paramHQL.append(" AND");
					} else {
						paramHQL.append(" WHERE");
					}
					
					paramHQL.append(" uc."+key+"=:"+key);
				}
			}
		}
		return paramHQL.toString();
	}
	
	/**
	 * 将存放在map里的查询条件的值设置到hql中
	 * @param paramMap
	 * @param session
	 * @param hql
	 * @param countHQL
	 * @return
	 */
	public Map<String, Query> getQueryMap(Map<String, String> paramMap, Session session, StringBuffer hql, StringBuffer countHQL) {
		Map<String, Query> queryMap = new HashMap<String, Query>();
		Query query = session.createQuery(hql.toString());
		Query countQuery = session.createQuery(countHQL.toString());
		if(null != paramMap) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				if(null != paramMap.get(key) && !"".equals(paramMap.get(key).toString())) {
					if(key.equals("userId")) {
						query.setParameter(key, Long.parseLong(paramMap.get(key)));
						countQuery.setParameter(key, Long.parseLong(paramMap.get(key)));
					} else {
						query.setParameter(key, paramMap.get(key));
						countQuery.setParameter(key, paramMap.get(key));
					}
				}
			}
		}
		queryMap.put("query", query);
		queryMap.put("countQuery", countQuery);
		
		return queryMap;
	}
	
	/**
	 * 通过id查询用户合同
	 * @param id
	 * @return
	 */
	public UserContract queryById(Long id) {
		UserContract userContract = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			userContract = (UserContract) session.get(UserContract.class, id);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		if(null == userContract) {
			userContract = new UserContract();
		}
		return userContract;
	}
	
	/**
	 * 新增用户合同
	 * @param pettyCash
	 * @return
	 */
	public UserContract add(UserContract userContract) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			if(null != userContract) {
				StringBuffer updateHQL = new StringBuffer();
				String param1 = "";
				
				String param2 = "";
				boolean update = false;
				if(0 == userContract.getIsEffective()) {
					param1 = " uc.isEffective=1";
					update = true;
				}
				
				if(0 == userContract.getIsRemind()) {
					param2 = " uc.isRemind=1";
					update = true;
				}
				if(update && null != userContract.getUserId()) {
					
					updateHQL.append("UPDATE UserContract uc");
					if(null != param1 && !"".equals(param1.trim())) {
						updateHQL.append(" SET "+param1);
						if(null != param2 && !"".equals(param2.trim())) {
							updateHQL.append(" , "+param2);
						}
					} else {
						if(null != param2 && !"".equals(param2.trim())) {
							updateHQL.append(" SET "+param2);
						}
					}
					updateHQL.append(" WHERE uc.userId="+userContract.getUserId());
					System.out.println(updateHQL.toString());
					Query query = session.createQuery(updateHQL.toString());
					query.executeUpdate();
					/*if(0 == userContract.getIsEffective()) {
						String update2 = "UPDATE ContractRemindMsg crm SET crm.status=1, crm.hasRead=1, crm.isDelete=1 WHERE crm.type='user_contract' AND crm.typeUser.id="+userContract.getUserId();
						Query query2 = session.createQuery(update2);
						query2.executeUpdate();
					}*/
				}
			}
			session.save(userContract);
		} catch(Exception ex) {
			ex.printStackTrace();
			userContract = null;
		}
		return userContract;
	}
	
	/**
	 * 更新用户合同
	 * @param userContract
	 * @return
	 */
	public UserContract update(UserContract userContract) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			if(null != userContract) {
				StringBuffer updateHQL = new StringBuffer();
				String param1 = "";
				
				String param2 = "";
				boolean update = false;
				if(0 == userContract.getIsEffective()) {
					param1 = " uc.isEffective=1";
					update = true;
				}
				
				if(0 == userContract.getIsRemind()) {
					param2 = " uc.isRemind=1";
					update = true;
				}
				if(update && null != userContract.getUserId() && null != userContract.getUserId()) {
					updateHQL.append("UPDATE UserContract uc");
					if(null != param1 && !"".equals(param1)) {
						updateHQL.append(" SET "+param1);
						if(null != param2 && !"".equals(param2)) {
							updateHQL.append(" , "+param2);
						}
					} else {
						if(null != param2 && !"".equals(param2)) {
							updateHQL.append(" SET "+param2);
						}
					}
					updateHQL.append(" WHERE uc.userId="+userContract.getUserId());
					Query query = session.createQuery(updateHQL.toString());
					query.executeUpdate();
					/*if(0 == userContract.getIsEffective()) {
						String update2 = "UPDATE ContractRemindMsg crm SET crm.status=1, crm.hasRead=1, crm.isDelete=1 WHERE crm.type='user_contract' AND crm.typeUser.id="+userContract.getUserId();
						Query query2 = session.createQuery(update2);
						query2.executeUpdate();
					}*/
				}
			}
			UserContract persist = (UserContract) session.get(UserContract.class, userContract.getId());
			userContract.setIsDelete(persist.getIsDelete());
			persist = (UserContract) BeanUtil.updateBean(persist, userContract);
			
			session.merge(persist);
		} catch(Exception ex) {
			ex.printStackTrace();
			userContract = null;
		}
		return userContract;
	}
	
	/**
	 * 通过id删除用户合同
	 * @param id
	 * @return
	 */
	public boolean delById(Long id) {
		boolean success = false;
		Session session = this.sessionFactory.getCurrentSession();
		try {
			UserContract userContract = (UserContract) session.get(UserContract.class, id);
			userContract.setIsDelete(1);
			userContract.setIsEffective(1);
			userContract.setIsRemind(1);
			session.merge(userContract);
			/*String update2 = "UPDATE ContractRemindMsg crm SET crm.status=1, crm.hasRead=1, crm.isDelete=1 WHERE crm.type='user_contract' AND crm.typeId="+userContract.getId()+" AND crm.typeUser.id="+userContract.getUserId();
			Query query2 = session.createQuery(update2);
			query2.executeUpdate();*/
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
}
