package com.dianba.pos.scheduler.extended;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExtendedScheduler {

    @Scheduled(cron = "* 0/2 * * * ?")
    public void hfCharge() {
        //TODO 花费充值,2分钟运行一次
    }
}
