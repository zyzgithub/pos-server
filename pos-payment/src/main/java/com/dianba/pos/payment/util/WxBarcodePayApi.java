package com.dianba.pos.payment.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WxBarcodePayApi {

    private static Logger logger = LoggerFactory.getLogger(WxBarcodePayApi.class);

    //1）被扫付款API
    public static final String WX_BARCODE_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
    //2）被扫付款查询API
    public static final String WX_BARCODE_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    //3) 撤销订单
    public static final String WX_BARCODE_REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    //4) 获取openId
    public static final String WX_BARCODE_GETOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
    //4) 退款
    public static final String WX_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 微信付款条形码付款：订单付款
     *
     * @param params
     * @return
     */
    public static Map<String, String> payOrder(Map<String, String> params) {
        String authCode = params.get("auth_code");
        String body = params.get("body");
        String outTradeNo = params.get("out_trade_no");
        String attach = params.get("attach");
        String totalFee = params.get("total_fee");
        String deviceInfo = params.get("device_info");
        String spBillCreateIP = params.get("spbill_create_ip");
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        String goodsTag = params.get("goods_tag");

        String payParamXml = PayService.createBarcodePayXml(authCode, body, outTradeNo, attach, totalFee, deviceInfo,
                spBillCreateIP, timeStart, timeExpire, goodsTag);
        try {
            String resp = WmHttpsRequest.sendPost(WX_BARCODE_PAY_URL, payParamXml);
            logger.info("payOrder, return:{}", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信退款
     *
     * @param params
     * @return
     */
    public static Map<String, String> refundOrder(Map<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        Integer totalFee = Integer.parseInt(params.get("total_fee"));
        Integer refundFee = Integer.parseInt(params.get("refund_fee"));
        String deviceInfo = params.get("device_info");

        String refundParamXml = PayService.createRefundXml(outTradeNo, totalFee, refundFee, deviceInfo);
        try {
            String resp = WmHttpsRequest.sendPost(WX_REFUND_URL, refundParamXml);
            logger.info("payOrder, return:{}", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信付款条形码付款：查询订单的付款状态
     *
     * @param outTradeNo 订单的payId
     * @return
     */
    public static Map<String, String> orderPayQuery(String outTradeNo) {
        String payParamXml = PayService.createOrderPayQueryXml(outTradeNo);
        try {
            String resp = WmHttpsRequest.sendPost(WX_BARCODE_QUERY_URL, payParamXml);
            logger.info("orderPayQuery, return:{}", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信付款条形码付款：撤销订单
     *
     * @param outTradeNo 订单的payId
     * @return
     */
    public static Map<String, String> reverseOrder(String outTradeNo) {
        String payParamXml = PayService.createReverseOrderXml(outTradeNo);
        try {
            String resp = WmHttpsRequest.sendPost(WX_BARCODE_REVERSE_URL, payParamXml);
            logger.info("reverseOrder, return:{}", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信付款条形码付款：根据条形码获取用户信息
     *
     * @param authCode
     * @return
     */
    public static Map<String, String> getOpenId(String authCode) {
        String payParamXml = PayService.createGetOpenIdXml(authCode);
        logger.info("getOpenId, payParamXml:{}", payParamXml);
        try {
            String resp = WmHttpsRequest.sendPost(WX_BARCODE_GETOPENID_URL, payParamXml);
            logger.info("getOpenId, return:{}", resp);
            return XMLUtil.doXMLParse(resp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", "521364678885009");
        params.put("total_fee", "1");
        params.put("refund_fee", "1");

        refundOrder(params);
    }
}
