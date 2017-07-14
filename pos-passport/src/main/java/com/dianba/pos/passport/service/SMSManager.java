package com.dianba.pos.passport.service;

import com.dianba.pos.base.BasicResult;

public interface SMSManager {

    String BASE_URL = "sms/";
    String SEND_VERIFY_CODE = BASE_URL + "requestVerificationCode";
    String VERIFY_SMS_CODE = BASE_URL + "verifySmsCode";

    BasicResult sendSMSCode(String phoneNumber);

    BasicResult verifySMSCode(String phoneNumber,String smsCode);
}
