package com.wm.service.impl.user;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.user.TumUserStatisticsServiceI;
@Service
@Transactional
public class TumUserStatisticsServiceImpl  extends CommonServiceImpl implements TumUserStatisticsServiceI{
	
	private static final String getStatCount = "select count(*) from `tum_user_statistics` where user_id = ? ";
	private static final String addStat = "insert tum_user_statistics(user_id,total_money,total_order,total_recharge) values(?,?,1,?)";
	private static final String updateStat = "update `tum_user_statistics` set total_money =(total_money+?),"
			+ " total_order=(total_order+1),total_recharge=(total_recharge+?) where user_id=?";
	
	public void updateStat(Integer userId, Integer consumeMoney, Integer rechargeMoney){
		Integer statCount = this.findOneForJdbc(getStatCount, Integer.class, userId);
		if(statCount.equals(0)){
			this.executeSql(addStat, userId, consumeMoney, rechargeMoney);
		} else if(statCount.equals(1)){
			this.executeSql(updateStat, consumeMoney, rechargeMoney, userId);
		}
	}

}
