package com.summer.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 */
public class DateUtils {

	/* 时间格式：yyyy-MM-dd */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/* 时间格式：yyyy-MM-dd HH:mm:ss */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取指定时间的上周星期天日期
	 * @return
	 */
	public static Date getLastWeekSunday(Date date) {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 将每周第一天设为星期一，默认是星期天
		cal.add(Calendar.WEEK_OF_MONTH, -1);// 周数减一，即上周
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 日子设为星期天
		return cal.getTime();
	}

	/**
	 * 获取指定时间的上周星期天日期
	 * @return
	 */
	public static Date getLastWeekSunday(String date) {
		try {
			Calendar cal = Calendar.getInstance(Locale.CHINA);
			SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
			cal.setTime(df.parse(date));
			cal.setFirstDayOfWeek(Calendar.MONDAY);// 将每周第一天设为星期一，默认是星期天
			cal.add(Calendar.WEEK_OF_MONTH, -1);// 周数减一，即上周
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 日子设为星期天
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前系统时间的上周星期天日期
	 * @return
	 */
	public static Date getSystemTimeLastWeekSunday() {
		return getLastWeekSunday(format(new Date()));
	}

	/**
	 * 获取指定时间的星期一与星期天
	 * @return
	 */
	public static Map<String, String> getWeekMonAndSun(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		Map<String, String> map = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		map.put("monday", df.format(cal.getTime()));
		// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		map.put("sunday", df.format(cal.getTime()));
		return map;
	}

	/**
	 * 获取指定时间的星期一与星期天
	 * @return {"monday":星期一;"sunday":星期天}
	 */
	public static Map<String, String> getWeekMonAndSun(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
			Map<String, String> map = new HashMap<String, String>();
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(date));
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
			map.put("monday", df.format(cal.getTime()));
			// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			// 增加一个星期，才是我们中国人理解的本周日的日期
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			map.put("sunday", df.format(cal.getTime()));
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前系统时间的星期一与星期天
	 * @return
	 */
	public static Map<String, String> getSystemTimeWeekMonAndSun() {
		return getWeekMonAndSun(new Date());
	}

	/***
	 * 获取当前系统时间(精确到秒)
	 * @return
	 */
	public static int getCurrentSecondTime() {
		long timeMillis = System.currentTimeMillis();
		long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
		int currentTime = (int) timeSeconds;
		return currentTime;
	}

	/**
	 * 时间转换
	 * @param date
	 * @return
	 */
	public static int data2UnixTimestamp(String date) {
		int time = 100;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
			Date parsedDate = dateFormat.parse(date);
			time = (int) (parsedDate.getTime() / 1000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 获取当日到周日的日期列表
	 * @return
	 */
	public static List<String> getDayList(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);// 年
		int month = cal.get(Calendar.MONTH) + 1;// 月
		int day = cal.get(Calendar.DATE);// 天
		int days = cal.getActualMaximum(Calendar.DATE);// 获取当月总天数
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 获取本周日
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		int sunday = cal.get(Calendar.DATE);
		if (sunday < day) {
			sunday = sunday + days;
		}
		List<String> dayList = new ArrayList<>();
		String strMonth = null;
		String strDay = null;
		for (int i = day; i <= sunday; i++) {
			if (i > days) {
				strMonth = (month + 1) > 10 ? ((month + 1) + "") : ("0" + (month + 1));
				strDay = (i % days) > 10 ? ((i % days) + "") : ("0" + (i % days));
			} else {
				strMonth = month > 10 ? (month + "") : ("0" + month);
				strDay = (i > 10) ? (i + "") : ("0" + i);
			}

			dayList.add(year + "-" + strMonth + "-" + strDay);
		}
		return dayList;
	}

	/**
	 * data转String
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * String转data
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date format(String date, String pattern) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * String转data
	 * @param date
	 * @return
	 */
	public static Date format(String date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * String转data
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * long转string
	 * @param expireTime
	 * @param pattern
	 * @return
	 */
	public static String format(Long expireTime, String pattern) {
		try {
			return format(new Date(expireTime), pattern);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Date转换为Timestamp
	 * @param date
	 * @param format
	 * @return
	 */
	public static Timestamp getDate2Timestamp(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String time = "";
		time = sdf.format(date);
		Timestamp ts = Timestamp.valueOf(time);

		return ts;
	}

	/**
	 * String转Timestamp
	 * @param str
	 * @return
	 */
	public static Timestamp getStrToTimestamp(String str) {
		Date date = format(str, YYYY_MM_DD_HH_MM_SS);
		Timestamp ts = DateUtils.getDate2Timestamp(date, YYYY_MM_DD_HH_MM_SS);
		return ts;
	}

	/**
	 * Timestamp转String
	 * @param timestamp
	 * @return
	 */
	public static String timestamp2String(Timestamp timestamp) {
		return timestamp2String(timestamp, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Timestamp转 String
	 * @param timestamp
	 * @param format
	 * @return
	 */
	public static String timestamp2String(Timestamp timestamp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(timestamp);
	}

	/**
	 * int转stirng
	 * @param stamp
	 * @return
	 */
	public static String timestampToHuman(int stamp) {
		Date date = new Date();
		date.setTime((long) stamp * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * 描述:获取上个月的最后一天.
	 * @return
	 */
	public static String getLastMaxMonthDate() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dft.format(calendar.getTime());
	}

	public static void main(String[] args) {
		System.out.println(getLastMaxMonthDate());
	}
}
