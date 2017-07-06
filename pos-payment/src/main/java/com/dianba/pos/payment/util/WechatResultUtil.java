package com.dianba.pos.payment.util;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class WechatResultUtil {

    public static final String WX_SUCCESS = "SUCCESS";

    public static boolean isSuccess(Map<String, String> params) {
        String resultCode = params.get("result_code");
        String returnCode = params.get("return_code");
        return StringUtils.equals(resultCode, WX_SUCCESS)
                && StringUtils.equals(returnCode, WX_SUCCESS);
    }

    public static String getErrorMsg(Map<String, String> params) {
        return params.get("return_msg");
    }
}
