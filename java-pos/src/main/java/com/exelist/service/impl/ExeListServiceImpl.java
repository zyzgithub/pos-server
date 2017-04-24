package com.exelist.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exelist.service.ExeListServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("exeListService")
@Transactional
public class ExeListServiceImpl extends CommonServiceImpl implements ExeListServiceI {
	
}