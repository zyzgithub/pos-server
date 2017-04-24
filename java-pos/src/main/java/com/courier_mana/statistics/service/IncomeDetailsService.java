package com.courier_mana.statistics.service;

import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 总收入费用Service
 * @author hyj
 */
public interface IncomeDetailsService {
	
	/**(OvO)
	 * 获得收入详情
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @param vo		搜索条件vo(包括地区、时间条件)
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return	takeOutDeduction	外卖订单收入
	 * 			dineInDeduction		门店订单收入
	 * 			deliveryFee			配送费
	 * 			boxFee				餐盒费
	 */
	public abstract Map<String, Object> getIncomeDetails(String orgIdsStr, SearchVo vo, boolean isConfirm);
}
