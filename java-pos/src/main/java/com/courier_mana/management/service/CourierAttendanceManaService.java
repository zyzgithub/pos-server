package com.courier_mana.management.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 管理-考勤管理模块接口
 * @author hyj
 *
 */
public interface CourierAttendanceManaService {
	/**(OvO)
	 * 获取快递员考勤信息
	 * @param vo		搜索条件(时间, 区域)VO
	 * @param courierId	快递员ID(必选)
	 * @return	返回快递员的考勤信息, 包括:
	 * 			courierName		快递员姓名
	 * 			checkIn			上班打卡记录
	 * 			checkOut		下班打卡记录
	 */
	public abstract List<Map<String, Object>> courierAttendance(SearchVo vo, Integer courierId);
}
