package com.wm.service.deduct;

import com.wm.entity.order.OrderEntity;

public interface CourierRechargePromotionServiceI {

	/**
	 * 获取快递员扫码充值推广的提成
	 * @param courierId 快递员提成
	 * @param rechargeMoney 扫码充值的金额
	 * @return
	 */
	Double getRechargePromotionDeduct(Integer courierId, double rechargeMoney, Integer userId);
	
	/**
	 * 保存充值是否获得奖励的记录
	 */
	void saveCourierScanPromotion(OrderEntity order);
	
	/**
	 * 获取同一个用户充值金额 快递员有没有获得过提成奖励  true表示没有  false表示已获得过该奖励
	 */
	boolean isReward(Integer userId);
	
	/**
	 * 根据充值金额获取  规则梯度id
	 */
	int getCourierScanRuleIdByMoney(Integer courierId, double rechargeMoney);
	
}
