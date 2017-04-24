package com.courier_mana.management.service;

import java.util.List;
import java.util.Map;

/**(OvO)
 * 管理-监控模块接口
 * @author hyj
 *
 */
public interface BuildingAndFloorMonitorService {
	/**(OvO)
	 * 获取店铺被哪些快递员绑定的信息
	 * @param	courierId		快递员ID(必选)
	 * @return	返回店铺-快递员关联的信息, 包含
	 * 			merchantId		店铺ID
	 * 			merchantName	店铺名称
	 * 			boundCouriers	店铺下绑定的快递员
	 * 				courierId	快递员ID
	 * 				courierName	快递员名称
	 */
	public abstract List<Map<String, Object>> getMerchantAssignmentInfo(Integer courierId);
	
	/**(OvO)
	 * 获得管理-监控-店铺监控-快递员浮层
	 * @param merchantId
	 * @return	返回和店铺同区域的快递员列表, 包含
	 * 			courierId		快递员ID
	 * 			courierName		快递员姓名
	 * 			isSelected		选中flag(true: 快递员在页面上处于选中状态)
	 */
	public abstract List<Map<String, Object>> getCourierList4Merchant(Integer merchantId);
	
	/**(OvO)
	 * 更新数据库中店铺-快递员关联信息
	 * @param merchantId	店铺ID
	 * @param courierIds	快递员ID列表
	 * @return	
	 */
	public abstract Map<String, Object> updateMerchantAssignment(Integer merchantId, Integer[] courierIds);
	
	/**(OvO)
	 * 获取大厦被分配情况的信息
	 * @param courierId	快递员ID(必选)
	 * @return	返回大厦被分配情况的信息, 包含:
	 * 			buildingId		大厦ID
	 * 			buildingName	大厦名称
	 * 			assignStatus	分配状态falg(false: 未分配; null部分未分配; true: 已分配)
	 * 			hasFloor		楼层flag(false: 大厦没有楼层)
	 */
	public abstract List<Map<String, Object>> getBuildingAssignmentInfo(Integer courierId);
}
