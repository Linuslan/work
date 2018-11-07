package com.linuslan.oa.workflow.flows.area.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.area.dao.IAreaDao;
import com.linuslan.oa.workflow.flows.area.service.IAreaService;
import com.linuslan.oa.workflow.flows.area.model.Area;

@Component("areaService")
@Transactional
public class IAreaServiceImpl extends IBaseServiceImpl implements IAreaService {
	
	@Autowired
	private IAreaDao areaDao;
	
	/**
	 * 新增区域
	 * @param area
	 * @return
	 */
	public boolean add(Area area) {
		return this.areaDao.add(area);
	}
	
	/**
	 * 更新区域信息
	 * @param area
	 * @return
	 */
	public boolean update(Area area) {
		return this.areaDao.update(area);
	}
	
	/**
	 * 删除区域，伪删除，将isDelete状态改为1
	 * @param area
	 * @return
	 */
	public boolean del(Area area) {
		return this.areaDao.del(area);
	}
	
	/**
	 * 通过id查询区域
	 * @param id
	 * @return
	 */
	public Area queryById(Long id) {
		return this.areaDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有区域
	 * @param ids
	 * @return
	 */
	public List<Area> queryByIds(List<Long> ids) {
		return this.areaDao.queryByIds(ids);
	}
	
	/**
	 * 查询区域列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Area> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.areaDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的区域
	 * @param paramMap
	 * @return
	 */
	public List<Area> queryAll() {
		return this.areaDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param area
	 * @throws Exception
	 */
	public void valid(Area area) throws Exception {
		if(null == area.getName() || "".equals(area.getName().trim())) {
			CodeUtil.throwExcep("区域名称不能为空");
		}
	}
}
