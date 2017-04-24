package com.sms.service.imp;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.stereotype.Service;

import com.sms.service.SmsByBusinessServiceI;

@Service("smsForWithdrawService")
public class SmsForWithdrawServiceImpl extends SmsServiceImpl implements SmsByBusinessServiceI{
	private final String businessCode = "withdraw";
	
	@Override
	public AjaxJson sendVerifyCode(String mobile) {
		return sendVerifyCode(mobile, businessCode, DEFAULT_VERIFYCODE_EXPIRED);
	}
	
	public String getBusinessCode() {
		return businessCode;
	}
}
