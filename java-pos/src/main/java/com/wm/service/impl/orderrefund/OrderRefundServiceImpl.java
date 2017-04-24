package com.wm.service.impl.orderrefund;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.orderrefund.OrderRefundServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("orderRefundService")
@Transactional
public class OrderRefundServiceImpl extends CommonServiceImpl implements OrderRefundServiceI {
	
}