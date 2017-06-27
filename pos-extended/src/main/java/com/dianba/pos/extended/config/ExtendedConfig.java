package com.dianba.pos.extended.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by zhangyong on 2017/6/27.
 */

@Configuration
@PropertySource("classpath:extended.properties")
public class ExtendedConfig {

    @Value("${extended.flow_charge.order_url}")
    private String extendedFlowChargeUrl;
    @Value("${extended.flow_charge.merchant_id}")
    private String extendedFlowMerchantId;
    @Value("${extended.flow_charge.key}")
    private String extendedFlowKey;
    @Value("${extended.flow_charge.product_url}")
    private String extendedFlowProductUrl;

    @Value("${extended.hf_charge.order_url}")
    private String extendedHfChargeUrl;
    @Value("${extended.hf_charge.merchant_id}")
    private String extendedHfMerchantId;
    @Value("${extended.hf_charge.key}")
    private String extendedHfKey;
    @Value("${extended.hf_charge.call_back_url}")
    private String extendedHfNotifyUrl;
    @Value("${extended.hf_charge.product_url}")
    private String extendedHfQueryOrderUrl;

    public String getExtendedFlowChargeUrl() {
        return extendedFlowChargeUrl;
    }

    public String getExtendedFlowMerchantId() {
        return extendedFlowMerchantId;
    }

    public String getExtendedFlowKey() {
        return extendedFlowKey;
    }

    public String getExtendedFlowProductUrl() {
        return extendedFlowProductUrl;
    }

    public String getExtendedHfChargeUrl() {
        return extendedHfChargeUrl;
    }

    public String getExtendedHfMerchantId() {
        return extendedHfMerchantId;
    }

    public String getExtendedHfKey() {
        return extendedHfKey;
    }

    public String getExtendedHfNotifyUrl() {
        return extendedHfNotifyUrl;
    }

    public String getExtendedHfQueryOrderUrl() {
        return extendedHfQueryOrderUrl;
    }
}
