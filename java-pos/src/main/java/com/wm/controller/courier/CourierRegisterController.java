package com.wm.controller.courier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import jeecg.system.service.SystemService;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.sms.service.SmsByBusinessServiceI;
import com.wm.controller.courier.dto.UserRegisterDTO;
import com.wm.controller.courier.dto.CrowdsourcingRegisterDTO;
import com.wm.entity.courierapply.CourierApplyEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.CourierRegisterServiceI;
import com.wm.util.AliOcs;
import com.wm.util.IPUtil;
import com.wm.util.StringUtil;



@Controller
@RequestMapping("ci/courier/register")
public class CourierRegisterController extends BasicController {
	
	@Autowired
	private CourierRegisterServiceI courierRegisterServiceI;
	
	@Resource(name="smsForRegisterService")
	private SmsByBusinessServiceI smsForRegisterService;
	
	@Resource(name="smsForResetService")
	private SmsByBusinessServiceI smsForResetService;
	
	@Autowired
	private SystemService systemService;
	/**
	 * 快递员首次注册
	 * @param request
	 * @param mobile
	 * @param password
	 * @param verifyCode
	 * @return
	 */
	@RequestMapping(params = "account")
	@ResponseBody
	public AjaxJson account(HttpServletRequest request, @RequestParam String mobile, @RequestParam String password, @RequestParam String verifyCode, @RequestParam Integer courierType){
		AjaxJson json = new AjaxJson();
		try{
			
			if(!StringUtil.isMobileNumber(mobile)){
				json = FAIL();
				json.setMsg("您输入的手机号码无效");
				return json;
			}
			
			if(courierRegisterServiceI.getTlmRegistApplyByMobile(mobile, "courier") != null){
				json = FAIL();
				json.setMsg("号码已注册，请直接登录");
				return json;
			}	

			if(StringUtils.isBlank(password) || StringUtils.length(password) < 6){
				json = FAIL();
				json.setMsg("密码少于6位字符");			
				return json;
			}
			
			if(StringUtils.isBlank(verifyCode)){
				json = FAIL();
				json.setMsg("缺少验证码");
				return json;
			}
			
			Object validCode = AliOcs.getObject(smsForRegisterService.getBusinessCode() + "_" + mobile);
//			HttpSession session = request.getSession();
//			if (session != null) {
//				validCode = (String) session
//						.getAttribute(RandomValidateUtil.RANDOMCODEKEY);
//			}
			if(validCode == null || !StringUtils.equals(mobile+verifyCode, validCode.toString())){
				json = FAIL();
				json.setMsg("验证码不正确");
				return json;
			}
			
			
			
			//保存用户注册信息到用户表
			WUserEntity wuse = new WUserEntity();
			wuse.setMobile(mobile);
			wuse.setPassword(password);
			wuse.setCreateTime(DateUtils.getSeconds());
			wuse.setIp(request.getRemoteAddr());
			wuse.setUserType("courier");
			wuse.setUserState(2);
			wuse.setIsDelete(0);
			wuse.setCreator(0);
			
			CourierApplyEntity courierApplyEntity = new CourierApplyEntity();
			courierApplyEntity.setMobile(mobile);
			courierApplyEntity.setPassword(password);
			courierApplyEntity.setCourierType(courierType);
			
			json = courierRegisterServiceI.saveWUserEntity(wuse, courierApplyEntity);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			json = FAIL();
			json.setMsg("注册出现错误");
			return json;
		}		
		return json;
	}
	
	/**
	 * 平台快递员和合作商快递员完善个人信息
	 * @param request
	 * @param userRegister
	 * @param errors
	 * @return
	 */
	@RequestMapping(params = "fill_personal_information")
	@ResponseBody
	public AjaxJson fillPersonalInformation(HttpServletRequest request, @Valid UserRegisterDTO userRegister, Errors errors){
		
		AjaxJson json = new AjaxJson();
		
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.warn("参数错误, 参数: " + JSON.toJSONString(userRegister));
			json.setSuccess(false);	
			json.setStateCode("04");
			json.setObj(errorsMap);
			json.setMsg("填写注册信息错误");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		
		//判断身份证是否有效
		if(!StringUtil.isIdCard(userRegister.getIdCard())){
			json = FAIL();
			json.setMsg("您输入的身份证号码无效");
			return json;
		}
		
		//判断身份证号码是否已经注册
//		if(courierRegisterServiceI.getTlmRegistApplyByIdCard(userRegister.getIdCard(), "courier") != null){
//			json = FAIL();
//			json.setMsg("该身份证号已经注册");
//			return json;
//		}	
				
		try {
			CourierApplyEntity courierApplyEntity  = systemService.findUniqueByProperty(CourierApplyEntity.class, "userId", userRegister.getId());
			WUserEntity wuse = systemService.get(WUserEntity.class, userRegister.getId());
			if(courierApplyEntity == null || wuse == null){
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("未找到首次注册记录, 请先使用手机号进行首次注册");
				return json;
			}
			
			courierApplyEntity.setGender(userRegister.getGender());
			courierApplyEntity.setUsername(userRegister.getUsername());
			courierApplyEntity.setPassword(userRegister.getPassword());
			courierApplyEntity.setMobile(userRegister.getMobile());
			courierApplyEntity.setUserType(userRegister.getUserType());
			courierApplyEntity.setIp(IPUtil.getRemoteIp(request) == null ? "127.0.0.1" : IPUtil.getRemoteIp(request));
			courierApplyEntity.setCreateTime(DateUtils.getSeconds());
			courierApplyEntity.setBankId(userRegister.getBankId());
			courierApplyEntity.setCardNo(userRegister.getCardNo());
			courierApplyEntity.setSourceBank(userRegister.getSourceBank());
			courierApplyEntity.setIdCardFrontImgUrl(userRegister.getIdCardFrontImgUrl());
			courierApplyEntity.setIdCardBackImgUrl(userRegister.getIdCardBackImgUrl());
			courierApplyEntity.setHoldIdCardFrontImgUrl(userRegister.getHoldIdCardFrontImgUrl());
			courierApplyEntity.setHoldIdCardBackImgUrl(userRegister.getHoldIdCardBackImgUrl());
			courierApplyEntity.setCheckStatus(0);
			courierApplyEntity.setIdCard(userRegister.getIdCard());
			courierApplyEntity.setExpectedDistArea(userRegister.getExpectedDistArea());
			courierApplyEntity.setBankCardFrontImgUrl(userRegister.getBankCardFrontImgUrl());
			courierApplyEntity.setCensusType(userRegister.getCensusType());
			courierApplyEntity.setCensusAddress(userRegister.getCensusAddress());
			courierApplyEntity.setProvince(userRegister.getProvince());
			courierApplyEntity.setCity(userRegister.getCity());
			courierApplyEntity.setAddress(userRegister.getAddress());
			courierApplyEntity.setMarriage(userRegister.getMarriage());
			courierApplyEntity.setNation(userRegister.getNation());
			courierApplyEntity.setNativePlace(userRegister.getNativePlace());
			courierApplyEntity.setEmergencyName(userRegister.getEmergencyName());
			courierApplyEntity.setEmergencyPhone(userRegister.getEmergencyPhone());
			courierApplyEntity.setSchooling(userRegister.getSchooling());
			courierApplyEntity.setHoldSchoolingFrontImgUrl(userRegister.getHoldSchoolingFrontImgUrl());
			courierApplyEntity.setUserId(userRegister.getId());
			courierApplyEntity.setAccountHolder(userRegister.getAccountHolder());
			
			//跟新用户表信息
			  
			  
			wuse.setId(userRegister.getId());
			wuse.setMobile(userRegister.getMobile());		 	
			wuse.setPassword(userRegister.getPassword());
			wuse.setCreateTime(userRegister.getCreateTime());
			wuse.setIp(IPUtil.getRemoteIp(request) == null ? "127.0.0.1" : IPUtil.getRemoteIp(request));
			wuse.setUserType(userRegister.getUserType());
			wuse.setUsername(userRegister.getUsername());
			wuse.setNickname(userRegister.getUsername());		 	
			wuse.setUserState(3);
			wuse.setIsDelete(0);
			wuse.setCreator(0);
			json = courierRegisterServiceI.fillPersonalInformation(courierApplyEntity, wuse);
			  
			json.setSuccess(true);
			json.setMsg("保存成功,请等待审核。");
			json.setStateCode("00");
		} catch (Exception e) {			
			e.printStackTrace();
			json.setStateCode("01");
			json.setSuccess(false);
			json.setMsg("保存失败");
		}		
		return json ;
	}
	
	/**
	 * 首次注册获取验证码
	 * @param mobile
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "accountSendSms")
	@ResponseBody
	public AjaxJson accountSendSms(@RequestParam String mobile, HttpServletRequest request){
		
//		AjaxJson json = new AjaxJson();
//		if(!StringUtil.isMobileNumber(mobile)){
//			json = FAIL();
//			json.setMsg("您输入的手机号码非法");
//			return json;
//		}
//		
//		try {
//			Map<String, Object> courierMap = courierRegisterServiceI.getTlmRegistApplyByMobile(mobile, "courier");
//			if(courierMap != null){
//				json = FAIL();
//				json.setMsg("该手机号已注册");
//				return json;
//			}
//			
//			json = this.sendSms(mobile, request);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			json.setMsg("根据手机号查询用户失败");
//			json.setSuccess(false);
//		}
//		
//		return json;	
		return smsForRegisterService.sendVerifyCode(mobile);
		
	}
	
	@RequestMapping(params = "resetPassSendSms")
	@ResponseBody
	public AjaxJson resetPassSendSms(String mobile, HttpServletRequest request){
//		AjaxJson json = new AjaxJson();
//		if(!StringUtil.isMobileNumber(mobile)){
//			json = FAIL();
//			json.setMsg("您输入的手机号码非法");
//			return json;
//		}
//		
//		try {
//			Map<String, Object> courierMap = courierRegisterServiceI.getTlmRegistApplyByMobile(mobile, "courier");
//			if(courierMap == null){
//				json = FAIL();
//				json.setMsg("无法根据该手机号找到快递员");
//				return json;
//			}
//			
//			json = this.sendSms(mobile, request);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			json.setMsg("根据手机号查询用户失败");
//			json.setSuccess(false);
//		}
//		
//		return json;
		return smsForResetService.sendVerifyCode(mobile);
	}
	
	
//	private AjaxJson sendSms(String mobile, HttpServletRequest request){
//		AjaxJson json = new AjaxJson();
//		SmsEntity sms = new SmsEntity();
//		sms.setPhone(mobile);
//		sms.setSendtime(new Date());
//		String content;
//		String message;
//
//		try{
//			String randomNum = AccountGenerator.getRandomNum(4);
//			content = "您好，您的验证码是：" + randomNum;
//			request.getSession().removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
//			request.getSession().setAttribute(RandomValidateUtil.RANDOMCODEKEY, mobile + randomNum);
//			
//			sms.setContent(content);
//			
//			message = "添加短信成功";
//			smsService.sendSms(sms);
//			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
//			new SessionThread(request).start();			
//			json.setSuccess(true);
//			json.setMsg("发送成功");
//		}catch(Exception ex){
//			ex.printStackTrace();
//			json.setSuccess(false);
//			json.setMsg("发送失败");
//		}
//		return json;
//	}
	
	/**
	 * 根据快递员类型获取快递员协议
	 * @param courierType
	 * @return
	 */
	@RequestMapping(params = "getCourierAgreement")
	@ResponseBody
	public AjaxJson getCourierAgreement(@RequestParam Integer courierType){
		AjaxJson json = new AjaxJson();
		try {
			Map<String, Object> courierAgreementMap = courierRegisterServiceI.getCourierAgreement(courierType);
			if(courierAgreementMap == null){
				json.setMsg("没有找到快递员协议");
				json.setStateCode("02");
				json.setSuccess(false);
				return json;
			}
			json.setMsg("获取快递员协议成功");
			json.setObj(courierAgreementMap);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取快递员协议失败");
		}
		return json;
	}
	
	/**
	 * 快递员绑定代理商
	 * @param courierId
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params = "saveBindUserId")
	@ResponseBody
	public AjaxJson saveBindUserId(@RequestParam Integer courierId, @RequestParam Integer merchantId){
		AjaxJson json = courierRegisterServiceI.saveBindUserId(courierId, merchantId);
		return json;
	}
	
	/**
	 * 众包快递员注册完善个人资料
	 * @param request
	 * @param dto
	 * @param errors
	 * @return
	 */
	@RequestMapping(params = "crowdsourcingRegister")
	@ResponseBody
	public AjaxJson crowdsourcingRegister(HttpServletRequest request, @Valid CrowdsourcingRegisterDTO dto, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入参数: " + JSON.toJSONString(dto));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值：" + errorsList.get(i).getRejectedValue());
			}
			json.setSuccess(false);
			json.setStateCode("04");
			json.setObj(errorsMap);
			json.setMsg("填写注册信息错误");
			logger.warn("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		//判断身份证是否有效
		if(!StringUtil.isIdCard(dto.getIdCard())){
			json.setStateCode("03");
			json.setSuccess(false);
			json.setMsg("您输入的身份证号码无效");
			return json;
		}
		try {
			CourierApplyEntity courierApplyEntity  = systemService.findUniqueByProperty(CourierApplyEntity.class, "userId", dto.getId());
			WUserEntity wuse = systemService.get(WUserEntity.class, dto.getId());
			if(courierApplyEntity == null || wuse == null){
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("未找到首次注册记录, 请先使用手机号进行首次注册");
				return json;
			}
//			if(courierApplyEntity.getCourierType().intValue() != 3){
//				json.setSuccess(false);
//				json.setStateCode("05");
//				json.setMsg("您之前注册的不是众包快递员！");
//				return json;
//			}
			logger.info("快递员" + dto.getId() + "完善个人信息");
			courierRegisterServiceI.saveCrowdsourcingInfoMation(request, dto, courierApplyEntity, wuse);
			json.setSuccess(true);
			json.setObj(new HashMap<String, Object>());
			json.setStateCode("00");
			json.setMsg("保存成功,请等待审核。");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("保存失败！");
		}
		return json;
	}

}
