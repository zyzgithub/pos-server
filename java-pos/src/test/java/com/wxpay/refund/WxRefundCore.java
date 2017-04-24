package com.wxpay.refund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class WxRefundCore {

	public static Map<String, String> filterNull(Map<String, String> map) {
		HashMap<String, String> result = new HashMap<String, String>();
		if (map == null || map.size() == 0) {
			return result;
		}
		for (Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (StringUtils.isEmpty(value) || key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
	
	public static String getSign(Map<String, String> map) {
		if (map.size() == 0) {
			throw new IllegalArgumentException();
		}
		Map<String, String> filteredMap = filterNull(map);
		List<String> list = new ArrayList<String>(filteredMap.keySet());
		Collections.sort(list);

		StringBuilder stringBuilder = new StringBuilder();
		for (String key : list) {
			stringBuilder.append(key).append("=").append(filteredMap.get(key)).append("&");
		}
		stringBuilder.append("key=").append(WxRefundConfig.KEY);
		return DigestUtils.md5Hex(stringBuilder.toString()).toUpperCase();
	}
	
}
