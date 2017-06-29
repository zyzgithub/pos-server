package com.dianba.pos.payment.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ParamUtil {

    public static String sortParamsByASCII(Map<String, String> map) {
        SortedMap<String, String> sortedMap = new TreeMap<>();
        for (String key : map.keySet()) {
            sortedMap.put(key, map.get(key));
        }
        return buildHttpGetStr(sortedMap);
    }

    public static String buildHttpGetStr(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb = sb.append(key).append("=").append(map.get(key)).append("&");
        }
        return sb.toString();
    }
}
