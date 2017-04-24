package com.log.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.log.service.OperateLogServiceI;

@Service("operateLogService")
@Transactional
public class OperateLogServiceImpl extends CommonServiceImpl implements OperateLogServiceI {
	
}