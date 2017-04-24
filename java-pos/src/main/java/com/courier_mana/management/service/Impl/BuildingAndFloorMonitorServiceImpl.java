package com.courier_mana.management.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.management.service.AssignMerchantToCourierService;
import com.courier_mana.management.service.BuildingAndFloorMonitorService;

@Service
public class BuildingAndFloorMonitorServiceImpl extends CommonServiceImpl implements BuildingAndFloorMonitorService{
	private final static Logger logger = LoggerFactory.getLogger(BuildingAndFloorMonitorServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Autowired
	private AssignMerchantToCourierService assignMerchantToCourierService;
	
	@Override
	public List<Map<String, Object>> getMerchantAssignmentInfo(Integer courierId) {
		logger.info("Invoke method: getMerchantAssignmentInfo， params: courierId - {}", courierId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 获取管辖范围的orgId
		 */
		List<Integer> orgIds = this.courierOrgService.getManageOrgIds(courierId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT morg.merchant_id merchantId, m.title merchantName, mc.id courierId, mc.username courierName ");
		sql.append(" FROM 0085_merchant_org morg, merchant m LEFT JOIN( ");
		sql.append(" 	SELECT cm.merchant_id, u.id, u.username ");
		sql.append(" 	FROM 0085_courier_merchant cm, `user` u ");
		sql.append(" 	WHERE cm.courier_id = u.id ");
		sql.append(" )mc ON mc.merchant_id = m.id ");
		sql.append(" WHERE morg.merchant_id = m.id AND morg.org_id IN( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		sql.append(" ORDER BY morg.org_id, morg.merchant_id, mc.username, mc.id ");
		
		logger.debug("Inside method: getMerchantAssignmentInfo, SQL: {}", sql.toString());
		
		/**
		 * 获取店铺-快递员关系的原始信息
		 */
		List<Map<String, Object>> sourceList = this.findForJdbc(sql.toString());
		
		/**
		 * 返回处理过的数据
		 */
		return this.getMerchantAssignmentInfoResultList(sourceList);
	}
	
	@Override
	public List<Map<String, Object>> getCourierList4Merchant(Integer merchantId){
		logger.info("Invoke method: getCourierList4Merchant， params: merchantId - {}", merchantId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(merchantId == null){
			throw new IllegalArgumentException("merchantId不能为空");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.id courierId, u.username courierName ");
		sql.append(" 	,IFNULL((SELECT 1 FROM 0085_courier_merchant cm WHERE cm.courier_id = u.id AND cm.merchant_id = morg.merchant_id),0) isSelected ");
		sql.append(" FROM 0085_merchant_org morg, 0085_courier_org corg, `user` u, 0085_courier_position cp ");
		sql.append(" WHERE morg.org_id = corg.org_id AND corg.courier_id = u.id AND u.is_delete = 0 AND cp.courier_id = u.id AND cp.position_id = 2 ");
		sql.append(" 	AND morg.merchant_id = ");
		sql.append(merchantId);
		sql.append(" ORDER BY u.username, u.id ");
		
		logger.debug("Inside method: getCourierList4Merchant, SQL: {}", sql.toString());
		
		return this.findForJdbc(sql.toString());
	}
	
	@Override
	public Map<String, Object> updateMerchantAssignment(Integer merchantId, Integer[] courierIds){
		logger.info("Invoke method: getCourierList4Merchant， params: merchantId - {}, courierIds - {}", merchantId, courierIds);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(merchantId == null){
			throw new IllegalArgumentException("merchantId不能为空");
		}
		if(courierIds == null){
			throw new IllegalArgumentException("courierIds不能为空");
		}
		
		/**
		 * 将前端传来的courierIds转换为Set
		 */
		Set<Integer> courierIdsSelected = new HashSet<Integer>(Arrays.asList(courierIds));
		
		/**
		 * 查询已经绑定到此商家的快递员ID
		 */
		List<Integer> courierIdsInDB = this.getCourierIdsByMerchantId(merchantId);
		
		Integer deleteCount = 0;
		Integer insertCount = 0;
		
		/**
		 * 遍历已经绑定到此的快递员ID, 并与前端选中的ID作比较
		 */
		for(Integer courierIdInDB: courierIdsInDB){
			/**
			 * 如果ID已绑定, 而且前端选定了
			 * 则不需要对数据库作任何操作
			 * 此ID从Set中删除(Set中的ID在后面会使用它进行数据库的插入操作)
			 */
			if(courierIdsSelected.contains(courierIdInDB)){
				courierIdsSelected.remove(courierIdInDB);
			}
			/**
			 * 如果ID已绑定, 但前端没有选定
			 * 则说明要对此ID进行解绑操作
			 */
			else{
				deleteCount += this.assignMerchantToCourierService.deleteOneMerchantAssignmentRecord(merchantId, courierIdInDB);
			}
		}
		
		/**
		 * 遍历处理后的Set
		 * 并将余下的ID绑定到指定店铺
		 */
		for(Integer courierId: courierIdsSelected){
			insertCount += this.assignMerchantToCourierService.insertOneMerchantAssignmentRecord(merchantId, courierId);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("deleteCount", deleteCount);
		result.put("insertCount", insertCount);
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> getBuildingAssignmentInfo(Integer courierId) {
		logger.info("Invoke method: getBuildingAssignmentInfo， params: courierId - {}", courierId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 获取管辖范围的orgId
		 */
		List<Integer> orgIds = this.courierOrgService.getManageOrgIds(courierId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT bd.*, cb.first_floor cFirstFloor, cb.last_floor cLastFloor, cb.courier_id courierId ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT b.id buildingId, b.`name` buildingName, borg.org_id, b.first_floor firstFloor, b.last_floor lastFloor ");
		sql.append(" 	FROM 0085_building b, 0085_building_org borg ");
		sql.append(" 	WHERE b.id = borg.building_id ");
		sql.append(" 		AND borg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		sql.append(" 	GROUP BY b.id ");
		sql.append(" )bd LEFT JOIN 0085_courier_building cb ON cb.building_id = bd.buildingId ");
		sql.append(" ORDER BY bd.org_id, bd.buildingId ");
		
		logger.debug("Inside method: getBuildingAssignmentInfo, SQL: {}", sql.toString());
		
		/**
		 * 获取大厦-快递员关系的原始信息
		 */
		List<Map<String, Object>> sourceList = this.findForJdbc(sql.toString());
		
		/**
		 * 返回处理过的数据
		 */
		return this.getBuildingAssignmentResultList(sourceList);
	}

	/**(OvO)
	 * 将原始的店铺-快递员关系信息组装成需要的结构
	 * @return	返回页面需要的店铺-快递员关系数据, 包括:
	 * 			merchantId		店铺ID
	 * 			merchantName	店铺名称
	 * 			boundCouriers	店铺下绑定的快递员
	 * 				courierId	快递员ID
	 * 				courierName	快递员名称
	 */
	private List<Map<String, Object>> getMerchantAssignmentInfoResultList(List<Map<String, Object>> sourceList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	//用于返回结果的List
		Map<String, Object> merchantData = null;			//保存每个店铺-快递员关系的Map
		List<Map<String, Object>> courierList = null;		//存放店铺下快递员的List
		Map<String, Object> courierInfo = null;				//快递员信息Map
		Integer preMerchantId = null;						//标志位, 快递员ID
		/**
		 * 遍历原始数据
		 */
		for(Map<String, Object> record: sourceList){
			Integer merchantId = Integer.parseInt(record.get("merchantId").toString());
			/**
			 * 对比记录中的快递员ID是否和标志位(preMerchantId)相同
			 * · 和标志位不同就在resultList中添加一个item(一个店铺-快递员关系信息)
			 */
			if(!merchantId.equals(preMerchantId)){
				/**
				 * 为店铺构建新的数据项
				 */
				merchantData = new HashMap<String, Object>();
				merchantData.put("merchantId", merchantId);
				merchantData.put("merchantName", record.get("merchantName"));
				courierList = new ArrayList<Map<String, Object>>();
				merchantData.put("boundCouriers", courierList);
				resultList.add(merchantData);
				/**
				 * 更新标志位
				 */
				preMerchantId = merchantId;
			}
			/**
			 * 将快递员信息添加到店铺数据中
			 * 先判断记录中是否包含快递员信息(因为存在店铺没有配置快递员的可能性)
			 * 如果没有快递员信息，就没必要向courierList添加数据项
			 */
			if(record.get("courierId") != null){
				courierInfo = new HashMap<String, Object>();
				courierInfo.put("courierId", record.get("courierId"));
				courierInfo.put("courierName", record.get("courierName"));
				courierList.add(courierInfo);
			}
		}
		return resultList;
	}
	
	/**(OvO)
	 * 将原始的大厦-快递员关系信息组装大厦被分配情况信息
	 * @param sourceList	原始信息List
	 * @return	返回页面需要的大厦被分配情况数据, 包括:
	 * 			buildingId		大厦ID
	 * 			buildingName	大厦名称
	 * 			assignStatus	分配状态falg(false: 未分配; null部分未分配; true: 已分配)
	 * 			hasFloor		楼层flag(false: 大厦没有楼层)
	 */
	private List<Map<String, Object>> getBuildingAssignmentResultList(List<Map<String, Object>> sourceList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	//用于返回结果的List
		List<Map<String, Object>> notAssignedList = new ArrayList<Map<String, Object>>();	//未分配的List
		List<Map<String, Object>> partiallyAssignedList = new ArrayList<Map<String, Object>>();	//未完全分配的List
		List<Map<String, Object>> assignedList = new ArrayList<Map<String, Object>>();	//已分配的List
		
		Map<String, Object> buildingData = null;			//保存每座大厦信息的Map
		Integer preBuildingId = null;						//标志位, 大厦ID
		Integer buildingFirstFloor = null;					//大厦首层
		Integer buildingLastFloor = null;					//大厦顶层
		int[] floorArray = null;							//标志位, 楼层数组, 保存每层分配了多少个快递员的数组, 用于判断大厦的分配状态
		
		/**
		 * 遍历原始信息
		 */
		for(Map<String, Object> record: sourceList){
			Integer buildingId = Integer.parseInt(record.get("buildingId").toString());
			/**
			 * 对比记录中的大厦ID是否和标志位(preBuildingId)相同
			 * · 和标志位不同就在resultList中添加一个item(大厦信息的Map)
			 */
			if(!buildingId.equals(preBuildingId)){
				/**
				 * 先处理上一个大厦的数据
				 * 标志位不为空, 说明record不是第一条数据
				 * 此方法内的变量都是上一个大厦的信息
				 * 要先处理这些旧信息再新建新数据项
				 */
				if(preBuildingId != null){
					sortBuildingData(buildingData, notAssignedList, partiallyAssignedList, assignedList, floorArray);
				}
				/**
				 * 为大厦构建新的数据项并且初始化数据项
				 */
				buildingData = new HashMap<String, Object>();
				buildingData.put("buildingId", buildingId);
				buildingData.put("buildingName",record.get("buildingName"));
				/**
				 * 判断大厦是否有楼层
				 */
				buildingFirstFloor = (Integer)record.get("firstFloor");
				buildingLastFloor = (Integer)record.get("lastFloor");
				if(buildingFirstFloor == null || buildingLastFloor == null){
					buildingData.put("hasFloor", false);
				}else{//只要有楼层信息, 这大厦就算是有楼层(首层和顶层序号一样也算有楼层)
					buildingData.put("hasFloor", true);
					/**
					 * 初始化楼层数组
					 */
					floorArray = new int[buildingLastFloor - buildingFirstFloor + 1];
				}
				/**
				 * 更新标志位
				 */
				preBuildingId = buildingId;
			}
			/**
			 * 如果大厦有楼层就统计各层的快递员绑定数量
			 */
			if(buildingData.get("hasFloor").equals(true)){
				logger.info(buildingData.get("buildingId").toString());
				Integer cFirstFloor = (Integer)record.get("cFirstFloor");	//快递员所绑定的最低楼层
				Integer cLastFloor = (Integer)record.get("cLastFloor");		//快递员所绑定的最高楼层
				if(cFirstFloor != null && cLastFloor!= null){
					for(int i=cFirstFloor-buildingFirstFloor;i<=cLastFloor-buildingFirstFloor;i++){
						floorArray[i]++;
					}
				}
			}
			/**
			 * 如果大厦没有楼层就判断记录中的快递员ID字段是否为空
			 * 不为空，则此大厦的分配情况就为已分配
			 */
			else{
				if(record.get("courierId") != null){
					buildingData.put("assignStatus", true);
				}
			}
		}
		/**
		 * 对上一座大厦的数据是在循环头部进行的
		 * 所以此处要补充对最后一条记录的处理
		 */
		sortBuildingData(buildingData, notAssignedList, partiallyAssignedList, assignedList, floorArray);
		/**
		 * 拼接三个List
		 */
		resultList.addAll(notAssignedList);
		resultList.addAll(partiallyAssignedList);
		resultList.addAll(assignedList);
		return resultList;
	}
	
	/**(OvO)
	 * 为buildingData分类, 并添加到相应的List中
	 * @param buildingData			大厦信息
	 * @param notAssignedList		未分配List
	 * @param partiallyAssignedList	部分未分配List
	 * @param assignedList			已分配List
	 * @param floorArray			楼层数组(大厦有楼层时会用到)
	 */
	private void sortBuildingData(Map<String, Object> buildingData, List<Map<String, Object>> notAssignedList, List<Map<String, Object>> partiallyAssignedList, List<Map<String, Object>> assignedList, int[] floorArray){
		/**
		 * 考虑大厦没有楼层的情况
		 */
		if(buildingData.get("hasFloor").equals(false)){
			/**
			 * 对于没有楼层的大厦, 在遍历时assignStatus要么为true要么就是null
			 * 所以判断assignStatus就知道大厦究竟是已分配还是未分配
			 * (没有楼层的大厦不可能为部分分配)
			 */
			if(buildingData.get("assignStatus") == null){
				buildingData.put("assignStatus", false);
				notAssignedList.add(buildingData);
			}else{
				buildingData.put("assignStatus", true);
				assignedList.add(buildingData);
			}
		}
		/**
		 * 再考虑大厦有楼层的情况
		 */
		else{
			/**
			 * 要判断大厦的分配状态, 就要对floorArray进行遍历分析
			 * 如果数组有一个元素为空, 肯定不是已分配状态
			 * 如果数组各元素相加后得数为0, 肯定是未分配状态
			 */
			boolean isAssigned = true;
			int total = 0;
			for(int i:floorArray){
				if(isAssigned == true && i == 0){
					isAssigned = false;
					continue;
				}
				total += i;
			}
			if(isAssigned == true){
				buildingData.put("assignStatus", true);
				assignedList.add(buildingData);
			}else if(total >0){
				buildingData.put("assignStatus", null);
				partiallyAssignedList.add(buildingData);
			}else{
				buildingData.put("assignStatus", false);
				notAssignedList.add(buildingData);
			}
		}
	}
	
	/**(OvO)
	 * 获取绑定到指定店铺的快递员ID列表
	 * @param merchantId	店铺ID
	 * @return	返回绑定到指定店铺的快递员ID
	 */
	private List<Integer> getCourierIdsByMerchantId(Integer merchantId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cm.courier_id id ");
		sql.append(" FROM 0085_courier_merchant cm ");
		sql.append(" WHERE cm.merchant_id = ");
		sql.append(merchantId);
		
		return findListbySql(sql.toString());
	}
	
}
