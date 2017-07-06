package com.dianba.pos.payment.support;

import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.util.MD5Util;
import com.dianba.pos.payment.util.ParamUtil;
import com.dianba.pos.payment.util.XMLUtil;
import com.dianba.pos.payment.xmlbean.WechatOrderXml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class WechatPayRemoteService {

    private static Logger logger = LogManager.getLogger(WechatPayRemoteService.class);

    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private AppConfig appConfig;

    /**
     * 微信付款条形码付款：订单付款
     */
    protected Map<String, String> payOrder(String authCode, String body, String outTradeNo
            , String attach, Integer totalFee, String deviceInfo, String spBillCreateIP
            , String goodsTag) {
        WechatOrderXml wechatOrderXml = new WechatOrderXml();
        wechatOrderXml.setAppid(wechatConfig.getKfzAppId());
        wechatOrderXml.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderXml.setAuthCode(authCode);
        wechatOrderXml.setBody(body);
        wechatOrderXml.setOutTradeNo(outTradeNo);
        if (!StringUtils.isEmpty(attach)) {
            wechatOrderXml.setAttach(attach);
        }
        wechatOrderXml.setTotalFee(totalFee);
        wechatOrderXml.setNonceStr(createNoncestr());
        if (!StringUtils.isEmpty(deviceInfo)) {
            wechatOrderXml.setDeviceInfo(deviceInfo);
        }
        if (!StringUtils.isEmpty(spBillCreateIP)) {
            wechatOrderXml.setSpbillCreateIp(spBillCreateIP);
        }
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        wechatOrderXml.setTimeStart(timeStart);
        wechatOrderXml.setTimeExpire(timeExpire);
        if (!StringUtils.isEmpty(goodsTag)) {
            wechatOrderXml.setGoodsTag(goodsTag);
        }
        String payParamXml = wechatOrderXml.buildSign(wechatConfig.getKfzApiKey());
        return postXml(wechatConfig.getBarcodePayUrl(), payParamXml);
    }

    /**
     * 微信公众号付款，JSAPI
     */
    protected Map<String, String> payOrderByJSAPI(String openId, String body, String detail, String outTradeNo
            , String attach, Integer totalFee, String deviceInfo, String spBillCreateIP
            , String goodsTag) {
        WechatOrderXml wechatOrderXml = new WechatOrderXml();
        wechatOrderXml.setAppid(wechatConfig.getPublicAppId());
        wechatOrderXml.setMchId(wechatConfig.getPublicMerchantId());
        wechatOrderXml.setTradeType("JSAPI");
        wechatOrderXml.setOpenid(openId);
        wechatOrderXml.setBody(body);
        wechatOrderXml.setDetail(detail);
        wechatOrderXml.setOutTradeNo(outTradeNo);
        wechatOrderXml.setNotifyUrl(appConfig.getPosCallBackHost() + PaymentURLConstant.WAP_WETCHAT_PAY_CALL_BACK_URL);
        if (!StringUtils.isEmpty(attach)) {
            wechatOrderXml.setAttach(attach);
        }
        wechatOrderXml.setTotalFee(totalFee);
        wechatOrderXml.setNonceStr(createNoncestr());
        if (!StringUtils.isEmpty(deviceInfo)) {
            wechatOrderXml.setDeviceInfo(deviceInfo);
        }
        if (!StringUtils.isEmpty(spBillCreateIP)) {
            wechatOrderXml.setSpbillCreateIp(spBillCreateIP);
        }
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        wechatOrderXml.setTimeStart(timeStart);
        wechatOrderXml.setTimeExpire(timeExpire);
        if (!StringUtils.isEmpty(goodsTag)) {
            wechatOrderXml.setGoodsTag(goodsTag);
        }
        String payParamXml = wechatOrderXml.buildSign(wechatConfig.getPublicApiKey());
        return postXml(wechatConfig.getOrderPayUrl(), payParamXml);
    }

    /**
     * 构造微信JS支付所需参数
     */
    protected Map<String, String> buildJsBridge(String prepayId) {
        SortedMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("appId", wechatConfig.getPublicAppId());
        sortedMap.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        sortedMap.put("nonceStr", createNoncestr());
        sortedMap.put("package", "prepay_id=" + prepayId);
        sortedMap.put("signType", "MD5");
        String paramStr = ParamUtil.buildHttpGetStr(sortedMap);
        paramStr = paramStr + "key=" + wechatConfig.getPublicApiKey();
        String sign = MD5Util.md5(paramStr);
        sortedMap.put("paySign", sign);
        return sortedMap;
    }

    /**
     * 微信付款条形码付款：查询订单的付款状态
     */
    protected Map<String, String> queryOrder(String outTradeNo) {
        WechatOrderXml wechatOrderXml = new WechatOrderXml();
        wechatOrderXml.setAppid(wechatConfig.getKfzAppId());
        wechatOrderXml.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderXml.setNonceStr(createNoncestr());
        wechatOrderXml.setOutTradeNo(outTradeNo);
        String orderQueryXml = wechatOrderXml.buildSign(wechatConfig.getKfzApiKey());
        return postXml(wechatConfig.getOrderQueryUrl(), orderQueryXml);
    }

    /**
     * 微信订单撤销
     */
    protected Map<String, String> reverseOrder(String outTradeNo) {
        WechatOrderXml wechatOrderXml = new WechatOrderXml();
        wechatOrderXml.setAppid(wechatConfig.getKfzAppId());
        wechatOrderXml.setMchId(wechatConfig.getKfzMerchantId());
        wechatOrderXml.setNonceStr(createNoncestr());
        wechatOrderXml.setOutTradeNo(outTradeNo);
        String orderReverseXml = wechatOrderXml.buildSign(wechatConfig.getKfzApiKey());
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
            String resp = HttpUtil.postXml(url, xml);
//            String resp = HttpsRequest.sendPost(wechatConfig, url, xml);
            return XMLUtil.doXMLParse(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
