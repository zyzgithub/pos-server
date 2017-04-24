package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.TomOrderTimerServiceI;
@Service("tomOrderTimerService")
@Transactional
public class TomOrderTimerServiceImpl extends CommonServiceImpl implements TomOrderTimerServiceI{

	@Override
	public void createOrUpdate(Integer orderId, Integer merchantId, Integer createTime) {
		String sql = "insert tom_order_timer(order_id, merchant_id,create_time,from_type) values(?,?,?,?)";
		this.executeSql(sql, orderId,merchantId,createTime,1);
	}


}
