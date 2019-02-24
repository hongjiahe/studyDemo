package com.hohe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chenxiaoguang
 * 2011-11-02 version 1
 */
public class DateUtil {
	
	public static String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
	public static String FORMAT_yyyy_MM = "yyyy-MM";
	public static String FORMAT_yyyy = "yyyy";
	public static String FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static String FORMAT_yyyyMMdd = "yyyyMMdd";
	public static String FORMAT_MM = "MM";
	public static String FORMAT_dd = "dd";
	
	public static String convertDateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String formatDateStr(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		return sdf.format(date);
	}
	
	public static Date convertStringToDate(String dateString, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
     * nDay为推迟的周数，1本周，-1向前推迟一周，2下周...
     * weekDay星期几, sunday=0, monday=1...saturday=6
     */
    public static String getCalDatebyWeek(int nDay, int weekDay){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, nDay*7);
    	
    	cal.set(Calendar.DAY_OF_WEEK, weekDay);//eg:Calendar.MONDAY
    	return DateUtil.convertDateToString(cal.getTime(), DateUtil.FORMAT_yyyy_MM_dd);
    }
    public static String[] getLastMonthEndDate(Date date, int nMon){
    	Calendar cal = Calendar.getInstance();
    	if(date != null){
    		cal.setTime(date);
    	}
    	cal.add(Calendar.MONTH, nMon);
    	int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    	
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 00, 00, 00);
    	String startDate = DateUtil.convertDateToString(cal.getTime(), DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), maxDay, 23, 59, 59);
    	String endDate = DateUtil.convertDateToString(cal.getTime(), DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
    	return new String[]{startDate, endDate};
    }
    public static String getLastByDay(int day){
    	Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);//eg:-30
        
		return DateUtil.convertDateToString(calendar.getTime(), DateUtil.FORMAT_yyyy_MM_dd);
		
    }
	public String converLongToDate(Long ms) {
	    Date date = new Date(ms);
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    String time = format.format(date);
		return time;
	}
	
	
	/**
	 * 2日期时间差
	 */
	public static long getDatesLag(String subtrahendStr, Date minuend){
		return (DateUtil.convertStringToDate(subtrahendStr, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss).getTime()-minuend.getTime());
	}
	
	public static long getDatesLag(Date fdate, Date minuend){
		return (fdate.getTime()-minuend.getTime());
	}
	
	public static int getDatesLag(String subtrahendStr, String endStr){
		Long time = (DateUtil.convertStringToDate(subtrahendStr, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss).getTime()-DateUtil.convertStringToDate(endStr, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss).getTime());
		Long days = time / (1000 * 60 * 60 * 24);
		return days.intValue();
	}
	
	public static int getDatesDays(String subtrahendStr, String endStr){
		Long time = (DateUtil.convertStringToDate(subtrahendStr, DateUtil.FORMAT_yyyy_MM_dd).getTime()-DateUtil.convertStringToDate(endStr, DateUtil.FORMAT_yyyy_MM_dd).getTime());
		Long days = time / (1000 * 60 * 60 * 24);
		return days.intValue();
	}
	/**
	 * 是否在指定时间之间
	 */
	public static boolean isInDatesLag(String startDate, String endDate, Date now){
		if(getDatesLag(startDate, now)<0 && getDatesLag(endDate, now)>0){
			return true;
		}
		return false;
	}
	public static boolean isBeforeDate(String compareDate, Date now){
		return now.before(DateUtil.convertStringToDate(compareDate, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
		//return getDatesLag(compareDate, now)>0?true:false;
	}
	public static boolean isAfterDate(String compareDate, Date now){
		return now.after(DateUtil.convertStringToDate(compareDate, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
		//return getDatesLag(compareDate, now)<0?true:false;
	}
	public static boolean isBeforeDate(String compareDate, Date now, int second){
		return getDatesLag(compareDate, now)>-second*1000?true:false;
	}
	public static boolean isAfterDate(String compareDate, Date now, int second){
		return getDatesLag(compareDate, now)<-second*1000?true:false;
	}
	public static boolean isSameWeek(String compareDate, Date now){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.convertStringToDate(compareDate, DateUtil.FORMAT_yyyy_MM_dd));
		cal2.setTime(now);
		if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
			return true;
		}
		return false;
	}
	
	public static Date getDateAfterTime(String dateStr, int second) {
		Date date = DateUtil.convertStringToDate(dateStr, DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second);
		return calendar.getTime();
	}
	public static Date getDateAfterDates(Date date, int dates) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + dates);
		return calendar.getTime();
	}
	public static Date getDateAfterMonths(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
		return calendar.getTime();
	}
	public static String removeMs(String dateStr){
		if(!StringUtil.isBlank(dateStr)){
			int i = dateStr.indexOf(".");
			if(i > -1){
				dateStr = dateStr.substring(0, i);
			}
		}
		return dateStr;
	}
	/**
	 * 0=sunday
	 */
	public static int getWeekDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		weekDay = weekDay - 1;
		return weekDay;
	}
	/**
	 * 返回两个日期间的差异天数
	 * 
	 * @param date1 大
	 * @param date2  小
	 * @return 如：5秒，20分钟，1小时51分钟，2天4小时，大于1个月(参照日期与比较日期之间的天数差异，正数表示参照日期在比较日期之后，0表示两个日期同天，负数表示参照日期在比较日期之前)
	 */
	public static String dayDiff3(Date date1, Date date2) {
		long diff = date1.getTime() - date2.getTime();
		if(diff <= 0){
			return "今日已结束";
		}
		diff = diff / 1000;
		if(diff / 60 == 0){
			return "还有"+(diff % 60) + "秒钟结束";
		}else if(diff / 3600 == 0){
			return "还有"+(diff % 3600 / 60) + "分钟结束";
		}else if(diff / (3600 * 24) == 0){
			int h = (int) (diff / 3600 );
			int m = (int) ((diff % 3600) / 60);
			return "还有"+h + "小时" + m + "分钟结束";
		}else if(diff / (3600 * 24) < 30){
			int d = (int) (diff / (3600 * 24));
			int h = (int) (diff % (3600 * 24) / 3600 );
			return "还有"+d + "天" + h + "小时结束";
		}else{
			return "还有"+"1个月以上结束";
		}
	}
	/**
	 * 获取自然本月的最后一天
	 */
	public static Date getMonthLastDay(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}
	/**
	 * 获取自然本月的第一天
	 */
	public static Date getMonthFirstDay(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	/**
	 * 增加|减少n天
	 * @param yyyyMMdd
	 * @param n
	 * @return
	 */
	public static String addDay(String yyyyMMdd, int n) {   
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(yyyyMMdd));
			cd.add(Calendar.DATE, n);//增加n天
			return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
	}   
	 
	//增加|减少 n月
	public static String addMonth(String yyyyMMdd, int n) {   
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar cd = Calendar.getInstance();
	        cd.setTime(sdf.parse(yyyyMMdd));
	        cd.add(Calendar.MONTH, n);//增加n个月
	        return sdf.format(cd.getTime());
	    } catch (Exception e) {
	        return null;
	    }
    }   

	public static String addMonthFormat(String yyyyMMdd, int n, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(yyyyMMdd));
			cd.add(Calendar.MONTH, n);//增加n天
			return sdf.format(cd.getTime());
		} catch (Exception e) {
			return null;
		}
	}   
}
