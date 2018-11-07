package com.linuslan.oa.util;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SerialNoFactory extends ApplicationObjectSupport {
	
	/*public static String getGroupNodeUniqueID() {
		String serialNo = "";
		
		String formatStr = "yyMMdd" + buildRandom(5);
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				formatStr);
		java.util.Date currentTime = new java.util.Date();
		serialNo = formatter.format(currentTime);
		
		return serialNo;
	}*/
	
	/**
	 * 获取序列号，或者订单号
	 * **/
	public static String getGroupNodeUniqueID(String className) {
		String serialNo = "";
		//SerialNoFactory fa = new SerialNoFactory();
		//ApplicationContext context = fa.getApplicationContext();
		try {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			SessionFactory sessionFactory = (SessionFactory) wac.getBean("sessionFactory");
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			String dateStr = DateUtil.parseDateToStr(new Date(), "yyyyMMdd");
			String hql = "SELECT MAX(t.serialNo) FROM "+className+" t WHERE t.serialNo LIKE :date";
			Query query = session.createQuery(hql);
			query.setParameter("date", dateStr+"%");
			try {
				String preSerialNo = query.uniqueResult().toString();
				String indexStr = preSerialNo.substring(8);
				int index = Integer.parseInt(indexStr);
				serialNo = dateStr+SerialNoFactory.insertZero(++index, 4);
			} catch(Exception ex) {
				ex.printStackTrace();
				serialNo = dateStr+"0001";
			}
			session.getTransaction().commit();
			session.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			String formatStr = "yyMMdd" + buildRandom(5);
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					formatStr);
			java.util.Date currentTime = new java.util.Date();
			serialNo = formatter.format(currentTime);
		}
		
		return serialNo;
	}
	
	public static String getGroupNodeUniqueID2() {
		String formatStr = "yyMMdd" + buildRandom(5);
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				formatStr);
		java.util.Date currentTime = new java.util.Date();
		String sid = formatter.format(currentTime);
		return sid;
	}

	/**
	 * ȡ��һ��ָ�����ȴ�С�����������.
	 * 
	 * @param length
	 *            int �趨��ȡ�������ĳ��ȡ�lengthС��11
	 * @return int ������ɵ������
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	public static String insertZero(int num, int len) {
		String result = num + "";
		for(int i = result.length(); i < len; i ++) {
			result = "0"+result;
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.parseInt("0014"));
		String dateStr = DateUtil.parseDateToStr(new Date(), "yyyyMMddHH");
		System.out.println(dateStr.substring(8));
		
		System.out.println(SerialNoFactory.insertZero(18, 4));
	}
}
