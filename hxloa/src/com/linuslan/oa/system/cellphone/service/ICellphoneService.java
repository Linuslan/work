package com.linuslan.oa.system.cellphone.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.cellphone.model.Cellphone;
import com.linuslan.oa.util.Page;

public interface ICellphoneService extends IBaseService {

	public abstract Page<Cellphone> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Cellphone> queryByIds(List<Long> ids);

	public abstract List<Cellphone> queryAllCellphones();

	public abstract Cellphone queryById(Long id);

	public abstract boolean add(Cellphone cellphone);

	public abstract boolean update(Cellphone cellphone);

	public abstract boolean del(Cellphone cellphone);
	
}
