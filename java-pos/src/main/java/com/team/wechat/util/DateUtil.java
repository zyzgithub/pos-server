package com.team.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf1  = new SimpleDateFormat("yyyyMM");
	public static String getDate() {
		return sdf.format(new Date());
	}
	public static String getyyyyMM() {
		
		return sdf1.format(new Date());
	}
	
	/**
	 * 算出本周的时间区间
	 * @return
	 */
	public static Map<String,String> getWeek(){
		Map<String,String> map = new HashMap<String, String>();
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
		String start = df.format(cal.getTime());
		//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		
		String end = df.format(cal.getTime());
		map.put("first", start);
		map.put("last", end);
		return map;
	}
	
	/**
	 * 算出本月时间区间
	 * @return
	 */
	public static Map<String,String> getMonth(){
		Map<String,String> map = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 //获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        map.put("first", first);
        map.put("last", last);
		return map;
	}
	
}
