package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.WechatPayManager;
import com.dianba.pos.payment.util.WxBarcodePayApi;
import com.dianba.pos.payment.util.WxBarcodePayReturnHandler;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultWechatPayManager implements WechatPayManager {

    private static Logger logger = LogManager.getLogger(DefaultWechatPayManager.class);

    @Autowired
    private OrderManager orderManager;

    @Override
    public String getOpenId(String authCode) {
        Map<String, String> result = WxBarcodePayApi.getOpenId(authCode);
        if (result != null && StringUtils.equals(result.get("return_code"), "SUCCESS")
                && StringUtils.equals(result.get("result_code"), "SUCCESS")) {
            return result.get("openid");
        } else {
            if (result != null) {
                logger.info("根据authCode获取用户信息失败，返回信息:{}", JSON.toJSONString(result));
            }
        }
        return null;
    }


    @Override
    public BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode
            , String deviceInfo, String spBillCreateIP) {
        BarcodePayResponse response;
        OrderEntry order = orderManager.getOrder(orderId);
        if (order == null) {
            response = BarcodePayResponse.FAILURE;
            response.setMsg("参数错误");
            logger.warn("无法根据订单id:{}获取订单", orderId);
            return response;
        }
        if ("1".equals(order.getPaymentType())) {
            response = BarcodePayResponse.FAILURE;
            response.setCode(1001);
            response.setMsg("订单已付款");
            logger.info("订单id:{}已付款,不需重复付款", orderId);
            return response;
        }
        //TODO 根据收银员ID获取商家信息（ID，NAME)
        Long merchantPassportId = passportId;
        //TODO 商家名字
        String title = "";

        Map<String, String> params = new HashMap<>();
        params.put("auth_code", authCode);
        if (StringUtils.isNotBlank(deviceInfo)) {
            params.put("device_info", deviceInfo);
        }
        if (StringUtils.isNotBlank(spBillCreateIP)) {
            params.put("spbill_create_ip", spBillCreateIP);
        }
        params.put("out_trade_no", order.getSequenceNumber());
        params.put("body", "1号生活715超市--" + title);
//        otherParams.put("total_fee", String.valueOf(order.getTotalPrice() / 100));
        params.put("total_fee", "1");
        try {
            Map<String, String> result = WxBarcodePayApi.payOrder(params);
            if (result == null) {
                response = BarcodePayResponse.FAILURE;
                response.setMsg("未知错误");
                return response;
            }

            result.put("out_trade_no", order.getSequenceNumber());
            //对付款结果进行处理
            response = WxBarcodePayReturnHandler.handle(result);
            //付款成功
            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {

//                String outTraceId = result.get("transaction_id");
//                order.setOutTraceId(outTraceId);
//                //更新订单对应的用户及订单的付款类型
//                if (userId.intValue() != 0) {
//                    WUserEntity user = this.get(WUserEntity.class, userId);
//                    if (user != null) {
//                        order.setWuser(user);
//                    }
//                }
//                if ("merchantQcCode".equals(order.getFromType())) {//商家扫用户条形码付款
//                    order.setPayType(OrderEntity.PayType.WEIXINPAY);
//                } else {
//                    order.setPayType(PayEnum.supermarkt_wxbarcode.getEn());
//                }
//                order.setOnlineMoney(order.getOrigin());
//                order.setPayState("pay");
//                order.setPayTime(DateUtils.getSeconds());
//                orderService.updateEntitie(order);

                // TODO 支付宝付款成功，调用2.0支付记录保存方法
//                //更新订单的状态，及其他业务处理
//                OrderHandler orderPayBackHandler = orderHandleService.getHandler(order.getOrderType());
//                orderPayBackHandler.handle(order);
            }
        } catch (Exception e) {
            response = BarcodePayResponse.FAILURE;
            e.printStackTrace();
        }
        return response;
    }

}
