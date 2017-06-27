package com.dianba.pos.payment.service.impl;

import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.payment.po.PosMerchantQuota;
import com.dianba.pos.payment.repository.PosMerchantQuotaJpaRepository;
import com.dianba.pos.payment.service.PosMerchantQuotaManager;
import com.dianba.pos.payment.service.PosRewardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class DefaultPosMerchantQuotaManager implements PosMerchantQuotaManager {

    @Autowired
    private PosMerchantQuotaJpaRepository posMerchantQuotaJpaRepository;
    @Autowired
    private PosRewardManager posRewardManager;

    public PosMerchantQuota getMerchantQuota(Long passportId, Integer rewardType) {
        Integer period = Integer.parseInt(DateUtil.getNowTime("yyyyMM"));
        PosMerchantQuota posMerchantQuota = posMerchantQuotaJpaRepository
                .findByPassportIdAndRewardTypeAndPeriod(passportId, rewardType, period);
        Map<String, BigDecimal> rewardMap = posRewardManager.getTotalRewardQuota(rewardType);
        BigDecimal rewardAmount = rewardMap.get("rewardAmount");
        BigDecimal totalRewardQuota = rewardMap.get("totalRewardQuota");
        if (posMerchantQuota == null) {
            if (rewardAmount.compareTo(BigDecimal.ZERO) > 0 && totalRewardQuota.compareTo(BigDecimal.ZERO) > 0) {
                posMerchantQuota = new PosMerchantQuota();
                posMerchantQuota.setPeriod(period);
                posMerchantQuota.setPassportId(passportId);
                posMerchantQuota.setRewardType(rewardType);
                posMerchantQuota.setCurrentReward(BigDecimal.ZERO);
                posMerchantQuota.setRemainingReward(totalRewardQuota);
                posMerchantQuota.setTotalRewardQuota(totalRewardQuota);
                posMerchantQuota = saveMerchantQuota(posMerchantQuota);
            }
        } else {
            if (totalRewardQuota.compareTo(posMerchantQuota.getTotalRewardQuota()) != 0) {
                posMerchantQuota.setRemainingReward(totalRewardQuota.subtract(posMerchantQuota.getCurrentReward()));
                posMerchantQuota.setTotalRewardQuota(totalRewardQuota);
            }
        }
        return posMerchantQuota;
    }

    @Transactional
    public PosMerchantQuota saveMerchantQuota(PosMerchantQuota posMerchantQuota) {
        return posMerchantQuotaJpaRepository.save(posMerchantQuota);
    }
}
