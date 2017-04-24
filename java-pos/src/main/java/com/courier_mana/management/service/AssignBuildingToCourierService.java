package com.courier_mana.management.service;

import java.util.List;
import java.util.Map;

/**(OvO)
 * 管理-配送管理-大厦分配模块接口
 * @author hyj
 */
public interface AssignBuildingToCourierService {
	/**(OvO)
	 * 获取得快递员-大厦的分配情况
	 * @param courierId		快递员ID(必选)
	 * @return	返回快递员和店铺之间的分配情况数据, 包括
	 * 			courierId			快递员ID(用于添加、删除分配的大厦)
	 * 			courierName			快递员姓名
	 * 			addresses			配送地址List, 包括
	 * 				buildingId		大厦ID(用于添加、删除分配的大厦)
	 * 				buildingName	大厦名
	 * 				floors			楼层
	 * 				hasFloor		楼层flag(false: 大厦没有楼层)
	 */
	public abstract List<Map<String, Object>> getAssignmentData(Integer courierId);
}
