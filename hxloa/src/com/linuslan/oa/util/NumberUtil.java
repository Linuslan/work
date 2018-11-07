package com.linuslan.oa.util;

import java.text.DecimalFormat;

public class NumberUtil {
	
	/**
	 * ��ʽ������
	 * @param number
	 * @param format
	 * @return
	 */
	public static String format(double number, String format) {
		String num = "0";
		try {
			DecimalFormat df = new DecimalFormat(format);
			num = df.format(number);
			if(num.equals("0.0")) {
				num = "0";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return num;
	}
}
