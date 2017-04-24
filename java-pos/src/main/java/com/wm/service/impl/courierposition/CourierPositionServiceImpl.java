package com.wm.service.impl.courierposition;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.courierposition.CourierPositionServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierPositionService")
@Transactional
public class CourierPositionServiceImpl extends CommonServiceImpl implements CourierPositionServiceI {
	
}