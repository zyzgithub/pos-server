package org.jeecgframework.codegenerate.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CodeDateUtils {
	public static final String DATESTYLE = "yyyyMMddHHmmss";
	public static final String DATESTYLE_EX = "yyyy-MM-dd_HH-mm-ss";
	public static final String DATESTYLE_ = "yyyy-MM-dd";
	public static final String DATESTYLE_YEAR_MONTH = "yyyyMM";
	public static final String DATESTYLE_SHORT = "yyyyMMdd";
	public static final String DATESTYLE_SHORT_EX = "yyyy/MM/dd";
	public static final String DATESTYLE_YEAR_MONTH_EX = "yyyy/MM";
	public static final String DATESTYLE_DETAIL = "yyyyMMddHHmmssSSS";

	public static String dateToString(Date paramDate) {
		if (paramDate == null)
			return "";
		return FormatDate(paramDate, "yyyy-MM-dd HH:mm:ss");
	}

	public static String dateToStringShort(Date paramDate) {
		if (paramDate == null)
			return "";
		return FormatDate(paramDate, "yyyy-MM-dd");
	}

	public static long diffTwoDate(Date paramDate1, Date paramDate2) {
		long l1 = paramDate1.getTime();
		long l2 = paramDate2.getTime();
		return l1 - l2;
	}

	public static int diffTwoDateDay(Date paramDate1, Date paramDate2) {
		long l1 = paramDate1.getTime();
		long l2 = paramDate2.getTime();
		int i = Integer.parseInt(((l1 - l2) / 3600L / 24L / 1000L) + "");
		return i;
	}

	public static String FormatDate(Date paramDate, String paramString) {
		if (paramDate == null)
			return "";
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				paramString);
		return localSimpleDateFormat.format(paramDate);
	}

	public static String getCurrDate() {
		Date localDate = new Date();
		return FormatDate(localDate, "yyyy-MM-dd");
	}

	public static Date getCurrDateTime() {
		return new Date(System.currentTimeMillis());
	}

	public static String getCurrTime() {
		Date localDate = new Date();
		return FormatDate(localDate, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDate10to8(String paramString) {
		String str1 = paramString.substring(0, 4);
		String str2 = paramString.substring(5, 7);
		String str3 = paramString.substring(8, 10);
		return str1 + str2 + str3;
	}

	public static String getDate8to10(String paramString) {
		String str1 = paramString.substring(0, 4);
		String str2 = paramString.substring(4, 6);
		String str3 = paramString.substring(6, 8);
		return str1 + "-" + str2 + "-" + str3;
	}

	public static String getDay(Date paramDate) {
		return FormatDate(paramDate, "dd");
	}

	public static String getHour(Date paramDate) {
		return FormatDate(paramDate, "HH");
	}

	public static String getMinute(Date paramDate) {
		return FormatDate(paramDate, "mm");
	}

	public static String getMonth(Date paramDate) {
		return FormatDate(paramDate, "MM");
	}

	public static int getMonth(Date paramDate1, Date paramDate2) {
		if (paramDate1.after(paramDate2)) {
			Date localObject = paramDate1;
			paramDate1 = paramDate2;
			paramDate2 = (Date) localObject;
		}
		Object localObject = Calendar.getInstance();
		((Calendar) localObject).setTime(paramDate1);
		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.setTime(paramDate2);
		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.setTime(paramDate2);
		localCalendar2.add(5, 1);
		int i = localCalendar1.get(1) - ((Calendar) localObject).get(1);
		int j = localCalendar1.get(2) - ((Calendar) localObject).get(2);
		if ((((Calendar) localObject).get(5) == 1)
				&& (localCalendar2.get(5) == 1))
			return i * 12 + j + 1;
		if ((((Calendar) localObject).get(5) != 1)
				&& (localCalendar2.get(5) == 1))
			return i * 12 + j;
		if ((((Calendar) localObject).get(5) == 1)
				&& (localCalendar2.get(5) != 1))
			return i * 12 + j;
		return i * 12 + j - 1 < 0 ? 0 : i * 12 + j;
	}

	public static String getSecond(Date paramDate) {
		return FormatDate(paramDate, "ss");
	}

	public static String getTime(String paramString1, String paramString2) {
		String str = "";
		int i = 31;
		int j = Integer.parseInt(paramString1);
		int k = Integer.parseInt(paramString2);
		if ((k == 4) || (k == 6) || (k == 9) || (k == 11))
			i = 30;
		if (k == 2) {
			i = 28;
			if (((j % 4 == 0) && (j % 100 == 0) && (j % 400 == 0))
					|| ((j % 4 == 0) && (j % 100 != 0)))
				i = 29;
		}
		str = paramString1 + "-" + paramString2 + "-" + String.valueOf(i);
		return str;
	}

	public static String getYear(Date paramDate) {
		return FormatDate(paramDate, "yyyy");
	}

	@SuppressWarnings("unused")
	public static void main(String[] paramArrayOfString) {
		CodeDateUtils localCodeDateUtils1 = new CodeDateUtils();
		String str = "2007-02-11";
		Date localDate = stringToDateShort(str);
		CodeDateUtils localCodeDateUtils2 = new CodeDateUtils();
	}

	public static Date stringToDate(String paramString) {
		if ((paramString == null) || (paramString.trim().length() == 0))
			return null;
		String str1 = paramString.trim();
		String str2 = "yyyy-MM-dd HH:mm:ss";
		Date localDate = stringToDate(str1, str2);
		if (localDate == null)
			localDate = stringToDate(str1, "yyyy-MM-dd");
		if (localDate == null)
			localDate = stringToDate(str1, "yyyyMMdd");
		return localDate;
	}

	public static Date stringToDate(String paramString1, String paramString2) {
		ParsePosition localParsePosition = new ParsePosition(0);
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				paramString2);
		Date localDate = localSimpleDateFormat.parse(paramString1,
				localParsePosition);
		return localDate;
	}

	public static Date stringToDateShort(String paramString) {
		String str = "yyyy-MM-dd";
		Date localDate = stringToDate(paramString, str);
		return localDate;
	}

	public String getBeginDate(String paramString1, String paramString2) {
		String str = "";
		Date localDate1 = stringToDateShort(paramString2);
		Date localDate2 = null;
		if (paramString1.equals("1"))
			localDate2 = localDate1;
		if (paramString1.equals("2"))
			localDate2 = getWeekBegin(localDate1);
		if (paramString1.equals("3"))
			localDate2 = getPeriodBegin(localDate1);
		else if (paramString1.equals("4"))
			localDate2 = getMonthBegin(localDate1);
		else if (paramString1.equals("5"))
			localDate2 = getSeasonBegin(localDate1);
		else if (paramString1.equals("6"))
			localDate2 = getHalfYearBegin(localDate1);
		else if (paramString1.equals("7"))
			localDate2 = getYearBegin(localDate1);
		str = dateToStringShort(localDate2);
		return str;
	}

	public String getDateChangeALL(String paramString1, String paramString2,
			int paramInt) {
		Date localDate1 = null;
		String str = "";
		if (paramString1.length() == 10)
			localDate1 = stringToDateShort(paramString1);
		if (paramString1.length() > 10)
			localDate1 = stringToDate(paramString1);
		if (paramString2.equals("1")) {
			str = "d";
		} else if (paramString2.equals("2")) {
			paramInt *= 7;
			str = "d";
		} else if (paramString2.equals("3")) {
			paramInt *= 10;
			str = "d";
		} else if (paramString2.equals("4")) {
			str = "m";
		} else if (paramString2.equals("5")) {
			paramInt *= 3;
			str = "m";
		} else if (paramString2.equals("6")) {
			paramInt *= 6;
			str = "m";
		} else if (paramString2.equals("7")) {
			str = "y";
		} else {
			str = "d";
		}
		Date localDate2 = getDateChangeTime(localDate1, str, paramInt);
		return dateToStringShort(localDate2);
	}

	public Date getDateChangeTime(Date paramDate, String paramString,
			int paramInt) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		j--;
		int k = Integer.parseInt(FormatDate(paramDate, "dd"));
		int m = Integer.parseInt(FormatDate(paramDate, "HH"));
		int n = Integer.parseInt(FormatDate(paramDate, "mm"));
		int i1 = Integer.parseInt(FormatDate(paramDate, "ss"));
		GregorianCalendar localGregorianCalendar = new GregorianCalendar(i, j,
				k, m, n, i1);
		if (paramString.equalsIgnoreCase("y"))
			localGregorianCalendar.add(1, paramInt);
		else if (paramString.equalsIgnoreCase("m"))
			localGregorianCalendar.add(2, paramInt);
		else if (paramString.equalsIgnoreCase("d"))
			localGregorianCalendar.add(5, paramInt);
		else if (paramString.equalsIgnoreCase("h"))
			localGregorianCalendar.add(10, paramInt);
		else if (paramString.equalsIgnoreCase("mi"))
			localGregorianCalendar.add(12, paramInt);
		else if (paramString.equalsIgnoreCase("s"))
			localGregorianCalendar.add(13, paramInt);
		return localGregorianCalendar.getTime();
	}

	public String getDateChangeTime(String paramString1, String paramString2,
			int paramInt) {
		Date localDate = stringToDate(paramString1);
		localDate = getDateChangeTime(localDate, paramString2, paramInt);
		return dateToString(localDate);
	}

	public String getEndDate(String paramString1, String paramString2) {
		String str = "";
		Date localDate1 = stringToDateShort(paramString2);
		Date localDate2 = null;
		if (paramString1.equals("1"))
			localDate2 = localDate1;
		if (paramString1.equals("2"))
			localDate2 = getWeekEnd(localDate1);
		if (paramString1.equals("3"))
			localDate2 = getPeriodEnd(localDate1);
		else if (paramString1.equals("4"))
			localDate2 = getMonthEnd(localDate1);
		else if (paramString1.equals("5"))
			localDate2 = getSeasonEnd(localDate1);
		else if (paramString1.equals("6"))
			localDate2 = getHalfYearEnd(localDate1);
		else if (paramString1.equals("7"))
			localDate2 = getYearEnd(localDate1);
		str = dateToStringShort(localDate2);
		return str;
	}

	@SuppressWarnings("unused")
	public Date getHalfYearBegin(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		String str = FormatDate(paramDate, "yyyy") + "-";
		if (j <= 6)
			str = str + "01-01";
		else
			str = str + "07-01";
		return stringToDateShort(str);
	}

	@SuppressWarnings("unused")
	public Date getHalfYearEnd(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		String str = FormatDate(paramDate, "yyyy") + "-";
		if (j <= 6)
			str = str + "06-30";
		else
			str = str + "12-31";
		return stringToDateShort(str);
	}

	public Date getMonthBegin(Date paramDate) {
		String str = FormatDate(paramDate, "yyyy-MM") + "-01";
		return stringToDateShort(str);
	}

	public Date getMonthEnd(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		int k = Integer.parseInt(FormatDate(paramDate, "dd"));
		GregorianCalendar localGregorianCalendar = new GregorianCalendar(i,
				j - 1, k, 0, 0, 0);
		int m = localGregorianCalendar.getActualMaximum(5);
		String str = FormatDate(paramDate, "yyyy") + "-"
				+ FormatDate(paramDate, "MM") + "-";
		if (m < 10)
			str = str + "0" + m;
		else
			str = str + m;
		return stringToDateShort(str);
	}

	public Date getPeriodBegin(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "dd"));
		String str = FormatDate(paramDate, "yyyy-MM") + "-";
		if (i <= 10)
			str = str + "01";
		else if (i <= 20)
			str = str + "11";
		else
			str = str + "21";
		return stringToDateShort(str);
	}

	public Date getPeriodEnd(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "dd"));
		String str = FormatDate(paramDate, "yyyy-MM") + "-";
		if (i <= 10)
			str = str + "10";
		else if (i <= 20)
			str = str + "20";
		else
			str = FormatDate(getMonthEnd(paramDate), "yyyy-MM-dd");
		return stringToDateShort(str);
	}

	@SuppressWarnings("unused")
	public Date getSeasonBegin(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		String str = FormatDate(paramDate, "yyyy") + "-";
		if ((j >= 1) && (j <= 3))
			str = str + "01-01";
		else if ((j >= 4) && (j <= 6))
			str = str + "04-01";
		else if ((j >= 7) && (j <= 9))
			str = str + "07-01";
		else if ((j >= 10) && (j <= 12))
			str = str + "10-01";
		return stringToDateShort(str);
	}

	@SuppressWarnings("unused")
	public Date getSeasonEnd(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		String str = FormatDate(paramDate, "yyyy") + "-";
		if ((j >= 1) && (j <= 3))
			str = str + "03-31";
		else if ((j >= 4) && (j <= 6))
			str = str + "06-30";
		else if ((j >= 7) && (j <= 9))
			str = str + "09-30";
		else if ((j >= 10) && (j <= 12))
			str = str + "12-31";
		return stringToDateShort(str);
	}

	public String getTimedes(String paramString1, String paramString2) {
		String str1 = "";
		Date localDate = stringToDateShort(paramString2);
		String str2 = "";
		String str3 = "01";
		String str4 = "01";
		str2 = getYear(localDate);
		str3 = getMonth(localDate);
		str4 = getDay(localDate);
		if (paramString1.equals("1")) {
			str1 = paramString2;
		} else if (paramString1.equals("4")) {
			str1 = str2 + "年" + str3 + "月";
		} else if (paramString1.equals("8")) {
			String str5 = str3 + "-" + str4;
			if (str5.equals("03-31"))
				str1 = str2 + "年 第1季度";
			else if (str5.equals("06-30"))
				str1 = str2 + "年 第2季度";
			else if (str5.equals("09-30"))
				str1 = str2 + "年 第3季度";
			else if (str5.equals("12-31"))
				str1 = str2 + "年 第4季度";
		} else if (paramString1.equals("32")) {
			str1 = str2 + "年";
		}
		return str1;
	}

	public Date getWeekBegin(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		j--;
		int k = Integer.parseInt(FormatDate(paramDate, "dd"));
		GregorianCalendar localGregorianCalendar = new GregorianCalendar(i, j,
				k);
		int m = 6;
		if (m == 0)
			m = 7;
		localGregorianCalendar.add(5, 0 - m + 1);
		return localGregorianCalendar.getTime();
	}

	public Date getWeekEnd(Date paramDate) {
		int i = Integer.parseInt(FormatDate(paramDate, "yyyy"));
		int j = Integer.parseInt(FormatDate(paramDate, "MM"));
		j--;
		int k = Integer.parseInt(FormatDate(paramDate, "dd"));
		GregorianCalendar localGregorianCalendar = new GregorianCalendar(i, j,
				k);
		int m = 6;
		if (m == 0)
			m = 7;
		localGregorianCalendar.add(5, 7 - m);
		return localGregorianCalendar.getTime();
	}

	public Date getYearBegin(Date paramDate) {
		String str = FormatDate(paramDate, "yyyy") + "-01-01";
		return stringToDateShort(str);
	}

	public Date getYearEnd(Date paramDate) {
		String str = FormatDate(paramDate, "yyyy") + "-12-31";
		return stringToDateShort(str);
	}

	@SuppressWarnings("unused")
	public boolean IsXperiodEnd(Date paramDate) {
		boolean i = false;
		String str1 = getDay(paramDate);
		String str2 = getMonth(paramDate);
		if (str1.equalsIgnoreCase("10"))
			i = false;
		else if (str1.equalsIgnoreCase("20"))
			i = true;
		return i;
	}

	public static String addDay(String paramString, int paramInt) {
		try {
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Calendar localCalendar = Calendar.getInstance();
			localCalendar.setTime(localSimpleDateFormat.parse(paramString));
			localCalendar.add(5, paramInt);
			return localSimpleDateFormat.format(localCalendar.getTime());
		} catch (Exception localException) {
		}
		return null;
	}

	public static String delDay(String paramString, int paramInt) {
		try {
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Calendar localCalendar = Calendar.getInstance();
			localCalendar.setTime(localSimpleDateFormat.parse(paramString));
			localCalendar.add(5, -paramInt);
			return localSimpleDateFormat.format(localCalendar.getTime());
		} catch (Exception localException) {
		}
		return null;
	}
}

/*
 * Location:
 * E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB
 * -INF\lib\org.jeecgframework.codegenerate.jar Qualified Name:
 * org.jeecgframework.codegenerate.util.CodeDateUtils JD-Core Version: 0.6.0
 */