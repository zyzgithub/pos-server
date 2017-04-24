package com.wm.dao.merchant.impl;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.merchant.TomMerchantMemberInfoDao;
import com.wm.entity.agent.AgentIncomeTimerEntity;

@Repository("tomMerchantMemberInfoDao")
public class TomMerchantMemberInfoDaoImpl extends GenericBaseCommonDao<AgentIncomeTimerEntity, Integer>
		implements TomMerchantMemberInfoDao {

	@Override
	public int addMoney(Integer userId, Integer merchantId, BigDecimal addedMoney) {
		String sql = "UPDATE tom_merchant_member_info t SET t.money=t.money + ? WHERE user_id=? AND merchant_id=?";
		return executeSql(sql, addedMoney, userId, merchantId);
	}

}
