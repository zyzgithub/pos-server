package com.wm.service.impl.couriersalarylog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.couriersalarylog.CourierSalaryLogServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierSalaryLogService")
@Transactional
public class CourierSalaryLogServiceImpl extends CommonServiceImpl implements CourierSalaryLogServiceI {
	
}