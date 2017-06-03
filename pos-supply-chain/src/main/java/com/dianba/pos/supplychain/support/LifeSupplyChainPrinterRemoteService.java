package com.dianba.pos.supplychain.support;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class LifeSupplyChainPrinterRemoteService {

    @Autowired
    private AppConfig appConfig;

    protected BasicResult postPrint(String requestUrl, Map<String, String> params) {
        String randomParameter = DefineRandom.randomString(32);
        params.put("partnerId", appConfig.getPosPartnerId());
        params.put("appId", appConfig.getPosAppId());
        params.put("randomParameter", randomParameter);
        CommonUtils.fillSignature(params, appConfig.getPosAppKey());
        String data = HttpRequest.post(appConfig.getPosSupplyChainUrl() + requestUrl, params);
        JSONObject response = JSONObject.parseObject(data);
        return response.toJavaObject(BasicResult.class);
    }
}
