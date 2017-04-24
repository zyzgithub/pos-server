package com.wm.service.favorites;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.favorites.FavoritesEntity;

public interface FavoritesServiceI extends CommonService{

	public String collectOrCancle(String type,int userid,int itemId);

	public FavoritesEntity findByUserAndMerchant(Integer userId,
			Integer merchantId);
	
}
