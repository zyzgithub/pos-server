package com.wm.dao.favorite.impl;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.favorite.FavoriteDao;
import com.wm.entity.favorites.FavoritesEntity;

@Repository("favoriteDao")
public class FavoriteDaoImpl extends GenericBaseCommonDao<FavoritesEntity, Integer> implements FavoriteDao{

	@Override
	public FavoritesEntity findByUserAndMerchant(Integer userId,
			Integer merchantId) {
		// TODO Auto-generated method stub
		String hql = "from FavoritesEntity f where f.userid = ? and f.itemId = ? and f.type = ?";
		Query query = createHqlQuery(hql, userId, merchantId, "2");
		Object obj = query.uniqueResult();
		if(null != obj)
			return (FavoritesEntity)obj;
		return null;
	} 

}
