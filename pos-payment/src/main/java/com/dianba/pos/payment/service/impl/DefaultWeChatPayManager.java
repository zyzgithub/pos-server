package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.WeChatPayManager;
import com.dianba.pos.payment.support.WechatPayRemoteService;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultWeChatPayManager extends WechatPayRemoteService implements WeChatPayManager {

    private static Logger logger = LogManager.getLogger(DefaultWeChatPayManager.class);
    private static final int RETRY_TIMES = 60;

    @Autowired
    private LifeOrderManager orderManager;
    @Autowired
    private PassportManager passportManager;

    @Override
    public BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode
            , String deviceInfo, String spBillCreateIP) throws Exception {
        BarcodePayResponse response;
        OrderEntry order = orderManager.getOrder(orderId);
        if (OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() == order.getStatus()) {
            throw new PosAccessDeniedException("订单已付款！无需重复付款！");
        }
        if (order.getTotalPrice() >= 3) {
            throw new PosAccessDeniedException("交易超限！微信单笔交易限额3000！");
        }
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        String body = "1号生活715超市--" + merchantPassport.getShowName();
        try {
            Map<String, String> result = payOrder(authCode, body, order.getSequenceNumber(), null
                    , order.getTotalPrice().intValue(), deviceInfo, spBillCreateIP, null);
            if (result == null) {
                response = BarcodePayResponse.FAILURE;
                response.setMsg("未知错误");
                return response;
            }
            result.put("out_trade_no", order.getSequenceNumber());
            //对付款结果进行处理
            response = handle(result);
        } catch (Exception e) {
            response = BarcodePayResponse.FAILURE;
            e.printStackTrace();
        }
        return response;
    }

    public BasicResult jsPayment(LifeOrder lifeOrder, String openId, String deviceInfo, String spBillCreateIP)
            throws Exception {
        if (OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() == lifeOrder.getStatus()) {
            throw new PosAccessDeniedException("订单已付款！无需重复付款！");
        }
        Passport merchantPassport = passportManager.findById(lifeOrder.getShippingPassportId());
        String body = "1号生活715超市--" + merchantPassport.getShowName();
        String detail = body;
        try {
            Map<String, String> result = payOrderByJSAPI(openId, body, detail
                    , lifeOrder.getSequenceNumber(), body
                    , lifeOrder.getTotalPrice().intValue(), deviceInfo, spBillCreateIP, null);
            if (result == null) {
                BasicResult.createFailResult("支付失败！");
            }
            result.put("out_trade_no", lifeOrder.getSequenceNumber());
            //对付款结果进行处理
            String returnCode = result.get("return_code");
            String resultCode = result.get("result_code");
            String returnMsg = result.get("return_msg");
            if (BarcodePayResponse.WX_SUCCESS.equals(returnCode)
                    && BarcodePayResponse.WX_SUCCESS.equals(resultCode)) {
                BasicResult basicResult = BasicResult.createSuccessResult();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sequenceNumber", lifeOrder.getSequenceNumber());
                jsonObject.putAll(buildJsBridge(result.get("prepay_id")));
                basicResult.setResponse(jsonObject);
                return basicResult;
            } else if (null != result.get("err_code_des")) {
                return BasicResult.createFailResult(result.get("err_code_des"));
            } else {
                return BasicResult.createFailResult(returnMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BasicResult.createFailResult("支付失败！");
    }

    /**
     * 判断支付是否成功
     *
     * @param result
     * @return
     */
    public boolean isPaySuccess(Map<String, String> result) {
        logger.info("isPaySuccess result:{}", result);
        String resultCode = result.get("result_code");
        String returnCode = result.get("return_code");
        return StringUtils.equals(resultCode, BarcodePayResponse.WX_SUCCESS)
                && StringUtils.equals(returnCode, BarcodePayResponse.WX_SUCCESS);
    }

    /**
     * 对支付结果进行处理
     *
     * @param result
     * @return
     */
    public BarcodePayResponse handle(Map<String, String> result) {
        if (isPaySuccess(result)) {
            return BarcodePayResponse.SUCCESS;
        } else if (isUserPaying(result)) {
            //等待用户输入密码
            return onWaitingEntryPassword(result);
        } else {
            //其他异常
            logger.info("其他异常 result:{}", result);
            BarcodePayResponse response = BarcodePayResponse.FAILURE;
            response.setMsg(result.get("trade_state_desc") + ", " + result.get("err_code_des"));
            return response;
        }
    }

    public boolean isUserPaying(Map<String, String> result) {
        logger.info("isUserPaying result:{}", result);
        String errCode = result.get("err_code");
        String errCodeDes = result.get("err_code_des");
        return StringUtils.equals(errCode, BarcodePayResponse.WX_EC_USERPAYING)
                || StringUtils.equals(errCodeDes, "交易已提交，请检查是否已扣款后再试，不要重复支付。");
    }

    /**
     * 等待用户输入密码时，循环查看订单是否被支付
     *
     * @param result
     * @return
     */
    public BarcodePayResponse onWaitingEntryPassword(Map<String, String> result) {
        String outTradeNo = result.get("out_trade_no");
        Map<String, String> query = null;
        for (int i = 0; i < RETRY_TIMES; i++) {
            try {
                Thread.sleep(2000);
                query = queryOrder(outTradeNo);
                if (query != null) {
                    if (StringUtils.equals(query.get("trade_state"), BarcodePayResponse.WX_TRADE_STATE_ERROR)) {
                        return BarcodePayResponse.FAILURE;
                    }
                    if (StringUtils.equals(query.get("trade_state"), "NOTPAY")) {
                        //用户取消支付！
                        return BarcodePayResponse.FAILURE;
                    }
                    if (isTradeStateSuccess(query)) {
                        logger.info("第{}次查询，支付成功...", i + 1);
                        return BarcodePayResponse.SUCCESS;
                    } else {
                        logger.info("第{}次查询，支付失败...", i + 1);
                    }
                }
            } catch (Exception e) {
                return BarcodePayResponse.FAILURE;
            }
        }
        BarcodePayResponse response = BarcodePayResponse.FAILURE;
        if (query != null && query.get("trade_state_desc") != null) {
            response.setMsg(result.get("trade_state_desc"));
            //撤销订单
            reverseOrder(outTradeNo);
        }
        return response;
    }


    /**
     * 查询订单是否支付
     *
     * @param result
     * @return
     */
    public boolean isTradeStateSuccess(Map<String, String> result) {
        logger.info("isTradeStateSuccess result:{}", result);
        String resultCode = result.get("result_code");
        String tradeState = result.get("trade_state");
        logger.info("resultCode:{}, tradeState:{}", resultCode, tradeState);
        return StringUtils.equals(resultCode, BarcodePayResponse.WX_SUCCESS)
                && StringUtils.equals(tradeState, BarcodePayResponse.WX_SUCCESS);
    }
}
