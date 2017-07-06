package com.dianba.pos.payment.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ParamUtil {

    private static Logger logger = LogManager.getLogger(ParamUtil.class);

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

    public static Map<String, String> convertRequestMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>();
        for (Object key : request.getParameterMap().keySet()) {
            paramsMap.put(key.toString(), request.getParameter(key.toString()));
            logger.info(key + "---->" + request.getParameter(key.toString()));
        }
        return paramsMap;
    }
}
