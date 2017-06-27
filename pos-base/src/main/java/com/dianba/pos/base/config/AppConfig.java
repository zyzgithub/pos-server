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

    @Value("${extended.flow.CHARGE_IP_PORT}")
    private String extendedFlowChargeIpPort;
    @Value("${extended.flow.MERCHANT_ID}")
    private String extendedFlowMerchantId;
    @Value("${extended.flow.KEY}")
    private String extendedFlowKey;
    @Value("${extended.hf.CHARGE_IP_PORT}")
    private String extendedHfChargeIpPort;
    @Value("${extended.hf.MERCHANT_ID}")
    private String extendedHfMerchantId;
    @Value("${extended.hf.KEY}")
    private String extendedHfKey;
    @Value("${extended.hf.NOTIFY_URL}")
    private String extendedHfNotifyUrl;

    public String getExtendedHfNotifyUrl() {
        return extendedHfNotifyUrl;
    }
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


    public String getExtendedFlowChargeIpPort() {
        return extendedFlowChargeIpPort;
    }

    public String getExtendedFlowMerchantId() {
        return extendedFlowMerchantId;
    }

    public String getExtendedFlowKey() {
        return extendedFlowKey;
    }

    public String getExtendedHfChargeIpPort() {
        return extendedHfChargeIpPort;
    }

    public String getExtendedHfMerchantId() {
        return extendedHfMerchantId;
    }

    public String getExtendedHfKey() {
        return extendedHfKey;
    }
}
