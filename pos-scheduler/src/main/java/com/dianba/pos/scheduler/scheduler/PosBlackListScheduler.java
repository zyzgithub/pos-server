package com.dianba.pos.scheduler.scheduler;

import com.dianba.pos.scheduler.service.ExceptionOrderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyong on 2017/7/5.
        */
@Component
public class PosBlackListScheduler {
    private Logger logger = LogManager.getLogger(PosBlackListScheduler.class);

    @Autowired
    private ExceptionOrderManager exceptionOrderManager;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void posBlackCheckOut() {
        logger.info("======================刷单检测开启中=============================");
        exceptionOrderManager.checkBlackPassport();
        logger.info("======================刷单检测结束了=============================");
    }
}
