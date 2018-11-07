package com.linuslan.oa.workflow.flows.customer.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.customer.dao.ICustomerPaybackDao;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPayback;
import com.linuslan.oa.workflow.flows.customer.model.CustomerPaybackLog;

@Component("customerPaybackDao")
public class ICustomerPaybackDaoImpl extends IBaseDaoImpl implements
		ICustomerPaybackDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public Page<CustomerPayback> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<CustomerPayback> list = null;
		Page<CustomerPayback> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM CustomerPayback r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM CustomerPayback r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subSql = this.getHQL(CustomerPayback.class, hql, paramMap, "r");
			hql.append(subSql);
			countHQL.append(subSql);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, CustomerPayback.class, hql.toString(), countHQL.toString(), paramMap);
		if (null != queryMap.get(ConstantVar.QUERY)) {
			query = (Query) queryMap.get(ConstantVar.QUERY);
			query.setFirstResult((currentPage - 1) * limit)
					.setMaxResults(limit);
			list = query.list();
		}

		long totalRecord = 0L;
		long totalPage = 0L;
		if (null != queryMap.get(ConstantVar.COUNT_QUERY)) {
			countQuery = (Query) queryMap.get(ConstantVar.COUNT_QUERY);
			totalRecord = ((Long) countQuery.uniqueResult()).longValue();
			totalPage = totalRecord % limit > 0L ? totalRecord / limit + 1L
					: totalRecord / limit;
		}
		if (list != null) {
			page = new Page<CustomerPayback>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<CustomerPayback> queryByUserId(Long userId) {
		List<CustomerPayback> customerPaybacks = new ArrayList<CustomerPayback>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM CustomerPayback c WHERE c.isDelete=0 AND c.userId=:userId";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		customerPaybacks.addAll(query.list());
		return customerPaybacks;
	}

	public List<CustomerPayback> queryByIds(List<Long> ids) {
		List<CustomerPayback> customerPaybacks = new ArrayList<CustomerPayback>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM CustomerPayback c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		customerPaybacks.addAll(query.list());
		return customerPaybacks;
	}

	public List<CustomerPayback> queryAllCustomerPaybacks() {
		List<CustomerPayback> customerPaybacks = new ArrayList<CustomerPayback>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM CustomerPayback c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		customerPaybacks.addAll(query.list());
		return customerPaybacks;
	}
	
	/**
	 * 通过公司和客户查询回款统计
	 * @param companyId
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> countByCompanyIdAndCustomerId(Long companyId, Long customerId) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * FROM v_payback_list t WHERE t.customer_id=:customerId AND t.company_id=:companyId";
			Session session = this.sessionFactory.getCurrentSession();
			if(null != companyId && null != customerId) {
				SQLQuery query = session.createSQLQuery(sql);
				query.setParameter("customerId", customerId);
				query.setParameter("companyId", companyId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = query.list();
				if(null != list && 0 < list.size()) {
					map = list.get(0);
				} else {
					throw new Exception("");
				}
			}
		} catch(Exception ex) {
			map = new HashMap<String, Object> ();
			map.put("TOTAL_SALE", 0);
			map.put("TOTAL_PAYBACK", 0);
			map.put("TOTAL_UNPAYBACK", 0);
		} finally {
			
		}
		return map;
	}

	public CustomerPayback queryById(Long id) {
		CustomerPayback customerPayback = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customerPayback = (CustomerPayback) session.get(CustomerPayback.class, id);
		return customerPayback;
	}

	public boolean add(CustomerPayback customerPayback) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(customerPayback);
		success = true;
		return success;
	}

	public boolean update(CustomerPayback customerPayback) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(customerPayback);
		success = true;
		return success;
	}

	public boolean del(CustomerPayback customerPayback) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		customerPayback.setIsDelete(Integer.valueOf(1));
		session.update(customerPayback);
		success = true;
		return success;
	}

	/**
	 * 新增日志
	 * @param customerPaybacklog
	 * @return
	 */
	public boolean addLog(CustomerPaybackLog customerPaybacklog) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(customerPaybacklog);
		success = true;
		return success;
	}
}
