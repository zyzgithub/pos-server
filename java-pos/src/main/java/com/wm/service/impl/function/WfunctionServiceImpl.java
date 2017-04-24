package com.wm.service.impl.function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.function.WfunctionServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("wfunctionService")
@Transactional
public class WfunctionServiceImpl extends CommonServiceImpl implements WfunctionServiceI {
	
}