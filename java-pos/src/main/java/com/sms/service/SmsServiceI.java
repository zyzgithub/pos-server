package com.sms.service;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.sms.entity.SmsEntity;

public interface SmsServiceI extends CommonService{
	/**
	 * 发送短信接口
	 * @param smsEntity
	 * @return
	 */
	public int sendSms(SmsEntity smsEntity);
	/**
	 * 发送短信
	 * @param mobile 手机
	 * @param content 内容
	 * @return
	 */
	boolean sendSms(String mobile, String content);
	
	/**
	 * 发送短信
	 * @param mobile 手机号码
	 * @param businessCode 业务代码
	 * @param expire 短信验证码过期时间
	 * @return
	 */
	AjaxJson sendVerifyCode(String mobile, String businessCode, int expire);
}
