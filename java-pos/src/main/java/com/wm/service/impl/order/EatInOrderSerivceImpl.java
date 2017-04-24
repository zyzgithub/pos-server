package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.EatInOrderServiceI;

@Service("eatInOrderSerivceImpl")
@Transactional
public class EatInOrderSerivceImpl extends CommonServiceImpl implements EatInOrderServiceI{

	@Override
	public void updateStatus(Integer orderId) {
		String insertSQL = "UPDATE eatin_order SET `status` = 'pay' WHERE order_id = ?";
		this.executeSql(insertSQL, orderId);		
	}

}
