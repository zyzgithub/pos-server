package com.wm.service.impl.rechargerecord;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.rechargerecord.RechargerecordEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.rechargerecord.RechargerecordServiceI;
import com.wm.service.user.WUserServiceI;

@Service("rechargerecordService")
@Transactional
public class RechargerecordServiceImpl extends CommonServiceImpl implements RechargerecordServiceI {
	
	@Autowired
	private WUserServiceI wUserService;

	@Override
	public RechargerecordEntity recharge(String payid, int userid, double money, String service) {
		RechargerecordEntity rechargeRecord = new RechargerecordEntity();
		rechargeRecord.setMoney(money);
		rechargeRecord.setService(service);
		rechargeRecord.setWuser(wUserService.get(WUserEntity.class, userid));
		rechargeRecord.setPayId(payid);
		this.save(rechargeRecord);
		return rechargeRecord;
	}

}