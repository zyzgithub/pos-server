package com.dianba.pos.payment.support;

import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.util.HttpsRequest;
import com.dianba.pos.payment.util.XMLUtil;
import com.dianba.pos.payment.xmlbean.WechatOrderDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;

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
        WechatOrderDto wechatOrderDto = new WechatOrderDto();
        wechatOrderDto.setAppid(wechatConfig.getKfzAppId());
        wechatOrderDto.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderDto.setAuthCode(authCode);
        wechatOrderDto.setBody(body);
        wechatOrderDto.setOutTradeNo(outTradeNo);
        if (!StringUtils.isEmpty(attach)) {
            wechatOrderDto.setAttach(attach);
        }
        wechatOrderDto.setTotalFee(totalFee);
        wechatOrderDto.setNonceStr(createNoncestr());
        if (!StringUtils.isEmpty(deviceInfo)) {
            wechatOrderDto.setDeviceInfo(deviceInfo);
        }
        if (!StringUtils.isEmpty(spBillCreateIP)) {
            wechatOrderDto.setSpbillCreateIp(spBillCreateIP);
        }
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        wechatOrderDto.setTimeStart(timeStart);
        wechatOrderDto.setTimeExpire(timeExpire);
        if (!StringUtils.isEmpty(goodsTag)) {
            wechatOrderDto.setGoodsTag(goodsTag);
        }
        String payParamXml = wechatOrderDto.buildSign(wechatConfig.getKfzApiKey());
        return postXml(wechatConfig.getBarcodePayUrl(), payParamXml);
    }

    /**
     * 微信公众号付款，JSAPI
     */
    protected Map<String, String> payOrderByJSAPI(String openId, String body, String detail, String outTradeNo
            , String attach, Integer totalFee, String deviceInfo, String spBillCreateIP
            , String goodsTag) {
        WechatOrderDto wechatOrderDto = new WechatOrderDto();
        wechatOrderDto.setAppid(wechatConfig.getPublicAppId());
        wechatOrderDto.setMchId(wechatConfig.getPublicMerchantId());
        wechatOrderDto.setTradeType("JSAPI");
        wechatOrderDto.setOpenid(openId);
        wechatOrderDto.setBody(body);
        wechatOrderDto.setDetail(detail);
        wechatOrderDto.setOutTradeNo(outTradeNo);
        wechatOrderDto.setNotifyUrl("http://apptest.0085.com/pos/payment/wap/notify_url/"+outTradeNo);
        if (!StringUtils.isEmpty(attach)) {
            wechatOrderDto.setAttach(attach);
        }
        wechatOrderDto.setTotalFee(totalFee);
        wechatOrderDto.setNonceStr(createNoncestr());
        if (!StringUtils.isEmpty(deviceInfo)) {
            wechatOrderDto.setDeviceInfo(deviceInfo);
        }
        if (!StringUtils.isEmpty(spBillCreateIP)) {
            wechatOrderDto.setSpbillCreateIp(spBillCreateIP);
        }
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        wechatOrderDto.setTimeStart(timeStart);
        wechatOrderDto.setTimeExpire(timeExpire);
        if (!StringUtils.isEmpty(goodsTag)) {
            wechatOrderDto.setGoodsTag(goodsTag);
        }
        String payParamXml = wechatOrderDto.buildSign(wechatConfig.getPublicApiKey());
        return postXml(wechatConfig.getOrderPayUrl(), payParamXml);
    }

    /**
     * 微信付款条形码付款：查询订单的付款状态
     */
    protected Map<String, String> queryOrder(String outTradeNo) {
        WechatOrderDto wechatOrderDto = new WechatOrderDto();
        wechatOrderDto.setAppid(wechatConfig.getKfzAppId());
        wechatOrderDto.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderDto.setNonceStr(createNoncestr());
        wechatOrderDto.setOutTradeNo(outTradeNo);
        String orderQueryXml = wechatOrderDto.buildSign(wechatConfig.getKfzApiKey());
        return postXml(wechatConfig.getOrderQueryUrl(), orderQueryXml);
    }

    /**
     * 微信订单撤销
     */
    protected Map<String, String> reverseOrder(String outTradeNo) {
        WechatOrderDto wechatOrderDto = new WechatOrderDto();
        wechatOrderDto.setAppid(wechatConfig.getKfzAppId());
        wechatOrderDto.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderDto.setNonceStr(createNoncestr());
        wechatOrderDto.setOutTradeNo(outTradeNo);
        String orderReverseXml = wechatOrderDto.buildSign(wechatConfig.getKfzApiKey());
        return postXml(wechatConfig.getOrderReverseUrl(), orderReverseXml);
    }

    private String createNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    private Map<String, String> postXml(String url, String xml) {
        try {
            String resp = HttpsRequest.sendPost(wechatConfig, url, xml);
            return XMLUtil.doXMLParse(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
