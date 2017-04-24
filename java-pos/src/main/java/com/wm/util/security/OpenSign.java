
package com.wm.util.security;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.tswj.PortConfig;

/**
 * 开放接口签名
 * @author folo
 *
 */
public class OpenSign {
	public static Logger log = Logger.getLogger(OpenSign.class.getName());
	/**
	 * 组装生成加密参数map
	 * @param timestamp
	 * @param key
	 * @return
	 */
	public static Map<String, Object> signMap(Long timestamp, String key){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("timestamp", timestamp);
		params.put("key", key);
		//加入签名
		String sign = OpenSign.sign(params);
		params.put("sign", sign);
		return params;
	}

	/**
	 * i玩派接口调用签名生成
	 * @param params
	 * @param timestamp
	 * @param key
	 * @return
	 */
	public static String sign(Map<String, Object> params) {
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		sortMap.putAll(params);
		String beforeEncMd5 = JSON.toJSONString(sortMap);
		return MD5(beforeEncMd5);
	}
	
	/**
	 * 生成加入签名后的参数
	 * @param params 参数
	 * @return 已签名的参数
	 */
	public static Map<String, Object> getSignMap(Map<String, Object> params) {
		params.put("timestamp", System.currentTimeMillis());
		params.put("appKey", PortConfig.SYSTEM_VALIDE_KEY);//加入私有key进行签名
		String sign = getSign(params);
		params.put("sign", sign);
		params.remove("appKey");//签名生成完毕 移除key
		log.info("sign:" + sign + "\n params:"+JSON.toJSONString(params));
		return params;
	}
	
	/**
	 * 生成接口调用签名
	 * @param params 接口调用参数(不含sign字段)
	 * @return 加密后的md5字符串
	 */
	public static String getSign(Map<String, Object> params) {
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		sortMap.putAll(params);
		String sign = JSON.toJSONString(sortMap);
		log.info("md5 json:" + sign);
		return MD5(sign);
	}
	
	/**
	 * 生成加入签名后的参数
	 * @param jsonParams 参数
	 * @return 已签名的参数
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getSignJson(JSONObject jsonParams){
		Map<String, Object> params = JSON.parseObject(jsonParams.toJSONString(), HashMap.class);
		return JSON.parseObject(JSON.toJSONString(getSignMap(params)), JSONObject.class);
	}
	
	/**
	 * 生成给i玩派调用token
	 * @param openid
	 * @param partnerid
	 * @param key
	 * @return 加密错误反悔NULL
	 */
	public static String EncToken(String openId, String appId, String appKey){
		JSONObject json = new JSONObject();
		json.put("openId", openId);
		json.put("appId", appId);
		json.put("timestamp", new Date().getTime());
		try {
			return DesEnc.ENCRYPTMethod(json.toString(), appKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String DecToken(String str, String key){
		try {
			return DesEnc.decrypt(str, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * map 按照key排序
	 * @author folo
	 *
	 */
	public static class MapKeyComparator implements Comparator<String> {
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}
	
	/**
	 * md5加密
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("utf-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
