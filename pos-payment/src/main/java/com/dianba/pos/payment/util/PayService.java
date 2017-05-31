package com.dianba.pos.payment.util;

import org.apache.commons.lang.StringUtils;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;


public class PayService {

    /**
     * 创建扫用户微信条形码支付参数的xml
     *
     * @param authCode
     * @param body
     * @param outTradeNo
     * @param attach
     * @param totalFee
     * @param deviceInfo
     * @param spBillCreateIP
     * @param timeStart
     * @param timeExpire
     * @param goodsTag
     * @return
     */
    public static String createBarcodePayXml(String authCode, String body, String outTradeNo,
                                             String attach, String totalFee, String deviceInfo, String spBillCreateIP,
                                             String timeStart, String timeExpire, String goodsTag) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", ConfigUtil.APPID_KFZ);
        parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
        parameters.put("auth_code", authCode);
        parameters.put("body", body);
        parameters.put("out_trade_no", outTradeNo);
        if (StringUtils.isNotBlank(attach)) {
            parameters.put("attach", attach);
        }
        parameters.put("total_fee", totalFee);
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());
        if (StringUtils.isNotBlank(deviceInfo)) {
            parameters.put("device_info", deviceInfo);
        }
        if (StringUtils.isNotBlank(spBillCreateIP)) {
            parameters.put("spbill_create_ip", spBillCreateIP);
        }
        parameters.put("time_start", timeStart);
        parameters.put("time_expire", timeExpire);
        if (StringUtils.isNotBlank(goodsTag)) {
            parameters.put("goods_tag", goodsTag);
        }
        String sign = PayCommonUtil.createSign(null, parameters, ConfigUtil.API_KEY);
        parameters.put("sign", sign);
        return PayCommonUtil.getRequestXml(parameters);
    }

    /**
     * 创建微信退款支付参数
     *
     * @param outTradeNo
     * @param totalFee
     * @param refundFee
     * @param deviceInfo
     * @return
     */
    public static String createRefundXml(String outTradeNo, int totalFee, int refundFee,
                                         String deviceInfo) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", ConfigUtil.APPID_KFZ);
        parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
        parameters.put("out_trade_no", outTradeNo);
        parameters.put("out_refund_no", UUID.randomUUID().toString());
        parameters.put("total_fee", String.valueOf(totalFee));
        parameters.put("refund_fee", String.valueOf(refundFee));
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());
        parameters.put("op_user_id", ConfigUtil.MCH_ID_KFZ);

        if (StringUtils.isNotBlank(deviceInfo)) {
            parameters.put("device_info", deviceInfo);
        }

        String sign = PayCommonUtil.createSign(null, parameters, ConfigUtil.API_KEY);
        parameters.put("sign", sign);
        return PayCommonUtil.getRequestXml(parameters);
    }

    /**
     * 微信订单支付状态查询
     *
     * @param outTradeNo 订单的payId
     * @return
     */
    public static String createOrderPayQueryXml(String outTradeNo) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", ConfigUtil.APPID_KFZ);
        parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());
        parameters.put("out_trade_no", outTradeNo);
        String sign = PayCommonUtil.createSign(null, parameters, ConfigUtil.API_KEY);
        parameters.put("sign", sign);
        return PayCommonUtil.getRequestXml(parameters);
    }

    /**
     * 撤销订单
     *
     * @param outTradeNo 订单的payId
     * @return
     */
    public static String createReverseOrderXml(String outTradeNo) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", ConfigUtil.APPID_KFZ);
        parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());
        parameters.put("out_trade_no", outTradeNo);
        String sign = PayCommonUtil.createSign(null, parameters, ConfigUtil.API_KEY);
        parameters.put("sign", sign);
        return PayCommonUtil.getRequestXml(parameters);
    }


    /**
     * 根据条形码获取openId
     *
     * @param authCode
     * @return
     */
    public static String createGetOpenIdXml(String authCode) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", ConfigUtil.APPID_KFZ);
        parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());
        parameters.put("auth_code", authCode);
        String sign = PayCommonUtil.createSign(null, parameters, ConfigUtil.API_KEY);
        parameters.put("sign", sign);
        return PayCommonUtil.getRequestXml(parameters);

    }

    /**
     * 模拟错误返回
     *
     * @return
     */
    public static String createFailXml(String errCode) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("result_code", "FAIL");
        parameters.put("return_code", "FAIL");
        parameters.put("err_code", errCode);
        return PayCommonUtil.getRequestXml(parameters);

    }

}
