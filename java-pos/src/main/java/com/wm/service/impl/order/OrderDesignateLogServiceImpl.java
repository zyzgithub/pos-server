package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.OrderDesignateLogServiceI;

@Service("orderDesignateLogService")
@Transactional
public class OrderDesignateLogServiceImpl extends CommonServiceImpl implements
		OrderDesignateLogServiceI {
	
}