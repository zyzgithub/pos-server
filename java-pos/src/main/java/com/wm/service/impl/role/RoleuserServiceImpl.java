package com.wm.service.impl.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.role.RoleuserServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("roleuserService")
@Transactional
public class RoleuserServiceImpl extends CommonServiceImpl implements RoleuserServiceI {
	
}