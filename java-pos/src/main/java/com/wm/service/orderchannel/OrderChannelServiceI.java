package com.wm.service.orderchannel;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.orderchannel.OrderChannel;


public interface OrderChannelServiceI extends CommonService{

	public OrderChannel getEntityByOrderId(Integer id);

}
