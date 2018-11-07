package com.linuslan.oa.system.cellphone.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.cellphone.dao.ICellphoneDao;
import com.linuslan.oa.system.cellphone.model.Cellphone;
import com.linuslan.oa.system.cellphone.service.ICellphoneService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("cellphoneService")
@Transactional
public class ICellphoneServiceImpl extends IBaseServiceImpl implements
		ICellphoneService {

	@Autowired
	private ICellphoneDao cellphoneDao;
	
	public Page<Cellphone> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.cellphoneDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Cellphone> queryByIds(List<Long> ids) {
		return this.cellphoneDao.queryByIds(ids);
	}

	public List<Cellphone> queryAllCellphones() {
		return this.cellphoneDao.queryAllCellphones();
	}

	public Cellphone queryById(Long id) {
		return this.cellphoneDao.queryById(id);
	}

	public boolean add(Cellphone cellphone) {
		return this.cellphoneDao.add(cellphone);
	}

	public boolean update(Cellphone cellphone) {
		Cellphone persist = this.cellphoneDao.queryById(cellphone.getId());
		BeanUtil.updateBean(persist, cellphone);
		return this.cellphoneDao.update(persist);
	}

	public boolean del(Cellphone cellphone) {
		return this.cellphoneDao.del(cellphone);
	}
	
}
