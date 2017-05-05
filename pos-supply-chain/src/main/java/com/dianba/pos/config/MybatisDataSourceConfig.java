package com.dianba.pos.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter(BaseConfig.class)
@MapperScan(basePackages = MybatisDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "supplyChainSqlSessionFactory")
public class MybatisDataSourceConfig{

    static final String PACKAGE = "com.dianba.supplychain.mapper";
    static final String MAPPER_LOCATION = "classpath:supplychain/mapper/*.xml";

    @Resource(name = "supplyChainDataSource")
    private DruidDataSource druidDataSource;

    @Bean(name = "supplyChainTransactionManager")
    public DataSourceTransactionManager supplyChainTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource);
    }

    @Bean(name = "supplyChainSqlSessionFactory")
    public SqlSessionFactory supplyChainSqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(druidDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MybatisDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}