package com.courier_mana.management.service;

import java.util.List;
import java.util.Map;

/**(OvO)
 * 管理-配送管理-店铺分配模块接口
 * @author hyj
 *
 */
public interface AssignMerchantToCourierService {
	/**(OvO)
	 * 获取得快递员-店铺的分配情况
	 * @param	courierId		快递员ID(必选)
	 * @return 返回快递员和店铺之间的分配情况数据, 包括
	 * 			courierId		快递员ID
	 * 			courierName		快递员姓名
	 * 			merchants		已分配给此快递员的店铺列表
	 * 				merchantId		店铺ID
	 * 				merchantName	店铺名称
	 */
	public abstract List<Map<String, Object>> getAssignmentData(Integer courierId);
	
	/**(OvO)
	 * 删除指定店铺, 指定快递员之间的绑定关系
	 * @param merchantId	店铺ID
	 * @param courierId		快递员ID
	 * @return	受影响的行数(1或0)
	 */
	public abstract Integer deleteOneMerchantAssignmentRecord(Integer merchantId, Integer courierId);
	
	/**(OvO)
	 * 添加指定店铺, 指定快递员之间的绑定关系
	 * @param merchantId	店铺ID
	 * @param courierId		快递员ID
	 * @return	受影响的行数(1或0)
	 */
	public abstract Integer insertOneMerchantAssignmentRecord(Integer merchantId, Integer courierId);
	
	/**(OvO)
	 * 获取可以分配给目标快递员的店铺列表
	 * @param courierId			快递员ID(必选)
	 * @return	可以分配给目标快递员的店铺列表, 包括
	 * 			merchantId		店铺ID
	 * 			merchantName	店铺名称
	 * 			isSelected		选定flag(1: 店铺在页面上处于选中状态)
	 */
	public abstract List<Map<String, Object>> getMerchantList(Integer courierId);
	
	/**(OvO)
	 * 更新数据库中店铺-快递员关联信息
	 * @param courierId			要修改关联的快递员ID(必选)
	 * @param merchantIds		店铺ID数组(必选)
	 * @param loginedCourierId	本次登录的快递员
	 * @return 返回更新后的快递员和店铺之间的分配情况数据, 包括
	 * 			courierId		快递员ID
	 * 			courierName		快递员姓名
	 * 			merchants		已分配给此快递员的店铺列表
	 * 				merchantId		店铺ID
	 * 				merchantName	店铺名称
	 */
	public abstract List<Map<String, Object>> updateAssignmentRecord(Integer courierId, Integer[] merchantIds, Integer loginedCourierId);
}
