package com.linuslan.oa.workflow.flows.saleStuff.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.saleStuff.dao.IFaceDao;
import com.linuslan.oa.workflow.flows.saleStuff.model.Face;
import com.linuslan.oa.workflow.flows.saleStuff.service.IFaceService;

@Component("faceService")
@Transactional
public class IFaceServiceImpl extends IBaseServiceImpl implements IFaceService {
	@Autowired
	private IFaceDao faceDao;
	
	/**
	 * 新增面数
	 * @param face
	 * @return
	 */
	public boolean add(Face face) {
		return this.faceDao.add(face);
	}
	
	/**
	 * 更新面数信息
	 * @param face
	 * @return
	 */
	public boolean update(Face face) {
		return this.faceDao.update(face);
	}
	
	/**
	 * 删除面数，伪删除，将isDelete状态改为1
	 * @param face
	 * @return
	 */
	public boolean del(Face face) {
		return this.faceDao.del(face);
	}
	
	/**
	 * 通过id查询面数
	 * @param id
	 * @return
	 */
	public Face queryById(Long id) {
		return this.faceDao.queryById(id);
	}
	
	/**
	 * 查询指定id的所有面数
	 * @param ids
	 * @return
	 */
	public List<Face> queryByIds(List<Long> ids) {
		return this.faceDao.queryByIds(ids);
	}
	
	/**
	 * 查询面数列表
	 * @param paramMap
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public Page<Face> queryPage(Map<String, String> paramMap, int currentPage, int limit) {
		return this.faceDao.queryPage(paramMap, currentPage, limit);
	}
	
	/**
	 * 查询所有的面数
	 * @param paramMap
	 * @return
	 */
	public List<Face> queryAll() {
		return this.faceDao.queryAll();
	}
	
	/**
	 * 检测有效值
	 * @param face
	 * @throws Exception
	 */
	public void valid(Face face) throws Exception {
		if(null == face.getName() || "".equals(face.getName().trim())) {
			CodeUtil.throwExcep("面数名称不能为空");
		}
	}
}
