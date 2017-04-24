package com.wm.service.impl.deduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wm.entity.courierinfo.CourierInfoEntity;
import com.wm.entity.deduct.CourierScanPromotionHistoryEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.service.deduct.CourierRechargePromotionServiceI;

@Service("CourierRechargePromotionService")
public class CourierRechargePromotionServiceImpl extends CommonServiceImpl implements CourierRechargePromotionServiceI {

	private final static Logger logger = LoggerFactory.getLogger(CourierRechargePromotionServiceImpl.class);
	
	@Override
	public Double getRechargePromotionDeduct(Integer courierId, double rechargeMoney, Integer userId) {
		Map<String, Object> map = getDeductAndScanRuleId(courierId, rechargeMoney);
		Double deduct = 0.0;
		//没有找到对应的梯度id
		int scanRuleId = getCourierScanRuleIdByMoney(courierId, rechargeMoney);
		if(scanRuleId == 0){
			logger.warn("无法找到充值金额对应奖励规则，参数courerId:{}, rechargeMoney:{}", courierId, rechargeMoney);
			return deduct;
		}
		//该用户没有获得过  相应scanRuleId梯度对应的奖励
		if(isReward(userId)){
			if(map != null){
				deduct = Double.parseDouble(map.get("deduct").toString());
			}
		}
		else {
			logger.info("本次充值没有提成，userId:{}, courierId:{}, rechargeMoney:{}", new Object[]{userId, courierId, rechargeMoney});
		}
		return deduct;
	}
	
	/**
	 * 保存充值是否获得奖励记录
	 */
	@Override
	public void saveCourierScanPromotion(OrderEntity order) {
		CourierScanPromotionHistoryEntity courierScanPromotionHistory = new CourierScanPromotionHistoryEntity();
		courierScanPromotionHistory.setCourierId(order.getInviteId());
		courierScanPromotionHistory.setMoney((int)(order.getOrigin()*100));
		int scanRuleId = getCourierScanRuleIdByMoney(order.getInviteId(), order.getOrigin());
		if(scanRuleId != 0 && isReward(order.getWuser().getId())){
			courierScanPromotionHistory.setReward("1");
		}
		else {
			courierScanPromotionHistory.setReward("0");
		}
		if(scanRuleId != 0){
			courierScanPromotionHistory.setScanRuleId(scanRuleId);
		}
		courierScanPromotionHistory.setUserId(order.getWuser().getId());
		save(courierScanPromotionHistory);
	}

	/**
	 * 获取同一个用户充值金额 快递员有没有获得过提成奖励  true表示没有  false表示已获得过该奖励
	 */
	@Override
	public boolean isReward(Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append(" select id from 0085_courier_scan_promotion_history");
		query.append(" where user_id = ? ");
		List<Map<String, Object>> list = this.findForJdbc(query.toString(), userId);
		if(CollectionUtils.isEmpty(list)){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 根据充值金额获取  规则梯度id
	 */
	@Override
	public int getCourierScanRuleIdByMoney(Integer courierId, double rechargeMoney) {
		Map<String, Object> map = getDeductAndScanRuleId(courierId, rechargeMoney);
		if(map != null && map.get("scanRuleId") != null){
			return Integer.parseInt(map.get("scanRuleId").toString());
		}
		else {
			return 0;
		}
	}
	
	private Map<String, Object>getDeductAndScanRuleId(Integer courierId, double rechargeMoney){
		Map<String, Object> map = new HashMap<String, Object>();
		double deduct = 0.0;
		try {
			CourierInfoEntity courierInfo = findUniqueByProperty(CourierInfoEntity.class, "courierId", courierId);
			if(courierInfo == null){
				logger.error("无法根据快递员ID:" + courierId + "找到其对应的快递员类型");
			}
			else {
				int courierType = courierInfo.getCourierType();
				
				StringBuilder sql = new StringBuilder("SELECT r.id scanRuleId, r.total_charge totalCharge, d.deduct deduct");
				sql.append(" from 0085_courier_scan_rule r, 0085_courier_scan_deduct d");
				sql.append(" where d.scan_rule_id=r.id and r.invalid=1");
				sql.append(" and d.courier_type=? order by totalCharge desc");
				
				List<Map<String, Object>> deductRules = this.findForJdbc(sql.toString(), courierType);
				
				if(CollectionUtils.isEmpty(deductRules)){
					logger.warn("找不到courierId:{}, 类型:{}对应的提成规则", courierId, courierType);
					map.put("deduct", deduct);
					return map;
					
				}
				else {
					double preDeduct = 0.0;
					Integer scanRuleId = 0;
					for(Map<String, Object> rule: deductRules){
						double totalCharge = Double.parseDouble(rule.get("totalCharge").toString());
						preDeduct = Double.parseDouble(rule.get("deduct").toString());
						scanRuleId = Integer.parseInt(rule.get("scanRuleId").toString());
						if(rechargeMoney >= totalCharge){
							break;
						}
					}
					 deduct = preDeduct;
					 map.put("deduct", deduct);
					 map.put("scanRuleId", scanRuleId);
					 return map;
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
