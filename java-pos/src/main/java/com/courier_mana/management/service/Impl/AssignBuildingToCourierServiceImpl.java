package com.courier_mana.management.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jodd.util.StringUtil;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.management.service.AssignBuildingToCourierService;

@Service
public class AssignBuildingToCourierServiceImpl extends CommonServiceImpl implements AssignBuildingToCourierService {
	private final static Logger logger = LoggerFactory.getLogger(AssignBuildingToCourierServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> getAssignmentData(Integer courierId) {
		logger.info("Invoke method: getAssignmentData， params: courierId - {}", courierId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 获取管辖的快递员列表
		 */
		List<Integer> courierIds = this.courierOrgService.getManageCouriersId(courierId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.id courierId, u.username courierName, cob.buildingId, cob.buildingName, cob.bFirstFloor, cob.bLastFloor, cob.cFirstFloor, cob.cLastFloor ");
		sql.append(" FROM `user` u LEFT JOIN( ");
		sql.append(" 	SELECT cb.courier_id, b.id buildingId, b.`name` buildingName, b.first_floor bFirstFloor, b.last_floor bLastFloor, cb.first_floor cFirstFloor, cb.last_floor cLastFloor ");
		sql.append(" 	FROM 0085_courier_building cb, 0085_building b ");
		sql.append(" 	WHERE cb.building_id = b.id ");
		sql.append(" )cob ON u.id = cob.courier_id ");
		sql.append(" WHERE u.id IN ( ");
		sql.append(StringUtil.join(courierIds, ","));
		sql.append(" ) ");
		sql.append(" ORDER BY u.id ");
		
		logger.debug("Inside method: getAssignmentData, SQL: {}", sql.toString());
		
		/**
		 * 获取原始的快递员店铺关系数据
		 */
		List<Map<String, Object>> sourceAssignmentData = this.findForJdbc(sql.toString());
		
		/**
		 * 返回处理后的快递员-大厦数据
		 */
		return this.getAssignmentDataResultList(sourceAssignmentData);
	}

	/**(OvO)
	 * 将原始的快递员-大厦关系信息组装成需要的结构
	 * @param sourceList	原始的快递员-店铺关系信息
	 * @return	返回页面需要的快递员-大厦关系数据, 包括:
	 * 
	 */
	private List<Map<String, Object>> getAssignmentDataResultList(List<Map<String, Object>> sourceList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	//用于返回结果的List
		Map<String, Object> courierData = null;			//保存每个快递员-大厦关系的Map
		List<Map<String, Object>> buildingList = null;	//存放与快递员有关大厦的List
		Map<String, Object> buildingInfo = null;		//大厦信息Map
		Set<Integer> floorSet = null;					//快递员绑定楼层Set, 使用Set是方便统计不连续的楼层, 但在数据库0085_courier_building表中, 快递员ID和大厦ID为联合主键, 不可能出现楼层不连续的现象, 这个以后再移除
		Integer preCourierId = null;					//标志位, 快递员ID
		Integer preBuildingId = null;					//标志位, 大厦ID, 默认应该为null
		/**
		 * 遍历原始数据
		 */
		for(Map<String, Object> record: sourceList){
			Integer courierId = Integer.parseInt(record.get("courierId").toString());
			Integer buildingId = (Integer)record.get("buildingId");	//此字段有可能为空, 用上面的方法去构造一个Integer必然GG
			/**
			 * 对比记录中的快递员ID是否和标志位(preCourierId)相同
			 * · 和标志位不同就在resultList中添加一个item(一个快递员店铺分配信息)
			 */
			if(!courierId.equals(preCourierId)){
				/**
				 * 为快递员构建新数据项
				 */
				courierData = new HashMap<String, Object>();
				courierData.put("courierId", courierId);
				courierData.put("courierName", record.get("courierName"));
				buildingList = new ArrayList<Map<String, Object>>();
				courierData.put("addresses", buildingList);
				resultList.add(courierData);
				/**
				 * 更新标志位
				 */
				preCourierId = courierId;
				preBuildingId = null;
			}
			/**
			 * 对比记录中的大厦ID是否和标志位(preBuildingId)相同
			 * · 和标志位不同就在buildingList中添加一个item(大厦信息)
			 */
			if(buildingId != null && !buildingId.equals(preBuildingId)){
				/**
				 * 为大厦构建新数据项
				 */
				buildingInfo = new HashMap<String, Object>();
				buildingInfo.put("buildingId", record.get("buildingId"));
				buildingInfo.put("buildingName", record.get("buildingName"));
				floorSet = new TreeSet<Integer>();
				/**
				 * 检查大厦是否有楼层
				 */
				if(record.get("bFirstFloor") != null){
					buildingInfo.put("hasFloor", true);
				}else{
					buildingInfo.put("hasFloor", false);
				}
				buildingList.add(buildingInfo);
				/**
				 * 更新标志位
				 */
				preBuildingId = buildingId;
			}
			/**
			 * 将快递员绑定的楼层信息写到大厦信息Map中
			 * 如果此记录并没有与大厦相关的信息或者大厦没有楼层就跳过
			 */
			if(buildingId != null && buildingInfo.get("hasFloor").equals(true)){
				Integer cFirstFloor = (Integer)record.get("cFirstFloor");
				Integer cLastFloor = (Integer)record.get("cLastFloor");
				if(cFirstFloor != null && cLastFloor != null){
					for(int i=cFirstFloor;i<=cLastFloor;i++){
						floorSet.add(i);
					}
				}
				buildingInfo.put("floors", floorSet.toArray(new Integer[0]));
			}
		}
		return resultList;
	}
}
