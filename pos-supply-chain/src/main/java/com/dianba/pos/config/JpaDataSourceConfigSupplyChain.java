package com.dianba.pos.config;


import com.dianba.pos.foundation.druid.config.AbstractDruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.dianba.supplychain.repository"}
        , entityManagerFactoryRef = "supplyChainJpaEntityManagerFactory"
        , transactionManagerRef = "supplyChainJpaTransactionManager")
public class JpaDataSourceConfigSupplyChain extends AbstractDruidDataSource {

    @Value("${supplychain.datasource.driverClassName}")
    private String driverClassName;

    @Value("${supplychain.datasource.url}")
    private String url;

    @Value("${supplychain.datasource.username}")
    private String username;

    @Value("${supplychain.datasource.password}")
    private String password;

    private static final String PACKAGES_TO_SCAN = "com.dianba.supplychain.po";

    @Bean(name = "supplyChainJpaDataSource")
//    @ConfigurationProperties(prefix = "datasource.supplychain")
    public DataSource buildDataSource() {
        return createDataSource(driverClassName, url, username, password);
    }

    @Bean(name = "supplyChainJpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean supplyChainJpaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName("SUPPLY_CHAIN");
        factory.setDataSource(buildDataSource());
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setPackagesToScan(PACKAGES_TO_SCAN.split(","));
        factory.setJpaProperties(createJpaProperties());
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean(name = "supplyChainJpaTransactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(supplyChainJpaEntityManagerFactory().getObject());
        return manager;
    }
}
