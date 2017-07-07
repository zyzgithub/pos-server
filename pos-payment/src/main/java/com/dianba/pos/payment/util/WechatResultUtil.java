package com.dianba.pos.payment.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class WechatResultUtil {

    public static final String WX_SUCCESS = "SUCCESS";
    public static final String WX_FAIL = "FAIL";

    public static boolean isSuccess(Map<String, String> params) {
        String resultCode = params.get("result_code");
        String returnCode = params.get("return_code");
        return StringUtils.equals(resultCode, WX_SUCCESS)
                && StringUtils.equals(returnCode, WX_SUCCESS);
    }

    public static String getErrorMsg(Map<String, String> params) {
        return params.get("return_msg");
    }

    public static void writeSuccessResult(HttpServletResponse response) {
        try {
            response.getWriter().write(returnXml(WX_SUCCESS, "OK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFailResult(HttpServletResponse response, String errMsg) {
        try {
            response.getWriter().write(returnXml(WX_FAIL, errMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String returnXml(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code>"
                + "<return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
    }
}
