package com.wm.service.rechargerecord;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.rechargerecord.RechargerecordEntity;

public interface RechargerecordServiceI extends CommonService{
	public RechargerecordEntity recharge(String payid, int userid, double money, String service);
}
