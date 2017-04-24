package com.wm.dao.user;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.user.NewAndOldUserVo;
import com.wm.entity.user.WUserEntity;

public interface UserDao extends IGenericBaseCommonDao {

	WUserEntity queryByOpenId(String openId);

	NewAndOldUserVo statisticsNewAndOldUser(String startTime, String endTime);

	BigDecimal getMoneyForUpdate(Integer userId);
	
	int updateMoney(BigDecimal postMoney, Integer userId, BigDecimal preMoney);
	
}
