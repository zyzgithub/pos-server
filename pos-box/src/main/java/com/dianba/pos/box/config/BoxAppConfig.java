package com.dianba.pos.box.config;

import com.dianba.pos.base.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app_config.properties")
public class BoxAppConfig extends AppConfig {

    @Value("${box.callback.host}")
    private String boxCallBackHost;

    public String getBoxCallBackHost() {
        return boxCallBackHost;
    }

    public void setBoxCallBackHost(String boxCallBackHost) {
        this.boxCallBackHost = boxCallBackHost;
    }
}
