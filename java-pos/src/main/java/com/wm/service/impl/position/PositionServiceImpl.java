package com.wm.service.impl.position;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.position.PositionEntity;
import com.wm.service.position.PositionServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("positionService")
@Transactional
public class PositionServiceImpl extends CommonServiceImpl implements PositionServiceI {
	
	@Override
	public PositionEntity getPositionEntity(Integer id) {
		return get(PositionEntity.class, id);
	}
}