package com.dianba.pos.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.common.util.DateUtil;
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

    public BasicResult getSettlementOrder(Long passportId) {
        Long merchantPassportId = 0L;
        List<PosSettlementDayly> posSettlementDaylies = statisticsOrder(passportId);
        List<PosSettlementDaylyVo> settlementDaylyVos = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PosSettlementDayly settlementDayly : posSettlementDaylies) {
            PosSettlementDaylyVo settlementDaylyVo = new PosSettlementDaylyVo();
            BeanUtils.copyProperties(settlementDayly, settlementDaylyVo);
            if (PaymentTypeEnum.ALIPAY.getKey().equals(settlementDaylyVo.getPaymentType())
                    || PaymentTypeEnum.ALIPAY_JS.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.ALIPAY.getValue());
            } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(settlementDaylyVo.getPaymentType())
                    || PaymentTypeEnum.WEIXIN_JS.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
            } else if (PaymentTypeEnum.CASH.getKey().equals(settlementDaylyVo.getPaymentType())) {
                settlementDaylyVo.setPaymentTypeTitle(PaymentTypeEnum.CASH.getValue());
            }
            settlementDaylyVos.add(settlementDaylyVo);
            totalAmount = totalAmount.add(settlementDaylyVo.getAmount());
            if (merchantPassportId == 0L) {
                merchantPassportId = settlementDaylyVo.getMerchantPassportId();
            }
        }
        PosMerchantType posMerchantType = posMerchantTypeManager.findByPassportId(merchantPassportId);
        boolean isDirectStore = false;
        if (posMerchantType != null) {
            isDirectStore = true;
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(settlementDaylyVos);
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("totalAmount", totalAmount);
        jsonObject.put("isDirectStore", isDirectStore ? 1 : 0);
        return basicResult;
    }

    private List<PosSettlementDayly> statisticsOrder(Long passportId) {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        String dateTime = posSettlementDaylyMapper.findLastSettlementTime(passportId);
        //计算结算信息
        List<PosSettlementDayly> posSettlementDaylies = posSettlementDaylyMapper
                .statisticsOrderByDay(passportId, dateTime);
        for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
            BigDecimal amount = posSettlementDayly.getAmount()
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            posSettlementDayly.setAmount(amount);
            posSettlementDayly.setPassportId(passportId);
            posSettlementDayly.setMerchantPassportId(merchantPassport.getId());
        }
        return posSettlementDaylies;
    }

    @Transactional
    public BasicResult settlementShift(Long passportId, BigDecimal cashAmount) throws Exception {
        List<PosSettlementDayly> posSettlementDaylies = statisticsOrder(passportId);
        if (posSettlementDaylies == null || posSettlementDaylies.isEmpty()) {
            return BasicResult.createSuccessResult();
        }
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        PosMerchantType posMerchantType = posMerchantTypeManager.findByPassportId(merchantPassport.getId());
        if (posMerchantType != null) {
            throw new PosAccessDeniedException("直营店结算请先支付！");
        }
        for (PosSettlementDayly settlementDayly : posSettlementDaylies) {
            settlementDayly.setIsPaid(1);
            settlementDayly.setCashAmount(cashAmount);
        }
        settlementDaylyJpaRepository.save(posSettlementDaylies);
        return BasicResult.createSuccessResult();
    }

    public BasicResult settlementPay(Long passportId, String paymentType, String authCode
            , BigDecimal cashAmount) throws Exception {
        List<PosSettlementDayly> posSettlementDaylies = statisticsOrder(passportId);
        if (posSettlementDaylies == null || posSettlementDaylies.isEmpty()) {
            return BasicResult.createSuccessResult();
        }
        BigDecimal realCashAmount = BigDecimal.ZERO;
        for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
            if (posSettlementDayly.getPaymentType().equals(PaymentTypeEnum.CASH.getKey())) {
                realCashAmount = posSettlementDayly.getAmount();
            }
            posSettlementDayly.setCashAmount(cashAmount);
        }
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(PaymentTypeEnum.ALIPAY.getKey());
        paymentTypes.add(PaymentTypeEnum.WEIXIN_NATIVE.getKey());
        if (!paymentTypes.contains(paymentType)) {
            throw new PosAccessDeniedException("请使用微信或者支付宝支付！");
        }
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentType);
        LifeOrder lifeOrder = settlementOrderManager.generateSettlementOrder(passportId
                , paymentTypeEnum, realCashAmount);
        if (lifeOrder == null) {
            return BasicResult.createFailResult("支付失败！订单异常！");
        }
        BasicResult basicResult = paymentManager.payOrder(passportId, lifeOrder.getId(), paymentType, authCode);
        String date = null;
        if (basicResult.isSuccess()) {
            for (PosSettlementDayly posSettlementDayly : posSettlementDaylies) {
                posSettlementDayly.setIsPaid(1);
                if (date == null) {
                    date = DateUtil.DateToString(posSettlementDayly.getCreateTime(), DateUtil.FORMAT_ONE);
                }
            }
            settlementDaylyJpaRepository.save(posSettlementDaylies);
            //更新指定收银员操作的指定日期前的订单为已经结算状态
            settlementOrderManager.updateSettlementCashOrderByUserAndDate(passportId, date);
        }
        return basicResult;
    }
}
