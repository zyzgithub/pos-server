package com.dianba.pos.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.dianba.supplychain.controller",
        "com.dianba.supplychain.service"
})
public class BaseConfig {
}
