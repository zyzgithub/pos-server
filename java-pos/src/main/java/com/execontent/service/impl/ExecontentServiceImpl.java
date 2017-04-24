package com.execontent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.execontent.service.ExecontentServiceI;

@Service("execontentService")
@Transactional
public class ExecontentServiceImpl extends CommonServiceImpl implements ExecontentServiceI {
	
}