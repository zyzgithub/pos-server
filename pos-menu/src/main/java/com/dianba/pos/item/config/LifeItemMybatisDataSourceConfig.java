package com.dianba.pos.item.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;

@Configuration
@AutoConfigureAfter(LifeItemBaseConfig.class)
@MapperScan(basePackages = LifeItemMybatisDataSourceConfig.PACKAGE, sqlSessionFactoryRef =
        "liftItemSqlSessionFactory")
public class LifeItemMybatisDataSourceConfig {

    static final String PACKAGE = "com.dianba.pos.item.mapper";
    static final String MAPPER_LOCATION = "classpath:*/mapper/*.xml";

    @Resource(name = "lifeItemDataSource")
    private DruidDataSource druidDataSource;

    @Bean(name = "lifeItemTransactionManager")
    public DataSourceTransactionManager lifeItemTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource);
    }

    @Bean(name = "lifeItemSqlSessionFactory")
    public SqlSessionFactory lifeItemSqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(druidDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(LifeItemMybatisDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
