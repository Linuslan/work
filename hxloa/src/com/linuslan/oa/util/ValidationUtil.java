package com.linuslan.oa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
	
	/**
	 * 判断字符串是否只包含字母，数字或下划线
	 * @param text
	 * @return
	 */
	public static boolean isAlphaNumberic(String text) {
		boolean success = false;
		try {
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]+$");
			Matcher m = p.matcher(text);
			success = m.find();
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * 判断字符串是否只包含字母和数字
	 * @param text
	 * @return
	 */
	public static boolean isAlphaNumbericNoLine(String text) {
		boolean success = false;
		try {
			Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
			Matcher m = p.matcher(text);
			success = m.find();
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public static boolean isNumber(String text) {
		boolean success = false;
		try {
			Pattern p = Pattern.compile("^[0-9]+$");
			Matcher m = p.matcher(text);
			success = m.find();
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * 判断字符串是否只包含字母，数字，下划线或点号
	 * @param text
	 * @return
	 */
	public static boolean isURI(String text) {
		boolean success = false;
		try {
			Pattern p = Pattern.compile("^[a-zA-Z0-9_./\\-]+$");
			Matcher m = p.matcher(text);
			success = m.find();
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public static boolean isCellphone(String text) {
		boolean success = false;
		try {
			Pattern p = Pattern.compile("^(13|15|18)\\d{9}$");
			Matcher m = p.matcher(text);
			success = m.find();
		} catch(Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public static void main(String[] args) {
		String text = "/.11/-22kj222s__dfsdf.._sd11_./-";
		//String text = "15998749382";
		System.out.println(ValidationUtil.isURI(text));
	}
}
