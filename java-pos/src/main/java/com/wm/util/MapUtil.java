package com.wm.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * map 工具类
 * @author folo
 *
 */
public class MapUtil {
	public static <T> T convertMapToBean(Map<String, Object> map, Class<T> t){
		try {
			T ret = t.newInstance();
		    BeanUtils.populate(ret, map);
		    return ret;
		}
		catch (Throwable e) {
		   e.printStackTrace();
		   return null;
		}
	}
	
	/**
	 * 
	 * @param m
	 */
	public static void getMapToBean(Map<String, Object> m){
		for (Map.Entry<String, Object> entry : m.entrySet()) {
			if (entry.getValue() == null) {
				entry.setValue("");
			}
		}
	}
	
	/**
	 * {gmt_create=2016-06-17 12:03:23, buyer_email=18516590624, notify_time=2016-06-17 12:03:24, gmt_payment=2016-06-17 12:03:24}
	 * @return key-value形式的map
	 */
	public static Map<String, String> stringToMap(String json){
		if(StringUtils.isNotBlank(json)){
			Map<String, String> map = new HashMap<String, String>();
			String[] properties = json.substring(1, json.length()-1).split(",");
			for(String pro : properties){
				String key = pro.trim().split("=")[0];
				String value = pro.trim().split("=")[1];
				map.put(key, value);
			}
			return map;
		}
		return null;
	}
}
