package com.wm.dao.menu;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.takeout.vo.MenuVo;

public interface MenuDao extends IGenericBaseCommonDao {

	List<MenuVo> findByMerchantId(Integer merchantId);
	
	List<MenuVo> findFirstPageMenuByMerchantId(Integer merchantId, Integer menuTypeId, Integer page, Integer rows);

	int findBuyCount(Integer merchantId);

	MenuVo findById(Integer menuId);
	
	MenuVo findIsPromotionById(Integer menuId, Boolean promotion);

	int queryTodayCountWhenPromoting(Integer userId, Integer menuId);
	
	/**
	 * 获取某个商家某一个商品的库存、下架、删除信息
	 * @param menuId 商品ID
	 * @return
	 */
	Map<String, Object> getMenuInfo(Integer menuId);

	/**
	 * 获取热销商品
	 * @param merchantId
	 * @param typeId
	 * @param searchKey 此参数暂未实现
	 * @param startTime
	 *@param endTime
	 * @param num
	 * @param pageSize   @return
     */
	List<Map<String,Object>> getTopSales(String merchantId, String typeId, String searchKey, Integer startTime, Integer endTime, Integer num, Integer pageSize);

	List<Map<String, Object>> getStockPrice(List<Long> menuIds);
}
