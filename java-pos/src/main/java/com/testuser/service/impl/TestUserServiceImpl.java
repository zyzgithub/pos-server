package com.testuser.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.testuser.service.TestUserServiceI;

@Service("testUserService")
@Transactional
public class TestUserServiceImpl extends CommonServiceImpl implements TestUserServiceI {
	
}