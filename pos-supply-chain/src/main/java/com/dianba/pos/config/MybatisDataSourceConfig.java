package com.dianba.pos.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = MybatisDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "supplyChainSqlSessionFactory")
public class MybatisDataSourceConfig {

    static final String PACKAGE = "com.dianba.supplychain.mapper";
    static final String MAPPER_LOCATION = "classpath:supplychain/mapper/*.xml";

    @Value("${supplychain.datasource.url}")
    private String url;

    @Value("${supplychain.datasource.username}")
    private String user;

    @Value("${supplychain.datasource.password}")
    private String password;

    @Value("${supplychain.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "supplyChainDataSource")
    public DataSource supplyChainDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "supplyChainTransactionManager")
    public DataSourceTransactionManager supplyChainTransactionManager() {
        return new DataSourceTransactionManager(supplyChainDataSource());
    }

    @Bean(name = "supplyChainSqlSessionFactory")
    public SqlSessionFactory supplyChainSqlSessionFactory(@Qualifier("supplyChainDataSource") DataSource supplyChainDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(supplyChainDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MybatisDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}