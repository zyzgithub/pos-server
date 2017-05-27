package com.dianba.pos.payment.support;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

public class PaymentRemoteService {

    @Autowired
    private AppConfig appConfig;

    protected BasicResult postPayWithCallBack(String requestUrl, String callBackUrl, Map<String, String> params) {
        String randomParameter = DefineRandom.randomString(32);
        params.put("partnerId", appConfig.getPosPartnerId());
        params.put("appId", appConfig.getPosAppId());
        params.put("randomParameter", randomParameter);
        if (!StringUtils.isEmpty(callBackUrl)) {
            params.put("notifyUrl", appConfig.getPosCallBackHost() + callBackUrl);
        }
        CommonUtils.fillSignature(params, appConfig.getPosAppKey());
        String data = HttpRequest.post(appConfig.getPosPaymentUrl() + requestUrl, params);
        JSONObject response = JSONObject.parseObject(data);
        return response.toJavaObject(BasicResult.class);
    }

    protected BasicResult postPay(String url, Map<String, String> params) {
        return postPayWithCallBack(url, null, params);
    }
}
