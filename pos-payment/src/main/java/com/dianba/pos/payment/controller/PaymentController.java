package com.dianba.pos.payment.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.AliPayManager;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.WechatPayManager;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(PaymentURLConstant.PAYMENT_ORDER)
public class PaymentController extends BasicWebService {

    private static Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private AliPayManager aliPayManager;
    @Autowired
    private WechatPayManager wechatPayManager;

    /**
     * 支付回调（统一渠道)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("notify")
    public JSONObject notify(HttpServletRequest request) {
        System.out.println(request.getParameterMap());
        return null;
    }

    /**
     * 支付订单
     *
     * @param passportId     通行证ID
     * @param orderId        订单ID
     * @param authCode       用户条形码
     * @param paymentTypeKey 支付类型Key
     * @return
     */
    @ResponseBody
    @RequestMapping("pay_order")
    public BasicResult payOrder(long passportId, long orderId
            , String authCode, String paymentTypeKey) throws Exception {
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        TransTypeEnum transTypeEnum = TransTypeEnum
                .getTransTypeEnum(Integer.parseInt(orderEntry.getTransType()));
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentTypeKey);
        long totalPrice = orderEntry.getTotalPrice() < orderEntry.getActualPrice()
                ? orderEntry.getActualPrice() : orderEntry.getTotalPrice();
        BarcodePayResponse barcodePayResponse;
        if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.ALIPAY.getKey())) {
            //支付宝条码支付
            barcodePayResponse = aliPayManager.barcodePayment(passportId, orderId, authCode);
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.WEIXIN_NATIVE.getKey())) {
            //微信条码支付
            barcodePayResponse = wechatPayManager.barcodePayment(passportId, orderId, authCode
                    , "", "");
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.CASH.getKey())) {
            //现金支付
            barcodePayResponse = BarcodePayResponse.createSuccessResult();
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.BALANCE.getKey())) {
            //TODO 余额支付,需要判断余额是否充足
//            barcodePayResponse = BarcodePayResponse.createSuccessResult();
            throw new Exception("暂不支持余额支付！");
        } else {
            throw new Exception("不支持的支付类型！" + paymentTypeEnum.getKey());
        }
        BasicResult basicResult = BasicResult.createFailResult();
        if (barcodePayResponse.isSuccess()) {
            try {
                //通知支付系统，保存支付信息
                basicResult = paymentManager.payOrder(passportId, orderId
                        , paymentTypeEnum.getKey()
                        , transTypeEnum
                        , totalPrice);
                //TODO 通知订单系统，订单已经支付
                orderManager.paymentOrder(orderId, transTypeEnum.getKey());

                if (basicResult.isSuccess() && !paymentTypeEnum.equals(PaymentTypeEnum.CASH)) {
                    //TODO 获取商家ID
                    long merchantPassportId = passportId;
                    //对商家余额进行偏移计算
                    OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderEntry.getType());
                    long offsetAmount = 0;
                    if (orderTypeEnum.getKey() == OrderTypeEnum.POS_PURCHASE_ORDER_TYPE.getKey()) {
                        offsetAmount = -Math.abs(orderEntry.getTotalPrice());
                    } else if (orderTypeEnum.getKey() == OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey()) {
                        if (orderEntry.getTotalPrice() > orderEntry.getActualPrice()) {
                            offsetAmount = orderEntry.getTotalPrice() - orderEntry.getActualPrice();
                        }
                    }
                    //对商家余额进行余额偏移
                    paymentManager.offsetBalance(passportId, orderEntry.getSequenceNumber()
                            , offsetAmount, transTypeEnum);
                }
            } catch (Exception e) {
                logger.error("订单保存异常!" + e.getMessage());
            }
            return basicResult;
        } else {
            basicResult = BasicResult.createFailResult(barcodePayResponse.getMsg());
            logger.info("支付失败！订单ID：" + orderId + "，返回：" + barcodePayResponse.getMsg()
                    + "，body：" + barcodePayResponse.getResponse().getBody().toString());
            return basicResult;
        }
    }
}
