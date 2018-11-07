package com.linuslan.oa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateProcessor implements JsonValueProcessor {
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private SimpleDateFormat format;

	public DateProcessor(String pattern) {
		try {
			format = new SimpleDateFormat(pattern);
		} catch(Exception ex) {
			format = new SimpleDateFormat(DateProcessor.DEFAULT_DATE_PATTERN);
		}
	}
	
	public DateProcessor() {
		format = new SimpleDateFormat(DateProcessor.DEFAULT_DATE_PATTERN);
	}
	
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return format(arg0);
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return format(arg1);
	}
	
	public Object format(Object obj) {
		if(null != obj) {
			Date date = (Date) obj;
			return format.format(date);
		} else {
			return null;
		}
	}

	public SimpleDateFormat getFormat() {
		return format;
	}

	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

	public static String getDefaultDatePattern() {
		return DEFAULT_DATE_PATTERN;
	}
}
