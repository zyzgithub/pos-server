package com.wm.service.impl.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.org.OrgEntity;
import com.wm.entity.statistics.OrgStatisticsDaylyEntity;
import com.wm.service.statistics.OrgStatisticsDaylyServiceI;

@Service("orgStatisticsDaylyService")
@Transactional
public class OrgStatisticsDaylyServiceImpl extends CommonServiceImpl implements OrgStatisticsDaylyServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(OrgStatisticsDaylyServiceImpl.class);

	@Override
	public void statisticsDayly(OrgEntity org) {
		DateTime deductDate = DateTime.now().minusDays(1);
		String sql1 = "select courier_id from 0085_courier_org where org_id=?";
		String sql2 = "select orders from 0085_courier_deduct_log where deduct_type=0 and courier_id=? and date(from_unixtime(account_date))=?";
		Integer orgId = org.getId();
		Double daylyTotalOrder = 0.0;
		Integer daylyTotalCourier = 0;
		List<Map<String, Object>> courierList = this.findForJdbc(sql1, new Object[]{orgId});
		if(courierList != null && courierList.size() > 0){
			for(Map<String, Object> courierMap : courierList){
				Integer courierId = Integer.parseInt(courierMap.get("courier_id").toString());
				List<Map<String, Object>> deductList = this.findForJdbc(sql2, new Object[]{courierId, deductDate.toString("yyyy-MM-dd")});
				if(deductList != null && deductList.size() > 0){
					for(Map<String, Object> deductMap : deductList){
						Double orders = Double.parseDouble(deductMap.get("orders").toString());
						if(orders > 0){
							daylyTotalOrder += orders;
							daylyTotalCourier ++;
						} else {
							logger.warn("该快递员【" + courierId + "】昨天的订单数为0!");
						}
					}
				} else {
					logger.warn("未找到该快递员【" + courierId + "】昨天的提成记录!");
				}
			}
		} else {
			logger.warn("该网点【" + orgId + "】未绑定任何快递员!");
		}
		Double daylyOrder = 0d;
		if(daylyTotalCourier > 0){
			daylyOrder = daylyTotalOrder/daylyTotalCourier;
		}
		OrgStatisticsDaylyEntity statistics = new OrgStatisticsDaylyEntity(orgId, daylyTotalOrder.intValue(), daylyTotalCourier, daylyOrder);
		this.save(statistics);
	}

	@Override
	public Map<String, Object> getYestodaySta(Integer orgId, String deductDay) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select dayly_total_order,dayly_total_courier,dayly_order from 0085_org_statistics_dayly ";
		sql += " where org_id=? and date(from_unixtime(update_date))=? ";
		list = this.findForJdbc(sql, new Object[]{orgId, deductDay});
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
}