package com.dianba.pos.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app_config.properties")
public class AppConfig {

    @Value("${pos.passport.url}")
    private String posPassportUrl;
    @Value("${pos.order.url}")
    private String posOrderUrl;
    @Value("${pos.payment.url}")
    private String posPaymentUrl;
    @Value("${pos.supplychain.url}")
    private String posSupplyChainUrl;
    @Value("${pos.finance.url}")
    private String posFinanceUrl;

    @Value("${pos.app.id}")
    private String posAppId;
    @Value("${pos.app.partnerId}")
    private String posPartnerId;
    @Value("${pos.app.key}")
    private String posAppKey;

    @Value("${pos.callback.host}")
    private String posCallBackHost;

    @Value("${pos.wechat.callback.host}")
    private String posWechatCallBackHost;

    public String getPosPassportUrl() {
        return posPassportUrl;
    }

    public String getPosOrderUrl() {
        return posOrderUrl;
    }

    public String getPosPaymentUrl() {
        return posPaymentUrl;
    }

    public String getPosSupplyChainUrl() {
        return posSupplyChainUrl;
    }

    public String getPosFinanceUrl() {
        return posFinanceUrl;
    }

    public String getPosAppId() {
        return posAppId;
    }

    public String getPosPartnerId() {
        return posPartnerId;
    }

    public String getPosAppKey() {
        return posAppKey;
    }

    public String getPosCallBackHost() {
        return posCallBackHost;
    }


    public String getPosWechatCallBackHost() {
        return posWechatCallBackHost;
    }
}
