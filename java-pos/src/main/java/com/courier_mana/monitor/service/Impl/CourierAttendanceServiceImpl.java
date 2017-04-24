package com.courier_mana.monitor.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.CourierAttendanceServiceI;

@Service
public class CourierAttendanceServiceImpl extends CommonServiceImpl implements CourierAttendanceServiceI {
	
	@Autowired
	private CourierOrgServicI courierOrgServicI;

	@Override
	public Integer getCourierOndutySum(Integer courierId) {
		int sum = 0;
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		List<Map<String, Object>> attendanceList=new ArrayList<Map<String,Object>>();
		//找出所有的快递员
		List<Map<String, Object>> courierList = courierOrgServicI.getManageCouriers(courierId);
		if(CollectionUtils.isEmpty(courierList)){
			return sum;
		}
		
		//根据快递员ID列表查询其打卡记录
		List<Integer> ids = new ArrayList<Integer>();
		if(courierList != null){
			for(Map<String, Object> courier : courierList){
				ids.add(Integer.parseInt(courier.get("id").toString()));
			}
		}		
		
		StringBuilder queryOrgSql = new StringBuilder();
		queryOrgSql.append(" select ca.user_id,  ca.type");
		queryOrgSql.append(" from 0085_courier_attendance ca");
		queryOrgSql.append("  where ");
		queryOrgSql.append("  create_time >= UNIX_TIMESTAMP(CURDATE())  ");
		queryOrgSql.append(" and ca.user_id in ( " + StringUtils.join(ids, ",") + ")");
		queryOrgSql.append(" ORDER BY user_id, create_time desc ");
		attendanceList = findForJdbc(queryOrgSql.toString());
	
		//构建快递员->是否打卡的映射
		Map<Integer, Boolean> courierIdAttaStatMap = new HashMap<Integer, Boolean>();
		//用户保存上一次迭代在快递员ID
		Integer preCoureierId = null;
		for(Map<String, Object> attCourier : attendanceList){
			
			Integer cid = Integer.parseInt(attCourier.get("user_id").toString());
			Integer type = Integer.parseInt(attCourier.get("type").toString());
			
			//判断这条打卡记录是不是当前迭代在快递员最近在打卡记录
			if(preCoureierId == null || !cid.equals(preCoureierId)){
				boolean isOnduty = (type == 0);
				courierIdAttaStatMap.put(cid, isOnduty);
			}
			
			preCoureierId = cid;
		}
		
		//统计所有快递员的打卡记录
		for(Map<String, Object> courier: courierList){
			Integer id = Integer.parseInt(courier.get("id").toString());
			//判断courier是否打卡
			if(courierIdAttaStatMap.get(id) != null && courierIdAttaStatMap.get(id).equals(Boolean.TRUE)){
				sum++;
			}
		}
		return sum;
	}
}


