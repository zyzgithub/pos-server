package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 盈亏报表Service
 * @author hyj
 */
public interface ProfitAndLossService {
	/**(OvO)
	 * 获得各网点盈亏情况列表
	 * @param orgIdsStr		纳入统计的区域ID(用","分隔)
	 * @param courierIds	纳入统计的快递员ID(用","分隔)
	 * @param timeType		时间搜索参数
	 * @param page			页码
	 * @param rows			每页显示记录条数
	 * @return
	 */
	public abstract List<Map<String, Object>> getOrgPALList(String orgIdsStr, SearchVo timeType, Integer page, Integer rows);
}
