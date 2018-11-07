package com.linuslan.oa.system.broadBand.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.broadBand.model.BroadBand;
import com.linuslan.oa.util.Page;

public interface IBroadBandService extends IBaseService {

	public abstract Page<BroadBand> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<BroadBand> queryByIds(List<Long> ids);

	public abstract List<BroadBand> queryAllBroadBands();

	public abstract BroadBand queryById(Long id);

	public abstract boolean add(BroadBand broadBand);

	public abstract boolean update(BroadBand broadBand);

	public abstract boolean del(BroadBand broadBand);
	
}
