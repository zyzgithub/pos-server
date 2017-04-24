package com.wm.service.impl.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.role.WRoleServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("wRoleService")
@Transactional
public class WRoleServiceImpl extends CommonServiceImpl implements WRoleServiceI {
	
}