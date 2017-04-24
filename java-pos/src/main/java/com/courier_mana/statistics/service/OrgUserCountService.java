package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 统计-网点人数统计service
 * @author hyj
 */
public interface OrgUserCountService {
	
	/**(OvO)
	 * 获取网点人数统计信息
	 * @param userId	当前登录用户
	 * @param vo		查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @param page		页数
	 * @param rows		每页显示内容数量
	 * @return	id			机构ID
	 * 			orgName		机构名称
	 * 			userCount	总用户量
	 * 			newUser		新用户数
	 * 			oldUser		旧用户数
	 * 			reorderRate	复购率
	 */
	public abstract List<Map<String, Object>> getOrgUserInfo(Integer userId, SearchVo vo, Integer page, Integer rows);
}
