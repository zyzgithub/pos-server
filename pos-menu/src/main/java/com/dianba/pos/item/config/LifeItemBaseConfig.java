package com.dianba.pos.item.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Configuration
@ComponentScan(basePackages = {
       "com.dianba.pos.menu.controller"
        ,"com.dianba.pos.item.service"
})
public class LifeItemBaseConfig {


    @Value("${lifeItem.datasource.driverClassName}")
    private String driverClassName;

    @Value("${lifeItem.datasource.url}")
    private String url;

    @Value("${lifeItem.datasource.username}")
    private String username;

    @Value("${lifeItem.datasource.password}")
    private String password;

    @Bean(name = "lifeItemDataSource")
    public DataSource buildDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

}
