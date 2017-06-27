package com.dianba.pos.payment.support;

import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.util.HttpsRequest;
import com.dianba.pos.payment.util.PayService;
import com.dianba.pos.payment.util.XMLUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class WechatPayRemoteService {

    private static Logger logger = LogManager.getLogger(WechatPayRemoteService.class);

    @Autowired
    private WechatConfig wechatConfig;

    /**
     * 微信付款条形码付款：订单付款
     */
    protected Map<String, String> payOrder(String authCode, String body, String outTradeNo
            , String attach, Integer totalFee, String deviceInfo, String spBillCreateIP
            , String goodsTag) {
        String payParamXml = PayService.createBarcodePayXml(authCode, body, outTradeNo, attach, totalFee, deviceInfo
                , spBillCreateIP, goodsTag);
        try {
            String resp = HttpsRequest.sendPost(wechatConfig.getBarcodePayUrl(), payParamXml);
            logger.info("wechat_barcode_pay, return:", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
