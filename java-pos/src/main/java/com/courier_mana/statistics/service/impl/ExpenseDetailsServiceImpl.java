package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.courier_mana.statistics.service.ExpenseDetailsService;
import com.wm.util.SqlUtils;

import jodd.util.StringUtil;

@Service
public class ExpenseDetailsServiceImpl extends CommonServiceImpl implements ExpenseDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(ExpenseDetailsServiceImpl.class);
	
	@Autowired
	CourierOrgServicI courierOrgService;
	
	@Override
	public Map<String, Object> wageAndDeductExpense(Integer userId, SearchVo vo) {
		logger.info("调用方法 wageAndDeductExpense(userId: {}, vo: {}) 获取员工支出.", userId, vo);
		/**
		 * 获取用户管理的区域ID
		 */
		List<Integer> orgIds = null;
		Integer orgId = vo.getOrgId();
		orgIds = this.courierOrgService.getManageOrgIds(userId, orgId);
		String orgIdsStr = StringUtils.join(orgIds, ",");
		
		List<Integer> courierIds = courierOrgService.getCourierIdsByOrgId(orgIdsStr);
		String courierIdsStr = StringUtils.join(courierIds, ",");
		return this.wageAndDeductExpense(orgIdsStr, courierIdsStr, vo);
	}

	@Override
	public Map<String, Object> wageAndDeductExpense(String orgIdsStr, String courierIdsStr, SearchVo vo) {
		Map<String, Object> wagesMap = this.getWagesMap(orgIdsStr, vo);
		Map<String, Object> deductsMap = this.getDeductMap(courierIdsStr, vo);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(deductsMap);
		result.putAll(wagesMap);
		return result;
	}

	@Override
	public Map<String, Object> marketingExpense(Integer userId, SearchVo vo, boolean isConfirm){
		logger.info("调用方法 marketingExpense(userId: {}, vo: {}, isConfirm: {}) 获取营销支出.", userId, vo, isConfirm);
		/**
		 * 获取用户管理的区域ID
		 */
		List<Integer> orgIds = null;
		Integer orgId = vo.getOrgId();
		if(orgId == null){
			orgIds = this.courierOrgService.getManageOrgIds(userId);
		}else{
			orgIds = this.courierOrgService.getManageOrgIds(userId, orgId);
		}
		
		String orgIdsStr = StringUtil.join(orgIds, ",");
		return this.marketingExpense(orgIdsStr, vo, isConfirm);
	}
	
	@Override
	public Map<String, Object> marketingExpense(String orgIdsStr, SearchVo vo, boolean isConfirm){
		/**
		 * 先获取优惠券支出, 会员卡支出, 随机立减支出
		 */
		Map<String, Object> result = this.getTotalDiscountSubsidy(orgIdsStr, isConfirm, vo);
		/**
		 * 再获取扫码商家返点支出
		 */
		result.put("scanRebate", this.getTotalMerchantRebate(orgIdsStr, vo));
		return result;
	}
	
	/**(OvO)
	 * 获取所有优惠补贴(优惠券支出, 会员卡支出, 随机立减支出)
	 * @param orgIdsStr	要统计的区域
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return
	 */
	private Map<String, Object> getTotalDiscountSubsidy(String orgIdsStr, boolean isConfirm, SearchVo timeType){
		logger.debug("调用方法 getTotalDiscountSubsidy(orgIdsStr: {}, isConfirm: {}) 获取优惠券支出, 会员卡支出, 随机立减支出.", orgIdsStr, isConfirm);
		String isConfirmStr = null;
		if(isConfirm){
			isConfirmStr = " AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ";
		}else{
			isConfirmStr = " AND o.pay_state = 'unpay' ";
		}
		
		/* 保证in()里面不null */
		String merchantIds = StringUtils.join(this.getMerchantIds(orgIdsStr), ",");
		if(merchantIds==null || merchantIds.equals(""))merchantIds= "-1";
		String whereSql = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(SUM(o.card),0) cardSum, IFNULL(SUM(o.member_discount_money),0) member, IFNULL(SUM(sdl.discount_money),0)/100 ranTotal ");
		sql.append(" FROM `order` o LEFT JOIN scan_discount_log sdl ON sdl.order_id = o.id ");
		sql.append(" WHERE 1=1 "+ whereSql +" ");
		sql.append(isConfirmStr);
		sql.append(" 	AND o.merchant_id IN ( ");
		sql.append(merchantIds);
		sql.append(" 	) ");
		return this.findOneForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 获取商家返点总额
	 * @param orgIdsStr	要统计的区域
	 * @return
	 */
	private BigDecimal getTotalMerchantRebate(String orgIdsStr, SearchVo timeType){
		logger.debug("调用方法 getTotalMerchantRebate(orgIds: {}) 获取商家返点总额.", orgIdsStr);
		/**
		 * 获取商家扫码订单总额, 以及返点率
		 */
		List<Map<String, Object>> scanOrderDetails = this.getScanOrderDetails(orgIdsStr, timeType);
		BigDecimal totalMerchantRebate = BigDecimal.ZERO; 
		for(Map<String, Object> item: scanOrderDetails){
			BigDecimal origin = new BigDecimal(item.get("origin").toString());
			BigDecimal maxRebate = new BigDecimal(item.get("maxRebate").toString());
			BigDecimal rebateRate = new BigDecimal(item.get("rebateRate").toString()).movePointLeft(2);
			/**
			 * 计算返点金额(保留两位小数)
			 */
			BigDecimal rebate = origin.multiply(rebateRate).setScale(2, BigDecimal.ROUND_HALF_UP);
			/**
			 * 判断返点金额是否大于配置的最大返点金额
			 */
			if(rebate.compareTo(maxRebate) == 1){
				totalMerchantRebate = totalMerchantRebate.add(maxRebate);
			}else{
				totalMerchantRebate = totalMerchantRebate.add(rebate);
			}
		}
		return totalMerchantRebate;
	}
	
	/**(OvO)
	 * 获取范围内商家的扫码首单总金额以及返点率
	 * @param orgIdsStr
	 * @return
	 */
	private List<Map<String, Object>> getScanOrderDetails(String orgIdsStr, SearchVo timeType){
		logger.debug("调用方法 getScanOrderDetails(orgIdsStr: {}) 扫码首单总金额.", orgIdsStr);
		
		String whereTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		String merchantIds = StringUtils.join(this.getMerchantIds(orgIdsStr), ",");
		if(merchantIds==null || merchantIds.equals("")) merchantIds = "-1";
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT orders.merchant_id merchantId, IFNULL(SUM(orders.origin),0) origin, rs.high_money maxRebate, rs.rebate_rate rebateRate ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT * ");
		sql.append(" 	FROM( ");
		sql.append(" 		SELECT o.merchant_id, o.user_id, o.origin ");
		sql.append(" 		FROM `order` o ");
		sql.append(" 		WHERE 1=1 "+ whereTime +" AND o.sale_type=2 and o.order_type='scan_order' ");
		sql.append(" 			AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 			AND o.merchant_id IN ( ");
		sql.append(merchantIds);
		sql.append(" 			) ");
		sql.append(" 		ORDER BY o.create_time ");
		sql.append(" 	)o ");
		sql.append(" 	GROUP BY o.merchant_id,o.user_id ");
		sql.append(" )orders, 0085_rebate_setup rs ");
		sql.append(" WHERE orders.merchant_id = rs.merchant_id AND rs.begin_time <= CURDATE() AND rs.end_time >= CURDATE() ");
		sql.append(" GROUP BY orders.merchant_id ");
		return this.findForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 根据区域ID获取商家ID
	 * @param orgIdsStr	区域ID
	 * @return	商家ID List
	 */
	private List<Integer> getMerchantIds(String orgIdsStr){
		logger.debug("调用方法 getMerchantIds(orgIds: {}) 扫码首单总金额.", orgIdsStr);
		
		// 如果入参为空则补上"-1"防止出现SQL语法错误
		if (orgIdsStr == null || orgIdsStr.isEmpty()){
			orgIdsStr = "-1";
		}
		
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
	
	/**(OvO)
	 * 构造固定支出(工资)Map
	 * @param userId	当前登录用户ID
	 * @param orgId		要统计的区域
	 * @return	返回固定支出信息map
	 */
	private Map<String, Object> getWagesMap(String orgIdsStr, SearchVo vo){
		logger.debug("调用方法 getWagesMap(orgIdsStr: {}, vo: {}) 构造固定支出(工资)Map.", orgIdsStr, vo);
		
		/**
		 * 获取区域下快递员、BD数量
		 */
		Map<String, Object> staffCount = this.countCourierAndBD(orgIdsStr);
		logger.debug("获得员工数量: {}", staffCount);
		
		/**
		 * 获得快递员、BD工资
		 */
		Map<String, Object> wages = this.getTotalWages(staffCount, vo);
		logger.debug("获得员工工资: {}", wages);
		return wages;
	}
	
	/**(OvO)
	 * 获得快递员、BD提成总数
	 * @param courierIdsStr	若干个快递员ID(ID用","分隔)
	 * @param timeType		搜索时间条件
	 * @return
	 */
	private Map<String, Object> getDeductMap(String courierIdsStr, SearchVo timeType){
		logger.debug("调用方法 getTotalDeduct(courierIdsStr: {}) 获得快递员、BD提成总数.", courierIdsStr);
		
		BigDecimal courierDeduct = BigDecimal.ZERO;
		BigDecimal BDDeduct = BigDecimal.ZERO;
		
		/**
		 * 获取指定时间内快递员派送订单情况以及快递员的职位
		 */
		List<Map<String, Object>> orderCountList = this.getOrderCountList(courierIdsStr ,timeType);
		
		/**
		 * 提成规则集合
		 */
		Map<String, List<Map<String, Object>>> ruleLists = new HashMap<String, List<Map<String, Object>>>();
		
		/**
		 * 遍历订单情况表
		 */
		for(Map<String, Object> item: orderCountList){
			Integer positionId = Integer.valueOf(item.get("positionId").toString());
			String mapKey = positionId + "rule";
			List<Map<String, Object>> ruleList = ruleLists.get(mapKey);
			if(ruleList == null){
				/**
				 * 没有该职位的规则表, 就到数据库去取
				 */
				ruleList = this.getDeductRule(positionId);
				ruleLists.put(mapKey, ruleList);
				/**
				 * 如果见鬼了还是没查到, 就跳过
				 */
				if(ruleList == null){
					logger.warn("没查询到指定规则表: {}", item);
					continue;
				}
			}
			Integer orderCount = Integer.valueOf(item.get("orderCount").toString());
			
			/**
			 * 遍历规则表, 根据订单数选择适当的计算规则
			 */
			for(Map<String, Object> rule: ruleList){
				Integer minOrder = Integer.valueOf(rule.get("minOrder").toString());
				/**
				 * 由于规则表是根据minOrder排倒序的
				 * 所以直接用minOrder和快递员的orderCount对比即可获得适当的规则
				 */
				if(minOrder <= orderCount){
					BigDecimal deduct = new BigDecimal(rule.get("deduct").toString());
					BigDecimal rewards = new BigDecimal(rule.get("rewards").toString());
					BigDecimal orders = BigDecimal.valueOf(orderCount);
					/**
					 * 根据职位不同, 分别统计总提成
					 */
					if(positionId < 3){
						courierDeduct = courierDeduct.add(deduct.multiply(orders).add(rewards));
						break;
					}else{
						BDDeduct = BDDeduct.add(deduct.multiply(orders).add(rewards));
						break;
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("courierDeduct", courierDeduct);
		result.put("BDDeduct", BDDeduct);
		return result;
	}
	
	/**(OvO)
	 * 获取今天快递员订单数和快递员职位
	 * @param courierIdsStr	若干个快递员ID(ID用","分隔)
	 * @return
	 */
	private List<Map<String, Object>> getOrderCountList(String courierIdsStr ,SearchVo timeType){
		logger.debug("调用方法 getOrderCountList(courierIds: {}) 获取今天快递员订单数和快递员职位.", courierIdsStr);
		
//		String whereTime = SqlUtils.getBeforeTimeNum(num);
		String whereTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT o.courier_id courierId, COUNT(o.id) orderCount, cp.position_id positionId ");
		sql.append(" FROM `order` o, 0085_courier_position cp ");
		sql.append(" WHERE 1=1 "+ whereTime +" AND o.courier_id > 0  AND o.courier_id IN ( ");
		sql.append(courierIdsStr);
		sql.append(" ) ");
		sql.append(" AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') AND cp.courier_id = o.courier_id AND cp.position_id < 4 ");
		sql.append(" GROUP BY o.courier_id ");
		return this.findForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 根据职位ID获取提成规则表
	 * @param positionId	职位ID
	 * @return	指定职位的提成规则(排序: 按订单量降序)
	 */
	private List<Map<String, Object>> getDeductRule(Integer positionId){
		logger.debug("调用方法 getDeductRule(positionId: {}) 根据职位ID获取提成规则表.", positionId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cd.position_id positionId, cgr.total_order minOrder, cd.deduct, cd.rewards ");
		sql.append(" FROM 0085_courier_grade_rule cgr , 0085_courier_deduct cd ");
		sql.append(" WHERE cd.type = 1 AND cd.position_id = ? AND cd.user_id = 0 AND cgr.invalid = 0 AND cgr.id = cd.grade_rule_id ");
		sql.append(" ORDER BY minOrder DESC ");
		return this.findForJdbc(sql.toString(), positionId);
	}
	
	/**(OvO)
	 * 获取指定区域下快递员、BD数量
	 * @param orgIdsStr	若干个区域ID列表(用","隔开)
	 * @return	courierCount	快递员数量
	 * 			BDCount			BD数量
	 */
	private Map<String, Object> countCourierAndBD(String orgIdsStr){
		logger.debug("调用方法 countCourierAndBD(orgIds: {}) 获取指定区域下快递员、BD数量.", orgIdsStr);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(CASE WHEN cpos.position_id = 2 THEN 1 ELSE NULL END),0) courierCount ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN cpos.position_id = 3 THEN 1 ELSE NULL END),0) BDCount ");
		sql.append(" FROM `user` u, 0085_courier_org corg, 0085_courier_position cpos ");
		sql.append(" WHERE u.user_state = 1 AND u.is_delete = 0 AND u.user_type = 'courier' AND u.id = corg.courier_id AND cpos.courier_id = corg.courier_id ");
		sql.append(" 	AND corg.org_id IN( ");
		sql.append(orgIdsStr);
		sql.append(" 	) ");
		return this.findOneForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 获得快递员、BD基本工资总数
	 * @param staffCount	快递员、BD数量Map
	 * @return
	 */
	private Map<String, Object> getTotalWages(Map<String, Object> staffCount, SearchVo timeType){
		logger.debug("调用方法 getTotalWages(staffCount: {}) 获得快递员、BD基本工资总数.", staffCount);
		
		BigDecimal courierCount = new BigDecimal(staffCount.get("courierCount").toString());
		BigDecimal BDCount = new BigDecimal(staffCount.get("BDCount").toString());
		BigDecimal minimumWages = this.getMinimumWages();
		BigDecimal days = this.getDays(timeType);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("courierWages", courierCount.multiply(minimumWages).divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP).multiply(days));
		result.put("BDWages", BDCount.multiply(minimumWages).divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP).multiply(days));
		
		return result;
	}
	
	/**(OvO)
	 * 根据时间搜索条件获得用于计算工资的天数
	 * @param timeType	时间搜索条件
	 * @return
	 */
	private BigDecimal getDays(SearchVo timeType){
		if("week".equals(timeType.getTimeType())){
			int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			int days;
			if(dayOfWeek > 1){
				days = dayOfWeek - 1;
			}
			else{
				days = 7;
			}
			return BigDecimal.valueOf(days);
		}else if("month".equals(timeType.getTimeType())){
			int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			return BigDecimal.valueOf(dayOfMonth);
		}else if("other".equals(timeType.getTimeType())){
			/**
			 *  如果period刚好能被86400整除而且period大于等于86400
			 *  则days会比预期大1
			 *  所以period减1
			 */
			int period = timeType.getEndTime() - timeType.getBeginTime() - 1;
			int days = period / SqlUtils.LENGTH_OF_DAY_IN_SECOND;
			return BigDecimal.valueOf(days);
		}else{
			return BigDecimal.ONE;
		}
	}
	
	/**(OvO)
	 * 获取快递员、BD的基本工资
	 * @return	基本工资(元)
	 */
	private BigDecimal getMinimumWages(){
		logger.debug("调用方法 getMinimumWages() 获取快递员、BD的基本工资.");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sc.`value` ");
		sql.append(" FROM system_config sc ");
		sql.append(" WHERE sc.`code` = 'wages' ");
		String resultStr = findOneForJdbc(sql.toString(), String.class);
		if(resultStr == null){
			logger.warn("数据库中没有配置快递员、BD基本工资, 将快递员、BD基本工资设为3000");
			resultStr = "3000";
		}
		return new BigDecimal(resultStr);
	}
}
