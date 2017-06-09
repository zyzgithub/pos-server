package com.dianba.pos.scheduler.config;

import com.dianba.pos.foundation.WebApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "com.dianba.pos.scheduler")
@EnableScheduling
public class SchedulerConfig extends WebApplicationConfig {

}
