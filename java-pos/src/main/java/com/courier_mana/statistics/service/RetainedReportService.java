package com.courier_mana.statistics.service;

import com.alibaba.fastjson.JSONObject;
import com.courier_mana.common.vo.SearchVo;

/**
 * 留存报表Service
 * @author hyj
 */
public interface RetainedReportService {
	
	/**
	 * 获取留存报表数据
	 * @author hyj
	 * @param timeType	时间类型
	 * @param warehouseId 仓库ID
	 * @return
	 */
	public abstract JSONObject getRetainedReportData(SearchVo timeType, Integer warehouseId);
	
	/**
	 * 获取所有仓库的信息(id + 名称)
	 * @return
	 */
	public abstract JSONObject getAllWarehouseInfo();
}
