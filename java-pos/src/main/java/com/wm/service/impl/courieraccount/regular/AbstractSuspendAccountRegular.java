package com.wm.service.impl.courieraccount.regular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wm.entity.courieraccount.CourierAccountSuspendEntity;
import com.wm.entity.position.PositionEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.position.PositionServiceI;


/**
 * 封号规则基类
 *
 */
public abstract class AbstractSuspendAccountRegular {
	protected CourierSalaryServiceI courierSalaryService;
	protected CourierAccountSuspendServiceI courierAccountSuspendService;
	protected AttendanceServiceI attendanceService;
	protected PositionServiceI positionService;
	
	/**
	 * 找出满足规则被封的账号
	 * @return
	 */
	public abstract List<SuspendAccount> findSuspendAccountsByRegular();
	
	/**
	 * 过滤已经被封的账号
	 * @param suspendAccounts
	 */
	protected void filterAlreadySuspendAccounts(List<Integer> accounts){
		//获取所有已经被封号的账号
		Map<Integer, CourierAccountSuspendEntity> alreadySuspendAccounts = courierAccountSuspendService.getAllSuspendAccounts();
		
		if(CollectionUtils.isNotEmpty(accounts)){
			Iterator<Integer> iter = accounts.iterator();
			while (iter.hasNext()) {
				Integer account = iter.next();
				//改账号已经被封
				if(alreadySuspendAccounts.get(account) != null){
					iter.remove();
				}
			}
		}
	}
	
	protected List<Integer> initCourierIds(Map<String, List<Integer>> positionAndTypeMap){
		
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = courierAccountSuspendService.getCourierIdsOfAccountSuspend(positionAndTypeMap);
		
		filterAlreadySuspendAccounts(courirerIds);
		return courirerIds;
	}
	
	
	protected void filterAfter(List<SuspendAccount> suspendAccounts){
	}
	
	public void setCourierSalaryService(CourierSalaryServiceI courierSalaryService) {
		this.courierSalaryService = courierSalaryService;
	}

	public void setCourierAccountSuspendService(
			CourierAccountSuspendServiceI courierAccountSuspendService) {
		this.courierAccountSuspendService = courierAccountSuspendService;
	}

	public void setAttendanceService(AttendanceServiceI attendanceService) {
		this.attendanceService = attendanceService;
	}
	
	public void setPositionService(PositionServiceI positionService){
		this.positionService = positionService;
	}

	public Map<String, List<Integer>> addPositionIdTiList(Map<String, String> map){
		Map<String, List<Integer>> positionAndTypeMap = new HashMap<String, List<Integer>>();
		List<Integer> positionIds = new ArrayList<Integer>();
		List<Integer> courierTypeList = new ArrayList<Integer>();
		try {
			//获取快递员职务
			String idString = map.get("idString");
			if(!idString.equals("0")){
				String[] ids = idString.split(",");
				for(int i=0; i<ids.length; i++){
					Integer id = Integer.parseInt(ids[i]);
					PositionEntity positionEntity = positionService.getPositionEntity(id);
					if(positionEntity != null){
						positionIds.add(id);
					}
				}
			}
			//获取快递员类型
			String courierTypeString = map.get("courierType");
			if(!courierTypeString.equals("0")){
				String[] types = courierTypeString.split(",");
				for(String str : types){
					courierTypeList.add(Integer.parseInt(str));
				}
			}
			
			positionAndTypeMap.put("positionIds", positionIds);
			positionAndTypeMap.put("courierTypeList", courierTypeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return positionAndTypeMap;
	}
	
	/**
	 * 被封账号
	 *
	 */
	public class SuspendAccount{
		private Integer courierId;
		private String suspendReason;
		
		public SuspendAccount(){
		}
		
		public SuspendAccount (Integer courierId, String suspendReason) {
			this.courierId = courierId;
			this.suspendReason = suspendReason;
		}
		public Integer getCourierId() {
			return courierId;
		}
		public void setCourierId(Integer courierId) {
			this.courierId = courierId;
		}
		public String getSuspendReason() {
			return suspendReason;
		}
		public void setSuspendReason(String suspendReason) {
			this.suspendReason = suspendReason;
		}
		
		
		
		@Override
		public boolean equals(Object obj){
			if(this == obj){
				return true;
			}
			
			if(!(obj instanceof SuspendAccount)){
				return false;
			}
			
			SuspendAccount o = (SuspendAccount)obj;
			if(courierId == null && o.courierId != null){
				return false;
			}
			if(courierId != null && o.courierId == null){
				return false;
			}
			return courierId.equals(o.courierId) && StringUtils.equals(suspendReason, o.suspendReason);
		}
		
		
		@Override
		public String toString(){
			return "{courierId:" + courierId + ", suspendReason:"+suspendReason+"}";
		}
		
		@Override
		public int hashCode(){
			return courierId.hashCode() + suspendReason.hashCode();
		}
	}
}
