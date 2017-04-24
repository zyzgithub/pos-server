package com.wm.service.impl.couriergroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.couriergroup.CourierGroupServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierGroupService")
@Transactional
public class CourierGroupServiceImpl extends CommonServiceImpl implements CourierGroupServiceI {
	
}