package com.wm.service.impl.favorites;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.dao.favorite.FavoriteDao;
import com.wm.entity.favorites.FavoritesEntity;
import com.wm.service.favorites.FavoritesServiceI;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;

@Service("favoritesService")
@Transactional
public class FavoritesServiceImpl extends CommonServiceImpl implements
		FavoritesServiceI {

	@Autowired
	private FavoriteDao favoriteDao;
	
	@Override
	public String collectOrCancle(String type, int userid, int itemId) {
		CriteriaQuery cq = new CriteriaQuery(FavoritesEntity.class);
		cq.eq("userid", userid);
		cq.eq("itemId", itemId);
		cq.eq("type", type);
		cq.add();
		List<FavoritesEntity> favorites = this
				.getListByCriteriaQuery(cq, false);

		if (favorites != null && favorites.size() > 0) {
			this.delete(favorites.get(0));
			return "cancle";
		} else {
			FavoritesEntity favorite = new FavoritesEntity();
			favorite.setUserid(userid);
			favorite.setItemId(itemId);
			favorite.setType(type);
			favorite.setFTime(DateUtils.getSeconds());
			this.save(favorite);
			return "collect";
		}
	}

	@Override
	public FavoritesEntity findByUserAndMerchant(Integer userId,
			Integer merchantId) {
		// TODO Auto-generated method stub
		return favoriteDao.findByUserAndMerchant(userId, merchantId);
	}

}