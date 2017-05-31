package com.dianba.pos.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 微信开发者账号配置
 */
@Configuration
@PropertySource("classpath:properties/wechat.properties")
public class WechatConfig {

    // 公众号APPID
    @Value("${wechat.appid}")
    private String appId;

    // 公众号应用密码
    @Value("${wechat.appsecrect}")
    private String appSecrect;

    // 公众号的商户号
    @Value("${wechat.merchantid}")
    private String merchantId;

    // 公众号APPID
    @Value("${wechat.kfz.appid}")
    private String appIdKFZ;

    // 公众号应用密码
    @Value("${wechat.kfz.appsecrect}")
    private String appSecrectKFZ;

    // 公众号的商户号
    @Value("${wechat.kfz.merchantid}")
    private String merchantIdKFZ;

    // API密钥，在公众平台上设置好api证书好，自己设置的密码
    @Value("${wechat.kfz.apikey}")
    private String apiKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecrect() {
        return appSecrect;
    }

    public void setAppSecrect(String appSecrect) {
        this.appSecrect = appSecrect;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAppIdKFZ() {
        return appIdKFZ;
    }

    public void setAppIdKFZ(String appIdKFZ) {
        this.appIdKFZ = appIdKFZ;
    }

    public String getAppSecrectKFZ() {
        return appSecrectKFZ;
    }

    public void setAppSecrectKFZ(String appSecrectKFZ) {
        this.appSecrectKFZ = appSecrectKFZ;
    }

    public String getMerchantIdKFZ() {
        return merchantIdKFZ;
    }

    public void setMerchantIdKFZ(String merchantIdKFZ) {
        this.merchantIdKFZ = merchantIdKFZ;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
