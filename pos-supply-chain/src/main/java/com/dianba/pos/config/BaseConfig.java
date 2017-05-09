package com.dianba.pos.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
        "com.dianba.supplychain.controller",
        "com.dianba.supplychain.service"
})
public class BaseConfig {

    @Value("${supplychain.datasource.driverClassName}")
    private String driverClassName;

    @Value("${supplychain.datasource.url}")
    private String url;

    @Value("${supplychain.datasource.username}")
    private String username;

    @Value("${supplychain.datasource.password}")
    private String password;

    @Bean(name = "supplyChainDataSource")
    public DataSource buildDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
