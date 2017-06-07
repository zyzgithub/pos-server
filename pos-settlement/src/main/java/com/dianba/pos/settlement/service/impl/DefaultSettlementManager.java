package com.dianba.pos.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.SettlementOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosMerchantType;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.service.PosMerchantTypeManager;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.settlement.mapper.PosSettlementDaylyMapper;
import com.dianba.pos.settlement.po.PosSettlementDayly;
import com.dianba.pos.settlement.repository.PosSettlementDaylyJpaRepository;
import com.dianba.pos.settlement.service.SettlementManager;
import com.dianba.pos.settlement.vo.PosSettlementDaylyVo;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultSettlementManager implements SettlementManager {

    private static Logger logger = LogManager.getLogger(DefaultSettlementManager.class);

    @Autowired
    private PosSettlementDaylyMapper posSettlementDaylyMapper;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private PosSettlementDaylyJpaRepository settlementDaylyJpaRepository;
    @Autowired
    private PosMerchantTypeManager posMerchantTypeManager;
    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private SettlementOrderManager settlementOrderManager;

    @Transactional
    public BasicResult getSettlementOrder(Long passportId, BigDecimal cashAmount) {
        List<PosSettlementDayly> posSettlementDaylies = settlementDaylyJpaRepository
                .findByPassportIdAndIsPaid(passportId, 0);
        Long merchantPassportId = 0L;
        if (cashAmount == null) {
            cashAmount = BigDecimal.ZERO;
        }
        if (posSettlementDaylies == null || posSettlementDaylies.size() == 0) {
            Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
            merchantPassportId = merchantPassport.getId();
            String dateTime = posSettlementDaylyMapper.findLastSettlementTime(passportId);
            boolean isSettlement = false;
            if (dateTime != null && CommonUtils.isToday(CommonUtils.dateFormatToLong(dateTime))) {
                isSettlement = true;
            }
            //是否已经做了今日结算
            if (!isSettlement) {
                //计算结算信息
                posSettlementDaylies = posSettlementDaylyMapper.statisticsOrderByDay(passportId, dateTime);
                for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
                    BigDecimal amount = posSettlementDayly.getAmount()
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                    posSettlementDayly.setAmount(amount);
                    posSettlementDayly.setCashAmount(cashAmount);
                    posSettlementDayly.setPassportId(passportId);
                    posSettlementDayly.setMerchantPassportId(merchantPassport.getId());
                }
                posSettlementDaylies = settlementDaylyJpaRepository.save(posSettlementDaylies);
            }
        }
        List<PosSettlementDaylyVo> settlementDaylyVos = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PosSettlementDayly settlementDayly : posSettlementDaylies) {
            PosSettlementDaylyVo settlementDaylyVo = new PosSettlementDaylyVo();
            BeanUtils.copyProperties(settlementDayly, settlementDaylyVo);
            if (PaymentTypeEnum.ALIPAY.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.ALIPAY.getValue());
            } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
            } else if (PaymentTypeEnum.CASH.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.CASH.getValue());
            }
            settlementDaylyVos.add(settlementDaylyVo);
            totalAmount = totalAmount.add(settlementDaylyVo.getAmount());
            if (cashAmount == null) {
                cashAmount = settlementDaylyVo.getCashAmount();
            }
            if (merchantPassportId == 0L) {
                merchantPassportId = settlementDaylyVo.getMerchantPassportId();
            }
        }
        PosMerchantType posMerchantType = posMerchantTypeManager.findByPassportId(merchantPassportId);
        boolean isDirectStore = false;
        //TODO 暂时有数据皆为直营店
        if (posMerchantType != null) {
            isDirectStore = true;
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(settlementDaylyVos);
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("totalAmount", totalAmount);
        jsonObject.put("cashAmount", cashAmount);
        jsonObject.put("isDirectStore", isDirectStore ? 1 : 0);
        return basicResult;
    }

    @Transactional
    public BasicResult settlementShift(Long passportId) throws Exception {
        List<PosSettlementDayly> posSettlementDaylies = settlementDaylyJpaRepository
                .findByPassportIdAndIsPaid(passportId, 0);
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        PosMerchantType posMerchantType = posMerchantTypeManager.findByPassportId(merchantPassport.getId());
        if (posMerchantType != null && posSettlementDaylies.size() > 0) {
            throw new PosAccessDeniedException("直营店结算请先支付！");
        }
        for (PosSettlementDayly settlementDayly : posSettlementDaylies) {
            settlementDayly.setIsPaid(1);
        }
        if (posSettlementDaylies.size() > 0) {
            settlementDaylyJpaRepository.save(posSettlementDaylies);
        }
        return BasicResult.createSuccessResult();
    }

    public BasicResult settlementPay(Long passportId, String paymentType, String authCode) throws Exception {
        List<PosSettlementDayly> posSettlementDaylies = settlementDaylyJpaRepository
                .findByPassportIdAndIsPaid(passportId, 0);
        BigDecimal cashAmount = BigDecimal.ZERO;
        for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
            if (posSettlementDayly.getPaymentType().equals(PaymentTypeEnum.CASH.getKey())) {
                cashAmount = posSettlementDayly.getAmount();
                break;
            }
        }
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(PaymentTypeEnum.ALIPAY.getKey());
        paymentTypes.add(PaymentTypeEnum.WEIXIN_NATIVE.getKey());
        if (!paymentTypes.contains(paymentType)) {
            throw new PosAccessDeniedException("请使用微信或者支付宝支付！");
        }
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentType);
        LifeOrder lifeOrder = settlementOrderManager.generateSettlementOrder(passportId, paymentTypeEnum, cashAmount);
        if (lifeOrder == null) {
            return BasicResult.createFailResult("支付失败！订单异常！");
        }
        BasicResult basicResult = paymentManager.payOrder(passportId, lifeOrder.getId(), paymentType, authCode);
        if (basicResult.isSuccess()) {
            for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
                posSettlementDayly.setIsPaid(1);
            }
        }
        settlementDaylyJpaRepository.save(posSettlementDaylies);
        return basicResult;
    }
}
