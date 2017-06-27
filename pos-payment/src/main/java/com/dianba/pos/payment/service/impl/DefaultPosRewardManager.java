package com.dianba.pos.payment.service.impl;

import com.dianba.pos.payment.po.PosMerchantQuota;
import com.dianba.pos.payment.po.PosReward;
import com.dianba.pos.payment.po.PosRewardLogger;
import com.dianba.pos.payment.repository.PosRewardJpaRepository;
import com.dianba.pos.payment.repository.PosRewardLoggerJpaRepository;
import com.dianba.pos.payment.service.PosMerchantQuotaManager;
import com.dianba.pos.payment.service.PosRewardManager;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultPosRewardManager implements PosRewardManager {

    @Autowired
    private PosRewardJpaRepository posRewardJpaRepository;
    @Autowired
    private PosRewardLoggerJpaRepository posRewardLoggerJpaRepository;
    @Autowired
    private PosMerchantQuotaManager posMerchantQuotaManager;

    public Map<String, BigDecimal> getTotalRewardQuota(Integer rewardType) {
        List<PosReward> posRewards = posRewardJpaRepository.findByStatusAndType(1, rewardType);
        BigDecimal rewardAmount = BigDecimal.ZERO;
        BigDecimal totalRewardQuota = BigDecimal.ZERO;
        for (PosReward posReward : posRewards) {
            if (posReward.getStartTime().before(new Date()) && posReward.getEndTime().after(new Date())) {
                rewardAmount = rewardAmount.add(posReward.getReward());
                totalRewardQuota = totalRewardQuota.add(posReward.getBigReward());
            }
        }
        Map<String, BigDecimal> rewardMap = new HashMap<>();
        rewardMap.put("rewardAmount", rewardAmount);
        rewardMap.put("totalRewardQuota", totalRewardQuota);
        return rewardMap;
    }

    public BigDecimal offsetRewardAmount(Long passportId, Long orderId, Integer orderType
            , PaymentTypeEnum paymentTypeEnum) {
        if (OrderTypeEnum.SCAN_ORDER_TYPE.getKey() != orderType
                && OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey() != orderType) {
            return BigDecimal.ZERO;
        }
        //0现金1在线支付
        Integer type;
        if (PaymentTypeEnum.CASH.getKey().equals(paymentTypeEnum.getKey())) {
            type = 0;
        } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(paymentTypeEnum.getKey())
                || PaymentTypeEnum.ALIPAY.getKey().equals(paymentTypeEnum.getKey())) {
            type = 1;
        } else {
            return BigDecimal.ZERO;
        }
        PosMerchantQuota posMerchantQuota = posMerchantQuotaManager.getMerchantQuota(passportId, type);
        if (posMerchantQuota == null || posMerchantQuota.getRemainingReward().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        List<PosReward> posRewards = posRewardJpaRepository.findByStatusAndType(1, type);
        BigDecimal reward = BigDecimal.ZERO;
        for (PosReward posReward : posRewards) {
            if (posReward.getStartTime().before(new Date()) && posReward.getEndTime().after(new Date())) {
                reward = reward.add(posReward.getReward());
            }
        }
        posMerchantQuota.setCurrentReward(posMerchantQuota.getCurrentReward().add(reward));
        posMerchantQuota.setRemainingReward(posMerchantQuota.getTotalRewardQuota()
                .subtract(posMerchantQuota.getCurrentReward()));
        posMerchantQuotaManager.saveMerchantQuota(posMerchantQuota);

        PosRewardLogger posRewardLogger = new PosRewardLogger();
        posRewardLogger.setOrderId(orderId);
        posRewardLogger.setPassportId(passportId);
        posRewardLogger.setType(type);
        posRewardLogger.setReward(reward);
        posRewardLoggerJpaRepository.save(posRewardLogger);
        return reward;
    }
}
