package com.dianba.pos.scheduler.config;

import com.dianba.pos.base.WebApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "com.dianba.pos.scheduler")
@EnableScheduling
public class SchedulerConfig extends WebApplicationConfig {

}
