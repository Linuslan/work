package com.linuslan.oa.system.broadBand.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.broadBand.service.IBroadBandService;
import com.linuslan.oa.system.broadBand.dao.IBroadBandDao;
import com.linuslan.oa.system.broadBand.model.BroadBand;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("broadBandService")
@Transactional
public class IBroadBandServiceImpl extends IBaseServiceImpl implements
		IBroadBandService {

	@Autowired
	private IBroadBandDao broadBandDao;
	
	public Page<BroadBand> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.broadBandDao.queryPage(paramMap, currentPage, limit);
	}

	public List<BroadBand> queryByIds(List<Long> ids) {
		return this.broadBandDao.queryByIds(ids);
	}

	public List<BroadBand> queryAllBroadBands() {
		return this.broadBandDao.queryAllBroadBands();
	}

	public BroadBand queryById(Long id) {
		return this.broadBandDao.queryById(id);
	}

	public boolean add(BroadBand broadBand) {
		return this.broadBandDao.add(broadBand);
	}

	public boolean update(BroadBand broadBand) {
		BroadBand persist = this.broadBandDao.queryById(broadBand.getId());
		BeanUtil.updateBean(persist, broadBand);
		return this.broadBandDao.update(persist);
	}

	public boolean del(BroadBand broadBand) {
		return this.broadBandDao.del(broadBand);
	}
	
}
