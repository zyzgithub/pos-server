package com.dianba.pos.item.config;

import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.datacache.item.ItemDataCacheService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemConfig {

    static {
        //TODO 地址修正
        DataCacheApplicationContextLoaderNotify.setItemRemoteServiceURL(
                "http://120.25.199.108:8081/");
        ItemDataCacheService.initItemCache();
    }
}
