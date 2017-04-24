package com.sms.service.imp;


import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.entity.SmsEntity;
import com.sms.service.SmsByBusinessServiceI;
import com.wm.service.courier.CourierRegisterServiceI;
import com.wm.util.StringUtil;

@Service("smsForRegisterService")
public class SmsForRegisterServiceImpl extends SmsServiceImpl implements SmsByBusinessServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsForRegisterServiceImpl.class);

	private final String businessCode = "register";
	
	@Autowired
	private CourierRegisterServiceI courierRegisterServiceI;
	
	@Override
	public int sendSms(SmsEntity smsEntity) {
		if(!StringUtil.isMobileNumber(smsEntity.getPhone())){
			logger.warn("valid phone number:{}!!!", smsEntity.getPhone());
			return 1;
		}
		return super.sendSms(smsEntity);
	}

	@Override
	public boolean sendSms(String mobile, String content) {
		return super.sendSms(mobile, content);
	}


	@Override
	public AjaxJson sendVerifyCode(String mobile) {
		AjaxJson json = new AjaxJson();
		try {
			if(courierRegisterServiceI.getTlmRegistApplyByMobile(mobile, "courier") != null){
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("该手机号已注册");
				return json;
			}
			
			return sendVerifyCode(mobile, businessCode, DEFAULT_VERIFYCODE_EXPIRED);
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("根据手机号查询用户失败");
			json.setSuccess(false);
			return json;
		}
	}

	public String getBusinessCode() {
		return businessCode;
	}
}
