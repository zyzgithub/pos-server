package com.wm.dao.favorite;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.favorites.FavoritesEntity;

public interface FavoriteDao extends IGenericBaseCommonDao{

	FavoritesEntity findByUserAndMerchant(Integer userId, Integer merchantId);

}
