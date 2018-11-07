package com.linuslan.oa.system.broadBand.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.system.broadBand.model.BroadBand;
import com.linuslan.oa.util.Page;

public interface IBroadBandDao extends IBaseDao {

	public abstract Page<BroadBand> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<BroadBand> queryByIds(List<Long> ids);

	public abstract List<BroadBand> queryAllBroadBands();

	public abstract BroadBand queryById(Long id);

	public abstract boolean add(BroadBand broadBand);

	public abstract boolean update(BroadBand broadBand);

	public abstract boolean del(BroadBand broadBand);
	
}
