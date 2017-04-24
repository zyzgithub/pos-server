package com.courier_mana.management.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.management.service.CourierAttendanceManaService;

@Service
public class CourierAttendanceManaServiceImpl extends CommonServiceImpl implements CourierAttendanceManaService {
	private final static Logger logger = LoggerFactory.getLogger(CourierAttendanceManaServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> courierAttendance(SearchVo vo, Integer courierId) {
		logger.info("Invoke method: courierAttendance， params: courierId - {}, vo - {}", courierId, vo);

		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 根据搜索条件(时间)调整SQL条件
		 */
		String period = "";	//查询时间
		{
			String timeType = vo.getTimeType();
			if(timeType == null || timeType.equals("day")){
				period = " AND ca.create_time >= UNIX_TIMESTAMP(CURDATE()) ";			//按天搜索的条件
			}else if(timeType.equals("week")){
				period = " AND ca.create_time >= UNIX_TIMESTAMP(CURDATE())-6*86400 ";	//近7天搜索的条件
			}else if(timeType.equals("month")){
				period = " AND ca.create_time >= UNIX_TIMESTAMP(CURDATE())-29*86400 ";	//近30天搜索的条件
			}
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ca.user_id courierId, u.username courierName, ca.type, ca.create_time time ");
		sql.append(" FROM 0085_courier_attendance ca, `user` u ");
		sql.append(" WHERE u.id = ca.user_id  ");
		sql.append(period);
		sql.append(" 	AND ca.user_id IN ( ");
		/**
		 * 根据搜索条件(地区)调整SQL条件
		 */
		List<Integer> courierIds = null;
		if(vo.getOrgId() != null){
			courierIds = this.courierOrgService.getManageCouriersId(courierId, vo.getOrgId());
		}else{
			courierIds = this.courierOrgService.getManageCouriersId(courierId);
		}
		sql.append(StringUtils.join(courierIds, ","));
		sql.append(" ) ");
		sql.append(" ORDER BY ca.user_id, ca.create_time DESC ");
		
		logger.debug("Inside method: courierAttendance, SQL: {}", sql.toString());
		/**
		 * 获取原始的考勤信息
		 */
		List<Map<String, Object>> attendanceList = findForJdbc(sql.toString());
		
		/**
		 * 返回经过整理后的考勤信息
		 */
		return this.getAttendanceResultList(attendanceList);
	}

	/**(OvO)
	 * 将原始的考勤信息组装成其他结构
	 * @param attendanceList	原始的考勤信息
	 * @return	快递员的考勤信息
	 */
	private List<Map<String, Object>> getAttendanceResultList(List<Map<String, Object>> attendanceList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	//用于返回结果的List
		Integer preCourierId = null;				//标志位, 上一个快递员的ID
		Map<String, Object> courierData = null;		//快递员考勤信息Map
		List<String> courierCheckInList = null;		//快递员上班打卡时间List
		List<String> courierCheckOutList = null;	//快递员下班打卡时间List
		Integer reCheckInCount = 0;					//标志位, 统计连续上班次数(用于调整两个打卡List的位置关系)
		Integer reCheckOutCount = 0;				//标志位, 统计连续下班次数(用于调整两个打卡List的位置关系)
		
		/**
		 * 遍历原始考勤信息并且组装数据
		 */
		for(Map<String, Object> record: attendanceList){
			Integer courierId = Integer.parseInt(record.get("courierId").toString());
			/**
			 * 对比记录中的快递员ID是否和标志位(precourierId)相同
			 * · 和标志位不同就在resultList中添加一个item(一个快递员的考勤信息)
			 * item里面包括courierName, courierCheckInList, courierCheckOutList
			 * 之后再向两个打卡记录List中添加数据
			 * · 如果和标志位相同则跳过添加item这一步骤
			 * 直接在两个打卡记录List中添加数据
			 */
			if(!courierId.equals(preCourierId)){
				/**
				 * 为快递员新建item
				 */
				courierCheckInList = new ArrayList<String>();
				courierCheckOutList = new ArrayList<String>();
				courierData = new HashMap<String, Object>();
				courierData.put("checkIn", courierCheckInList);
				courierData.put("checkOut", courierCheckOutList);
				courierData.put("courierName", record.get("courierName"));
				resultList.add(courierData);
				/**
				 * 怎么可以忘记更新标志位呢?
				 */
				preCourierId = courierId;
				reCheckInCount = 0;
				reCheckOutCount = 0;
			}
			/**
			 * 判断此记录是上班记录还是下班记录
			 * 按照实际情况想两个打卡List添加数据
			 */
			Integer recordType = Integer.parseInt(record.get("type").toString());
			if(recordType.equals(0)){			//上班记录
				for(int i=1;i < reCheckOutCount; i++){	//调整记录位置
					courierCheckInList.add("");
				}
				courierCheckInList.add(this.getTimeStampStr(Integer.parseInt(record.get("time").toString())));
				reCheckInCount ++;				//更新标志位
				reCheckOutCount = 0;			//更新标志位
			}else if(recordType.equals(1)){		//下班记录
				for(int i=1;i <= reCheckInCount; i++){	//调整记录位置
					courierCheckOutList.add("");
				}
				courierCheckOutList.add(this.getTimeStampStr(Integer.parseInt(record.get("time").toString())));
				reCheckOutCount ++;				//更新标志位
				reCheckInCount = -1;			//更新标志位
			}else{								//以防万一
				courierCheckInList.add("");
				courierCheckOutList.add("");
			}
		}
		
		return resultList;
	}
	
	/**(OvO)
	 * 获得指定时间的时间戳字符串
	 * @param second 指定时间(秒数)
	 * @return	返回时间戳字符串
	 */
	private String getTimeStampStr (Integer second){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(second * 1000l));
	}
}
