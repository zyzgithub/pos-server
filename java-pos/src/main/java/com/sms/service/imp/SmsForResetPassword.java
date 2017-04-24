package com.sms.service.imp;


import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.service.SmsByBusinessServiceI;
import com.wm.service.courier.CourierRegisterServiceI;

@Service("smsForResetService")
public class SmsForResetPassword extends SmsServiceImpl implements SmsByBusinessServiceI {

	private final String businessCode = "resetpassword";
	
	@Autowired
	private CourierRegisterServiceI courierRegisterServiceI;
	
	@Override
	public AjaxJson sendVerifyCode(String mobile) {
		AjaxJson json = new AjaxJson();
		try {
			if(courierRegisterServiceI.getTlmRegistApplyByMobile(mobile, "courier") == null){
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("无法根据该手机号找到快递员");
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
