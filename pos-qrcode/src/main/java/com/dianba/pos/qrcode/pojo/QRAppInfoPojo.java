package com.dianba.pos.qrcode.pojo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class QRAppInfoPojo implements Serializable {

    private String appId;
    private String callBackUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        try {
            this.callBackUrl = java.net.URLEncoder.encode(callBackUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.callBackUrl = callBackUrl;
    }
}
