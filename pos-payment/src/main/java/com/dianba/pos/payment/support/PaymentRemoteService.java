package com.dianba.pos.payment.support;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.payment.config.CreditLoanConfig;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.MD5;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentRemoteService {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private CreditLoanConfig creditLoanConfig;

    protected BasicResult postPayWithCallBack(String requestUrl, String callBackUrl, Map<String, String> params) {
        if (!StringUtils.isEmpty(callBackUrl)) {
            params.put("notifyUrl", appConfig.getPosCallBackHost() + callBackUrl);
        }
        signatureParams(params);
        String data = HttpRequest.post(appConfig.getPosPaymentUrl() + requestUrl, params);
        JSONObject response = JSONObject.parseObject(data);
        return response.toJavaObject(BasicResult.class);
    }

    protected void signatureParams(Map<String, String> params) {
        String randomParameter = DefineRandom.randomString(32);
        params.put("partnerId", appConfig.getPosPartnerId());
        params.put("appId", appConfig.getPosAppId());
        params.put("randomParameter", randomParameter);
        CommonUtils.fillSignature(params, appConfig.getPosAppKey());
    }

    protected BasicResult postPay(String url, Map<String, String> params) {
        return postPayWithCallBack(url, null, params);
    }

    protected BasicResult postCreditLoan(String requestUrl, Long passportId, Map<String, String> params) {
        String randomParameter = DefineRandom.randomString(32);
        Map<String, Object> signParams = new LinkedHashMap<>();
        signParams.put("sp_id", creditLoanConfig.getCode());
        signParams.put("nonce_str", randomParameter);
        signParams.put("secret_key", creditLoanConfig.getSecretKey());
        signParams.put("card_id", passportId);
        String sign = HttpUtils.toRequestPrams(signParams) + "&param_count=" + (params.size() + 5);
        String md5Sign = MD5.encode(sign).toUpperCase();
        params.put("sign", md5Sign);
        for (String key : signParams.keySet()) {
            params.put(key, signParams.get(key).toString());
        }
        String data = HttpRequest.post(appConfig.getPosFinanceUrl() + requestUrl, params);
        JSONObject jsonObject = JSONObject.parseObject(data);
        BasicResult basicResult = jsonObject.toJavaObject(BasicResult.class);
        return basicResult;
    }
}
