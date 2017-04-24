package com.wm.service.impl.opendevelopuser;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wm.service.opendevelopuser.OpenDevelopUserServiceI;

@Service("openDevelopUserService")
@Transactional
public class OpenDevelopUserServiceImpl extends CommonServiceImpl implements OpenDevelopUserServiceI {
	
}