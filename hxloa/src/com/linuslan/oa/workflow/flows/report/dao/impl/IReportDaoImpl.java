package com.linuslan.oa.workflow.flows.report.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.report.dao.IReportDao;

@Component("reportDao")
public class IReportDaoImpl extends IBaseDaoImpl implements IReportDao {

	private static Logger logger = Logger.getLogger(IReportDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 库存管理
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryStockReportPage(Map<String, String> paramMap, String date, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			int lastYear = year;
			int lastMonth = month - 1 > 0 ? month - 1 : 12;
			if(lastMonth == 12) {
				lastYear --;
			}
			String lastMonthStr = lastYear + "-" + (lastMonth > 9 ? lastMonth : "0"+lastMonth);
			String groupBy = " GROUP BY t2.article_id, t2.format_id, t.company_id";
			//展示的字段
			String sql = "SELECT t1.article_id, t1.article_name, t1.format_id, t1.format_name, t2.company_id, t7.name company_name," +
					" NVL(t2.checkin_quantity, 0) checkin_quantity," +
					" NVL(t2.checkin_total, 0) checkin_total," +
					" NVL(t2.loss_total, 0) loss_total," +
					" NVL(t3.checked_quantity, 0) checked_out_total," +
					" NVL(t14.checked_loss, 0) checked_out_loss," +
					" NVL(t4.checking_quantity, 0) checking_out_total," +
					" NVL(t5.sale_checked_quantity, 0) sale_checked_out_total," +
					" NVL(t6.sale_checking_quantity, 0) sale_checking_out_total," +
					" NVL(t8.last_month_checkin_total, 0) last_month_checkin_total," +
					" NVL(t8.last_month_checkin, 0) last_month_checkin,"+
					" NVL(t8.last_month_loss, 0) last_month_loss,"+
					" NVL(t9.last_month_checked, 0) last_month_checked_out," +
					" NVL(t15.last_month_checked_loss, 0) last_month_checked_out_loss," +
					" NVL(t10.last_month_sale_checked, 0) last_month_sale_checked_out," +
					" NVL(t11.month_checkin_total, 0) month_checkin_total," +
					" NVL(t11.month_checkin, 0) month_checkin,"+
					" NVL(t11.month_loss, 0) month_checkin_loss,"+
					" NVL(t12.month_checked, 0) month_checked_out," +
					" NVL(t16.month_checked_loss, 0) month_checked_out_loss," +
					" NVL(t13.month_sale_checked, 0) month_sale_checked_out," +
					" NVL(t11.month_loss, 0) month_loss FROM ";
			
			StringBuffer articleSQL = new StringBuffer("SELECT t.id article_id, t.name article_name, t2.id format_id, t2.name format_name FROM wf_checkin_article t LEFT JOIN wf_format t2 ON t.id=t2.article_id AND t2.is_delete=0 WHERE t.is_delete=0");
			//总入库
			StringBuffer totalCheckin = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) checkin_quantity, NVL(SUM(t2.loss), 0) loss_total, (NVL(SUM(t2.quantity), 0) + NVL(SUM(t2.loss), 0)) checkin_total FROM wf_checkin t, wf_checkin_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkin_id AND t.status=4 AND t.checkin_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalCheckin, "t1", "t2");
			totalCheckin.append(groupBy);
			
			//总已出库（不包含损毁）
			StringBuffer totalChecked = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) checked_quantity FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.checkout_type_id <> 65 AND t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND t.checkout_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalChecked, "t1", "t2");
			totalChecked.append(groupBy);
			
			//总的损毁出库
			StringBuffer totalLossCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) checked_loss FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND t.checkout_type_id=65 AND t.checkout_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalLossCheckedOut, "t1", "t2");
			totalLossCheckedOut.append(groupBy);
			
			//总待审出库
			StringBuffer totalChecking = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) checking_quantity FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status <> 4 AND t.checkout_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalChecking, "t1", "t2");
			totalChecking.append(groupBy);
			
			//总已销售出库
			StringBuffer totalSaleChecked = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.checkout_quantity), 0) sale_checked_quantity FROM wf_sale t, wf_sale_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.sale_id AND t.sale_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalSaleChecked, "t1", "t2");
			totalSaleChecked.append(groupBy);
			
			//总待审销售出库
			StringBuffer totalSaleChecking = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0)-NVL(SUM(t2.checkout_quantity), 0) sale_checking_quantity FROM wf_sale t, wf_sale_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.sale_id AND t.sale_date <= LAST_DAY(TO_DATE(:month, 'yyyy-mm'))");
			this.buildSQL(paramMap, totalSaleChecking, "t1", "t2");
			totalSaleChecking.append(groupBy);
			
			//截止上月总入库
			StringBuffer lastMonthCheckin = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) last_month_checkin, NVL(SUM(t2.loss), 0) last_month_loss, (NVL(SUM(t2.quantity), 0) + NVL(SUM(t2.loss), 0)) last_month_checkin_total FROM wf_checkin t, wf_checkin_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkin_id AND t.status=4 AND t.checkin_date <= LAST_DAY(TO_DATE(:lastMonth, 'yyyy-mm'))");
			this.buildSQL(paramMap, lastMonthCheckin, "t1", "t2");
			lastMonthCheckin.append(groupBy);
			
			//截止上月总出库（不包含损毁）
			StringBuffer lastMonthCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) last_month_checked FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.checkout_type_id <> 65 AND t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND t.checkout_date <= LAST_DAY(TO_DATE(:lastMonth, 'yyyy-mm'))");
			this.buildSQL(paramMap, lastMonthCheckedOut, "t1", "t2");
			lastMonthCheckedOut.append(groupBy);
			
			//截止上月总损毁出库
			StringBuffer lastMonthLossCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) last_month_checked_loss FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND t.checkout_type_id=65 AND t.checkout_date <= LAST_DAY(TO_DATE(:lastMonth, 'yyyy-mm'))");
			this.buildSQL(paramMap, lastMonthLossCheckedOut, "t1", "t2");
			lastMonthLossCheckedOut.append(groupBy);
			
			
			//截止上月销售总出库
			StringBuffer lastMonthSaleCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.checkout_quantity), 0) last_month_sale_checked FROM wf_sale t, wf_sale_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.sale_id AND t.sale_date <= LAST_DAY(TO_DATE(:lastMonth, 'yyyy-mm'))");
			this.buildSQL(paramMap, lastMonthSaleCheckedOut, "t1", "t2");
			lastMonthSaleCheckedOut.append(groupBy);
			
			//本月入库
			StringBuffer monthCheckin = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) month_checkin, NVL(SUM(t2.loss), 0) month_loss, (NVL(SUM(t2.quantity), 0) + NVL(SUM(t2.loss), 0)) month_checkin_total FROM wf_checkin t, wf_checkin_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkin_id AND t.status=4 AND TO_CHAR(t.checkin_date, 'yyyy-mm') = :month");
			this.buildSQL(paramMap, monthCheckin, "t1", "t2");
			monthCheckin.append(groupBy);
			
			//本月出库（不包含损毁出库）
			StringBuffer monthCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) month_checked FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.checkout_type_id <> 65 AND t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND TO_CHAR(t.checkout_date, 'yyyy-mm') = :month");
			this.buildSQL(paramMap, monthCheckedOut, "t1", "t2");
			monthCheckedOut.append(groupBy);
			
			//本月损毁出库
			StringBuffer monthLossCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.quantity), 0) month_checked_loss FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.checkout_id AND t.status=4 AND t.checkout_type_id=65 AND TO_CHAR(t.checkout_date, 'yyyy-mm') = :month");
			this.buildSQL(paramMap, monthLossCheckedOut, "t1", "t2");
			monthLossCheckedOut.append(groupBy);
			
			//本月销售出库
			StringBuffer monthSaleCheckedOut = new StringBuffer("SELECT t.company_id, t2.article_id, t2.format_id, NVL(SUM(t2.checkout_quantity), 0) month_sale_checked FROM wf_sale t, wf_sale_content t2 WHERE t.is_delete=0 AND t2.is_delete=0 AND t.id=t2.sale_id AND TO_CHAR(t.sale_date, 'yyyy-mm') = :month");
			this.buildSQL(paramMap, monthSaleCheckedOut, "t1", "t2");
			monthSaleCheckedOut.append(groupBy);
			
			sql = sql + "("+articleSQL.toString()+") t1 LEFT JOIN ("+totalCheckin.toString()+") t2 ON t1.article_id=t2.article_id AND t1.format_id=t2.format_id" +
					//总已出库
					" LEFT JOIN ("+totalChecked.toString()+") t3 ON t3.article_id=t2.article_id AND t3.format_id=t2.format_id AND t3.company_id=t2.company_id" +
					//总损毁出库
					" LEFT JOIN ("+totalLossCheckedOut.toString()+") t14 ON t14.article_id=t2.article_id AND t14.format_id=t2.format_id AND t14.company_id=t2.company_id" +
					//总待审出库
					" LEFT JOIN ("+totalChecking.toString()+") t4 ON t4.article_id=t2.article_id AND t4.format_id=t2.format_id AND t4.company_id=t2.company_id" +
					//总销售已出库
					" LEFT JOIN ("+totalSaleChecked.toString()+") t5 ON t5.company_id=t2.company_id AND t5.article_id=t2.article_id AND t5.format_id=t2.format_id" +
					//总待审销售出库
					" LEFT JOIN ("+totalSaleChecking.toString()+") t6 ON t6.company_id=t2.company_id AND t6.article_id=t2.article_id AND t6.format_id=t2.format_id" +
					//截止上月总入库
					" LEFT JOIN ("+lastMonthCheckin.toString()+") t8 ON t2.article_id=t8.article_id AND t2.format_id=t8.format_id AND t2.company_id=t8.company_id" +
					//截止上月总出库
					" LEFT JOIN ("+lastMonthCheckedOut.toString()+") t9 ON t9.article_id=t8.article_id AND t9.format_id=t8.format_id AND t9.company_id=t8.company_id" +
					//截止上月总损毁出库
					" LEFT JOIN ("+lastMonthLossCheckedOut.toString()+") t15 ON t15.article_id=t8.article_id AND t15.format_id=t8.format_id AND t15.company_id=t8.company_id" +
					//截止上月销售总出库
					" LEFT JOIN ("+lastMonthSaleCheckedOut.toString()+") t10 ON t10.company_id=t8.company_id AND t10.article_id=t8.article_id AND t10.format_id=t8.format_id" +
					//本月入库
					" LEFT JOIN ("+monthCheckin.toString()+") t11 ON t2.article_id=t11.article_id AND t2.format_id=t11.format_id AND t2.company_id=t11.company_id" +
					//本月出库
					" LEFT JOIN ("+monthCheckedOut.toString()+") t12 ON t12.article_id=t11.article_id AND t12.format_id=t11.format_id AND t12.company_id=t11.company_id" +
					//本月损毁出库
					" LEFT JOIN ("+monthLossCheckedOut.toString()+") t16 ON t16.article_id=t11.article_id AND t16.format_id=t11.format_id AND t16.company_id=t11.company_id" +
					//本月销售出库
					" LEFT JOIN ("+monthSaleCheckedOut.toString()+") t13 ON t13.company_id=t11.company_id AND t13.article_id=t11.article_id AND t13.format_id=t11.format_id" +
					" LEFT JOIN sys_company t7 ON t7.id=t2.company_id AND t7.is_delete=0";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = this.buildSQLQuery(paramMap, session, sql);
			query.setFirstResult((page - 1)*rows);
			query.setMaxResults(rows);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			SQLQuery countQuery = this.buildSQLQuery(paramMap, session, "SELECT COUNT(*) FROM ("+sql+")");
			query.setParameter("month", date);
			query.setParameter("lastMonth", lastMonthStr);
			countQuery.setParameter("month", date);
			countQuery.setParameter("lastMonth", lastMonthStr);
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("查询库存报表异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>>(list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询一年单个商品的出入库详单
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryStockDetailReportPage(Map<String, String> paramMap, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			String sql = "SELECT t1.name company_name, t2.name customer_name, TO_CHAR(t.order_date, 'yyyy-mm-dd') order_date, TO_CHAR(t.order_date, 'yyyy') year, TO_CHAR(t.order_date, 'mm') month, t.company_id, t.customer_id, t.article_id, t.format_id, t.price, t.checkin_quantity, t.checkout_quantity, t.checkin_total, t.checkout_total FROM v_stock_detail_report_list t LEFT JOIN sys_company t1 ON t1.id=t.company_id LEFT JOIN wf_customer t2 ON t2.id=t.customer_id WHERE 1=1";
			StringBuffer conditionSQL = new StringBuffer("");
			this.buildSQL(paramMap, conditionSQL, "t", "t");
			sql += conditionSQL.toString()+" ORDER BY t.order_date ASC";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = this.buildSQLQuery(paramMap, session, sql);
			query.setFirstResult((page - 1)*rows);
			query.setMaxResults(rows);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			SQLQuery countQuery = this.buildSQLQuery(paramMap, session, "SELECT COUNT(*) FROM ("+sql+")");
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("查询库存报表异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>>(list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询销售报表
	 * @param paramMap
	 * @param date 查询的时间（yyyy-MM），默认为当前年月
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> querySaleReportPage(Map<String, String> paramMap, String date, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			int lastYear = year;
			int lastMonth = month - 1 > 0 ? month - 1 : 12;
			if(lastMonth == 12) {
				lastYear --;
			}
			String lastMonthStr = lastYear + "-" + (lastMonth > 9 ? lastMonth : "0"+lastMonth);
			String sql = "SELECT t1.id, t1.name, t1.type, t2.company_id, t8.name company_name, NVL(t2.sale_money, 0) total_sale_amount," +
					" NVL(t3.sale_money, 0) last_sale_amount, NVL(t4.sale_money, 0) month_sale_amount," +
					" NVL(t5.total_back_amount, 0) total_back_amount, NVL(t6.last_month_back_amount, 0) last_month_back_amount," +
					" NVL(t7.month_back_amount, 0) month_back_amount FROM ";
			//华夏蓝客户和联拓客户的合集
			StringBuffer conditionSQL = new StringBuffer("");
			if(null != paramMap && CodeUtil.isNotEmpty(paramMap.get("customerId"))) {
				String customer = paramMap.get("customerId");
				try {
					String customerId = customer.split("_")[0];
					String type = customer.split("_")[1];
					paramMap.put("customerId", customerId);
					paramMap.put("type", type);
					conditionSQL.append(" WHERE t.id=:customerId AND t.type=:type");
				} catch(Exception ex) {
					
				}
			}
			String customerSQL = "(SELECT * FROM customer_list t"+conditionSQL+") t1";
			conditionSQL = new StringBuffer("");
			this.buildSQL(paramMap, conditionSQL, "t1", "t2");
			//总销售额
			String totalSaleAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.sale_money), 0) sale_money FROM v_sale_union_list t WHERE 1=1 "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t2";
			//上月销售额
			String lastSaleAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.sale_money), 0) sale_money FROM v_sale_union_list t WHERE TO_CHAR(t.sale_date, 'yyyy-mm') = :lastMonth "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t3";
			//本月销售额
			String monthSaleAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.sale_money), 0) sale_money FROM v_sale_union_list t WHERE TO_CHAR(t.sale_date, 'yyyy-mm') = :month "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t4";
			//总回款
			String totalBackAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.pay_money), 0) total_back_amount FROM wf_customer_payback t WHERE t.is_delete=0 "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t5";
			//上月回款
			String lastBackAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.pay_money), 0) last_month_back_amount FROM wf_customer_payback t WHERE t.is_delete=0 AND TO_CHAR(t.pay_date, 'yyyy-mm')=:lastMonth "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t6";
			//本月回款
			String monthBackAmount = "(SELECT t.customer_id, t.company_id, t.type, NVL(SUM(t.pay_money), 0) month_back_amount FROM wf_customer_payback t WHERE t.is_delete=0 AND TO_CHAR(t.pay_date, 'yyyy-mm')=:month "+conditionSQL.toString()+" GROUP BY t.customer_id, t.company_id, t.type) t7";
			
			sql = sql + customerSQL
				+ " LEFT JOIN "+totalSaleAmount+" ON t2.customer_id = t1.id AND t2.type = t1.type"
				+ " LEFT JOIN "+lastSaleAmount+" ON t3.customer_id = t2.customer_id AND t3.company_id=t2.company_id AND t3.type=t2.type"
				+ " LEFT JOIN "+monthSaleAmount+" ON t4.customer_id = t2.customer_id AND t4.company_id=t2.company_id AND t4.type=t2.type"
				+ " LEFT JOIN "+totalBackAmount+" ON t5.customer_id=t2.customer_id AND t5.type=t2.type AND t5.company_id=t2.company_id"
				+ " LEFT JOIN "+lastBackAmount+" ON t6.customer_id=t5.customer_id AND t6.company_id = t5.company_id AND t6.type=t5.type"
				+ " LEFT JOIN "+monthBackAmount+" ON t7.customer_id=t5.customer_id AND t7.company_id = t5.company_id AND t7.type=t5.type"
				+ " LEFT JOIN sys_company t8 ON t8.id = t2.company_id";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = this.buildSQLQuery(paramMap, session, sql);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter("month", date);
			query.setParameter("lastMonth", lastMonthStr);
			
			SQLQuery countQuery = this.buildSQLQuery(paramMap, session, "SELECT COUNT(*) FROM ("+sql+")");
			countQuery.setParameter("month", date);
			countQuery.setParameter("lastMonth", lastMonthStr);
			
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error("查询销售报表异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询销售明细账
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> querySaleDetailReportPage(Map<String, String> paramMap, String date, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			String startDate = "1900-01-01";
			String endDate = date;
			if(null != paramMap.get("order_date_startDate") && !"".equals(paramMap.get("order_date_startDate"))) {
				startDate = paramMap.get("order_date_startDate");
				paramMap.remove("order_date_startDate");
			}
			if(null != paramMap.get("order_date_endDate") && !"".equals(paramMap.get("order_date_endDate"))) {
				endDate = paramMap.get("order_date_endDate");
				paramMap.remove("order_date_endDate");
			}
			StringBuffer conditionSQL = new StringBuffer("");
			
			this.buildSQL(paramMap, conditionSQL, "t", "t2");
			String sql = "SELECT t3.order_date, TO_CHAR(t3.order_date, 'yyyy') year, TO_CHAR(t3.order_date, 'mm') month, t3.article_id, t3.format_id, t3.price, SUM(t3.quantity) quantity, SUM(t3.quantity)*t3.price total_amount, 0 sale_money, 0 pay_money  FROM";
			
			//由于华夏蓝的特殊，所以去查询出库的记录，而不是去查华夏蓝销售订单
			String hxlSQL = "SELECT t.checkout_date order_date, t2.article_id, t2.format_id, SUM(t2.quantity) quantity, t2.price, SUM(t2.quantity)*t2.price total_amount FROM wf_checkout t, (SELECT t3.*, t4.checkin_article_id article_id FROM wf_checkout_content t3 LEFT JOIN wf_checkout_article t4 ON t3.checkout_article_id=t4.id) t2 WHERE t.id=t2.checkout_id AND t.is_delete=0 AND t2.is_delete=0 AND t.company_id=4 AND t.status = 4 "+conditionSQL.toString()+" GROUP BY t.checkout_date, t2.article_id, t2.format_id, t2.price";
			//联拓销售直接查联拓销售单
			conditionSQL = new StringBuffer("");
			this.buildSQL(paramMap, conditionSQL, "t5", "t6");
			String saleSQL = "SELECT t5.sale_date order_date, t6.article_id, t6.format_id, SUM(t6.checkout_quantity) quantity, t6.price, SUM(t6.checkout_quantity)*t6.price total_amount FROM wf_sale t5, wf_sale_content t6 WHERE t5.id=t6.sale_id AND t5.is_delete=0 AND t6.is_delete=0 "+conditionSQL.toString()+" GROUP BY t5.sale_date, t6.article_id, t6.format_id, t6.price";
			
			String saleDetailSQL = hxlSQL +" UNION ALL "+saleSQL;
			
			//截止日期的总销售额
			//String saleTotalAmount = "SELECT t.sale_date, NVL(SUM(t.sale_money), 0) sale_money, 0 pay_money FROM v_sale_union_list t WHERE 1=1 AND t.sale_date <= (SELECT t5.sale_date order_date FROM wf_sale t5 WHERE t5.is_delete=0 GROUP BY t5.sale_date) GROUP BY t.sale_date";
			//截止日期的总回款
			//String paybackTotalAmount = "SELECT t7.pay_date sale_date, 0 sale_money, NVL(SUM(t7.pay_money), 0) FROM wf_customer_payback t7 WHERE t7.is_delete=0 AND 1=1 AND t7.pay_date <= (SELECT t5.sale_date order_date FROM wf_sale t5 WHERE t5.is_delete=0 GROUP BY t5.sale_date) GROUP BY t7.pay_date";
			//String amountDetail = saleTotalAmount + " UNION ALL " + paybackTotalAmount;
			
			sql = "SELECT t9.name article_name, t10.name format_name, TO_CHAR(t11.order_date, 'yyyy-mm-dd') order_date, t11.year, t11.month, t11.article_id, t11.format_id, t11.price, t11.quantity, t11.total_amount, t11.sale_money, t11.pay_money" +
					" FROM (SELECT * FROM ("+ sql + " (" + saleDetailSQL + ") t3 GROUP BY t3.order_date, t3.article_id, t3.format_id, t3.price)) t11" +
					" LEFT JOIN wf_checkin_article t9 ON t9.id = article_id LEFT JOIN wf_format t10 ON t10.id = format_id WHERE 1=1 AND order_date<=TO_DATE('"+endDate+"', 'yyyy-mm-dd') AND order_date >= TO_DATE('"+startDate+"', 'yyyy-mm-dd')";
			sql += " ORDER BY order_date ASC";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = this.buildSQLQuery(paramMap, session, sql);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			SQLQuery countQuery = this.buildSQLQuery(paramMap, session, "SELECT COUNT(*) FROM ("+sql+")");
			
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error("查询销售明细账异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询销售明细账的回款明细标签页数据
	 * @param paramMap
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryPaybackDetailReportPage(Map<String, String> paramMap, int page, int rows) {
		Page<Map<String, Object>> pageData = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		long totalRecord = 0;
		long totalPage = 0;
		try {
			String sql = "SELECT t6.name company_name, t7.name customer_name, TO_CHAR(t5.order_date, 'yyyy') YEAR, TO_CHAR(t5.order_date, 'mm') MONTH," +
					" t5.customer_id, t5.company_id, TO_CHAR(t5.order_date, 'yyyy-mm-dd') order_date, t5.sale_amount, t5.payback_amount, t5.total_payback_amount FROM ";
			
			//按天统计回款明细
			String payback = "(SELECT t.customer_id, t.company_id, t.pay_date, SUM(t.pay_money) payback_amount FROM wf_customer_payback t WHERE t.is_delete=0 GROUP BY t.customer_id, t.company_id, t.pay_date)";
			//按天统计销售额
			String saleDetail = "(SELECT customer_id, company_id, sale_date, SUM(sale_money) sale_amount FROM v_sale_union_list GROUP BY customer_id, company_id, sale_date)";
			//按天统计回款明细，以及截止至回款时间前的总销售额
			String salePayback = "SELECT t1.customer_id, t1.company_id, t1.pay_date order_date, SUM(sale_amount) sale_amount, payback_amount FROM (" +
					payback+" t1 LEFT JOIN " + saleDetail + " t2 ON t1.customer_id=t2.customer_id AND t1.company_id=t2.company_id AND t1.pay_date >= t2.sale_date" +
					") GROUP BY t1.customer_id, t1.company_id, t1.pay_date, payback_amount";
			//按天统计回款明细，以及截止至回款时间前的总销售额，以及截止至回款时间前的总回款
			String salePaybackUnpayback = "SELECT t3.*, SUM(t4.payback_amount) total_payback_amount FROM ("+salePayback+") t3" +
					" LEFT JOIN "+payback+" t4 ON t3.customer_id = t4.customer_id AND t3.company_id=t4.company_id AND t4.pay_date <= t3.order_date" +
					" GROUP BY t3.customer_id, t3.company_id, t3.order_date, t3.payback_amount, t3.sale_amount";
			sql = sql + "("+salePaybackUnpayback+") t5 LEFT JOIN sys_company t6 ON t6.id=t5.company_id LEFT JOIN wf_customer t7 ON t7.id=t5.customer_id WHERE 1=1";
			StringBuffer conditionSQL = new StringBuffer("");
			this.buildSQL(paramMap, conditionSQL, "t5", "");
			sql = sql + conditionSQL.toString() + " ORDER BY t5.order_date";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = this.buildSQLQuery(paramMap, session, sql);
			query.setFirstResult((page - 1)*rows).setMaxResults(rows);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			SQLQuery countQuery = this.buildSQLQuery(paramMap, session, "SELECT COUNT(*) FROM ("+sql+")");
			
			list.addAll(query.list());
			totalRecord = CodeUtil.parseLong(countQuery.list().get(0));
			totalPage = Page.countTotalPage(totalRecord, rows);
		} catch(Exception ex) {
			logger.error("查询销售明细账的回款明细异常", ex);
		} finally {
			pageData = new Page<Map<String, Object>> (list, totalRecord, totalPage, page);
		}
		return pageData;
	}
	
	/**
	 * 查询所有的客户集合，包括华夏蓝和联拓
	 * @return
	 */
	public List<Map<String, Object>> queryAllCustomers() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		try {
			String sql = "SELECT * FROM customer_list";
			Session session = this.sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list.addAll(query.list());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void buildSQL(Map<String, String> paramMap, StringBuffer sql, String tb1, String tb2) {
		if(null != paramMap) {
			Set<Entry<String, String>> entrySet = paramMap.entrySet();
			if(null != entrySet) {
				Iterator<Entry<String, String>> iter = entrySet.iterator();
				Entry<String, String> entry = null;
				String key = null;
				String value = null;
				String column = null;
				String flag = null;
				while(iter.hasNext()) {
					entry = iter.next();
					key = entry.getKey();
					try {
						column = key.substring(0, key.lastIndexOf("_"));
						flag = key.substring(key.lastIndexOf("_")+1);
					} catch(Exception ex) {
						column = key;
					}
					
					value = entry.getValue();
					if(CodeUtil.isNotEmpty(key) && CodeUtil.isNotEmpty(value)) {
						if("companyId".equals(key)) {
							sql.append(" AND "+tb1+".company_id=:companyId");
						} else if("customerId".equals(key)) {
							sql.append(" AND "+tb1+".customer_id=:customerId");
						} else if("articleId".equals(key)) {
							sql.append(" AND "+tb2+".article_id=:articleId");
						} else if("formatId".equals(key)) {
							sql.append(" AND "+tb2+".format_id=:formatId");
						} else if(CodeUtil.isNotEmpty(flag)) {
							if(flag.indexOf("startDate") >= 0) {
								sql.append(" AND "+column+" >= TO_DATE(:"+key+", 'yyyy-mm-dd')");
							} else if(flag.indexOf("endDate") >= 0) {
								sql.append(" AND "+column+" <= TO_DATE(:"+key+", 'yyyy-mm-dd')");
							}
						} else {
							sql.append(" AND type=:type");
						}
					}
				}
			}
		}
	}
	
	public SQLQuery buildSQLQuery(Map<String, String> paramMap, Session session, String sql) {
		SQLQuery query = session.createSQLQuery(sql.toString());
		if(null != paramMap) {
			Set<Entry<String, String>> entrySet = paramMap.entrySet();
			if(null != entrySet) {
				Iterator<Entry<String, String>> iter = entrySet.iterator();
				Entry<String, String> entry = null;
				String key = null;
				String value = null;
				while(iter.hasNext()) {
					entry = iter.next();
					key = entry.getKey();
					value = entry.getValue();
					if(CodeUtil.isNotEmpty(key) && CodeUtil.isNotEmpty(value)) {
						if("companyId".equals(key) || "articleId".equals(key) || "formatId".equals(key)
								|| "customerId".equals(key)) {
							query.setParameter(key, CodeUtil.parseLong(value));
						} else {
							query.setParameter(key, value);
						}
					}
				}
			}
		}
		return query;
	}
	
	public static void main(String[] args) {
		String key = "abcddd";
		System.out.println(key.substring(0, key.lastIndexOf("_")));
	}
}
