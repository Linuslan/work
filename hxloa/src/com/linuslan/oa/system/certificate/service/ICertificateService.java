package com.linuslan.oa.system.certificate.service;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.system.certificate.model.Certificate;
import com.linuslan.oa.util.Page;

public interface ICertificateService extends IBaseService {

	public abstract Page<Certificate> queryPage(Map<String, String> paramMap,
			int currentPage, int limit);

	public abstract List<Certificate> queryByIds(List<Long> ids);

	public abstract List<Certificate> queryAllCertificates();

	public abstract Certificate queryById(Long id);

	public abstract boolean add(Certificate certificate);

	public abstract boolean update(Certificate certificate);

	public abstract boolean del(Certificate certificate);
	
}
