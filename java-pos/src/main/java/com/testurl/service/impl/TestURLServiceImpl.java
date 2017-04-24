package com.testurl.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.testurl.service.TestURLServiceI;

@Service("testURLService")
@Transactional
public class TestURLServiceImpl extends CommonServiceImpl implements TestURLServiceI {
	
}