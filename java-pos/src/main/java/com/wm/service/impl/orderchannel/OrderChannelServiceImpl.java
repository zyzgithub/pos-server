package com.wm.service.impl.orderchannel;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.orderchannel.OrderChannel;
import com.wm.service.orderchannel.OrderChannelServiceI;

@Service("orderChannelService")
@Transactional
public class OrderChannelServiceImpl extends CommonServiceImpl implements OrderChannelServiceI {
	@Override
	public OrderChannel getEntityByOrderId(Integer id) {
			return this.findUniqueByProperty(OrderChannel.class, "orderId", id);		
	}
}
