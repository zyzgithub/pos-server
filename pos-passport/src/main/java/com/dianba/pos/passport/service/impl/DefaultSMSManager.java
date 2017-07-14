package com.dianba.pos.passport.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.passport.service.SMSManager;
import com.dianba.pos.passport.support.PassportRemoteService;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class DefaultSMSManager extends PassportRemoteService implements SMSManager {

    @Override
    public BasicResult sendSMSCode(String phoneNumber) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phoneNumber", phoneNumber);
        return post(SEND_VERIFY_CODE, jsonObject);
    }

    @Override
    public BasicResult verifySMSCode(String phoneNumber, String smsCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phoneNumber);
        jsonObject.put("smsCode", smsCode);
        jsonObject.put("smsType", SmsCodeTypeEnum.REGISTER.getKey());
        return post(VERIFY_SMS_CODE, jsonObject);
    }
}
