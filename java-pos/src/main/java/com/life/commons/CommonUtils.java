package com.life.commons;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author chinahuangxc
 */
public class CommonUtils {

    /**
     * 日期格式 -- "年-月" 如：2001-01
     */
    public static final String Y_M = "yyyy-MM";
    /**
     * 日期格式 -- "年-月-日" 如：2001-01-01
     */
    public static final String Y_M_D = "yyyy-MM-dd";
    /**
     * 日期格式 -- "年-月-日 时" 如：2001-01-01 20
     */
    public static final String Y_M_D_HH = "yyyy-MM-dd HH";
    /**
     * 日期格式 -- "年-月-日 时:分" 如：2001-01-01 20:20
     */
    public static final String Y_M_D_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式 -- "年-月-日 时:分:秒" 如：2001-01-01 20:20:00
     */
    public static final String Y_M_D_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式(中国格式) -- "点分" 如：9点10分
     */
    public static final String HH_MM_CH = "HH点mm分";

    // 1分钟
    public static final long MINUTE_MILLISECOND_TIME = 1000L * 60L;
    // 半小时
    public static final long HALF_AN_HOURS_MILLISECOND_TIME = 1000L * 60L * 30L;
    // 1个小时
    public static final long HOURS_MILLISECOND_TIME = 1000L * 60L * 60L;
    // 1天
    public static final long DAY_MILLISECOND_TIME = 1000L * 60L * 60L * 24L;
    // 1天
    public static final long TIMEDAY = TimeUnit.DAYS.toMillis(1);
    public static final long TIME_ZONE_OFFSET_MILLIS = TimeZone.getDefault().getRawOffset();
    // 一个固定内存的对象 用于某些Map场景 当value值为固定对象且没有存在意义时 可直接使用该对象 主要是为了节省内存空间
    public static final Object STATIC_FINAl = new Object();
    
    public static final DateTimeFormatter dateTimeFormater = DateTimeFormat.forPattern(Y_M_D_HH_MM_SS);

    /**
     * 今天凌晨相对应的毫秒数
     */
    public static long getTodayEarlyMorningMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * <pre>
     * 获取指定区间的一个随机值
     * 注意：该区间包含了最小值，但不包含最大值
     * </pre>
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static long random(long min, long max) {
        return (long) (Math.random() * (max - min) + min);
    }

    /**
     * <pre>
     * 纯随时获取一个值
     * <b>注意：</b>参数必须为正数
     * 该方法返回的数值不包括临界值[0~~(max-1)]
     * </pre>
     */
    public static int random(int max) {
        return DefineRandom.random(max);
    }

    public static String dateFormat(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    public static String dateFormatHours(long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public static long dateFormatToLong(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime).getTime();
        } catch (ParseException ex) {
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static long hoursMillisecond(float i) {
        return (long) (i * HOURS_MILLISECOND_TIME);
    }

    public static long dayMillisecond(int i) {
        return i * DAY_MILLISECOND_TIME;
    }

    public static int dayOfYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static String currentMonthOfCH(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int month = calendar.get(Calendar.MONTH) + 1;

        return (month < 10 ? "0" + month : month) + "月";
    }

    public static String currentDayOfCH(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (day < 10 ? "0" + day : day) + "日";
    }

    /**
     * 指定时间的小时(24小时制)
     */
    public static int currentHours(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static long getMinuteMillisecondTime(int minute) {
        return MINUTE_MILLISECOND_TIME * minute;
    }
    
    /**
     * 获取指定时间的下一个工作日同一时间段的对应毫秒数
     * 
     * @param time
     * @return
     */
	public static long nextWorkingDay(long time) {
		int dayOfWeek = dayOfWeekForCh(time);
		if (dayOfWeek == 6) {
			// 星期六
			time += TimeUnit.DAYS.toMillis(2);
		}
		if (dayOfWeek == 7) {
			// 星期天
			time += TimeUnit.DAYS.toMillis(1);
		}
		return time;		
	}
	
	/**
	 * <pre>
     * 获取指定时间的下一个步数的工作日同一时间段的对应毫秒数
     * <b>注意：</b>dayDelay 表示需要向后延迟的天数 
     * 但并不是绝对的延迟，而是根据距离的时间来进行判断
     * 如周六的下个工作日为周一 那么这个为基数
     * 周日的下个工作日为周一 那么此时得向后延迟一天
     * 
     * 可以理解为同"步数"下的下一个工作日
     * </pre>
     * 
     * @param time
     * @param deliveryDelay
     * 
     * @return
     */
	public static long nextStepsWorkingDay(long time, int steps) {
		int dayOfWeek = dayOfWeekForCh(time);
		if (dayOfWeek == 6 || dayOfWeek == 7) {
			// 星期六 或 星期天
			time += TimeUnit.DAYS.toMillis(steps);
		}
		return time;		
	}
	
    /**
     * <pre>
     * 是否仍然处于冷却时间
     * 当给定的匹配时间大于当前时间减去指定分钟数时返回true(表示处于冷却时间内)
     * @param matchMillsecondTime 用于匹配的时间
     * @param minute 期望冷却的时间(单位：分钟)
     * </pre>
     */
    public static boolean isChillDownMillisecondTime(long matchMillsecondTime, int minute) {
        // 当前时间 - 5分钟
        long time = System.currentTimeMillis() - getMinuteMillisecondTime(minute);
        return matchMillsecondTime > time;
    }

    /**
     * 获取当前的年
     */
    public static int year() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前的月
     */
    public static int month() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int monthForCH() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前的日
     */
    public static int day() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 今天相对于本月的天数
     */
    public static int dayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int dayOfMonth(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天相对于今年的天数
     */
    public static int dayOfYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 当前小时
     */
    public static int currentHours() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int dayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int dayOfWeekForCh() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        return dayOfWeek;
    }

    public static int dayOfWeekForCh(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        return dayOfWeek;
    }

    public static boolean isCurrentWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long weekBeginTime = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        long weekEndTime = calendar.getTimeInMillis();

        return time >= weekBeginTime && time <= weekEndTime;
    }

    /**
     * <pre>
     * 获取今天指定小时、分钟的毫秒数
     * <b>注意：</b>秒和毫秒都为0
     * </pre>
     */
    public static long getTimeMillisecond(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeMillisecond(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeMillisecond(int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static int toHour(long timeMillisecond) {
        int hour = (int) (timeMillisecond / 1000 / 60 / 60);
        return hour;
    }

    public static int toMinute(long timeMillisecond) {
        int minute = (int) (timeMillisecond / 1000 / 60);
        return minute;
    }

    public static int getHour(long timeSecond) {
        int hour = (int) ((timeSecond / 3600));
        return hour;
    }

    public static int getMinute(long timeSecond) {
        int minute = (int) ((timeSecond % 3600) / 60);
        return minute;
    }

    /**
     * <pre>
     * 通过距离计算显示的字符串内容
     * 当目标距离小于1公里的时候显示结果以米为单位
     * 当目标距离大于1公里的时候显示结果以公里(千米)为单位
     * </pre>
     *
     * @param distance 目标距离(单位必须为米)
     * @return
     */
    public static String formatDistance(double distance) {
        return distance < 1000 ? (((int) distance) + "米") : (CommonUtils.formatNumber(distance / 1000f, "#.##") + "公里");
    }

    /**
     * <pre>
     * 计算公里数，不足一公里按一公里算
     * </pre>
     *
     * @param distance 目标距离(单位必须为米)
     * @return
     */
    public static int formatKilometer(double distance) {
        int kilometer = (int) (distance / 1000);
        if (distance % 1000 > 0) {
            kilometer += 1;
        }
        return kilometer;
    }

    public static boolean isMobileNum(String matchValue) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{1}\\d{8}$");
        Matcher m = p.matcher(matchValue.trim());
        return m.find();
    }

    public static boolean isNumber(String matchValue) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(matchValue);
        return m.matches();
    }

    public static boolean isDouble(String matchValue) {
        Pattern p = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
        Matcher m = p.matcher(matchValue);
        return m.matches();
    }

    public static boolean isLegalChar(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{" + minLength + "," + maxLength + "}$");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isLegalPassword(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{" + minLength + "," + maxLength + "})");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isLegalNumberPassword(String matchValue, int minLength, int maxLength) {
        Pattern p = Pattern.compile("^[0-9]{" + minLength + "," + maxLength + "}$");
        Matcher m = p.matcher(matchValue);
        return m.find();
    }

    public static boolean isIPAddress(String matchValue) {
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

        Matcher m = pattern.matcher(matchValue);

        return m.find();
    }

    public static boolean isEmail(String matchValue) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

        Matcher m = pattern.matcher(matchValue);

        return m.find();
    }

    public static String defineDateFormat(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * <pre>
     * 获取过去时间里指定小时、分钟的最接近毫秒数
     * <b>如：</b>参数为05，00
     * 若当前时间是04点30分那么将返回昨天05点00的毫秒数
     * 若当前时间是05点30分那么将返回今天05点00的毫秒数
     * </pre>
     */
    public static long getLastTimeMillisecond(int hour, int minute) {
        // 获取今日的指定时间毫秒数
        long lastTimeMillisecond = getTimeMillisecond(hour, minute);
        // 当前系统时间
        long currentTime = System.currentTimeMillis();
        // 当前系统时间未到今日指定的时间(仍然为未来时间)
        if (currentTime < lastTimeMillisecond) {
            // 将时间设置为昨天相应的小时、分钟时间毫秒
            lastTimeMillisecond -= CommonUtils.DAY_MILLISECOND_TIME;
        }
        return lastTimeMillisecond;
    }
    
    public static long getNextWorkingTime() {
    	return 0;
    }

    /**
     * 判断给定的匹配时间(毫秒)是否已经超过了当天凌晨5点
     */
    public static boolean isBeyondEarlyMorningFiveHour(long matchTime) {
        return isBeyondLastTimeMillisecond(5, 0, matchTime);
    }

    /**
     * <pre>
     * 给定一个用于匹配的时间(毫秒)判断是否超过了最接近过去时间中指定的对应毫秒
     * </pre>
     */
    public static boolean isBeyondLastTimeMillisecond(int lastHour, int lastMinute, long matchTimeMillisecond) {
        // 获取过去的时间中指定小时、分钟的毫秒数
        long lastTimeMillisecond = getLastTimeMillisecond(lastHour, lastMinute);
        return matchTimeMillisecond >= lastTimeMillisecond;
    }

    /**
     * 获取当前时间的分钟数
     */
    public static int currentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static String currentMinutes() {
        int minute = currentMinute();
        return minute < 10 ? "0" + minute : minute + "";
    }

    /**
     * 获取指定时间的分钟数
     */
    public static String currentMinute(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int minute = calendar.get(Calendar.MINUTE);
        return minute < 10 ? "0" + minute : minute + "";
    }

    /**
     * 给定比较的时间是否为今天
     */
    public static boolean isToday(long compareTime) {
        long nowTime = System.currentTimeMillis();
        return isSameDay(nowTime, compareTime);
    }

    /**
     * 给定的两个比较时间是否为同一天
     */
    public static boolean isSameDay(long sourceTime, long targetTime) {
        sourceTime += TIME_ZONE_OFFSET_MILLIS;
        targetTime += TIME_ZONE_OFFSET_MILLIS;

        long nowDay = sourceTime / TIMEDAY;
        long compareDay = targetTime / TIMEDAY;
        return nowDay == compareDay;
    }
    
	public static boolean isWeekend() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// 节假日才进行提现判断
		if (dayOfWeek == 1 || dayOfWeek == 7) {
			return true;
		}
		return false;
	}
	
	public static boolean isWeekend(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// 节假日才进行提现判断
		if (dayOfWeek == 1 || dayOfWeek == 7) {
			return true;
		}
		return false;
	}

    public static long delayTime(int delayNumberDay, int delayNumberHour, int delayNumberMinue, int delaySecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + delayNumberDay);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + delayNumberHour);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + delayNumberMinue);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + delaySecond);
        return calendar.getTimeInMillis();
    }

    /**
     * <pre>
     * 检查当前时间是否在指定的时间范围内；精确到分钟，秒默认为0
     * beginHour 时间范围的开始时刻(小时)
     * beginMinue 时间范围的开始时刻(分钟)
     *
     * endHour 时间范围的结束时刻(小时)
     * endMinue 时间范围的结束时刻(分钟)
     * </pre>
     */
    public static boolean isRangeTime(int beginHour, int beginMinue, int endHour, int endMinue) {
        long beginTime = getTimeMillisecond(beginHour, beginMinue);
        long endTime = getTimeMillisecond(endHour, endMinue);

        long nowTime = System.currentTimeMillis();

        return beginTime <= nowTime && nowTime <= endTime;
    }

    /**
     * <pre>
     * 检查指定时间是否在指定的范围内；精确到小时
     *
     * beginHour 时间范围的开始时刻(小时)
     * endHour 时间范围的结束时刻(小时)
     *
     * matchTime 用于匹配的时间
     * </pre>
     */
    public static boolean isRangeTime(int beginHour, int endHour, long matchTime) {
        int hour = currentHours(matchTime);

        return beginHour <= hour && endHour > hour;
    }

    public static long startOfMonth() {
        return startOfMonth(1, 0, 0, 0);
    }

    public static long endOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前月份指定日期的时间
     *
     * @param day
     * @param hour
     * @param minue
     * @param second
     * @return
     */
    public static long startOfMonth(int day, int hour, int minue, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minue);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * <pre>
     * 当second小于60时返回包含秒的数据
     * 当second小于3600时返回包含分的数据
     * 当second大于等于3600时返回包含时、分的数据
     * </pre>
     */
    public static String secondToTimeValue(long second) {
        StringBuilder timeVlue = new StringBuilder();
        if (second < 60) {
            timeVlue.append(second).append("秒");
            return timeVlue.toString();
        }
        long minute = second / 60;
        if (minute < 60) {
            timeVlue.append(minute).append("分钟");
            return timeVlue.toString();
        }
        long hour = minute / 60;
        minute = minute % 60;
        timeVlue.append(hour).append("小时").append(minute).append("分钟");

        return timeVlue.toString();
    }

    public static String formatNumber(double value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(value);
    }
    
	public static String unRounding(double value) {
		int i = (int) (value * 100);
		return String.valueOf(i / 100f);
	}

    public static boolean isNullString(String value) {
        return value == null || value.trim().length() <= 0;
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static boolean isNotNullString(String value) {
        return value != null && value.trim().length() > 0 && !value.equals("null");
    }

    public static String md5(String str) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes("UTF8"));
			byte bytes[] = m.digest();

			for (int i = 0; i < bytes.length; i++) {
				if ((bytes[i] & 0xff) < 0x10) {
					sb.append("0");
				}
				sb.append(Long.toString(bytes[i] & 0xff, 16));
			}
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}

    public String numberToString(byte v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public String numberToString(short v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public String numberToString(int v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public String numberToString(long v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public String numberToString(float v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public String numberToString(double v) {
        StringBuilder builder = new StringBuilder();
        builder.append(v);
        return builder.toString();
    }

    public static String parameterSort(Map<String, String> params, List<String> filterParameter) {
        String[] keys = params.keySet().toArray(new String[0]);
        // 排序
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        String v;

        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            if (filterParameter.contains(key)) {
                continue;
            }
            v = params.get(key);
            if (v == null || v.equals("")) {
                continue;
            }
            builder.append(key).append("=").append(v).append("&");
        }
        return builder.toString();
    }

	public static String generateTokeCode(String key) {
		String value = System.currentTimeMillis() + new Random().nextInt() + key;
		// 获取数据指纹，指纹是唯一的
		return md5(value);
	}

    public static void main(String[] args) {
//    	for (int i = 0; i < 10000; i++) {
//    		System.out.println(isPassTime() + DateTime.now().toString(Y_M_D_HH_MM_SS));
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//    	if(true){
//    		return;
//    	}
    	System.out.println(isWeekend(System.currentTimeMillis()));
    	System.out.println(isWeekend(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
    	System.out.println(isWeekend(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
    	System.out.println(isWeekend(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3)));
        System.out.println("今天是星期" + dayOfWeekForCh() + "(中国)");
        System.out.println("今天是星期" + dayOfWeek() + "(国际)");

        long time = getTimeMillisecond(20, 30);
        System.out.println("今天20点30分 " + dateFormat(time) + "  " + new Date(time) + "  毫秒数 " + time);
        time = getTimeMillisecond(21, 30);
        System.out.println("今天21点30分 " + dateFormat(time) + "  " + new Date(time) + "  毫秒数 " + time);

        time = Calendar.getInstance().getTimeInMillis();

        System.out.println("当前时间：" + dateFormat(time));

        System.out.println("今天凌晨0点 " + dateFormat(getTodayEarlyMorningMillisecond()));
        System.out.println(month() + 1 + "月1号凌晨5点 " + CommonUtils.dateFormat(CommonUtils.startOfMonth(1, 5, 0, 0)));

        System.out.println(secondToTimeValue(85568));

        float f = Float.parseFloat(CommonUtils.formatNumber(246953 / 1000f, "#.##"));
        System.out.println(f);

        System.out.println("isMobileNum " + isMobileNum("15912233456"));

        System.out.println(toHour(1000 * 60 * 60 * 4));
        System.out.println(toHour(15120000));

        System.out.println(isLegalChar("_1a1a2b2sfasdfab3c", 6, 12));

        System.out.println(isLegalPassword("1hhD123", 6, 20));

        System.out.println(isIPAddress("120.24.57.17"));
        System.out.println(isEmail("aa@p.qq"));

        System.out.println(dateFormatToLong("2015-07-13 00:00:00"));
        System.out.println(dateFormatToLong("2015-07-13 23:59:59"));

        System.out.println("");
        System.out.println(toHour(642960000) + " ----------- ");

        System.out.println(dateFormat(1435641851996L));
        System.out.println(dateFormat(1435646717995L));

        System.out.println(dateFormat(1435641851996L));
        System.out.println(dateFormat(1435641851996L));

        System.out.println(dateFormat(1435675850422L));

        System.out.println(isLegalNumberPassword("12344", 4, 6));
        System.out.println("md5 " + md5("123456"));

        System.out.println("本月开始时间 " + CommonUtils.dateFormat(CommonUtils.startOfMonth()));

        System.out.println("本月结束时间 " + CommonUtils.dateFormat(CommonUtils.endOfMonth()));

        System.out.println(isNumber("abc"));
        System.out.println(isNumber("987"));
        System.out.println(isNumber("98a"));

        System.out.println(isDouble("100.0"));
        
        System.out.println(generateTokeCode("10001"));
        System.out.println(generateTokeCode("10001"));
        System.out.println(generateTokeCode("10001"));
        System.out.println(generateTokeCode("10001"));
    }

    /**
     * 特殊时间段，拒绝提现
     * @return
     */
	public static boolean isSpecialTime() {
		String[] specialList = {
			"2017-01-02 00:00:00","2017-01-03 00:00:00"// 国庆期间
		};
		for (int i = 0; i < specialList.length; i = i + 2) {
			DateTime startDateTime = DateTime.parse(specialList[i], dateTimeFormater);
			DateTime endDateTime = DateTime.parse(specialList[i + 1], dateTimeFormater);
			if (startDateTime.isBeforeNow() && endDateTime.isAfterNow()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 放通时段
	 * @return
	 */
	public static boolean isPassTime() {
		String[] passList = {
//			"2016-10-08 12:00:00","2016-10-09 00:00:00",
//			"2016-10-09 12:00:00","2016-10-10 00:00:00"
		};
		for (int i = 0; i < passList.length; i = i + 2) {
			DateTime startDateTime = DateTime.parse(passList[i], dateTimeFormater);
			DateTime endDateTime = DateTime.parse(passList[i+1], dateTimeFormater);
			if (startDateTime.isBeforeNow() && endDateTime.isAfterNow()) {
				return true;
			}
		}
		return false;
	}
}