package com.linuslan.oa.workflow.flows.saleStuff.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.dao.IEffectDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Effect;
import com.linuslan.oa.workflow.flows.saleStuff.service.IEffectService;

@Component("effectService")
@Transactional
public class IEffectServiceImpl extends IBaseServiceImpl implements
		IEffectService {
	@Autowired
	private IEffectDao effectDao;
	
	/**
	 * 新增效果
	 * @param effect
	 * @return
	 */
	public boolean add(Effect effect) {
		return this.effectDao.add(effect);
	}
	
	/**
	 * 更新效果信息
	 * @param effect
	 * @return
	 */
	public boolean update(Effect effect) {
		return this.effectDao.update(effect);
	}
	
	/**
	 * 删除效果，伪删除，将isDelete状态改为1
	 * @param effect
	 * @return
	 */
	public boolean del(Effect effect) {
		return this.effectDao.del(effect);
	}
	
	/**
	 * 通过id查询效果
	 * @param id
	 * @return
	 */
	public Effect queryById(Long id) {
		return this.effectDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有效果
	 * @param ids
	 * @return
	 */
	public List<Effect> queryByIds(List<Long> ids) {
		return this.effectDao.queryByIds(ids);
	}
	
	/**
	 * 查询效果列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Effect> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.effectDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的效果
	 * @param paramMap
	 * @return
	 */
	public List<Effect> queryAll() {
		return this.effectDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param effect
	 * @throws Exception
	 */
	public void valid(Effect effect) throws Exception {
		if(null == effect.getName() || "".equals(effect.getName().trim())) {
			CodeUtil.throwExcep("效果名称不能为空");
		}
	}
}
