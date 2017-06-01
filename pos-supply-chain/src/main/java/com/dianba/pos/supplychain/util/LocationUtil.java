package com.dianba.pos.supplychain.util;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocationUtil {

    private static final double R = 6367000.0;
    private static final String URL = "http://api.map.baidu.com/geocoder/v2/";
    private static Logger logger = LogManager.getLogger(LocationUtil.class);

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * (OvO) 根据两点经纬度计算距离(单位为米)
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return
     */
    public static double distanceSimplify(double lat1, double lng1, double lat2, double lng2) {
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        double lx = rad(dx) * R * Math.cos(rad(b)); // 东西距离
        double ly = R * rad(dy); // 南北距离
        return Math.sqrt(lx * lx + ly * ly); // 用平面的矩形对角距离公式计算总距离
    }

    public static Integer getAdcode(String latStr, String lngStr) {
        if (StringUtils.isBlank(latStr) || StringUtils.isBlank(lngStr)) {
            return null;
        }
        try {
            JSONObject params = new JSONObject();
            params.put("location", latStr + "," + lngStr);
            params.put("ak", "xEETdReQI0ATOkRq7BihWDil");
            params.put("output", "json");
            params.put("pois", 0);

            JSONObject retJson = HttpUtil.get(URL, params);
            if (null == retJson) {
                logger.warn("解析经纬度[{}]失败," + latStr + "," + lngStr);
            }
            if (0 == retJson.getIntValue("status")) {
                return retJson.getJSONObject("result").getJSONObject("addressComponent").getInteger("adcode");
            }
            logger.warn("解析经纬度[{}]失败 result:{}, " + latStr + "," + lngStr + "," + retJson.toJSONString());
        } catch (Exception e) {
            logger.warn("解析经纬度[{}]失败," + latStr + "," + lngStr, e);
        }
        return null;
    }
}
