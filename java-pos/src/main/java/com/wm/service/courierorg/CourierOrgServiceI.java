package com.wm.service.courierorg;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

public interface CourierOrgServiceI extends CommonService{

	/**
	 * 根据parentOrgId,查找这个机构下的所有快递员
	 * @param parentOrgId 父机构ID
	 * @return 所有快递员
	 */
	List<Integer> queryCouriersByParentOrgId(Integer parentOrgId);
	
}
