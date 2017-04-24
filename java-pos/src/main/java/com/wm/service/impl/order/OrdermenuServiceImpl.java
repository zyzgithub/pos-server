package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.OrdermenuServiceI;

@Service("ordermenuService")
@Transactional
public class OrdermenuServiceImpl extends CommonServiceImpl implements OrdermenuServiceI {
	
}