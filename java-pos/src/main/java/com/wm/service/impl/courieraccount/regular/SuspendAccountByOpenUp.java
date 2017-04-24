package com.wm.service.impl.courieraccount.regular;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;


public class SuspendAccountByOpenUp extends AbstractSuspendAccountRegular {

	private String supendReason = "没有开拓合格的商家";
	
	@Override
	public List<SuspendAccount> findSuspendAccountsByRegular() {
		DateTime now = DateTime.now();
		List<SuspendAccount> result = new ArrayList<SuspendAccount>();
		
		//获取开拓商家的约束规则
		Map<String, String> openupMap = courierAccountSuspendService.getOpenupConstraint();
		if(openupMap != null){
			Map<String, List<Integer>> positionAndTypeMap = addPositionIdTiList(openupMap);
			
			//获取所有待计算的快递员列表
			List<Integer> courirerIds = initCourierIds(positionAndTypeMap);
			
			int openupConstraint = Integer.parseInt(openupMap.get("value").toString());
			
			//如果开拓商家的约束规则生效
			if(openupConstraint > 0 && positionAndTypeMap.get("positionIds").size() > 0){
				String openupStartDate = now.minusDays(openupConstraint).toString("yyyy-MM-dd");
				String openupEndDate = now.toString("yyyy-MM-dd");
				
				if(CollectionUtils.isNotEmpty(courirerIds)){
					for(Integer courierId: courirerIds){
						List<Map<String, Object>> list = courierAccountSuspendService.getApprovalOpenupRecords(courierId, openupStartDate, openupEndDate);
						
						if(list.size() == 0){
							//封号
							SuspendAccount suspendAccount = new SuspendAccount(courierId, supendReason);
							result.add(suspendAccount);
						}
					}
				}
			}
		}
		return result;
	}

}
