package com.linuslan.oa.system.phone.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.phone.model.Phone;
import com.linuslan.oa.system.phone.dao.IPhoneDao;
import com.linuslan.oa.system.phone.service.IPhoneService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("phoneService")
@Transactional
public class IPhoneServiceImpl extends IBaseServiceImpl implements
		IPhoneService {
	
	@Autowired
	private IPhoneDao phoneDao;
	
	public Page<Phone> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.phoneDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Phone> queryByIds(List<Long> ids) {
		return this.phoneDao.queryByIds(ids);
	}

	public List<Phone> queryAllPhones() {
		return this.phoneDao.queryAllPhones();
	}

	public Phone queryById(Long id) {
		return this.phoneDao.queryById(id);
	}

	public boolean add(Phone phone) {
		return this.phoneDao.add(phone);
	}

	public boolean update(Phone phone) {
		Phone persist = this.phoneDao.queryById(phone.getId());
		BeanUtil.updateBean(persist, phone);
		return this.phoneDao.update(persist);
	}

	public boolean del(Phone phone) {
		return this.phoneDao.del(phone);
	}
}
