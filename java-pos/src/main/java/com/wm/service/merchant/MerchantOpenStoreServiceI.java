package com.wm.service.merchant;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.dto.merchant.MerchantOpenStoreDto;

public interface MerchantOpenStoreServiceI {

	/**
	 * 验证手机号是否已被商家注册
	 * @param phone
	 * @return
	 */
	public AjaxJson ifExistPhone(String phone);

	/**
	 * 商家自助开店注册接口
	 * @param merchantOpenStoreDto
	 * @return
	 */
	public AjaxJson confirmRegister(MerchantOpenStoreDto merchantOpenStoreDto);
	
	
	/**
	 * 忘记密码
	 * @param phone
	 * @param password
	 * @return
	 */
	public AjaxJson retrievePassword(String phone, String password);

	/**
	 * 商家开通外卖服务接口
	 * @param merchantOpenStoreDto
	 * @return
	 */
	public AjaxJson openWaimai(MerchantOpenStoreDto merchantOpenStoreDto);

	/**
	 * 获取省和市
	 * @return
	 */
	public AjaxJson getOrgList();

	/**
	 * 获取商家申请外卖信息
	 * @param merchantId
	 * @return
	 */
	public AjaxJson getApplyWaimaiStatus(Integer merchantId);
}
