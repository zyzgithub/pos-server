package com.wm.service.impl.systemconfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.service.systemconfig.SystemconfigServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("systemconfigService")
@Transactional
public class SystemconfigServiceImpl extends CommonServiceImpl implements SystemconfigServiceI {

	@Override
	public String getValByCode(String code) {
		SystemconfigEntity sysConfig = this.findUniqueByProperty(
				SystemconfigEntity.class, "code", code);
		return sysConfig.getValue();
	}

}