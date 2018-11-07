package com.linuslan.oa.system.certificate.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.system.certificate.service.ICertificateService;
import com.linuslan.oa.system.certificate.dao.ICertificateDao;
import com.linuslan.oa.system.certificate.model.Certificate;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.Page;

@Component("certificateService")
@Transactional
public class ICertificateServiceImpl extends IBaseServiceImpl implements
		ICertificateService {
	@Autowired
	private ICertificateDao certificateDao;

	public Page<Certificate> queryPage(Map<String, String> paramMap,
			int currentPage, int limit) {
		return this.certificateDao.queryPage(paramMap, currentPage, limit);
	}

	public List<Certificate> queryByIds(List<Long> ids) {
		return this.certificateDao.queryByIds(ids);
	}

	public List<Certificate> queryAllCertificates() {
		return this.certificateDao.queryAllCertificates();
	}

	public Certificate queryById(Long id) {
		return this.certificateDao.queryById(id);
	}

	public boolean add(Certificate certificate) {
		return this.certificateDao.add(certificate);
	}

	public boolean update(Certificate certificate) {
		Certificate persist = this.certificateDao.queryById(certificate.getId());
		BeanUtil.updateBean(persist, certificate);
		return this.certificateDao.update(persist);
	}

	public boolean del(Certificate certificate) {
		return this.certificateDao.del(certificate);
	}
}
