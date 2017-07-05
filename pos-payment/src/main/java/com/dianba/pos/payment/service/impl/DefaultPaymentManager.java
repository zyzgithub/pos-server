package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.item.service.PosItemManager;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.po.LifeOrderItemSnapshot;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.vo.LifeOrderVo;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosMerchantRate;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.service.PosMerchantRateManager;
import com.dianba.pos.payment.po.LifePaymentCurrencyAccount;
import com.dianba.pos.payment.po.LifePaymentCurrencyOffsetLogger;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.repository.LifePaymentCurrencyAccountJpaRepository;
import com.dianba.pos.payment.repository.LifePaymentCurrencyOffsetLoggerJpaRepository;
import com.dianba.pos.payment.service.*;
import com.dianba.pos.payment.support.PaymentRemoteService;
import com.dianba.pos.payment.vo.PassportCurrencyVo;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private WeChatPayManager weChatPayManager;
    @Autowired
    private TransLoggerManager transLoggerManager;
    @Autowired
    private PosMerchantRateManager posMerchantRateManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private PosItemManager posItemManager;
    @Autowired
    private LifePaymentCurrencyAccountJpaRepository currencyAccountJpaRepository;
    @Autowired
    private LifePaymentCurrencyOffsetLoggerJpaRepository currencyOffsetLoggerJpaRepository;
    @Autowired
    private PosRewardManager posRewardManager;

    public BasicResult balancePayment(long passportId, long orderId, String paymentPassword) {
        LifeOrder lifeOrder = orderManager.getLifeOrder(orderId, false);
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //支付类型
        params.put("paymentType", PaymentTypeEnum.VIP_BALANCE.getKey());
        //交易类型
        params.put("transType", TransTypeEnum.SUPPLYCHAIN_INCOME.getKey() + "");
        //合作商户ID
        params.put("partnerUserId", passportId + "");
        //订单ID/订单批次编号
        params.put("partnerTradeNumber", lifeOrder.getSequenceNumber());
        //交易单位金额
        params.put("transUnitAmount", String.valueOf(lifeOrder.getTotalPrice()));
        //交易单位数量
        params.put("transNumber", "1");
        //交易总金额
        params.put("transTotalAmount", String.valueOf(lifeOrder.getTotalPrice()));
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
                return completeOrder(lifeOrder, PaymentTypeEnum.BALANCE, TransTypeEnum.PAYMENT, true);
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
        LifeOrder lifeOrder = orderManager.getLifeOrder(orderId, false);
        TransTypeEnum transTypeEnum = TransTypeEnum.PAYMENT;
        if (OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey() == lifeOrder.getType()) {
            transTypeEnum = TransTypeEnum.SUPPLYCHAIN_INCOME;
        }
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentTypeKey);
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
            barcodePayResponse = weChatPayManager.barcodePayment(passportId, orderId, authCode
                    , "", "");
        } else if (paymentTypeEnum.getKey().equals(PaymentTypeEnum.CASH.getKey())) {
            //现金支付，直接返回成功，不校验
            barcodePayResponse = BarcodePayResponse.createSuccessResult();
        } else {
            throw new Exception("不支持的支付类型！" + paymentTypeEnum.getKey());
        }
        if (barcodePayResponse.isSuccess()) {
            return processPaidOrder(lifeOrder, authCode, paymentTypeEnum, true);
        } else {
            logger.info("支付失败！订单ID：" + orderId + "，返回：" + barcodePayResponse.getMsg());
            return BasicResult.createFailResult(barcodePayResponse.getMsg());
        }
    }

    private BasicResult completeOrder(LifeOrder lifeOrder
            , PaymentTypeEnum paymentTypeEnum, TransTypeEnum transTypeEnum
            , boolean returnOrderInfo) {
        BasicResult basicResult = BasicResult.createSuccessResult();
        try {
            //通知订单系统，订单已经支付
            basicResult = orderManager.paymentOrder(lifeOrder.getId(), paymentTypeEnum);
            Map<Long, Integer> itemIdMaps = new HashMap<>();
            //修改商品库存
            if (OrderTypeEnum.SCAN_ORDER_TYPE.getKey() == lifeOrder.getType()) {
                for (LifeOrderItemSnapshot itemSnapshot : lifeOrder.getItemSnapshots()) {
                    itemIdMaps.put(itemSnapshot.getItemId(), itemSnapshot.getNormalQuantity());
                }
                posItemManager.offsetItemRepertory(itemIdMaps);
            }
            BigDecimal offsetRewardAmount = BigDecimal.ZERO;
            if (basicResult.isSuccess()) {
                if (!paymentTypeEnum.equals(PaymentTypeEnum.CASH)) {
                    //对商家余额进行偏移计算
                    OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(lifeOrder.getType());
                    long offsetAmount = 0;
                    if (orderTypeEnum.getKey() == OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey()) {
                        if (lifeOrder.getTotalPrice().compareTo(lifeOrder.getActualPrice()) > 0) {
                            offsetAmount = lifeOrder.getTotalPrice().subtract(lifeOrder.getActualPrice()).longValue();
                        }
                    } else if (orderTypeEnum.getKey() == OrderTypeEnum.SCAN_ORDER_TYPE.getKey()) {
                        //进行扣点计算
                        PosMerchantRate posMerchantRate = posMerchantRateManager
                                .findByMerchantPassportId(lifeOrder.getShippingPassportId());
                        BigDecimal commissionRate = PosMerchantRate.COMMISSION_RATE;
                        if (posMerchantRate != null) {
                            if (1 == posMerchantRate.getIsNeed()) {
                                commissionRate = posMerchantRate.getCommissionRate();
                            } else {
                                commissionRate = BigDecimal.ZERO;
                            }
                        }
                        BigDecimal amount = lifeOrder.getTotalPrice().subtract(
                                lifeOrder.getTotalPrice().multiply(commissionRate)
                        ).setScale(0, BigDecimal.ROUND_HALF_UP);
                        offsetAmount = amount.longValue();
                    } else if (orderTypeEnum.getKey() == OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey()) {
                        offsetAmount = -lifeOrder.getTotalPrice().longValue();
                    } else if (orderTypeEnum.getKey() == OrderTypeEnum.POS_SETTLEMENT_ORDER_TYPE.getKey()) {
                        offsetAmount = lifeOrder.getTotalPrice().longValue();
                    }
                    if (offsetAmount != 0) {
                        //对商家余额进行余额偏移
                        basicResult = offsetBalance(lifeOrder.getShippingPassportId(), lifeOrder.getSequenceNumber()
                                , offsetAmount, transTypeEnum);
                        if (!basicResult.isSuccess()) {
                            logger.info("余额偏移处理失败！订单ID:" + lifeOrder.getId()
                                    + "，错误消息：" + basicResult.getMsg());
                        }
                    }
                }
                //消费返现
                offsetRewardAmount = posRewardManager.offsetRewardAmount(lifeOrder.getShippingPassportId()
                        , lifeOrder.getId(), lifeOrder.getType(), paymentTypeEnum);
                offsetVipBalance(lifeOrder.getShippingPassportId(), lifeOrder.getSequenceNumber()
                        , offsetRewardAmount, paymentTypeEnum);
            } else {
                logger.info("订单确认支付失败！订单ID:" + lifeOrder.getId() + "，错误消息：" + basicResult.getMsg());
            }
            if (returnOrderInfo) {
                //返回订单详情-加商品列表
                LifeOrderVo lifeOrderVo = orderManager.getLifeOrder(lifeOrder.getId());
                basicResult.setResponse(lifeOrderVo);
                basicResult.getResponse().put("rewardAmount", offsetRewardAmount
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                return basicResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basicResult;
    }

    public BasicResult offsetBalance(Long passportId, String transSequenceNumber
            , Long offsetAmount, TransTypeEnum transTypeEnum) {
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

    @Transactional
    public void offsetVipBalance(Long passportId, String transSequenceNumber
            , BigDecimal offsetAmount, PaymentTypeEnum paymentTypeEnum) {
        Passport passport = passportManager.findById(passportId);
        if (offsetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        LifePaymentCurrencyAccount paymentCurrencyAccount = currencyAccountJpaRepository
                .findByPassportIdAndChannelIdAndCurrencyType(passportId, passport.getFromChannel()
                        , CurrencyTypeEnum.VIP_BALANCE.getKey());
        LifePaymentCurrencyOffsetLogger currencyOffsetLogger = new LifePaymentCurrencyOffsetLogger();
        currencyOffsetLogger.setPassportId(passportId);
        currencyOffsetLogger.setChannelId(passport.getFromChannel());
        currencyOffsetLogger.setCurrencyType(CurrencyTypeEnum.VIP_BALANCE.getKey());
        currencyOffsetLogger.setBeforeAmount(paymentCurrencyAccount.getCurrentAmount());
        currencyOffsetLogger.setOffsetAmount(offsetAmount);
        currencyOffsetLogger.setAfterAmount(paymentCurrencyAccount.getCurrentAmount().add(offsetAmount));
        currencyOffsetLogger.setTransType(paymentTypeEnum.getKey());
        currencyOffsetLogger.setTransTitle(paymentTypeEnum.getValue());
        currencyOffsetLogger.setRelationTransType(TransTypeEnum.PAYMENT.getKey());
        currencyOffsetLogger.setRelationTransSequence(transSequenceNumber);
        paymentCurrencyAccount.setCurrentAmount(paymentCurrencyAccount.getCurrentAmount().add(offsetAmount));
        if (offsetAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentCurrencyAccount.setTotalIntoAmount(paymentCurrencyAccount.getTotalIntoAmount().add(offsetAmount));
        } else {
            paymentCurrencyAccount.setTotalOutputAmount(paymentCurrencyAccount.getTotalOutputAmount()
                    .add(offsetAmount.abs()));
        }
        transLoggerManager.saveTransLog(transSequenceNumber, passportId, "", paymentTypeEnum
                , TransTypeEnum.RECHARGE, offsetAmount.longValue());
        currencyAccountJpaRepository.save(paymentCurrencyAccount);
        currencyOffsetLoggerJpaRepository.save(currencyOffsetLogger);
    }

    @Override
    public BasicResult processPaidOrder(LifeOrder lifeOrder, String userCode, PaymentTypeEnum paymentTypeEnum
            , boolean returnOrderInfo) {
        TransTypeEnum transTypeEnum = TransTypeEnum.PAYMENT;
        BasicResult basicResult = completeOrder(lifeOrder, paymentTypeEnum, transTypeEnum, returnOrderInfo);
        if (basicResult.isSuccess()) {
            //保存支付信息
            transLoggerManager.saveTransLog(lifeOrder.getSequenceNumber()
                    , lifeOrder.getShippingPassportId(), userCode
                    , paymentTypeEnum, transTypeEnum, lifeOrder.getTotalPrice().longValue());
        }
        return basicResult;
    }

    public BasicResult processPaidOrder(String sequenceNum, String userCode, PaymentTypeEnum paymentTypeEnum
            , boolean returnOrderInfo) {
        LifeOrder lifeOrder = orderManager.getLifeOrder(sequenceNum, false);
        return processPaidOrder(lifeOrder, userCode, paymentTypeEnum, returnOrderInfo);
    }
}
