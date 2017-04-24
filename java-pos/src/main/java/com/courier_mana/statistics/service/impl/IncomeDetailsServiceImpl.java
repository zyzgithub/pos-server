package com.courier_mana.statistics.service.impl;

import java.util.ArrayList;
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
import com.courier_mana.statistics.service.IncomeDetailsService;
import com.wm.util.SqlUtils;

@Service
public class IncomeDetailsServiceImpl extends CommonServiceImpl implements IncomeDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(IncomeDetailsServiceImpl.class);
	
	@Autowired
	CourierOrgServicI courierOrgService;
	
	@Override
	public Map<String, Object> getIncomeDetails(String orgIdsStr, SearchVo vo, boolean isConfirm) {
		logger.info("调用方法 getIncomeDetails(orgIdsStr: {}, vo: {}, isConfirm: {}) 获取收入金额.", orgIdsStr, vo, isConfirm);
		
		/**
		 * 根据入参调整SQL条件
		 */
		String isConfirmStr = null;
		if(isConfirm){
			isConfirmStr = " AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ";
		}else{
			isConfirmStr = " AND o.pay_state = 'unpay' ";
		}
		
		String whereTime = SqlUtils.getTimeWhere4SQL(vo, "o.create_time");
		
		/**
		 * 获取区域下的商家ID
		 */
		List<Integer> merchantIds = this.getMerchantIds(orgIdsStr);
		if(merchantIds.size()<=0)merchantIds.add(-1);
		logger.debug("获得商家ID: {}", merchantIds);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(SUM(d.takeOutDeduction),0) takeOutDeduction, IFNULL(SUM(d.dineInDeduction),0) dineInDeduction ");
		sql.append(" 	,IFNULL(SUM(d.deliveryFee),0) deliveryFee, IFNULL(SUM(d.boxFee),0) boxFee ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT SUM(CASE WHEN o.sale_type=1 AND o.order_type='normal' THEN o.origin ELSE 0 END) * m.deduction takeOutDeduction ");
		sql.append(" 		,SUM(CASE WHEN o.sale_type=2 AND o.order_type='normal' THEN o.origin ELSE 0 END) * m.deduction dineInDeduction ");
		sql.append(" 		,SUM(o.delivery_fee) deliveryFee ");
		sql.append(" 		,SUM(o.cost_lunch_box) boxFee ");
		sql.append(" 	FROM `order` o, merchant m ");
		sql.append(" 	WHERE 1 = 1 "+ whereTime +" ");
		sql.append(" 		AND o.merchant_id IN ( ");
		sql.append(StringUtils.join(merchantIds, ","));
		sql.append(" 		) ");
		sql.append(isConfirmStr);
		sql.append(" 		AND o.merchant_id = m.id ");
		sql.append(" 	GROUP BY o.merchant_id ");
		sql.append(" )d ");
		logger.debug("SQL: {}", sql.toString());
		
		return this.findOneForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 根据区域ID获取商家ID
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @return	商家ID List
	 */
	private List<Integer> getMerchantIds(String orgIdsStr){
		logger.debug("调用方法 getMerchantIds(orgIds: {}) 扫码首单总金额.", orgIdsStr);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT morg.merchant_id ");
		sql.append(" FROM  0085_merchant_org morg ");
		sql.append(" WHERE morg.org_id IN ( ");
		sql.append(orgIdsStr);
		sql.append(" ) ");
		List<Integer> merchantId = new ArrayList<Integer>();
		for(Map<String, Object> map: this.findForJdbc(sql.toString())){
			merchantId.add(Integer.valueOf(map.get("merchant_id").toString()));
		}
		return merchantId;
	}

}
