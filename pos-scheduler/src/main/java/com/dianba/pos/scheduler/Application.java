package com.dianba.pos.scheduler;

import com.dianba.pos.scheduler.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;


public class Application {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerConfig.class, args);
    }
}
