package com.linuslan.oa.workflow.flows.report.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;

public interface IReportDao extends IBaseDao {

	/**
	 * 库存报表
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryStockReportPage(Map<String, String> paramMap, String date, int page, int rows);
	
	/**
	 * 查询一年单个商品的出入库详单
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryStockDetailReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询销售报表
	 * @param paramMap
	 * @param date 查询的时间（yyyy-MM），默认为当前年月
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> querySaleReportPage(Map<String, String> paramMap, String date, int page, int rows);
	
	/**
	 * 查询销售明细账
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> querySaleDetailReportPage(Map<String, String> paramMap, String date, int page, int rows);
	
	/**
	 * 查询销售明细账的回款明细标签页数据
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryPaybackDetailReportPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询所有的客户集合，包括华夏蓝和联拓
	 * @return
	 */
	public List<Map<String, Object>> queryAllCustomers();
}
