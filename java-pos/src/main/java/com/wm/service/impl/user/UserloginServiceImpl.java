package com.wm.service.impl.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.user.UserloginServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("userloginService")
@Transactional
public class UserloginServiceImpl extends CommonServiceImpl implements UserloginServiceI {
	
}