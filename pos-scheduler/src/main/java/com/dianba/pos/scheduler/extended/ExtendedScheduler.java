package com.dianba.pos.scheduler.extended;

import com.dianba.pos.extended.service.Charge19eManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExtendedScheduler {

    @Autowired
    private Charge19eManager charge19eManager;

    @Scheduled(cron = "* 0/2 * * * ?")
    public void hfCharge() {
        //TODO 花费充值,2分钟运行一次
        charge19eManager.orderListHfCharge();
    }
}
