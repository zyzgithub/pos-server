package com.wm.service.impl.courieraccount.regular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.courier_mana.common.Constants;
import com.wm.entity.courieraccount.CourierAccountSuspendEntity;

/**
 * 
 * 根据打卡记录封号规则
 *
 */
public class SuspendAccountByPunch extends AbstractSuspendAccountRegular {
	private String supendReason = "未打卡";
	
	@Override
	public List<SuspendAccount> findSuspendAccountsByRegular() {
		List<SuspendAccount> result = new ArrayList<SuspendAccount>();
		
		DateTime now = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
//		DateTime now = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2016-01-16");
		
		
		//获取打卡的约束规则
		Map<String, String> punchMap = courierAccountSuspendService.getPunchConstraint();
		if(punchMap != null){
			Map<String, List<Integer>> positionAndTypeMap = addPositionIdTiList(punchMap);
			
			//获取所有待计算的快递员列表
			List<Integer> courirerIds = initCourierIds(positionAndTypeMap);
			
			int punchConstraint = Integer.parseInt(punchMap.get("value").toString());
			
			if(punchConstraint > 0 && positionAndTypeMap.get("positionIds").size() > 0){
				if(CollectionUtils.isNotEmpty(courirerIds)){
					String attendanceStartDate = now.minusDays(punchConstraint).toString("yyyy-MM-dd");
					String attendanceEndDate = now.toString("yyyy-MM-dd");
							
					for(Integer courierId: courirerIds){
						//获取每个快递员的考勤记录
						List<Map<String, Object>> ondutyRecords 
								= attendanceService.getAttendceRecords(courierId, attendanceStartDate, attendanceEndDate, Constants.ONDUTY);
							
						if(ondutyRecords.size() == 0){
							//封号
							SuspendAccount suspendAccount = new SuspendAccount(courierId, supendReason);
							result.add(suspendAccount);
						}
					}
				}
				
				filterAfter(result);
			}
		}
		return result;
	}
	
	@Override
	public void filterAfter(List<SuspendAccount> suspendAccounts){;
		
		DateTime now = DateTime.now();
//		DateTime now = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2016-01-16");
		int punchConstraint = Integer.parseInt(courierAccountSuspendService.getPunchConstraint().get("value").toString());
		Map<Integer, CourierAccountSuspendEntity> unlockedAccounts = courierAccountSuspendService.getUnlockAccounts(now.minusDays(punchConstraint).toDate(), now.toDate());
		
		Iterator<SuspendAccount> iterator = suspendAccounts.iterator();
		while (iterator.hasNext()) {
			SuspendAccount suspendAccount = iterator.next();
			if(unlockedAccounts.get(suspendAccount.getCourierId()) != null){
				iterator.remove();
			}
			
		}
	}
}
