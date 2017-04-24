package com.wm.service.position;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.position.PositionEntity;

public interface PositionServiceI extends CommonService{
	
	PositionEntity getPositionEntity(Integer id);

}
