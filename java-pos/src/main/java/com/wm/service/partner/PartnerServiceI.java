package com.wm.service.partner;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.partner.PartnerVo;

public interface PartnerServiceI extends CommonService{
	
	/**
	 * 根据合作方标识ID获取商家和菜品信息
	 * @param openid
	 * @return
	 */
	public PartnerVo findMerchantMenuList(String openid,Integer page, Integer rows);

}
