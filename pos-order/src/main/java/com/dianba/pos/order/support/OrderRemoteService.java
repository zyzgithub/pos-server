package com.dianba.pos.order.support;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class OrderRemoteService {

    @Autowired
    private AppConfig appConfig;

    protected BasicResult postOrder(String url, Map<String, String> parameters) {
        parameters.put("partnerId", appConfig.getPosPartnerId());
        parameters.put("appId", appConfig.getPosAppId());
        CommonUtils.fillSignature(parameters, appConfig.getPosAppKey());
        String data = HttpRequest.post(appConfig.getPosOrderUrl() + url, parameters);
        JSONObject response = JSONObject.parseObject(data);
        return response.toJavaObject(BasicResult.class);
    }
}
