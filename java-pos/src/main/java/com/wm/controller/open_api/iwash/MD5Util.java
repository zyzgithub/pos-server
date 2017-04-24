package com.wm.controller.open_api.iwash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MD5Util {
	public static String key="iwantwash";
	//进行MD5加密
	public static final String getStringMD5(String text){
		String md5String = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'a', 'b', 'c', 'd', 'e', 'f'};

		byte arrByte[] = text.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(arrByte);
			byte arrMD5Byte[] = messageDigest.digest();
			char arrChar[] = new char[arrMD5Byte.length *2 ];
			int i = 0;
			for(byte b : arrMD5Byte){
				arrChar[i++] = hexDigits[b >>> 4 & 0xf];
				arrChar[i++] = hexDigits[b & 0xf];
			}
			md5String = new String(arrChar);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return md5String;
	}
	
	/** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                ) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    
    public static Map<String, String> getNewParams(Map<String, String> params){
    	//除去数组中的空值和签名参数
		Map<String, String> sArray=MD5Util.paraFilter(params);
		//获得待签名字符串
		String text=MD5Util.createLinkString(sArray);
		text=text+key;
		String newSign=MD5Util.getStringMD5(text);
		sArray.put("sign", newSign);
		return sArray;
    }
}
