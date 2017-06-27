package com.dianba.pos.qrcode.config;

import java.io.UnsupportedEncodingException;

public class WeChatURLConstant {

    private static final String AUTH_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?"
            + "appid=APPID&redirect_uri=REDIRECT_URI"
            + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

    private static final String AUTH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
            + "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code ";

    public static String getAuthCodeUrl(String appId, String redirectUrl, String state) {
        String callBackUrl = "";
        try {
            callBackUrl = java.net.URLEncoder
                    .encode(redirectUrl
                            , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (state == null) {
            state = "STATE";
        }
        return AUTH_CODE_URL.replace("APPID", appId)
                .replace("REDIRECT_URI", callBackUrl)
                .replace("STATE", state);
    }

    public static String getAuthTokenUrl(String appId, String secret, String code) {
        return AUTH_TOKEN_URL.replace("APPID", appId)
                .replace("SECRET", secret)
                .replace("CODE", code);
    }
}
