package com.linuslan.oa.workflow.flows.saleStuff.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.dao.IGlossinessDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Glossiness;
import com.linuslan.oa.workflow.flows.saleStuff.service.IGlossinessService;

@Component("glossinessService")
@Transactional
public class IGlossinessServiceImpl extends IBaseServiceImpl implements
		IGlossinessService {

	@Autowired
	private IGlossinessDao glossinessDao;
	
	/**
	 * 新增光泽度
	 * @param glossiness
	 * @return
	 */
	public boolean add(Glossiness glossiness) {
		return this.glossinessDao.add(glossiness);
	}
	
	/**
	 * 更新光泽度信息
	 * @param glossiness
	 * @return
	 */
	public boolean update(Glossiness glossiness) {
		return this.glossinessDao.update(glossiness);
	}
	
	/**
	 * 删除光泽度，伪删除，将isDelete状态改为1
	 * @param glossiness
	 * @return
	 */
	public boolean del(Glossiness glossiness) {
		return this.glossinessDao.del(glossiness);
	}
	
	/**
	 * 通过id查询光泽度
	 * @param id
	 * @return
	 */
	public Glossiness queryById(Long id) {
		return this.glossinessDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有光泽度
	 * @param ids
	 * @return
	 */
	public List<Glossiness> queryByIds(List<Long> ids) {
		return this.glossinessDao.queryByIds(ids);
	}
	
	/**
	 * 查询光泽度列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Glossiness> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.glossinessDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的光泽度
	 * @param paramMap
	 * @return
	 */
	public List<Glossiness> queryAll() {
		return this.glossinessDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param glossiness
	 * @throws Exception
	 */
	public void valid(Glossiness glossiness) throws Exception {
		if(null == glossiness.getName() || "".equals(glossiness.getName().trim())) {
			CodeUtil.throwExcep("光泽度名称不能为空");
		}
	}
}
