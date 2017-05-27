package com.dianba.pos.item.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.dianba.pos.base.druid.config.AbstractDruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.dianba.pos.item.repository"}
        , entityManagerFactoryRef = "lifeItemJpaEntityManagerFactory"
        , transactionManagerRef = "lifeItemJpaTransactionManager")
public class LifeItemJpaDataSourceConfig extends AbstractDruidDataSource {

    private static final String PACKAGES_TO_SCAN = "com.dianba.pos.item.po";

    @Resource(name = "lifeItemDataSource")
    private DruidDataSource druidDataSource;

    @Bean(name = "lifeItemJpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean lifeItemJpaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName("life_item");
        factory.setDataSource(druidDataSource);
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setPackagesToScan(PACKAGES_TO_SCAN.split(","));
        factory.setJpaProperties(createJpaProperties());
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean(name = "lifeItemJpaTransactionManager")
    public PlatformTransactionManager lifeItemJpaTransactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(lifeItemJpaEntityManagerFactory().getObject());
        return manager;
    }
}
