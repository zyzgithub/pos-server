package com.wm.util;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.wm.controller.open_api.ValidUtil;

/**
 * 数据转换
 * @author folo
 *
 */
public class ConverDataUtil {
	
	public static Long toLong(Object obj){
		try {
			return Long.parseLong(String.valueOf(obj));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Integer toInt(Object obj){
		try {
			if(obj instanceof Long) return ((Long)obj).intValue();
			return Integer.parseInt(String.valueOf(obj));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Double toDouble(Object obj){
		try {
			return Double.parseDouble(String.valueOf(obj));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static BigDecimal toBigDecimal(Object obj){
		try {
			String str = toString(obj);
			if(ValidUtil.anyEmpty(str)) return null;
			return new BigDecimal(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toString(Object obj){
		try {
			return String.valueOf(obj);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 强制转换类型
	 * @param source 源类型
	 * @param c 转换后的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T forceToBaseType(Object source, Class<?> c){
		if(c.getName().equals(String.class.getName())) return (T) ConverDataUtil.toString(source);
		else if(c.getName().equals(Integer.class.getName())) return (T) ConverDataUtil.toInt(source);
		else if(c.getName().equals(Long.class.getName())) return (T) ConverDataUtil.toLong(source);
		else if(c.getName().equals(Double.class.getName())) return (T) ConverDataUtil.toDouble(source);
		else if(c.getName().equals(BigDecimal.class.getName())) return (T) ConverDataUtil.toBigDecimal(source);
		else{
			try {
				if(source instanceof String) return (T) JSON.parseObject((String) source, c);
				else return (T) JSON.parseObject(JSON.toJSONString(source), c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return null;
	}
}
