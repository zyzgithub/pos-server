package com.courier_mana.common;

/**
 * 一个和字符串有关的工具类
 * @author hyj
 *
 */
public class StringUtil {

	/**
	 * 确保id字符串为非空字符串
	 * @param idsStr	若干个id组成的字符串
	 * @return	如果入参为空, 或者空字符串则返回"-1"
	 */
	public static String checkIdsString(String idsStr){
		if(idsStr == null || idsStr.isEmpty()){
			idsStr = "-1";
		}
		return idsStr;
	}
}
