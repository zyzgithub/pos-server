package com.dianba.pos.box.service.impl;

import com.dianba.pos.box.po.BoxAccountLog;
import com.dianba.pos.box.repository.BoxAccountLogJpaRepository;
import com.dianba.pos.box.service.BoxAccountLogManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DefaultBoxAccountLogManager implements BoxAccountLogManager {

    private Logger logger = LogManager.getLogger(DefaultBoxAccountLogManager.class);

    @Autowired
    private BoxAccountLogJpaRepository boxAccountLogJpaRepository;

    @Transactional
    public void saveOpenLog(String openId) {
        BoxAccountLog boxAccountLog = new BoxAccountLog();
        boxAccountLog.setOpenId(openId);
        boxAccountLog.setOpenTime(new Date());
        boxAccountLogJpaRepository.save(boxAccountLog);
    }

    @Transactional
    public void saveLeaveLog(String openId) {
        BoxAccountLog boxAccountLog = boxAccountLogJpaRepository.findByOpenIdAndLeaveTimeIsNull(openId);
        if (boxAccountLog == null) {
            logger.warn("leave by warn way, openid:" + openId);
        } else {
            boxAccountLog.setLeaveTime(new Date());
            boxAccountLogJpaRepository.save(boxAccountLog);
        }
    }
}
