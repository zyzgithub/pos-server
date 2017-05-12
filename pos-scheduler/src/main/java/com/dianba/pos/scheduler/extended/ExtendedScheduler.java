package com.dianba.pos.scheduler.extended;

import com.dianba.pos.extended.service.Charge19eManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExtendedScheduler {

    private Logger logger = LogManager.getLogger(ExtendedScheduler.class);

    @Autowired
    private Charge19eManager charge19eManager;

    @Scheduled(cron = "0/2 * * * * ?")
    public void hfCharge() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().substring(0, 6);
        logger.info(id + "########话费流量充值定时任务开始#############");
        charge19eManager.orderListHfCharge();
        charge19eManager.orderListHfCharge();
        charge19eManager.orderListFlowCharge();
        logger.info(id + "########话费流量充值定时任务结束#############");
    }
}
