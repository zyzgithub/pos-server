package com.dianba.pos.extended.util;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
public class FlowCharge19EUtil {

    /**
     * 流量充值请求地址
     **/

//   public static final String FLOW_CHARGET_IP_PORT = "http://114.247.40.65:10000/";
   public static final String FLOW_CHARGET_IP_PORT = "http://epublic.19e.cn/flow_chargecenter/";


    /***流量充值*/
//    public static final String FLOW_CHARGE_URL = FLOW_CHARGET_IP_PORT +"flow_center/createOrder/createOrder.jhtml";
   public static final String FLOW_CHARGE_URL = "createOrder/createOrder.jhtml";


    /******根据手机号等参数获取产品信息***/
//   public static final String QUERY_PRODUCT = FLOW_CHARGET_IP_PORT +"flow_center/queryProduct/queryProductInfo.jhtml";
    public static final String QUERY_PRODUCT = FLOW_CHARGET_IP_PORT + "queryProduct/queryProductInfo.jhtml";

//    public static final String MERCHANT_ID = "DL20130318104801102044";
   public static final String MERCHANT_ID = "AA0ae02017050816303689733";
//    public static final String KEY = "123456789";
   public static final String KEY = "d0CQEGdioiySxBoYnQYyS3884Kp1OZd6hq1OZd0CQYSo1OxhiJexBYy";
    public static String getKeyedDigest(String paramsStr, String key) {

        MessageDigest md5 = null;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(paramsStr.getBytes("UTF-8"));

            byte[] temp= md5.digest(key.getBytes("UTF-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
