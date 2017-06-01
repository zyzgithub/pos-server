package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.po.LifePaymentTransactionLogger;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.repository.LifePaymentTransLoggerJpaRepository;
import com.dianba.pos.payment.service.AliPayManager;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.WechatPayManager;
import com.dianba.pos.payment.support.PaymentRemoteService;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultPaymentManager extends PaymentRemoteService implements PaymentManager {

    private static Logger logger = LogManager.getLogger(DefaultAliPayManager.class);

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private AliPayManager aliPayManager;
    @Autowired
    private WechatPayManager wechatPayManager;
    @Autowired
    private LifePaymentTransLoggerJpaRepository transLoggerJpaRepository;

    @Autowired
    private AppConfig appConfig;


    public BasicResult payOrder(long passportId, long orderId
            , String paymentType, TransTypeEnum transType, long transTotalAmount) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //支付类型
        params.put("paymentType", paymentType);
        //交易类型
        params.put("transType", transType.getKey() + "");
        //合作商户ID
        params.put("partnerUserId", passportId + "");
        //订单ID/订单批次编号
        params.put("partnerTradeNumber", orderId + "");
        //交易单位金额
        params.put("transUnitAmount", String.valueOf(transTotalAmount));
        //交易单位数量
        params.put("transNumber", "1");
        //交易总金额
        params.put("transTotalAmount", String.valueOf(transTotalAmount));
        //交易标题
        params.put("transTitle", transType.getValue());
        //交易备注
        params.put("remark", "POS-" + transType.getValue());
        //是否使用优惠券
        params.put("useConpon", "0");
        //优惠额度
        params.put("discountAmount", "0");
        return postPayWithCallBack(UNIFIED_ORDER, PaymentURLConstant.PAYMENT_ORDER + "notify", params);
    }

    @Transactional
    public BasicResult payOrder(long passportId, long orderId, String paymentTypeKey
            , String authCode) throws Exception {
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        TransTypeEnum transTypeEnum = TransTypeEnum
                .getTransTypeEnum(Integer.parseInt(orderEntry.getTransType()));
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentTypeKey);
        long totalPrice = orderEntry.getTotalPrice() < orderEntry.getActualPrice()
                ? orderEntry.getActualPrice() : orderEntry.getTotalPrice();
        BarcodePayResponse barcodePayResponse;
        if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.ALIPAY.getKey())) {
            if (StringUtils.isEmpty(authCode)) {
                return BasicResult.createFailResult("用户条码不能为空！");
            }
            //支付宝条码支付
            barcodePayResponse = aliPayManager.barcodePayment(passportId, orderId, authCode);
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.WEIXIN_NATIVE.getKey())) {
            if (StringUtils.isEmpty(authCode)) {
                return BasicResult.createFailResult("用户条码不能为空！");
            }
            //微信条码支付
            barcodePayResponse = wechatPayManager.barcodePayment(passportId, orderId, authCode
                    , "", "");
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.CASH.getKey())) {
            //现金支付，直接返回成功，不校验
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
                //保存支付信息
                saveTransLog(orderEntry.getSequenceNumber(), passportId, authCode, paymentTypeEnum, transTypeEnum
                        , totalPrice);
                //通知订单系统，订单已经支付
                basicResult = orderManager.paymentOrder(orderId, transTypeEnum.getKey());
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
                    basicResult = offsetBalance(passportId, orderEntry.getSequenceNumber()
                            , offsetAmount, transTypeEnum);
                    if (!basicResult.isSuccess()) {
                        logger.info("余额偏移处理失败！订单ID:" + orderEntry.getId() + "，错误消息：" + basicResult.getMsg());
                    }
                } else if (!basicResult.isSuccess()) {
                    logger.info("订单确认支付失败！订单ID:" + orderEntry.getId() + "，错误消息：" + basicResult.getMsg());
                }
                //返回订单详情-加商品列表
                LifeOrder lifeOrder = orderManager.getLifeOrder(orderId);
                basicResult.setResponse(JSONObject.parseObject(JSONObject.toJSON(lifeOrder).toString()));
                return basicResult;
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

    private void saveTransLog(String transSequenceNumber, Long passportId, String authCode
            , PaymentTypeEnum paymentType, TransTypeEnum transType, Long transAmount) {
        LifePaymentTransactionLogger transactionLogger = new LifePaymentTransactionLogger();
        transactionLogger.setTransSequenceNumber(transSequenceNumber);
        transactionLogger.setPassportId(passportId);
        transactionLogger.setPaymentType(paymentType.getKey());
        transactionLogger.setTransType(transType.getKey());
        transactionLogger.setPartnerId(appConfig.getPosPartnerId());
        transactionLogger.setAppId(appConfig.getPosAppId());
        transactionLogger.setPartnerUserId(passportId + "");
        transactionLogger.setPartnerTradeNumber(authCode);
        transactionLogger.setChannelId(paymentType.getChannelId());
        transactionLogger.setTransUnitAmount(transAmount);
        transactionLogger.setTransNumber(1);
        transactionLogger.setTransTotalAmount(transAmount);
        transactionLogger.setTransTitle(transType.getValue());
        transactionLogger.setUseConpon(GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        transactionLogger.setDiscountAmount(0L);
        transLoggerJpaRepository.save(transactionLogger);
    }

    @Override
    public BasicResult offsetBalance(long passportId, String transSequenceNumber
            , long offsetAmount, TransTypeEnum transTypeEnum) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //余额偏移数额
        params.put("offsetAmount", offsetAmount + "");
        //交易标题
        params.put("transTitle", transTypeEnum.getValue());
        //交易类型
        params.put("transType", transTypeEnum.getKey() + "");
        //交易订单号
        params.put("transSequenceNumber", transSequenceNumber + "");
        return postPay(OFFSET_BALANCE, params);
    }
}
