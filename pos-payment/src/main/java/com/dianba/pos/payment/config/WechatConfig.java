package com.dianba.pos.payment.config;

import com.dianba.pos.payment.util.MD5Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.UnsupportedEncodingException;

/**
 * 微信开发者账号配置
 */
@Configuration
@PropertySource("classpath:properties/wechat.properties")
public class WechatConfig {

    private static Logger logger = LogManager.getLogger(WechatConfig.class);

    //获取网页授权地址
    @Value("${wechat.auth_code.url}")
    private String authCodeUrl;
    //访问授权地址
    @Value("${wechat.access_token.url}")
    private String accessTokenUrl;
    //统一下单地址
    @Value("${wechat.pay.url}")
    private String orderPayUrl;
    //条码下单地址
    @Value("${wechat.barcode.pay.url}")
    private String barcodePayUrl;
    //条码订单付款状态查询地址
    @Value("${wechat.order.query.url}")
    private String orderQueryUrl;
    //条码订单撤销地址
    @Value("${wechat.order.reverse.url}")
    private String orderReverseUrl;

    // 公众号APPID
    @Value("${wechat.kfz.appid}")
    private String kfzAppId;
    // 公众号应用秘钥
    @Value("${wechat.kfz.appsecrect}")
    private String kfzAppSecrect;
    // 公众号的商户号
    @Value("${wechat.kfz.merchantid}")
    private String kfzMerchantId;
    // API密钥，在公众平台上设置好api证书好，自己设置的密码
    @Value("${wechat.kfz.apikey}")
    private String kfzApiKey;

    //公众平台APPID
    @Value("${wechat.public.appid}")
    private String publicAppId;
    //公众平台商户号ID
    @Value("${wechat.public.merchant_id}")
    private String publicMerchantId;
    //公众平台商户秘钥
    @Value("${wechat.public.app_secrect}")
    private String publicAppSecrect;
    //公众平台商户支付秘钥
    @Value("${wechat.public.apikey}")
    private String publicApiKey;

    public String getAuthCodeUrl(String redirectUrl) {
        String state = MD5Util.md5(redirectUrl + publicAppSecrect);
        String callBackUrl = "";
        try {
            callBackUrl = java.net.URLEncoder
                    .encode(redirectUrl
                            , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        authCodeUrl = authCodeUrl.replace("APPID", publicAppId)
                .replace("REDIRECT_URI", callBackUrl)
                .replace("STATE", state);
        logger.info("微信授权回调地址：" + authCodeUrl);
        return authCodeUrl;
    }

    public String getAccessTokenUrl(String code) {
        return accessTokenUrl.replace("APPID", publicAppId)
                .replace("SECRET", publicAppSecrect)
                .replace("CODE", code);
    }

    public String getOrderPayUrl() {
        return orderPayUrl;
    }

    public String getBarcodePayUrl() {
        return barcodePayUrl;
    }

    public String getOrderQueryUrl() {
        return orderQueryUrl;
    }

    public String getOrderReverseUrl() {
        return orderReverseUrl;
    }

    public String getKfzAppId() {
        return kfzAppId;
    }

    public String getKfzAppSecrect() {
        return kfzAppSecrect;
    }

    public String getKfzMerchantId() {
        return kfzMerchantId;
    }

    public String getKfzApiKey() {
        return kfzApiKey;
    }

    public String getPublicAppId() {
        return publicAppId;
    }

    public String getPublicMerchantId() {
        return publicMerchantId;
    }

    public String getPublicAppSecrect() {
        return publicAppSecrect;
    }

    public String getPublicApiKey() {
        return publicApiKey;
    }
}
