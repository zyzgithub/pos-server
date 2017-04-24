package com.wm.service.impl.courieraccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.Constants;
import com.wm.entity.courieraccount.CourierAccountSuspendEntity;
import com.wm.entity.position.PositionEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;

@Service("courierAccountSuspendService")
public class CourierAccountSuspendServiceImpl extends CommonServiceImpl
		implements CourierAccountSuspendServiceI {
	private final static String ENABLE = "1";
	
	@Autowired
	private SystemconfigServiceI systemconfigService;
	
	@Autowired
	private WUserServiceI wUserService;
	
//	@SuppressWarnings("unchecked")
	private  Map<String, String> getSuspendAccountRegularSet(String code) {
//		String key = CacheKeyUtil.getSuspendConstraintKey(code);
//		Object obj = AliOcs.getObject(key);
//		if(obj != null){
//			return (Map<String, String>)obj;
//		}
		
		Map<String, String> constraintSetMap = new HashMap<String, String>();
		String constraintSet = systemconfigService.getValByCode(code);
		if(constraintSet != null){
			String value = constraintSet.toString().split("-")[0];
			String enableFlag = constraintSet.toString().split("-")[1];
			String idString = constraintSet.toString().split("-")[2];
			String courierType = constraintSet.toString().split("-")[3];
			
			constraintSetMap.put("enableFlag", enableFlag);
			constraintSetMap.put("value", value);
			constraintSetMap.put("idString", idString);
			constraintSetMap.put("courierType", courierType);
		}
//		AliOcs.set(key, constraintSetMap, 24*60*60);
		return constraintSetMap;
	}

	private boolean isEnable(String regularSet){
		boolean enable = false;
		Map<String, String> constraintSetMap = getSuspendAccountRegularSet(regularSet);
		if(constraintSetMap != null && constraintSetMap.get("enableFlag") != null){
			enable = StringUtils.equals(ENABLE, constraintSetMap.get("enableFlag"));
		}
		return enable;
	}
	
	@Override
	public boolean isPunchConstraintEnable(){
		return isEnable(Constants.PUNCH_CONSTRAINT_SET);
	}
	
	@Override
	public boolean isScrambleConstraintEnable(){
		return isEnable(Constants.SCAMBLE_CONSTRAINT_SET);
	}
	
	@Override
	public boolean isOpenupConstraintEnable(){
		return isEnable(Constants.OPENUP_CONSTRAINT_SET);
	}
	
	@Override
	public Map<String, String> getPunchConstraint(){
		return getSuspendAccountRegularSet(Constants.PUNCH_CONSTRAINT_SET);
		
	}
	
	@Override
	public Map<String, String> getScrambleConstraint(){
		return getSuspendAccountRegularSet(Constants.SCAMBLE_CONSTRAINT_SET);
		 
	}
	
	@Override
	public Map<String, String> getOpenupConstraint(){
		return getSuspendAccountRegularSet(Constants.OPENUP_CONSTRAINT_SET);
		 
	}
	
	@Override
	public void save(Integer courierId, Date suspendTime, String suspendReason){
		CourierAccountSuspendEntity entity = new CourierAccountSuspendEntity();
		entity.setCourierId(courierId);
		entity.setSuspendTime(suspendTime);
		entity.setSuspendReason(suspendReason);
		entity.setCurrentState(1);
		
		this.save(entity);
	}
	
	@Override
	public void suspendAccount(Integer courierId, String suspendReason){
		save(courierId, new Date(), suspendReason);
		//修改用户的状态
		WUserEntity userEntity = get(WUserEntity.class, courierId);
		userEntity.setUserState(Constants.LOCKED);
		wUserService.save(userEntity);
	}
	
	@Override
	public Map<Integer, CourierAccountSuspendEntity> getAllSuspendAccounts() {
		Map<Integer, CourierAccountSuspendEntity> accounts = new HashMap<Integer, CourierAccountSuspendEntity>();
		List<CourierAccountSuspendEntity> list = findByProperty(CourierAccountSuspendEntity.class, "currentState", 1);
		if(CollectionUtils.isNotEmpty(list)){
			for(CourierAccountSuspendEntity entity: list){
				accounts.put(entity.getCourierId(), entity);
			}
		}
		return accounts;
	}

	@Override
	public Map<Integer, CourierAccountSuspendEntity> getUnlockAccounts(Date startDate, Date endDate) {
		Map<Integer, CourierAccountSuspendEntity> accounts = new HashMap<Integer, CourierAccountSuspendEntity>();
		CriteriaQuery cq = new CriteriaQuery(CourierAccountSuspendEntity.class);
		cq.eq("currentState", 0);
		if(startDate != null){
			cq.ge("unlockTime", startDate);
		}
		
		if(endDate != null){
			cq.lt("unlockTime", endDate);
		}
		cq.add();
		
		List<CourierAccountSuspendEntity> list = this.getListByCriteriaQuery(cq, false);
		if(CollectionUtils.isNotEmpty(list)){
			for(CourierAccountSuspendEntity entity: list){
				accounts.put(entity.getCourierId(), entity);
			}
		}
		return accounts;
	}
	
	@Override
	public List<Map<String, Object>> getScrambleLogs(Integer courierId, String startDate, String endDate){
		StringBuilder sql = new StringBuilder("select id, courier_id courierId, order_id orderId, success, msg, from_unixtime(create_time) createTime");
		sql.append(" from 0085_courier_scramble_log ");
		sql.append(" where courier_id = ?");
		sql.append(" and date(from_unixtime(create_time)) >= ? ");
		sql.append(" and date(from_unixtime(create_time)) < ? ");
		
		return this.findForJdbc(sql.toString(), courierId, startDate, endDate);
	}
	
	@Override
	public List<Map<String, Object>> getApprovalOpenupRecords(Integer courierId, String startDate, String endDate){
		StringBuilder sql = new StringBuilder("select id, courier_id courierId, merchant_name merchantName, merchant_source merchantSource, from_unixtime(create_time) createTime");
		sql.append(" from 0085_merchant_apply ");
		sql.append(" where state = 2" );
		sql.append(" and courier_id = ?");
		sql.append(" and date(create_time) >= ? ");
		sql.append(" and date(create_time) < ? ");
		
		return this.findForJdbc(sql.toString(), courierId, startDate, endDate);
	}

	@Override
	public List<Integer> getCourierIdsOfAccountSuspend(
			Map<String, List<Integer>> positionAndTypeMap) {
		List<Integer> courierIds = new ArrayList<Integer>();
		if(CollectionUtils.isNotEmpty(positionAndTypeMap.get("positionIds")) && CollectionUtils.isNotEmpty(positionAndTypeMap.get("courierTypeList"))){
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT u.id from " );
			sql.append(" (SELECT id ,IFNULL(user_state, 1) user_state FROM `user` WHERE user_type = 'courier') u ");
			sql.append(" LEFT JOIN 0085_courier_position cp on  u.id=cp.courier_id" );
			sql.append(" LEFT JOIN 0085_position p on cp.position_id = p.id" );
			sql.append(" LEFT JOIN 0085_courier_info ci on ci.courier_id = u.id" );
			sql.append(" WHERE  u.user_state = 1 ");
			if(CollectionUtils.isNotEmpty(positionAndTypeMap.get("positionIds"))){
				sql.append("and p.id in ( " + StringUtils.join(positionAndTypeMap.get("positionIds"), ",") + ")" );
			}
			if(CollectionUtils.isNotEmpty(positionAndTypeMap.get("courierTypeList"))){
				sql.append(" and  ci.courier_type in (" + StringUtils.join(positionAndTypeMap.get("courierTypeList"), ",") + ")" );
			}
			
					   
			List<Map<String, Object>> list = this.findForJdbc(sql.toString());
			
			if(CollectionUtils.isNotEmpty(list)){
				for(Map<String, Object> map: list){
					courierIds.add(Integer.valueOf(map.get("id").toString()));
				}
			}
			
			return courierIds;
		}
		else{
			return courierIds;
		}
	}

}
