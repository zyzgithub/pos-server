package com.sms.service;

import org.jeecgframework.core.common.model.json.AjaxJson;

/**
 * 发送短信验证码业务接口类
 */
public interface SmsByBusinessServiceI extends SmsServiceI {
	int DEFAULT_VERIFYCODE_EXPIRED = 3 * 60;
	/**
	 * 发送短信验证码
	 * @param mobile 手机号码
	 * @return
	 */
	AjaxJson sendVerifyCode(String mobile);
	
	/**
	 * 获取业务编码
	 * @return
	 */
	String getBusinessCode();
}
