package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

/**(OvO)
 * 复购报表Service
 * @author hyj
 */
public interface ReorderStatisticService {
	
	/**(OvO)
	 * 获取复购报表数据
	 * @param userId	当前登录用户ID
	 * @param period	查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @param beginTime	自定义开始时间
	 * @param endTime	自定义结束时间
	 * @param page		页码
	 * @param rows		每页显示记录数
	 */
	public abstract List<Map<String, Object>> getReorderInfo(Integer userId, String period, Integer beginTime, Integer endTime, Integer page, Integer rows);
}
