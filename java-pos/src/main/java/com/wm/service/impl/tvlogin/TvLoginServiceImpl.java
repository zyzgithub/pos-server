package com.wm.service.impl.tvlogin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.tvlogin.TvLoginServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("tvLoginService")
@Transactional
public class TvLoginServiceImpl extends CommonServiceImpl implements TvLoginServiceI {
	
}