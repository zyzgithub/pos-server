package com.wm.util;

/**
 * 工具类
 * @author l-PC
 *
 */
public class IntegerUtil {
	
	/**
	 * 元素是否存在， 存在返回:true。 不存在返回:false
	 * @param num
	 * @return
	 */
	public static Boolean isEmpty(Integer num){
		return num == null?false:true;
	}
	
	/**
	 * 转换成Integer
	 * @param obj
	 * @return
	 */
	public static Integer objToInteger(Object obj){
		return (Integer)obj;
	}
}
