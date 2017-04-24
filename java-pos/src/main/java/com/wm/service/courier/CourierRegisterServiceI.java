package com.wm.service.courier;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.controller.courier.dto.CrowdsourcingRegisterDTO;
import com.wm.entity.courierapply.CourierApplyEntity;
import com.wm.entity.user.WUserEntity;

public interface CourierRegisterServiceI {	
	
	/**
	 * 保存注册信息到用户表
	 * @param request
	 * @param mobile
	 * @param loginPassword
	 * @param verifyCode
	 */
	public AjaxJson saveWUserEntity(WUserEntity wuse, CourierApplyEntity courierApplyEntity);
	
	/**
	 * 根据身份证号获取快递员
	 * @param IdCard
	 * @return
	 */
	public Map<String, Object> getTlmRegistApplyByIdCard(String IdCard, String userType);
	/**
	 * 根据手机号获取快递员
	 * @param Mobile
	 * @return
	 */
	public Map<String, Object> getTlmRegistApplyByMobile(String mobile, String userType);
	
	/**
	 * 保存快递员个人信息
	 * @param request
	 * @param courierApplyEntity
	 * @return
	 */
	public AjaxJson fillPersonalInformation(CourierApplyEntity courierApplyEntity, WUserEntity wuse);
	 
	/**
	 * 根据快递员类型获取快递员协议
	 * @param courierType
	 * @return
	 */
	public Map<String, Object> getCourierAgreement(Integer courierType);
	
	/**
	 * 快递员绑定代理商
	 * @param courierId
	 * @param merchantId
	 * @return
	 */
	public AjaxJson saveBindUserId(Integer courierId, Integer merchantId);
	
	/**
	 * 判断快递员注册是否通过， 被拒绝返回true
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isApproved(String username, String password);
	
	/**
	 * 快递员审核被驳回，获取快递员之前填写的资料
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getCourierApplyInfo(Integer userId);
	
	/**
	 * 众包快递员注册完善个人信息
	 * @param request
	 * @param dto
	 * @param courierApplyEntity
	 * @param wUserEntity
	 */
	public void saveCrowdsourcingInfoMation(HttpServletRequest request, CrowdsourcingRegisterDTO dto, CourierApplyEntity courierApplyEntity, WUserEntity wUserEntity);
}
