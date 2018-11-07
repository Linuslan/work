package com.linuslan.oa.system.notice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.notice.dao.INoticeDao;
import com.linuslan.oa.system.notice.model.Notice;
import com.linuslan.oa.system.notice.model.NoticeReceiver;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("noticeDao")
public class INoticeDaoImpl extends IBaseDaoImpl implements INoticeDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Page<Notice> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<Notice> list = null;
		Page<Notice> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM Notice r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM Notice r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSQL = this.getHQL(Notice.class, hql, paramMap, "r");
			hql.append(subSQL);
			countHQL.append(subSQL);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, Notice.class, hql.toString(), countHQL.toString(), paramMap);
		if (queryMap.get(ConstantVar.QUERY) != null) {
			query = (Query) queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (queryMap.get(ConstantVar.COUNT_QUERY) != null) {
			countQuery = (Query) queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Notice>(list, totalRecord, totalPage, currentPage);
		}

		return page;
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
		List<Map<String, Object>> list = null;
		Page<Map<String, Object>> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("SELECT t.id, t.tb_name, t.tb_id, t.is_read, t.is_deal, t.show_type, t.user_id, t.user_name," +
				" t.is_delete, t2.title, t2.id noticeid, t2.sender_name, TO_CHAR(t.send_date, 'yyyy-mm-dd hh24:mi:ss') senddate" +
				" FROM sys_message t, sys_notice t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.tb_id=t2.id AND t.tb_name='sys_notice'" +
				" AND t.user_id=:userId");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM ("+hql.toString()+")");
		if (paramMap != null) {
			getSQL(paramMap, hql, new StringBuffer(""));
		}

		SQLQuery query = null;
		SQLQuery countQuery = null;
		Map<String, SQLQuery> queryMap = getSQLQuery(paramMap, hql, countHQL,
				session, query, countQuery);
		if (queryMap.get("query") != null) {
			query = (SQLQuery) queryMap.get("query");
			query.setParameter("userId", userId);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (queryMap.get("countQuery") != null) {
			countQuery = (SQLQuery) queryMap.get("countQuery");
			countQuery.setParameter("userId", userId);
			totalRecord = Long.parseLong(countQuery.list().get(0).toString());
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<Map<String, Object>>(list, totalRecord, totalPage, currentPage);
		}
		return page;
	}

	public List<Notice> queryByUserId(Long userId) {
		List<Notice> notices = new ArrayList<Notice>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Notice c WHERE c.isDelete=0 AND c.userId=:userId";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		notices.addAll(query.list());
		return notices;
	}

	public List<Notice> queryByIds(List<Long> ids) {
		List<Notice> notices = new ArrayList<Notice>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Notice c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		notices.addAll(query.list());
		return notices;
	}
	
	/**
	 * 查询接收对象对应的id和名称
	 * @param ids
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> queryReceivers(List<Long> ids, String type) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Session session = this.sessionFactory.getCurrentSession();
		String columns = "t.id";
		if("sys_group".equals(type)) {
			columns += ", t.text name";
		} else {
			columns += ", t.name";
		}
		String sql = "SELECT "+columns+" FROM "+type+" t WHERE t.is_delete=0 AND t.id IN (:ids)";
		//查询在职员工
		if("sys_user".equals(type)) {
			sql += " AND t.is_leave=1";
		}
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameterList("ids", ids);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list.addAll(query.list());
		return list;
	}
	
	/**
	 * 通过公告id查询接收对象
	 * @param noticeId
	 * @return
	 */
	public List<NoticeReceiver> queryReceiversByNoticeId(Long noticeId) {
		List<NoticeReceiver> list = new ArrayList<NoticeReceiver> ();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM NoticeReceiver nr WHERE nr.noticeId=:noticeId";
		Query query = session.createQuery(hql);
		query.setParameter("noticeId", noticeId);
		list.addAll(query.list());
		return list;
	}

	public List<Notice> queryAllNotices() {
		List<Notice> notices = new ArrayList<Notice>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Notice c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		notices.addAll(query.list());
		return notices;
	}

	public Notice queryById(Long id) {
		Notice notice = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		notice = (Notice) session.get(Notice.class, id);
		return notice;
	}

	private void getSQL(Map<String, String> paramMap, StringBuffer hql,
			StringBuffer countHQL) {
		try {
			if (paramMap != null) {
				Set<String> keySet = paramMap.keySet();
				Iterator<String> iter = keySet.iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					if ((paramMap.get(key) != null)
							&& (!"".equals(((String) paramMap.get(key)).trim()))) {
						if (hql.indexOf("WHERE") > 0)
							hql.append(" AND");
						else {
							hql.append(" WHERE");
						}

						if (countHQL.indexOf("WHERE") > 0)
							countHQL.append(" AND");
						else {
							countHQL.append(" WHERE");
						}

						if ("userId".equals(key)) {
							hql.append(" t2." + key + " = :" + key);
							countHQL.append(" t2." + key + " = :" + key);
						} else {
							hql.append(" t2." + key + " LIKE :" + key);
							countHQL.append(" t2." + key + " LIKE :" + key);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Map<String, Query> getQuery(Map<String, String> paramMap,
			StringBuffer hql, StringBuffer countHQL, Session session,
			Query query, Query countQuery) {
		Map<String, Query> queryMap = new HashMap<String, Query>();
		try {
			if (session != null) {
				query = session.createQuery(hql.toString());
				countQuery = session.createQuery(countHQL.toString());
				if (paramMap != null) {
					Set<String> keySet = paramMap.keySet();
					Iterator<String> iter = keySet.iterator();
					while (iter.hasNext()) {
						String key = (String) iter.next();
						if ((paramMap.get(key) != null)
								&& (!"".equals(((String) paramMap.get(key))
										.trim()))) {
							if ("userId".equals(key)) {
								query.setParameter(key, Long.parseLong(paramMap.get(key)));
								countQuery.setParameter(key, Long.parseLong(paramMap.get(key)));
							} else {
								query.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
								countQuery.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
							}
						}
					}
				}
				queryMap.put("query", query);
				queryMap.put("countQuery", countQuery);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return queryMap;
	}
	
	private Map<String, SQLQuery> getSQLQuery(Map<String, String> paramMap,
			StringBuffer hql, StringBuffer countHQL, Session session,
			SQLQuery query, SQLQuery countQuery) {
		Map<String, SQLQuery> queryMap = new HashMap<String, SQLQuery>();
		try {
			if (session != null) {
				query = session.createSQLQuery(hql.toString());
				countQuery = session.createSQLQuery(countHQL.toString());
				if (paramMap != null) {
					Set<String> keySet = paramMap.keySet();
					Iterator<String> iter = keySet.iterator();
					while (iter.hasNext()) {
						String key = (String) iter.next();
						if ((paramMap.get(key) != null)
								&& (!"".equals(((String) paramMap.get(key))
										.trim()))) {
							if ("userId".equals(key)) {
								query.setParameter(key, Long.parseLong(paramMap.get(key)));
								countQuery.setParameter(key, Long.parseLong(paramMap.get(key)));
							} else {
								query.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
								countQuery.setParameter(key, "%"
										+ ((String) paramMap.get(key)).trim()
										+ "%");
							}
						}
					}
				}
				queryMap.put("query", query);
				queryMap.put("countQuery", countQuery);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return queryMap;
	}

	public boolean add(Notice notice) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(notice);
		success = true;
		return success;
	}

	public boolean update(Notice notice) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(notice);
		success = true;
		return success;
	}

	public boolean del(Notice notice) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		notice.setIsDelete(Integer.valueOf(1));
		session.update(notice);
		success = true;
		return success;
	}
	
	/**
	 * 批量新增接收对象
	 * @param receivers
	 * @return
	 */
	public boolean addReceivers(List<NoticeReceiver> receivers) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO sys_notice_receiver(id, type, type_id, type_name, create_date, notice_id)" +
					" VALUES(sys_notice_receiver_seq.nextval, ?, ?, ?, SYSDATE, ?)";
			ps = conn.prepareStatement(sql);
			if(null != receivers && 0 < receivers.size()) {
				Iterator<NoticeReceiver> iter = receivers.iterator();
				NoticeReceiver receiver = null;
				while(iter.hasNext()) {
					receiver = iter.next();
					if(null != receiver.getNoticeId()
							&& null != receiver.getTypeId()
							&& null != receiver.getType()) {
						ps.setString(1, receiver.getType());
						ps.setLong(2, receiver.getTypeId());
						ps.setString(3, receiver.getTypeName());
						ps.setLong(4, receiver.getNoticeId());
						ps.addBatch();
					}
				}
				ps.executeBatch();
			}
			conn.commit();
			return true;
		} catch(Exception ex) {
			try {
				conn.rollback();
			} catch(Exception e) {
				e.printStackTrace();
			}
			CodeUtil.throwRuntimeExcep(ex);
			return false;
		} finally {
			try {
				if(null != conn) {
					conn.close();
				}
				if(null != ps) {
					ps.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除接收对象
	 * @param noticeId
	 * @return
	 */
	public boolean delReceiversByNoticeId(Long noticeId) {
		boolean success = false;
		String sql = "DELETE FROM sys_notice_receiver WHERE notice_id=:noticeId";
		if(null != noticeId) {
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("noticeId", noticeId);
			query.executeUpdate();
			success = true;
		}
		return success;
	}
}
