package com.dianba.pos.qrcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/qrwechat.properties")
public class QRWeChatConfig {

    @Value("${qr.wechat.kfz.appid}")
    private String appId;
    @Value("${qr.wechat.kfz.merchant_id}")
    private String merchantId;
    @Value("${qr.wechat.kfz.secrect_key}")
    private String secrectKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecrectKey() {
        return secrectKey;
    }

    public void setSecrectKey(String secrectKey) {
        this.secrectKey = secrectKey;
    }
}
