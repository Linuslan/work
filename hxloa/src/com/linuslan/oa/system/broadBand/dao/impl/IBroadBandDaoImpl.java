package com.linuslan.oa.system.broadBand.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.account.model.Account;
import com.linuslan.oa.system.broadBand.dao.IBroadBandDao;
import com.linuslan.oa.system.broadBand.model.BroadBand;
import com.linuslan.oa.util.ConstantVar;
import com.linuslan.oa.util.Page;

@Component("broadBandDao")
public class IBroadBandDaoImpl extends IBaseDaoImpl implements IBroadBandDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Page<BroadBand> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		List<BroadBand> list = null;
		Page<BroadBand> page = null;
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("FROM BroadBand r WHERE r.isDelete=0");
		StringBuffer countHQL = new StringBuffer(
				"SELECT COUNT(*) FROM BroadBand r WHERE r.isDelete=0");
		if (paramMap != null) {
			String subHQL = this.getHQL(BroadBand.class, hql, paramMap, "r");
			hql.append(subHQL);
			countHQL.append(subHQL);
		}

		Query query = null;
		Query countQuery = null;
		Map<String, Query> queryMap = buildQuery(session, BroadBand.class, hql.toString(), countHQL.toString(), paramMap);
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
			page = new Page<BroadBand>(list, totalRecord, totalPage, currentPage);
		}

		return page;
	}

	public List<BroadBand> queryByIds(List<Long> ids) {
		List<BroadBand> broadBands = new ArrayList<BroadBand>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM BroadBand c WHERE c.isDelete=0 AND c.id IN (:ids)";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		broadBands.addAll(query.list());
		return broadBands;
	}

	public List<BroadBand> queryAllBroadBands() {
		List<BroadBand> broadBands = new ArrayList<BroadBand>();
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM BroadBand c WHERE c.isDelete=0";
		Query query = session.createQuery(hql);
		broadBands.addAll(query.list());
		return broadBands;
	}

	public BroadBand queryById(Long id) {
		BroadBand broadBand = null;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		broadBand = (BroadBand) session.get(BroadBand.class, id);
		return broadBand;
	}

	public boolean add(BroadBand broadBand) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.save(broadBand);
		success = true;
		return success;
	}

	public boolean update(BroadBand broadBand) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		session.update(broadBand);
		success = true;
		return success;
	}

	public boolean del(BroadBand broadBand) {
		boolean success = false;
		Session session = null;
		session = this.sessionFactory.getCurrentSession();
		broadBand.setIsDelete(Integer.valueOf(1));
		session.update(broadBand);
		success = true;
		return success;
	}
	
}
