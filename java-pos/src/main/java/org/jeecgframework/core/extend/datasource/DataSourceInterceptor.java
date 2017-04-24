package org.jeecgframework.core.extend.datasource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 *类名：DataSourceInterceptor.java
 *功能：aop拦截DataSource
 *描述：jeecm 为营销管理 - cm - CourierManager专用连接池配置
 *作者：Roar,
 */
@Component
@Aspect
public class DataSourceInterceptor{ 
    Logger log = Logger.getLogger(DataSourceInterceptor.class);
    
    public void setDataSourceJeecg(JoinPoint jp) {
        log.info("dataSource: jeecm =============>:"+jp.getSignature());
        DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_cm);
    }
}
