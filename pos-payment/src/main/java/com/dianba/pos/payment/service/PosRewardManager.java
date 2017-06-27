package com.dianba.pos.payment.service;

import com.xlibao.common.constant.payment.PaymentTypeEnum;

import java.math.BigDecimal;
import java.util.Map;

public interface PosRewardManager {

    Map<String, BigDecimal> getTotalRewardQuota(Integer rewardType);

    BigDecimal offsetRewardAmount(Long passportId, Long orderId, Integer orderType
            , PaymentTypeEnum paymentTypeEnum);
}
