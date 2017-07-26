package com.dianba.pos.box.service.impl;

import com.dianba.pos.box.po.BoxDoorInfo;
import com.dianba.pos.box.repository.BoxDoorInfoJpaRepository;
import com.dianba.pos.box.service.BoxAccountLogManager;
import com.dianba.pos.box.service.BoxDoorInfoManager;
import com.dianba.pos.box.service.BoxOrderManager;
import com.dianba.pos.core.annotation.EternalCacheable;
import com.dianba.pos.order.po.LifeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultBoxDoorInfoManager implements BoxDoorInfoManager {

    @Autowired
    private BoxDoorInfoJpaRepository boxDoorInfoJpaRepository;
    @Autowired
    private BoxOrderManager boxOrderManager;
    @Autowired
    private BoxAccountLogManager boxAccountLogManager;

    public BoxDoorInfo getDoorInfoByPassportId(Long passportId) {
        return boxDoorInfoJpaRepository.findByPassportId(passportId);
    }

    @EternalCacheable
    public BoxDoorInfo getDoorInfoByAccessSN(String accessSN) {
        return boxDoorInfoJpaRepository.findByAccessSN(accessSN);
    }

    @Override
    public void saveLeaveLog(Long passportId, String rfids) {
        LifeOrder lifeOrder = boxOrderManager.getOrderByRfids(passportId, rfids);
        if (lifeOrder != null) {
            String openId = lifeOrder.getReceiptUserId();
            boxAccountLogManager.saveLeaveLog(openId);
        }
    }
}
