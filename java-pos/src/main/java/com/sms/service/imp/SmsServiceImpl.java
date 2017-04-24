package com.sms.service.imp;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sms.entity.SmsEntity;
import com.sms.service.SmsServiceI;
import com.sms.util.SmsUtil;
import com.wm.controller.user.AccountGenerator;
import com.wm.util.AliOcs;
import com.wm.util.StringUtil;

@Service("smsService")
@Transactional
public class SmsServiceImpl extends CommonServiceImpl implements SmsServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Override
	public int sendSms(SmsEntity smsEntity) {
		try {
			this.save(smsEntity);
			SmsUtil.sendMsg(smsEntity.getContent(), smsEntity.getPhone());
		} catch (Exception e) {
			logger.error("send msg failed !!! smsEntity:{}", JSONObject.toJSONString(smsEntity));
		}
		return 0;
	}
	
	@Override
	public boolean sendSms(String mobile, String content){
		try {
			SmsEntity smsEntity = new SmsEntity();
			smsEntity.setPhone(mobile);
			smsEntity.setContent(content);
			save(smsEntity);
			String responseText = SmsUtil.sendMsg(content, mobile);
			if(responseText != null){
				return true;
			} else {
				return false;
			}
		} 
		catch (Exception e) {
			logger.error("send msg failed !!! mobile:{},content:{}", mobile, content);
			return false;
		}
	}
	
	public AjaxJson sendVerifyCode(String mobile, String businessCode, int expire){
		AjaxJson json = new AjaxJson();
		if(!StringUtil.isMobileNumber(mobile)){
			json.setSuccess(false);
			json.setStateCode("02");
			json.setMsg("您输入的手机号码非法");
			return json;
		}
		
		String verifyCode = AccountGenerator.getRandomNum(4);
		AliOcs.set(businessCode + "_" + mobile, mobile+verifyCode, expire);
		String content = "您好，您的验证码是：" + verifyCode;
		if(!sendSms(mobile, content)){
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("发送短信验证码失败！");
		} else {
			json.setSuccess(true);
			json.setStateCode("00");
			json.setMsg("发送短信验证码成功！");
		}
		return json;
	}
	
}