package com.wm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.courier_mana.common.vo.SearchVo;

public class SqlUtils {
	
	public static final int LENGTH_OF_DAY_IN_SECOND = 86400;
	
	public static String getTimeToLong(){
		
		return null;
	}
	
	/**
	 * 当前时间往前推多少天
	 * @param num   向前推的天数
	 * @return
	 */
	public static String getDateBefore(Integer num){
		num = num == null? 0:num;
		String time = "";
		try {
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate = new Date();
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			
			date.set(Calendar.DATE, date.get(Calendar.DATE) - num);
			Date endDate = dft.parse(dft.format(date.getTime()));
			time = endDate.getTime()+"";
			time = time.substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * 获取当月的第一天
	 * @return
	 */
	public static String getNowMonthBegin(){
		String month = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
			Calendar c = Calendar.getInstance();    
	        c.add(Calendar.MONTH, 0);
	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        String first = format.format(c.getTime());
	        month = format.parse(first).getTime() + "";
	        month = month.substring(0, 10);
		} catch (Exception e) {
		}
		return month;
	}
	
	/**
	 * 当前时间凌晨 0 点
	 * @return
	 */
	public static String getNowDayBegin(){
		String day = "";
		try {
			Date date = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = sdf.format(date);
			Date parse = sdf.parse(dateStr);
			day = parse.getTime()+"";
			day = parse.getTime()+"";
			day = day.substring(0, 10);
		} catch (ParseException e) {
		}
		return day;
	}
	
   /**
	* 取本周7天的第一天（周一的日期）
	*/
	public static String getNowWeekBegin() {
		
			String week = "";
			try {
				int mondayPlus;
				Calendar cd = Calendar.getInstance();
				//获得今天是一周的第几天，星期日是第一天，星期二是第二天......
				int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
				if (dayOfWeek == 1) {
				mondayPlus = 0;
				} else {
				mondayPlus = 1 - dayOfWeek;
				}
				GregorianCalendar currentDate = new GregorianCalendar();
				currentDate.add(GregorianCalendar.DATE, mondayPlus);
				Date monday = currentDate.getTime();
				DateFormat df = DateFormat.getDateInstance();
				String preMonday = df.format(monday);
				week = df.parse(preMonday).getTime() + "";
				week = week.substring(0,10);
			} catch (Exception e) {
			}
			return week;
	}
	
	public static void main(String[] args){System.out.println(getDateBySecond(1471858261));}
	
	/**
	 * 根据条件构造SQL中的时间条件
	 * @param searchVo
	 * @return
	 * @throws ParseException 
	 */
	public static String getTimeWhere4SQL(SearchVo searchVo){
		
		String timewhere = "";
		if("day".equals(searchVo.getTimeType()) || searchVo.getTimeType() == null){
			timewhere = " and o.create_time >= "+ getNowDayBegin() +" ";
		}else if("week".equals(searchVo.getTimeType())){
			timewhere = " and "+ getNowWeekBegin() +" <= o.create_time";
		}else if("month".equals(searchVo.getTimeType())){
			timewhere = " and o.create_time >= "+ getNowMonthBegin() +"";
		}else if("other".equals(searchVo.getTimeType())){
			if(searchVo.getBeginTime()!=null && searchVo.getBeginTime() !=0 && searchVo.getEndTime()!=null && searchVo.getEndTime() !=0){
				timewhere = " and o.create_time >= " + searchVo.getBeginTime();
				timewhere += " and o.create_time <= " + searchVo.getEndTime();
			}else if(searchVo.getBeginTime()!=null && searchVo.getBeginTime() !=0){
				timewhere = " and o.create_time >= "+ searchVo.getBeginTime();
			}else if(searchVo.getEndTime()!=null && searchVo.getEndTime() !=0){
				timewhere = " and o.create_time <= " + searchVo.getEndTime();
			}
		}
		return timewhere;
	}
	
	/**
	 * 根据条件构造SQL中的时间条件
	 * {此方法中未对beginTime, endTime进行非空监测}
	 * @param timeType	查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @param field		要套用条件的字段名
	 * @return
	 */
	public static String getTimeWhere4SQL(SearchVo timeType, String field){
		
		String timewhere = "";
		if("week".equals(timeType.getTimeType())){
			timewhere = " AND "+ field +" >= " + getTheBeginningOfThisWeek() + " ";
		}else if("month".equals(timeType.getTimeType())){
			timewhere = " AND " + field + " >= "+ getTheBeginningOfThisMonth() +" ";
		}else if("other".equals(timeType.getTimeType())){
			timewhere = " AND "+ field +" >= " + timeType.getBeginTime() + " "
						+ " AND "+ field +" < " + timeType.getEndTime() + " ";
		}else{
			timewhere = " AND " + field + " >= "+ getTheBeginningOfToday() +" ";
		}
		return timewhere;
	}
	
	/**
	 * 当前时间往前推N天   时间戳 SQL 语句    
	 * @param num
	 * @return
	 */
	public static String getBeforeTimeNum(Integer num) {
		String sql = "AND o.create_time >= " + getDateBefore(num);
		return sql;
	}
	
	/**
	 * 根据搜索条件获取SQL中相应的日期值
	 * @author hyj
	 * @param timeType	时间类型
	 * @param field		字段名
	 * @return
	 */
	public static String getDateTimeWhere4SQL(SearchVo timeType, String field){
		String result = "";
		if("week".equals(timeType.getTimeType())){
			result = " AND "+ field +" >= '" + getTheBeginningDateOfThisWeek() + "' ";
		}else if("month".equals(timeType.getTimeType())){
			result = " AND " + field + " >= '"+ getTheBeginningDateOfThisMonth() +"' ";
		}else if("other".equals(timeType.getTimeType())){
			result = " AND "+ field +" >= '" + getDateBySecond(timeType.getBeginTime()) + "' "
						+ " AND "+ field +" < '" + getDateBySecond(timeType.getEndTime()) + "' ";
		}else{
			result = " AND " + field + " >= CURDATE() ";
		}
		return result;
	}
	
	/**(OvO)
	 * 获得当日的开端
	 * @return	当日开端的秒数
	 */
	private static int getTheBeginningOfToday(){
		int result = (int)(System.currentTimeMillis()/1000);
		result -=  (result + 28800) % 86400;
		return result;
	}
	
	/**(OvO)
	 * 获得本周的开端
	 * @return	本周开端的秒数
	 */
	private static int getTheBeginningOfThisWeek(){
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek > 1){
			return getTheBeginningOfToday() - (dayOfWeek - 2) * LENGTH_OF_DAY_IN_SECOND;
		}else{
			return getTheBeginningOfToday() - 6 * LENGTH_OF_DAY_IN_SECOND;
		}
	}
	
	/**
	 * 获得本周的开端(日期)
	 * @author hyj
	 * @return	本周开端的日期
	 */
	private static String getTheBeginningDateOfThisWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek > 1){
			calendar.add(Calendar.DATE, 2 - dayOfWeek);
		}else{
			calendar.add(Calendar.DATE, - 6);
		}
		Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**(OvO)
	 * 获得本月的开端(日期)
	 * @author hyj
	 * @return	本月开端的日期
	 */
	private static String getTheBeginningDateOfThisMonth(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(calendar.getTime());
	}
	
	/**(OvO)
	 * 获得本月的开端
	 * @return	本月开端的秒数
	 */
	private static int getTheBeginningOfThisMonth(){
		Calendar calendar = Calendar.getInstance();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		return getTheBeginningOfToday() - (dayOfMonth - 1) * LENGTH_OF_DAY_IN_SECOND;
	}
	
	private static String getDateBySecond(int second){
		long millisecond = second * 1000l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(millisecond));
	}
}
