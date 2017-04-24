package com.ci.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.ci.service.CustomerInterfaceServiceI;

@Service("customerInterfaceService")
@Transactional
public class CustomerInterfaceServiceImpl extends CommonServiceImpl implements CustomerInterfaceServiceI {
	
}