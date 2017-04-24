package com.wm.service.impl.courierpositionlog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.courierpositionlog.CourierPositionLogServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierPositionLogService")
@Transactional
public class CourierPositionLogServiceImpl extends CommonServiceImpl implements CourierPositionLogServiceI {
	
}