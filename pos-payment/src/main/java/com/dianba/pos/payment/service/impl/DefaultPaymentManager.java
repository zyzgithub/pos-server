package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.item.service.PosItemManager;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.vo.LifeOrderVo;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosMerchantRate;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.service.PosMerchantRateManager;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.AliPayManager;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.TransLoggerManager;
import com.dianba.pos.payment.service.WechatPayManager;
import com.dianba.pos.payment.support.PaymentRemoteService;
import com.dianba.pos.payment.vo.PassportCurrencyVo;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultPaymentManager extends PaymentRemoteService implements PaymentManager {

    private static Logger logger = LogManager.getLogger(DefaultAliPayManager.class);

    @Autowired
    private LifeOrderManager orderManager;
    @Autowired
    private AliPayManager aliPayManager;
    @Autowired
    private WechatPayManager wechatPayManager;
    @Autowired
    private TransLoggerManager transLoggerManager;
    @Autowired
    private PosMerchantRateManager posMerchantRateManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private PosItemManager posItemManager;

    @Autowired
    private AppConfig appConfig;

    public BasicResult balancePayment(long passportId, long orderId, String paymentPassword) {
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //支付类型
        params.put("paymentType", PaymentTypeEnum.VIP_BALANCE.getKey());
        //交易类型
        params.put("transType", TransTypeEnum.SUPPLYCHAIN_INCOME.getKey() + "");
        //合作商户ID
        params.put("partnerUserId", passportId + "");
        //订单ID/订单批次编号
        params.put("partnerTradeNumber", orderEntry.getSequenceNumber());
        //交易单位金额
        params.put("transUnitAmount", String.valueOf(orderEntry.getTotalPrice()));
        //交易单位数量
        params.put("transNumber", "1");
        //交易总金额
        params.put("transTotalAmount", String.valueOf(orderEntry.getTotalPrice()));
        //交易标题
        params.put("transTitle", TransTypeEnum.SUPPLYCHAIN_INCOME.getValue());
        //交易备注
        params.put("remark", "POS-余额支付");
        //是否使用优惠券
        params.put("useConpon", "0");
        //优惠额度
        params.put("discountAmount", "0");
        //预支付
        BasicResult basicResult = postPayWithCallBack(UNIFIED_ORDER, UNIFIED_ORDER, params);
        if (basicResult.isSuccess()) {
            String prePaymentId = basicResult.getResponse().get("prePaymentId").toString();
            params = new HashMap<>();
            Map<String, String> paymentParams = new HashMap<>();
            paymentParams.put("prePaymentId", prePaymentId);
            paymentParams.put("timeStamp", System.currentTimeMillis() + "");
            signatureParams(paymentParams);
            JSONObject jsonObject = new JSONObject();
            for (String key : paymentParams.keySet()) {
                jsonObject.put(key, paymentParams.get(key));
            }
            params.put("passportId", passportId + "");
            params.put("paymentParameter", jsonObject.toJSONString());
            params.put("paymentPassword", paymentPassword);
            basicResult = postPay(BALANCE_PAYMENT, params);
            if (basicResult.isSuccess()) {
                return completeOrder(basicResult, orderEntry, passportId
                        , PaymentTypeEnum.BALANCE, TransTypeEnum.PAYMENT);
            }
        }
        return basicResult;
    }

    public BasicResult checkPayPasswordKey(Long passportId, String paymentPassword) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", passportId + "");
        params.put("paymentPassword", paymentPassword);
        return postPay(PAYMENT_PASSWORD_VAILD, params);
    }

    public BasicResult passportCurrency(long passportId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", passportId + "");
        BasicResult basicResult = postPay(PASSPORT_CURRENCY, params);
        if (basicResult.isSuccess()) {
            JSONArray jsonArray = basicResult.getResponseDatas();
            List<PassportCurrencyVo> passportCurrencyVos = JsonHelper.toList(jsonArray, PassportCurrencyVo.class);
            for (PassportCurrencyVo passportCurrency : passportCurrencyVos) {
                passportCurrency.setCurrentAmount(passportCurrency.getCurrentAmount()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                passportCurrency.setFreezeAmount(passportCurrency.getFreezeAmount()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            }
            basicResult.setResponseDatas(passportCurrencyVos);
        }
        return basicResult;
    }

    public BasicResult payOrder(long passportId, long orderId, String paymentTypeKey
            , String authCode) throws Exception {
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        TransTypeEnum transTypeEnum = TransTypeEnum.PAYMENT;
        if (OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey() == orderEntry.getType()) {
            transTypeEnum = TransTypeEnum.SUPPLYCHAIN_INCOME;
        }
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
        } else {
            throw new Exception("不支持的支付类型！" + paymentTypeEnum.getKey());
        }
        BasicResult basicResult = BasicResult.createFailResult();
        if (barcodePayResponse.isSuccess()) {
            //保存支付信息
            transLoggerManager.saveTransLog(orderEntry.getSequenceNumber()
                    , passportId, authCode, paymentTypeEnum, transTypeEnum, totalPrice);
            return completeOrder(basicResult, orderEntry, passportId, paymentTypeEnum, transTypeEnum);
        } else {
            basicResult = BasicResult.createFailResult(barcodePayResponse.getMsg());
            logger.info("支付失败！订单ID：" + orderId + "，返回：" + barcodePayResponse.getMsg()
                    + "，body：" + barcodePayResponse.getResponse().getBody().toString());
            return basicResult;
        }
    }

    private BasicResult completeOrder(BasicResult basicResult, OrderEntry orderEntry
            , Long passportId, PaymentTypeEnum paymentTypeEnum, TransTypeEnum transTypeEnum) {
        try {
            //通知订单系统，订单已经支付
            basicResult = orderManager.paymentOrder(orderEntry.getId(), paymentTypeEnum);
            Map<Long, Integer> itemIdMaps = new HashMap<>();
            //修改商品库存
            if (OrderTypeEnum.SCAN_ORDER_TYPE.getKey() == orderEntry.getType()) {
                for (OrderItemSnapshot itemSnapshot : orderEntry.getItemSnapshots()) {
                    itemIdMaps.put(itemSnapshot.getItemId(), itemSnapshot.getNormalQuantity());
                }
                posItemManager.offsetItemRepertory(itemIdMaps);
            }
            if (basicResult.isSuccess() && !paymentTypeEnum.equals(PaymentTypeEnum.CASH)) {
                Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
                //对商家余额进行偏移计算
                OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderEntry.getType());
                long offsetAmount = 0;
                if (orderTypeEnum.getKey() == OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey()) {
                    if (orderEntry.getTotalPrice() > orderEntry.getActualPrice()) {
                        offsetAmount = orderEntry.getTotalPrice() - orderEntry.getActualPrice();
                    }
                } else if (orderTypeEnum.getKey() == OrderTypeEnum.SCAN_ORDER_TYPE.getKey()) {
                    //进行扣点计算
                    PosMerchantRate posMerchantRate = posMerchantRateManager
                            .findByMerchantPassportId(merchantPassport.getId());
                    BigDecimal commissionRate = PosMerchantRate.COMMISSION_RATE;
                    if (posMerchantRate != null) {
                        if (1 == posMerchantRate.getIsNeed()) {
                            commissionRate = posMerchantRate.getCommissionRate();
                        } else {
                            commissionRate = BigDecimal.ZERO;
                        }
                    }
                    BigDecimal amount = BigDecimal.valueOf(orderEntry.getTotalPrice()).subtract(
                            BigDecimal.valueOf(orderEntry.getTotalPrice()).multiply(commissionRate)
                    ).setScale(0, BigDecimal.ROUND_HALF_UP);
                    offsetAmount = amount.longValue();
                } else if (orderTypeEnum.getKey() == OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey()) {
                    offsetAmount = -orderEntry.getTotalPrice();
                } else if (orderTypeEnum.getKey() == OrderTypeEnum.POS_SETTLEMENT_ORDER_TYPE.getKey()) {
                    offsetAmount = orderEntry.getTotalPrice();
                }
                if (offsetAmount != 0) {
                    //对商家余额进行余额偏移
                    basicResult = offsetBalance(merchantPassport.getId(), orderEntry.getSequenceNumber()
                            , offsetAmount, transTypeEnum);
                    if (!basicResult.isSuccess()) {
                        logger.info("余额偏移处理失败！订单ID:" + orderEntry.getId()
                                + "，错误消息：" + basicResult.getMsg());
                    }
                }
            } else if (!basicResult.isSuccess()) {
                logger.info("订单确认支付失败！订单ID:" + orderEntry.getId() + "，错误消息：" + basicResult.getMsg());
            }
            //返回订单详情-加商品列表
            LifeOrderVo lifeOrderVo = orderManager.getLifeOrder(orderEntry.getId());
            basicResult.setResponse(JSONObject.parseObject(JSONObject.toJSON(lifeOrderVo).toString()));
            return basicResult;
        } catch (Exception e) {
            logger.error("订单保存异常!" + e.getMessage());
        }
        return basicResult;
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
