package com.dianba.pos.payment.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

public class PayCommonUtil {

    public static String createNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * @param characterEncoding 编码格式
     * @param parameters        请求参数
     * @return
     * @author 李欣桦
     * @date 2014-12-5下午2:29:34
     * @Description：sign签名
     */
    public static String createSign(String characterEncoding, SortedMap<String, String> parameters, String apiKey) {
        StringBuffer sb = new StringBuffer();
        Set<Entry<String, String>> es = parameters.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        String sign = MD5Util.md5Encode(sb.toString(), characterEncoding)
                .toUpperCase();
        return sign;
    }


    /**
     * @param parameters 请求参数
     * @return
     * @author 李欣桦
     * @date 2014-12-5下午2:32:05
     * @Description：将请求参数转换为xml格式的string
     */
    public static String getRequestXml(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Entry<String, String>> es = parameters.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
                    || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String setXML(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode
                + "]]></return_code><return_msg><![CDATA[" + returnMsg
                + "]]></return_msg></xml>";
    }
}
