package com.wm.service.statistics;

import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.org.OrgEntity;

public interface OrgStatisticsDaylyServiceI extends CommonService{

	void statisticsDayly(OrgEntity org);

	/**
	 * 获取网点昨天的提成统计
	 * @param orgId
	 * @param deductDay
	 */
	Map<String, Object> getYestodaySta(Integer orgId, String deductDay);

}
