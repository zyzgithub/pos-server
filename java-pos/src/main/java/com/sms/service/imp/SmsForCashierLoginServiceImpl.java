package com.sms.service.imp;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.service.SmsByBusinessServiceI;
import com.wm.entity.supermarket.CashierEntity;
import com.wm.service.user.CashierServiceI;

@Service("smsForCashierLoginService")
public class SmsForCashierLoginServiceImpl extends SmsServiceImpl implements SmsByBusinessServiceI{
	private final String businessCode = "cashier_login";
	
	@Autowired
	private CashierServiceI cashierService;
	
	@Override
	public AjaxJson sendVerifyCode(String mobile) {
		AjaxJson json = new AjaxJson();
		try {
			CashierEntity entity = cashierService.findUniqueByProperty(CashierEntity.class, "mobile", mobile);
			if(entity == null){
				json.setState("02");
				json.setMsg("请输入正确的手机号码");
				json.setSuccess(false);
				return json;
			}
			
			return sendVerifyCode(mobile, businessCode, DEFAULT_VERIFYCODE_EXPIRED);
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("请输入正确的手机号码");
			json.setSuccess(false);
			return json;
		}
	}

	public String getBusinessCode() {
		return businessCode;
	}
	
}
