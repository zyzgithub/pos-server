package com.wm.dao.merchant;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface TomMerchantMemberInfoDao extends IGenericBaseCommonDao {

	/**
	 * 根据用户ID，商家ID为对应的商家会员添加余额
	 * @param userId 用户ID
	 * @param merchantId 商家ID
	 * @param addedMoney 要增加的金额（分）
	 * @return 成功修改的记录
	 */
	int addMoney(Integer userId, Integer merchantId, BigDecimal addedMoney);

}
