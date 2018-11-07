package com.linuslan.oa.workflow.flows.unit.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.unit.dao.IUnitDao;
import com.linuslan.oa.workflow.flows.unit.model.Unit;
import com.linuslan.oa.workflow.flows.unit.service.IUnitService;

@Component("unitService")
@Transactional
public class IUnitServiceImpl extends IBaseServiceImpl implements
		IUnitService {
	@Autowired
	private IUnitDao unitDao;
	
	/**
	 * 新增单位
	 * @param unit
	 * @return
	 */
	public boolean add(Unit unit) {
		return this.unitDao.add(unit);
	}
	
	/**
	 * 更新单位信息
	 * @param unit
	 * @return
	 */
	public boolean update(Unit unit) {
		return this.unitDao.update(unit);
	}
	
	/**
	 * 删除单位，伪删除，将isDelete状态改为1
	 * @param unit
	 * @return
	 */
	public boolean del(Unit unit) {
		return this.unitDao.del(unit);
	}
	
	/**
	 * 通过id查询单位
	 * @param id
	 * @return
	 */
	public Unit queryById(Long id) {
		return this.unitDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有单位
	 * @param ids
	 * @return
	 */
	public List<Unit> queryByIds(List<Long> ids) {
		return this.unitDao.queryByIds(ids);
	}
	
	/**
	 * 查询单位列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Unit> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.unitDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的单位
	 * @param paramMap
	 * @return
	 */
	public List<Unit> queryAll() {
		return this.unitDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param unit
	 * @throws Exception
	 */
	public void valid(Unit unit) throws Exception {
		if(null == unit.getName() || "".equals(unit.getName().trim())) {
			CodeUtil.throwExcep("单位名称不能为空");
		}
	}
}
