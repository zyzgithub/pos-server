package com.dianba.pos.passport.support;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class PassportRemoteService {

    @Autowired
    private AppConfig appConfig;

    protected BasicResult post(String url, Object params) {
        JSONObject response = HttpUtil.post(appConfig.getPosPassportUrl() + url, params);
        return response.toJavaObject(BasicResult.class);
    }
}
