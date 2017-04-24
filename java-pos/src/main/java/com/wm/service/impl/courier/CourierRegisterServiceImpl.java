package com.wm.service.impl.courier;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.Constants;
import com.wm.controller.courier.dto.CrowdsourcingRegisterDTO;
import com.wm.entity.courierapply.CourierApplyEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.CourierRegisterServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.util.IPUtil;

@Service
@Transactional
public class CourierRegisterServiceImpl extends CommonServiceImpl implements CourierRegisterServiceI{	

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CourierRegisterServiceImpl.class);
	
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	@Override
	public Map<String, Object> getTlmRegistApplyByIdCard(String IdCard, String userType) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT id ");
		query.append(" FROM 0085_courier_apply");
		query.append(" WHERE");
		query.append(" id_card = ?");		
		query.append(" AND user_type = ?");	
		query.append(" AND check_status <> 2");	
		return findOneForJdbc(query.toString(),  IdCard, userType);
	}
	
	@Override
	public Map<String, Object> getTlmRegistApplyByMobile(String mobile, String userType) {
		try {
			StringBuilder query = new StringBuilder();
			query.append(" SELECT id ");
			query.append(" FROM `user`");
			query.append(" WHERE");
			query.append(" mobile = ?");		
			query.append(" AND user_type = ?");	
			query.append(" AND is_delete = 0");
			return findOneForJdbc(query.toString(),  mobile, userType);	
		} catch (IncorrectResultSizeDataAccessException e) {
			logger.warn("数据库中已有数据异常,存在两条记录,mobile:{}, userType:{}", mobile, userType);
			return new HashMap<String, Object>();
		}
	
	}

	@Override
	public AjaxJson fillPersonalInformation(CourierApplyEntity courierApplyEntity, WUserEntity wuse) {
		AjaxJson j = new AjaxJson();
		this.saveOrUpdate(courierApplyEntity);
		logger.info("wuser:"+ JSON.toJSONString(wuse));
		this.saveOrUpdate(wuse);
		if (courierApplyEntity.getIdCard() == null) {
			j.setSuccess(false);
			j.setMsg("保存个人信息失败");
			j.setStateCode("01");
		} else {			
			j.setSuccess(true);
			j.setMsg("保存个人信息成功");
			j.setStateCode("00");
		}
		return j;		
	}

	@Override
	public AjaxJson saveWUserEntity(WUserEntity wuse, CourierApplyEntity courierApplyEntity) {
		AjaxJson j = new AjaxJson();		
		this.saveOrUpdate(wuse);
		logger.info("wuse:"+ JSON.toJSONString(wuse));
		courierApplyEntity.setUserId(wuse.getId());
		this.save(courierApplyEntity);
		logger.info("courierApplyEntity:"+ JSON.toJSONString(courierApplyEntity));

		Map<String, Object> wuseMap = new HashMap<String, Object>();
		wuseMap.put("id", wuse.getId());
		wuseMap.put("password", wuse.getPassword());
		wuseMap.put("userType", wuse.getUserType());
		wuseMap.put("userState", wuse.getUserState());
		wuseMap.put("mobile", wuse.getMobile());
		wuseMap.put("isDelete", wuse.getIsDelete());
		wuseMap.put("createTime", wuse.getCreateTime());
		wuseMap.put("courierType", courierApplyEntity.getCourierType());
		j.setObj(wuseMap);
		logger.info("return ajaxjson:"+ JSON.toJSONString(j));
		if (wuse.getId() == null || wuse.getId() == 0) {
			j.setSuccess(false);
			j.setMsg("注册帐号失败");
			j.setStateCode("01");
		} else {			
			j.setSuccess(true);
			j.setMsg("注册账号成功");
			j.setStateCode("00");
		}
		return j;		
	}

	@Override
	public Map<String, Object> getCourierAgreement(Integer courierType) {
		StringBuilder query = new StringBuilder();
		query.append(" select id, title, ifnull(content, '') content, type, ifnull(href, '') href, creator, ifnull(update_time, create_time) updateTime, ifnull(logo, '') logo " );
		query.append(" from mana_regulation mr" );
		query.append(" left join regulation_platform_user rpu on  mr.id = rpu.regulation_id " );
		query.append(" where platform_type_id = ? and user_type_id = 1 " );
		return findOneForJdbc(query.toString(), courierType);
	}

	@Override
	public AjaxJson saveBindUserId(Integer courierId, Integer merchantId) {
		AjaxJson json = new AjaxJson();
		logger.info("快递员id:" + courierId + " , 代理商id:" + merchantId);
		try {
			//如果不能找到courierId对应的  申请合作商快递员记录
			CourierApplyEntity courierApplyEntity = this.findUniqueByProperty(CourierApplyEntity.class, "userId", courierId);
			if(courierApplyEntity == null){
				json.setMsg("找不到该快递员的申请记录");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
			//该快递员不是合作商快递员
			if(!courierApplyEntity.getCourierType().equals(Constants.COURIER_COOPERATE_BUSINESS)){
				json.setMsg("您不是合作商快递员，不能帮定代理商");
				json.setSuccess(false);
				json.setStateCode("03");
				return json;
			}
			//代理商被删除
			WUserEntity wuserEntity = this.get(WUserEntity.class, merchantId);
			if(wuserEntity == null || !wuserEntity.getIsDelete().equals(0)){
				logger.warn("找不到代理商, merchantId:" + merchantId);
				json.setMsg("找不到代理商");
				json.setSuccess(false);
				json.setStateCode("08");
				return json;
			}
			
			//只能扫描代理商的二维码
//			String ruleType = findOneForJdbc("SELECT rule_type from merchant m LEFT JOIN agent_info ai ON ai.user_id = m.user_id WHERE m.id = ?", String.class, merchantId);
			String ruleType = findOneForJdbc("SELECT rule_type from  agent_info ai  WHERE ai.user_id = ?", String.class, merchantId);
			if(StringUtils.isEmpty(ruleType) || !"1".equals(ruleType)){
				json.setMsg("您扫描的二维码不是代理商的二维码， 请扫描代理商的二维码");
				json.setSuccess(false);
				json.setStateCode("05");
				return json;
			}
			//对于同一个代理商，只需要绑定一次
			if(courierApplyEntity.getBindUserId() != null && courierApplyEntity.getBindUserId() == merchantId){
				json.setMsg("您已绑定了该代理商， 请不要重复绑定");
				json.setSuccess(false);
				json.setStateCode("04");
				return json;
			}
			//未与当前代理商解绑，不能去绑定其他的代理商
			Integer agentId = courierInfoService.getCourierBindUserId(courierId);
			if(agentId != null && agentId !=0){
				logger.info("快递员" + courierId + "已经绑定了代理商" + agentId);
				json.setMsg("您需要与当前代理商解绑，才能绑定其它代理商");
				json.setSuccess(false);
				json.setStateCode("06");
				return json;
			}
			courierApplyEntity.setBindUserId(merchantId);
			courierApplyEntity.setAgentCheckStatus(1);
			saveOrUpdate(courierApplyEntity);
			WUserEntity userEntity = this.get(WUserEntity.class, courierId);
			if(userEntity != null){
				userEntity.setUserState(7);
				saveOrUpdate(userEntity);
			}
			json.setMsg("您已成功绑定代理商,请等待审核");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("绑定代理商失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}

	@Override
	public boolean isApproved(String username, String password) {
		StringBuilder query = new StringBuilder();
		query.append(" select check_status checkStatus from 0085_courier_apply");
		query.append(" where username = ? and password = ? order by create_time desc");
		query.append(" limit 0, 1");
		Integer checkStatus = findOneForJdbc(query.toString(), Integer.class, username, password);
		if(checkStatus != null && checkStatus == 2){
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> getCourierApplyInfo(Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append(" select  emergency_phone emergencyPhone, census_address censusAddress, city, card_no cardNo, ca.id, bank_card_front_img_url bankCardFrontImgUrl, ");
		query.append(" schooling, courier_type courierType, province, id_card idCard, expected_dist_area expectedDistArea, ");
		query.append(" hold_id_card_front_img_url holdIdCardFrontImgUrl, id_card_back_img_url idCardBackImgUrl, native_place nativePlace, ");
		query.append(" hold_schooling_front_img_url holdSchoolingFrontImgUrl, marriage, nation, ");
		query.append(" bank_id bankId, emergency_name emergencyName, address, account_holder accountHolder, ");
		query.append(" census_type censusType, id_card_front_img_url idCardFrontImgUrl, source_bank sourceBank, ");
		query.append(" `name` bankName");
		query.append(" from 0085_courier_apply ca");
		query.append(" left join bank b on b.id = ca.bank_id");
		query.append(" where user_id = ?");
		return findOneForJdbc(query.toString(), userId);
	}

	/**
	 * 众包快递员注册完善个人信息
	 * @param request
	 * @param dto
	 * @param courierApplyEntity
	 * @param wUserEntity
	 */
	@Override
	public void saveCrowdsourcingInfoMation(HttpServletRequest request,
			CrowdsourcingRegisterDTO dto, CourierApplyEntity courierApplyEntity, WUserEntity wUserEntity) {
		courierApplyEntity.setGender(dto.getGender());
		courierApplyEntity.setUsername(dto.getUsername());
		courierApplyEntity.setUserType(dto.getUserType());
		courierApplyEntity.setIp(IPUtil.getRemoteIp(request) == null ? "127.0.0.1" : IPUtil.getRemoteIp(request));
		courierApplyEntity.setCreateTime(DateUtils.getSeconds());
		courierApplyEntity.setBankId(dto.getBankId());
		courierApplyEntity.setCardNo(dto.getCardNo());
		courierApplyEntity.setIdCardFrontImgUrl(dto.getIdCardFrontImgUrl());
		courierApplyEntity.setIdCardBackImgUrl(dto.getIdCardBackImgUrl());
		courierApplyEntity.setCheckStatus(0);
		courierApplyEntity.setIdCard(dto.getIdCard());
		courierApplyEntity.setBankCardFrontImgUrl(dto.getBankCardFrontImgUrl());
		courierApplyEntity.setCity(dto.getCity());
		courierApplyEntity.setUserId(dto.getId());
		courierApplyEntity.setAccountHolder(dto.getAccountHolder());
		saveOrUpdate(courierApplyEntity);
		logger.info("courierApplyEntity:"+ JSON.toJSONString(courierApplyEntity));
		
		//跟新用户表信息
		wUserEntity.setId(dto.getId());
		wUserEntity.setIp(IPUtil.getRemoteIp(request) == null ? "127.0.0.1" : IPUtil.getRemoteIp(request));
		wUserEntity.setUserType(dto.getUserType());
		wUserEntity.setUsername(dto.getUsername());
	    wUserEntity.setNickname(dto.getUsername());		 	
		wUserEntity.setUserState(3);
		wUserEntity.setIsDelete(0);
		wUserEntity.setCreator(0);
		saveOrUpdate(wUserEntity);
		logger.info("wUserEntity:"+ JSON.toJSONString(wUserEntity));
	}
	
}
