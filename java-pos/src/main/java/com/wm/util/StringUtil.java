package com.wm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;



/**
 * 字符串处理工具类
 * @author lfq
 * @email  545987886@qq.com
 * @2015-5-8
 */
public class StringUtil {

	 //顺序表  
    private static String orderStr = "";
	
	/**
	 * 时间比较
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Boolean compareDate(Date d1,Date d2){ 
		if (d1 == null || d2== null) {
			return false;  
		}
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    String s1 = sdf.format(d1);  
	    String s2 = sdf.format(d2);  
	    if(s1.equals(s2)){
	    	return true;  
	    }
	    else{
	    	return false;  
	    }
	} 
	/**
	 * 判断对象是否为空
	 * @author lfq
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		if(obj==null){
			return true;
		}else if("".equals(obj.toString())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 识别多个对象是否非空， 其中一个为空时返回false
	 * <p>
	 * 	String: null=true, "" = true
	 *  Integer: null=true, <0 = true
	 *  Long: null=true, <0 = true 
	 * </p>
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty(Object... objs){
		for (Object obj : objs) {
			if(obj instanceof Long && ((Long) obj) < 0) return true;
			if(obj instanceof Integer && ((Integer) obj) < 0) return true;
			else if(isEmpty(obj)) return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param list
	 * @return if list null , return (null) ; else (1,2,3,...)
	 */
	public static  String list2SqlIn(List<String> list) {
		if(list == null || list.isEmpty()){
			return "(null)";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (i == list.size() - 1) {
				sb.append(s);
			} else {
				sb.append(s).append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	

	/**
	 * 
	 * @param list
	 * @return if list null , return (null) ; else (1,2,3,...)
	 */
	public static String listQuota2SqlIn(List<String> list) {
		if(list == null || list.isEmpty()){
			return "(null)";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("('");
		for (int i = 0; i < list.size(); i++) {
			Object s = list.get(i);
			if (i == list.size() - 1) {
				sb.append(s);
//				sb.append("'").append(s).append("'");
			} else {
				sb.append(s).append(",");
			}
		}
		sb.append("')");
		return sb.toString();
	}
	
	/**
	 * 去除字符串中的回车、换行符、制表符和非法字符（非Asccll码）
	 * @param oldStr
	 * @return
	 */
	public static String replaceNTSRElement(String oldStr){
		Pattern p = Pattern.compile("\\s*|\t|\r|\n|^\\p{ASCII}");
		Matcher m = p.matcher(oldStr);
		return m.replaceAll("");
	}
	
	public static void main(String[] args) {
//		System.out.println(listQuota2SqlIn(Lists.newArrayList("asdf", "dsdd","32433")));
		String shopcartJson = "[{\"menuId\":\"7364\",\"menuName\":\"恒顺\t白醋\t500ml\",\"price\":0.01,\"count\":1,\"repertory\":1000,\"promoting\":false,\"limitCount\":0}]";
		System.out.println(replaceNTSRElement(shopcartJson));
	}
	
	/**
	 * 去掉小数点后面的0
	 * @param s
	 * @return
	 */
	 public static String subZeroAndDot(String s){  
	        if(s.indexOf(".") > 0){  
	            s = s.replaceAll("0+?$", "");//去掉多余的0  
	            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
	        }  
	        return s;  
	    }  
	 
	 

		/**
		 * 判断是否为手机号码
		 * 
		 * @param mobile 手机号码
		 * @return
		 */
	public static boolean isMobileNumber( String mobile ) {
		if( StringUtils.isBlank(mobile)){
			return false;
		}
				
		Pattern p = Pattern.compile( "^((13[0-9])|(145,147)|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$" );
		Matcher m = p.matcher( mobile );
		return m.matches();
	}
	
	/**
	 * 判断是否为身份证号码
	 * @param idCard
	 * @return
	 */
	public static boolean isIdCard(String idCard){
		if( StringUtils.isBlank(idCard)){
			return false;
		}

//		Pattern p = Pattern.compile("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$)$");
		Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$");
		return p.matcher(idCard).matches();
	}
	

    static {
        for (int i = 33; i < 127; i++) {
            orderStr += Character.toChars(i)[0];
        }
    }

    /**
     * 判断是否有顺序
     *
     * @param str
     * @return
     * @Author zhoucong
     */
    public static boolean isOrder(String str) {
        if (!str.matches("((\\d)|([a-z])|([A-Z]))+")) {
            return false;
        }
        return orderStr.contains(str);
    }


    /**
     * 判断是否相同
     *
     * @param str
     * @return
     * @Author zhoucong
     */
    public static boolean isSame(String str) {
        String regex = str.substring(0, 1) + "{" + str.length() + "}";
        return str.matches(regex);
    }


}
