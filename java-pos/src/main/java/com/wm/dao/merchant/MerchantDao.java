package com.wm.dao.merchant;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.takeout.dto.WXHomeDTO;
import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.controller.takeout.vo.OrderSimpleVo;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantStatisticsMenuVo;
import com.wm.util.PageList;

public interface MerchantDao extends IGenericBaseCommonDao{

	List<MerchantSimpleVo> findByLocation(int cityId,WXHomeDTO wx);

	OrderSimpleVo lasetOrder(Integer userId);

	/**
	 * 查询用户的商家收藏列表
	 * @param userId , userid
	 * @return
	 */
	List<MerchantSimpleVo> queryUserFavMerchantByUserId(Integer userId, int page, int rows);

	MerchantEntity queryByMenuId(Integer menuId);
	
	PageList<MerchantStatisticsMenuVo> statisticsMenu(String title,String startTime, String endTime, Integer page, Integer rows);
	
}
