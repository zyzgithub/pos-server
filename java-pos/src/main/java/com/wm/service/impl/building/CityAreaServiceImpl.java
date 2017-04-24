package com.wm.service.impl.building;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.building.CityAreaServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("cityAreaService")
@Transactional
public class CityAreaServiceImpl extends CommonServiceImpl implements CityAreaServiceI {
	
}