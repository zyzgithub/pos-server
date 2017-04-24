package com.wm.service.statistics;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.statistics.MerchatStatisticsDalyEntity;

public interface MerchatStatisticsDalyServiceI extends CommonService{

	/**
	 * 保存日统计数据
	 * @param msde
	 */
	void saveDayly(MerchatStatisticsDalyEntity msde);

	/**
	 * 查询商家订单统计
	 * @param merchantId
	 * @param statiDay 统计日期 yyyy-MM-dd
	 * @return
	 */
	List<Map<String, Object>> findOrderStati(Integer merchantId, String statiDay);

}
