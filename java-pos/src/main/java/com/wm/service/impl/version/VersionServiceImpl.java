package com.wm.service.impl.version;

import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.version.VersionEntity;
import com.wm.service.version.VersionServiceI;

@Service("versionService")
@Transactional
public class VersionServiceImpl extends CommonServiceImpl implements VersionServiceI {

	@Override
	public VersionEntity getLastVersion(String type) {
		List<VersionEntity> list = this.findHql(" from VersionEntity as v where v.type=? order by v.currentVersion desc ", new Object[] { type });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}