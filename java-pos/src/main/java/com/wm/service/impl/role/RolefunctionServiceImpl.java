package com.wm.service.impl.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.role.RolefunctionServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("rolefunctionService")
@Transactional
public class RolefunctionServiceImpl extends CommonServiceImpl implements RolefunctionServiceI {
	
}