package com.wm.service.menu;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.menu.MenutypeEntity;

public interface MenutypeServiceI extends CommonService{

	List<MenutypeEntity> queryByMerchantId(Integer merchantId);
	
	
	/**
	 * 获取未删除商品的商品类型.
	 * @param merchantId
	 * @param type:1.过滤没有商品的分类; 2.查询所有的商品分类，该分类下没有商品也查出来
	 * @return
	 */
	List<MenutypeEntity> getTypeByMerchantId(Integer merchantId, int type);
	
	/**
	 * 根据id获取商品分类
	 * @param typeId
	 * @return
	 */
	List<MenutypeEntity> getTypeByTypeId(Integer typeId);
	
	/**
	 * 根据商家id获取促销活动
	 * @param merchantId
	 * @return
	 */
	List<Integer> getPromotionActivityIds(Integer merchantId);
}
