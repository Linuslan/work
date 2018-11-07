package com.linuslan.oa.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CodeUtil {
	public static void throwExcep(String msg) throws Exception {
		throw new Exception(msg);
	}
	
	public static void throwRuntimeExcep(String msg) throws RuntimeException {
		throw new RuntimeException(msg);
	}
	
	public static void throwRuntimeExcep(Exception ex) throws RuntimeException {
		throw new RuntimeException(CodeUtil.getStackTrace(ex));
	}
	
	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	/**
	 * 检测字符串是否为空
	 * 为空返回true
	 * 不为空返回false
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String param) {
		boolean success = true;
		if(null == param || "".equals(param.trim())) {
			success = true;
		} else {
			success = false;
		}
		return success;
	}
	
	/**
	 * 检测字符串是否不为空
	 * 不为空返回true
	 * 为空返回false
	 * @param param
	 * @return
	 */
	public static boolean isNotEmpty(String param) {
		boolean success = true;
		if(null != param && !"".equals(param.trim())) {
			success = true;
		} else {
			success = false;
		}
		return success;
	}
	
	/**
	 * 截取类名，首字母为小写
	 * @param clz
	 * @return
	 */
	public static String getClassName(Class clz) {
		String className = clz.getSimpleName();
		if(1 < className.length()) {
			className = className.substring(0, 1).toLowerCase()+className.substring(1);
		} else {
			className = className.toLowerCase();
		}
		return className;
	}
	
	public static String parseString(Object obj) {
		try {
			if(null != obj) {
				return String.valueOf(obj);
			} else {
				return "";
			}
		} catch(Exception ex) {
			return "";
		}
	}
	
	/**
	 * 从对象中取出属性名为key的属性值
	 * @param object 对象
	 * @param key 对象的属性名
	 * @return
	 */
	public static String parseString(Object object, String key) {
		if(null == object) {
			return "";
		} else {
			return CodeUtil.parseString(BeanUtil.getValue(object, key));
		}
		
	}
	
	public static Long parseLong(Object obj) {
		try {
			return Long.parseLong(obj.toString());
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static Integer parseInt(Object obj) {
		try {
			return Integer.parseInt(obj.toString());
		} catch(Exception ex) {
			return 0;
		}
	}
	
	public static Integer parseInteger(Object obj) {
		try {
			return Integer.parseInt(obj.toString());
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static Double parseDouble(Object obj) {
		try {
			return Double.parseDouble(obj.toString());
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static Double parsedouble(Object obj) {
		try {
			return Double.parseDouble(obj.toString());
		} catch(Exception ex) {
			return 0.0d;
		}
	}
	
	public static Float parseFloat(Object obj) {
		try {
			return Float.parseFloat(obj.toString());
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static Float parsefloat(Object obj) {
		try {
			return Float.parseFloat(obj.toString());
		} catch(Exception ex) {
			return 0f;
		}
	}
	
	public static BigDecimal parseBigDecimal(Object obj) {
		try {
			DecimalFormat df = new DecimalFormat("#0.00");
			Double dbl = CodeUtil.parseDouble(obj);
			BigDecimal decimal = new BigDecimal(df.format(dbl));
			return decimal;
		} catch(Exception ex) {
			ex.printStackTrace();
			return new BigDecimal("0.00");
		}
	}
	
	public static Object parseNumberic(Object value, Class clz) {
		Object obj = null;
		try {
			if(value.getClass() == String.class) {
				if(clz == Long.class || "long".equals(clz.getName())) {
					obj = CodeUtil.parseLong(value);
				} else if(clz == Integer.class || "int".equals(clz.getName())) {
					obj = CodeUtil.parseInt(value);
				} else if(clz == Double.class || "double".equals(clz.getName())) {
					obj = CodeUtil.parseDouble(value);
				} else if(clz == Float.class || "float".equals(clz.getName())) {
					obj = CodeUtil.parseFloat(value);
				} else if(clz == BigDecimal.class) {
					obj = CodeUtil.parseBigDecimal(value);
				}
			} else {
				obj = value;
			}
		} catch(Exception ex) {
			
		}
		return obj;
	}
	
	public static BigDecimal initBigDecimal(BigDecimal decimal) {
		return decimal == null ? new BigDecimal("0") : decimal;
	}
	
	public static void main(String[] args) {
		int obj = 900;
		System.out.println(CodeUtil.parseBigDecimal(obj).subtract(CodeUtil.parseBigDecimal("900")).compareTo(new BigDecimal("0")));
	}
}
