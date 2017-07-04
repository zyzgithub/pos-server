package com.dianba.pos.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/alipay.properties")
public class AlipayConfig {

    // 商户appid
    @Value("${alipay.appid}")
    private String appid;
    // 私钥 pkcs8格式的
    @Value("${alipay.rsa_private_key}")
    private String rsaRrivateKey;
    // 支付宝公钥
    @Value("${alipay.public_key}")
    private String alipayPublicKey;
    // 请求网关地址
    @Value("${alipay.url}")
    private String url;
    // 编码
    public static final String CHARSET = "UTF-8";
    // 返回格式
    public static final String FORMAT = "json";
    // 日志记录目录
    public static final String LOG_PATH = "/log";
    // RSA2
    public static final String SIGNTYPE = "RSA";

    public String getAppid() {
        return appid;
    }

    public String getRsaRrivateKey() {
        return rsaRrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public String getUrl() {
        return url;
    }
}
