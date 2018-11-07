package com.linuslan.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static String parseDateToStr(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date parseStrToDate(String dateStr, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	/**
	 * ͨ�����ʱ���������ȡ��ʱ�����ڵ���
	 * @param date
	 * @return
	 */
	public static int getWeekByDate(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(2);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * �õ�ĳ���еĵڼ��ܵ���һ����
	 * @param year
	 * @param weeks
	 * @return
	 */
	public static Date getMondayByWeeks(int year, int weeks) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(2);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, weeks);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}
	
	/**
	 * �õ�ĳ���еĵڼ��ܵ���������
	 * @param year
	 * @param weeks
	 * @return
	 */
	public static Date getSundayByWeeks(int year, int weeks) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(2);
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, weeks);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}
	
	/**
	 * �õ�ĳ�µ��������
	 * ��6����30�죬��õ��ľ���30��5����31�죬��õ��ľ���31
	 * @param date
	 * @return
	 */
	public static int getMaxDayOfMonth(String date) {
		Date d = null;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			calendar.setTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * �õ�ĳ�µ��������
	 * ��6����30�죬��õ��ľ���30��5����31�죬��õ��ľ���31
	 * @param date
	 * @return
	 */
	public static int getMaxDayOfMonth(String date, String format) {
		Date d = null;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		try {
			d = new SimpleDateFormat(format).parse(date);
			calendar.setTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * �õ�ĳ�µ���С��������1��
	 * @param date
	 * @return
	 */
	public static int getMinDayOfMonth(String date) {
		Date d = null;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			calendar.setTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * �õ�ĳ�µ���С��������1��
	 * @param date
	 * @return
	 */
	public static int getMinDayOfMonth(String date, String format) {
		Date d = null;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		try {
			d = new SimpleDateFormat(format).parse(date);
			calendar.setTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * �õ����
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		int year = 0;
		year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		return year;
	}
	
	/**
	 * �õ��·�
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		int month = 0;
		month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		return month;
	}
	
	/**
	 * �õ�����
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		int month = 0;
		month = Integer.parseInt(new SimpleDateFormat("dd").format(date));
		return month;
	}
	
	/**
	 * �õ����
	 * @param date
	 * @return
	 */
	public static int getYear(String date) {
		int year = 0;
		String[] dateArr = date.split("-");
		year = Integer.parseInt(dateArr[0]);
		return year;
	}
	
	/**
	 * �õ��·�
	 * @param date
	 * @return
	 */
	public static int getMonth(String date) {
		int month = 0;
		String[] dateArr = date.split("-");
		month = Integer.parseInt(dateArr[1]);
		return month;
	}
	
	/**
	 * �õ�����
	 * @param date
	 * @return
	 */
	public static int getDay(String date) {
		int month = 0;
		String[] dateArr = date.split("-");
		month = Integer.parseInt(dateArr[2]);
		return month;
	}
	
	/**
	 * �õ������������ĺ�����
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDiffMillis(Date startDate, Date endDate) {
		long diffMillis = 0;
		
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		long startMillis = startDate.getTime();
		long endMillis = endDate.getTime();
		diffMillis = endMillis - startMillis;
		return diffMillis;
	}
	
	/**
	 * �õ������������ĺ�����
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDiffMillisNoConvert(Date startDate, Date endDate) {
		long diffMillis = 0;
		
		long startMillis = startDate.getTime();
		long endMillis = endDate.getTime();
		diffMillis = endMillis - startMillis;
		return diffMillis;
	}

	/**
	 * �õ�����������������
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffYear(Date startDate, Date endDate) {
		int diff = 0;
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startYear = start.get(Calendar.YEAR);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int endYear = end.get(Calendar.YEAR);
		diff = endYear - startYear;
		return diff;
	}
	
	
	/**
	 * �õ�����������������
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffMonth(Date startDate, Date endDate) {
		int diff = 0;
		
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startMonth = start.get(Calendar.MONTH);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int endMonth = end.get(Calendar.MONTH);
		
		diff = endMonth - startMonth;
		return diff;
	}
	
	/**
	 * �õ�����������������
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffDay(Date startDate, Date endDate) {
		int diff = 0;
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startDay = start.get(Calendar.DATE);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int endDay = end.get(Calendar.DATE);
		
		diff = endDay - startDay;
		return diff;
	}
	
	/**
	 * �õ�������������Сʱ��
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffHours(Date startDate, Date endDate) {
		int diff = 0;
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startHours = start.get(Calendar.HOUR_OF_DAY);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int endHours = end.get(Calendar.HOUR_OF_DAY);
		
		diff = endHours - startHours;
		return diff;
	}
	
	/**
	 * �õ����������ķ�����
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffMinutes(Date startDate, Date endDate) {
		int diff = 0;
		/*
		 * �����Ľ���ʱ��С�ڿ�ʼʱ�䣬�򽫿�ʼʱ��ͽ���ʱ��Ե�
		 */
		if(endDate.before(startDate)) {
			Date tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startMinutes = start.get(Calendar.MINUTE);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int endMinutes = end.get(Calendar.MINUTE);
		
		diff = endMinutes - startMinutes;
		return diff;
	}
	
	/**
	 * ͨ�������õ�ʱ��
	 * @param millis
	 * @return
	 */
	public static Date getDateByMillisMultipliedThousand(Long millis) {
		Date date = null;
		if(null != millis && 0l != (long)millis) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis*1000);
			date = calendar.getTime();
		}
		return date;
	}
	
	/**
	 * �õ�ʣ��ʱ��
	 * @param startDate ��ʽΪyyyy-MNM-dd
	 * @param endDate ��ʽΪyyyy-MNM-dd
	 * @return
	 */
	public static String getDiffDate(Date startDate, Date endDate) {
		String diffDate = "";
		if(startDate.before(endDate) || startDate.equals(endDate)) {

			int startYear = DateUtil.getYear(startDate);
			int startMonth = DateUtil.getMonth(startDate);
			int startDay = DateUtil.getDay(startDate);
			int endYear = DateUtil.getYear(endDate);
			int endMonth = DateUtil.getMonth(endDate);
			int endDay = DateUtil.getDay(endDate);
			int diffYear = endYear - startYear;
			int diffDay = endDay - startDay;
			int diffMonth = endMonth - startMonth;
			int leftMonth = 0;
			if(diffYear > 0) {
				leftMonth = 12 - startMonth;
				int leftDay = 0;
				/*
				 * �õ��տ�ʼ�·ݵ�ʣ������
				 */
				String startdate = startYear + "-" + startMonth;
				int maxDay = DateUtil.getMaxDayOfMonth(startdate, "yyyy-MM");
				leftDay = maxDay - startDay;
				
				
				/*
				 * �õ��տ�ʼ��ݵ�ʣ������
				 */
				for(int i = startMonth+1; i <= 12; i ++) {
					String date = startYear + "-" + i;
					leftDay += DateUtil.getMaxDayOfMonth(date, "yyyy-MM");
				}
				
				for(int i = startYear + 1; i < endYear; i ++) {
					leftMonth += 12;
					for(int j = 1; j <= 12; j ++) {
						String date = i+"-"+j;
						leftDay += DateUtil.getMaxDayOfMonth(date, "yyyy-MM");
					}
				}
				
				String enddate = endYear + "-" + endMonth;
				maxDay = DateUtil.getMaxDayOfMonth(enddate, "yyyy-MM");
				leftDay += endDay;
				
				/*
				 * �õ�������ݵ�ʣ������
				 */
				for(int i = 1; i <= endMonth - 1; i ++) {
					String date = endYear + "-" + i;
					leftDay += DateUtil.getMaxDayOfMonth(date, "yyyy-MM");
				}
				
				//����ʣ����·�
				leftMonth += endMonth;
				diffYear = leftMonth / 12;
				diffMonth = leftMonth - diffYear*12;
				if(diffYear > 0) {
					if(diffMonth > 0) {
						diffDate = diffYear+"��"+diffMonth+"���£���"+leftDay+"�죩";
					} else {
						diffDate = diffYear+"�꣨��"+leftDay+"�죩";
					}
				} else {
					if(diffMonth > 0) {
						diffDate = diffMonth+"���£���"+leftDay+"�죩";
					} else {
						diffDate = leftDay+"��";
					}
				}
			} else {
				int leftDay = 0;
				/*
				 * ͬһ�겻ͬ��
				 * ͬһ��ͬһ��
				 */
				if(diffMonth > 0) {
					/*
					 * �õ��տ�ʼ�·ݵ�ʣ������
					 */
					String startdate = startYear + "-" + startMonth;
					int maxDay = DateUtil.getMaxDayOfMonth(startdate, "yyyy-MM");
					leftDay = maxDay - startDay;
					for(int i = startMonth+1; i < endMonth; i ++) {
						String date = startYear + "-" + i;
						leftDay += DateUtil.getMaxDayOfMonth(date, "yyyy-MM");
					}
					
					leftDay += endDay;
					
					//����ʣ����·�
					leftMonth += endMonth - startMonth;
					diffYear = leftMonth / 12;
					diffMonth = leftMonth - diffYear*12;
					if(diffYear > 0) {
						if(diffMonth > 0) {
							diffDate = diffYear+"��"+diffMonth+"���£���"+leftDay+"�죩";
						} else {
							diffDate = diffYear+"�꣨��"+leftDay+"�죩";
						}
					} else {
						if(diffMonth > 0) {
							diffDate = diffMonth+"���£���"+leftDay+"�죩";
						} else {
							diffDate = leftDay+"��";
						}
					}
				} else {
					/*
					 * �õ��տ�ʼ�·ݵ�ʣ������
					 */
					leftDay = endDay - startDay;
					
					//����ʣ����·�
					diffYear = leftMonth / 12;
					diffMonth = leftMonth - diffYear*12;
					if(diffYear > 0) {
						if(diffMonth > 0) {
							diffDate = diffYear+"��"+diffMonth+"���£���"+leftDay+"�죩";
						} else {
							diffDate = diffYear+"�꣨��"+leftDay+"�죩";
						}
					} else {
						if(diffMonth > 0) {
							diffDate = diffMonth+"���£���"+leftDay+"�죩";
						} else {
							diffDate = leftDay+"��";
						}
					}
				}
			}
			
		}
		return diffDate;
	}
	/*
	public static java.sql.Date getDateByMillis(Long millis) {
		java.sql.Date date = null;
	}*/
}
