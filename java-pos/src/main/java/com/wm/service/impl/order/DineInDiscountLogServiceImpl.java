package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.DineInDiscountLogServiceI;

@Service("dineInDiscountLogService")
@Transactional
public class DineInDiscountLogServiceImpl extends CommonServiceImpl implements DineInDiscountLogServiceI{
	
	private static final String getCount = "select count(*) from dine_in_discount_log where order_id=?";

	private static final String insertSQL = "insert into dine_in_discount_log ( order_id, origin_money, online_money, discount, discount_money, "
			+ " create_time, merchant_id, user_id, merchant_share_percent, platform_share_percent ) select o.id as order_id, "
			+ " o.origin * 100 as origin_money, ? * 100 as online_money, mi.dine_in_discount as discount, (o.origin - ?) * 100 as discount_money, "
			+ " now() as create_time, o.merchant_id as merchant_id, o.user_id as user_id, mi.merchant_share_percent as merchant_share_percent, "
			+ " mi.platform_share_percent as platform_share_percent from `order` as o left join 0085_merchant_info as mi on o.merchant_id = mi.merchant_id "
			+ " where o.id = ? and mi.is_dine_in_discount = 1";

	@Override
	public void createLog(Integer orderId,Double onlineMoney) {
		Integer count = this.findOneForJdbc(getCount, Integer.class, orderId);
		if(count == null || count.equals(0)){
			this.executeSql(insertSQL, onlineMoney,onlineMoney,orderId);
		}
		
	}

}
