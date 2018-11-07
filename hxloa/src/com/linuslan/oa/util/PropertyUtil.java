package com.linuslan.oa.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtil {
	
	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	
	private static String configPath = "config/config.properties";
	
	/**
	 * 通过路径获取配置文件信息
	 * @param path
	 * @return
	 */
	private static Properties readProperty(String path) throws RuntimeException {
		Properties props = null;
		try {
			InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(path);
			props = new Properties();
			props.load(is);
		} catch(Exception ex) {
			logger.error("读取禁止登陆用户异常");
			CodeUtil.throwRuntimeExcep("读取禁止登陆用户异常");
		}
		return props;
	}
	
	public static String getConfigPropertyValue(String key) throws RuntimeException {
		String value = "";
		try {
			Properties props = readProperty(configPath);
			value= props.getProperty(key);
		} catch(Exception ex) {
			value = "";
			CodeUtil.throwRuntimeExcep("读取config.properties文件的"+key+"属性出错，异常原因："+CodeUtil.getStackTrace(ex));
		}
		return value;
	}
}
