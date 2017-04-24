package com.wm.service.impl.order;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.orderlimitlog.OrderLimitLogEntity;
import com.wm.service.order.DineInDiscountLogServiceI;
import com.wm.service.order.OrderLimitLogServiceI;

@Service("orderLimitLogService")
@Transactional
public class OrderLimitLogServiceImpl extends CommonServiceImpl implements OrderLimitLogServiceI{
	
	
	private static final String selSQL = "from com.wm.entity.orderlimitlog.OrderLimitLogEntity o  WHERE o.userId =? and o.merchantId =? and o.payType = ? and FROM_UNIXTIME((o.createTime),'%Y-%m-%d' ) = DATE_FORMAT(NOW(),'%Y-%m-%d')";
	  
	private static final String insertSQL = "insert into order_limit_log(merchant_id,user_id,create_time,pay_type,amount) values(?,?,?,?,?)";
	
	private static final String updateSQL = "update order_limit_log set amount = ? where id = ?";

	@Override
	public void updateOrderLimitLog(OrderLimitLogEntity orderLimitLog) {
			this.updateEntitie(orderLimitLog);
	}

	@Override
	public OrderLimitLogEntity getOrderLimitLogToday(Integer userId, Integer merchantId,String payType) {
		List<OrderLimitLogEntity> list =  this.findHql(selSQL, new Object[]{userId,merchantId,payType});
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}

	@Override
	public void createOrderLimitLog(OrderLimitLogEntity orderLimitLog) {
		this.executeSql(insertSQL, orderLimitLog.getMerchantId(),orderLimitLog.getUserId(),orderLimitLog.getCreateTime(),orderLimitLog.getPayType(),orderLimitLog.getAmount());
		
	}


}
