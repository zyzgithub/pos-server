package com.wm.service.courierbuilding;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.courierbuilding.CourierBuildingEntity;

public interface CourierBuildingServiceI extends CommonService {

	/**
	 * 通过快递员id和大厦id查
	 * 
	 * @param courierId
	 * @param buildingId
	 * @return
	 */
	CourierBuildingEntity findByCidBid(Integer courierId, Integer buildingId);

	/**
	 * 批量保存快递员大厦
	 * 在快递员管理员选择录入的时候，可以批量指定大厦
	 * @param courierId
	 *            快递员尖
	 * @param buildingIds
	 *            以,分隔的大厦列表，如("1,2,3,4")
	 */
	void batchSaveCourierBuilding(Integer courierId, String buildingIds);
	

	/**
	 * 保存快递员对应大厦的楼层
	 * 在快递员管理员选择录入的时候，可以批量指定大厦
	 * @param courierId
	 *            快递员尖
	 * @param buildingIds
	 *            以,分隔的大厦列表，如("1,2,3,4")
	 * @param floors 大厦的楼层， 使用逗号分隔开，如(1,2,3,4)
	 */
	void saveCourierBuildingFloors(Integer courierId, Integer buildingId, String floors);


	/**
	 * 查询快递员分配的对应网点的大厦和楼层列表
	 * @param courierId 快递员id
	 * @return
	 */
	List<Map<String, Object>> queryBldsFloorsByCid(Integer courierId);
	


}
