package com.wm.service.impl.courieraccount.regular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.wm.entity.courieraccount.CourierAccountSuspendEntity;

/**
 * 
 * 根据抢单封号规则
 *
 */
public class SuspendAccountByScramble extends AbstractSuspendAccountRegular {
	
	private String supendReason = "未抢单";
	
	@Override
	public List<SuspendAccount> findSuspendAccountsByRegular() {
		DateTime now = DateTime.now();
//		DateTime now = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2016-02-24");
		List<SuspendAccount> result = new ArrayList<SuspendAccount>();
		
		//获取抢单的约束规则
		Map<String, String> scambleMap = courierAccountSuspendService.getScrambleConstraint();
		if(scambleMap != null){
			Map<String, List<Integer>> positionAndTypeMap = addPositionIdTiList(scambleMap);
			
			//获取所有待计算的快递员列表
			List<Integer> courirerIds = initCourierIds(positionAndTypeMap);
			
			int scambleConstraint = Integer.parseInt(scambleMap.get("value").toString());
			
			//如果抢单的约束规则生效
			if(scambleConstraint > 0 && positionAndTypeMap.get("positionIds").size() > 0){
				String scambleStartDate = now.minusDays(scambleConstraint).toString("yyyy-MM-dd");
				String scambleEndDate = now.toString("yyyy-MM-dd");
						
				if(CollectionUtils.isNotEmpty(courirerIds)){
					for(Integer courierId: courirerIds){
						List<Map<String, Object>> list = courierAccountSuspendService.getScrambleLogs(courierId, scambleStartDate, scambleEndDate);
						
						if(list.size() == 0){
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
	public void filterAfter(List<SuspendAccount> suspendAccounts){
		
		DateTime now = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
//		DateTime now = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2016-01-24");
		int scrambleConstraint = Integer.parseInt(courierAccountSuspendService.getScrambleConstraint().get("value").toString());
		Map<Integer, CourierAccountSuspendEntity> unlockedAccounts = courierAccountSuspendService.getUnlockAccounts(now.minusDays(scrambleConstraint).toDate(), now.toDate());
		
		Iterator<SuspendAccount> iterator = suspendAccounts.iterator();
		while (iterator.hasNext()) {
			SuspendAccount suspendAccount = iterator.next();
			if(unlockedAccounts.get(suspendAccount.getCourierId()) != null){
				iterator.remove();
			}
			
		}
	}
}
