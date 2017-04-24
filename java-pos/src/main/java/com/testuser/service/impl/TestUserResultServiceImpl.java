package com.testuser.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.testuser.service.TestUserResultServiceI;

@Service("testUserResultService")
@Transactional
public class TestUserResultServiceImpl extends CommonServiceImpl implements TestUserResultServiceI {
	
}