package com.wm.service.impl.deduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeecg.system.service.SystemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.common.Constants;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.deduct.DeductServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;

@Service("deductLogService")
@Transactional
public class DeductLogServiceImpl extends CommonServiceImpl implements DeductLogServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(DeductLogServiceImpl.class);
	
//	private static final Integer baseSalary = 3000; // 快递员基本工资
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private DeductServiceI deductService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	public DeductLogEntity getDeductByRule(List<Map<String, Object>> deductRule, Double totalOrder, DeductLogEntity deductLog) {
		if(null == deductLog){
			deductLog = new DeductLogEntity();
		}
		Integer courierId = deductLog.getCourierId();
		if(CollectionUtils.isEmpty(deductRule)){
			logger.warn("未找到快递员{}对应的提成规则", courierId);
			return deductLog;
		}
		if(totalOrder <= 0){
			logger.warn("该快递员{}昨日可拿提成订单数为0", courierId);
			return deductLog;
		}
		else{
			for(Map<String, Object> map : deductRule){
				if(Double.parseDouble(map.get("total_order").toString()) <= totalOrder){
					Double total = 0.0;
					Double deduct = 0.0;
					Integer type = deductLog.getDeductType();
					if(type.equals(0)){
						logger.info("计算快递员{}日提成,可拿提成订单数为{}", courierId, totalOrder);
						Double reward = Double.valueOf(map.get("rewards").toString()) * 100;
						Double subsidy = Double.valueOf(map.get("subsidy").toString()) * 100;
						deductLog.setReward(reward/100);
						deductLog.setMealSubsidy(subsidy/100);
						deduct = Double.valueOf(map.get("deduct").toString()) * 100;
						deduct = deduct * totalOrder;
						total = deduct + reward + subsidy;
					} else if(type.equals(1)){
						logger.info("计算快递员管理层{}提成", courierId);
						deduct = Double.valueOf(map.get("mng_deduct").toString()) * 100;
						deduct = deduct * totalOrder;
						total = deduct;
					} else if(type.equals(4) || type.equals(5)){
						logger.info("计算供应链司机或者快递员{}提成", courierId);
						Double reward = Double.valueOf(map.get("rewards").toString()) * 100;
						Double subsidy = Double.valueOf(map.get("subsidy").toString()) * 100;
						deductLog.setReward(reward / 100);
						deductLog.setMealSubsidy(subsidy / 100);
						deduct = Double.valueOf(map.get("deduct").toString()) * 100;
						Integer baseSalary = 0;
						String baseSalaryConfig = systemService.getSystemConfigValue("baseSalary");
						if(!StringUtils.isEmpty(baseSalaryConfig)){
							baseSalary = Integer.parseInt(baseSalaryConfig);
						}
						logger.info("baseSalary:{}", baseSalary);
						if(totalOrder > baseSalary){
							logger.info("supply deduct ruleId【{}】: {}*({}-{})", map.get("id"), deduct, totalOrder, baseSalary);
							deduct = deduct * (totalOrder - baseSalary);
							total = deduct + reward + subsidy;
						} else {
							deductLog.setOrders(totalOrder);
							return deductLog;
						}
					} else {
						logger.error("unknow deduct type:{}", type);
						return deductLog;
					}
					deductLog.setOrders(totalOrder);
					deductLog.setDeduct(deduct / 100);
					deductLog.setTotalDeduct(total / 100);
					return deductLog;
				}
			}
		}
		return deductLog;
	}
	
	public static void main(String[] args) {
		System.out.println(Double.valueOf("0.005") * 100);
	}

	public Double getDeductByRule(List<Map<String, Object>> deductRule, Double orderPerDay, Integer monthOrders) {
		Double retDuduct= 0d;
		if(deductRule != null && deductRule.size() > 0 && orderPerDay > 0){
			for(Map<String, Object> map : deductRule){
				if(Double.parseDouble(map.get("total_order").toString()) <= orderPerDay){
					Double deduct = Math.rint(Double.valueOf(map.get("sub_deduct").toString()) * 100);
					retDuduct += deduct * monthOrders;
					retDuduct /= 100.0;
					return retDuduct;
				}
			}
		}
		return retDuduct;
	}
	
	/**
	 * 按group分类统计快递员昨天的提成收入
	 * @param courierId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCourierGroupMenu(Integer courierId, String startDate, String endDate, String incomeType){
		List<Map<String, Object>> list = this.getCourierOrderGroups(courierId, startDate, startDate, incomeType);
		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		List<Map<String, Object>> deductRule = new ArrayList<Map<String,Object>>();
		if(courierType == Constants.COURIER_COOPERATE_BUSINESS){
			Integer bindUserId = courierInfoService.getCourierBindUserId(courierId);
			if(bindUserId != null){
				deductRule = deductService.getCourierDeductRule(courierId, bindUserId, courierType);
			}
		}
		if(courierType == Constants.COURIER){
			deductRule = deductService.getCourierDeductRule(courierId, 0, courierType);
		}
		Map<String, Object> totalMap = new HashMap<String, Object>();
		totalMap.put("name", "总计");
		Integer totalOrder = 0;
		Double totalDeduct = 0.0;
		for(Map<String, Object> map : list){
			Double total = Double.parseDouble(map.get("total").toString());//订单数
			DeductLogEntity deductLog = new DeductLogEntity();
			deductLog.setCourierId(courierId);
			deductLog = this.getDeductByRule(deductRule, total, deductLog);
			Double deduct = deductLog.getTotalDeduct();
			map.put("deduct", deduct.toString());
			totalOrder += total.intValue();
			totalDeduct += deduct;
		}
		totalMap.put("total", totalOrder.toString());
		totalMap.put("deduct", totalDeduct.toString());
		list.add(totalMap);
		return list;
	}

	/**
	 * 按group分类统计快递员可拿提成的送餐份数
	 * @param courierId
	 * @param incomeType 
	 * @return
	 */
	public List<Map<String, Object>> getCourierOrderGroups(Integer courierId, String startDate, String endDate, String incomeType){
		String sql = "select distinct c.name,sum(om.quantity) total from order_menu om ";
		sql += " left join menu m on om.menu_id=m.id ";
		sql += " left join `order` o on o.id=om.order_id ";
		sql += " left join merchant mer on o.merchant_id=mer.id ";
		sql += " left join 0085_merchant_info mi on mi.merchant_id=mer.id ";
		sql += " left join category c on c.id=mer.group_id ";
		sql += " where o.state in ('confirm','done') and o.rstate<>'berefund' and o.courier_id=? ";
		sql += " and om.price>= case when mi.courier_min_deduct_money is not null then mi.courier_min_deduct_money else 0 end  ";
		if(StringUtils.isNotEmpty(startDate)){
			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
		}
		if(StringUtils.isNotEmpty(incomeType)){
			sql += " and c.name = '" + incomeType + "'";
		}
		sql += " group by c.name";
		return this.findForJdbc(sql, new Object[]{courierId});
	}

	/**
	 * 按group分类统计快递员昨天的提成收入
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCourierDeductGroups(Integer courierId, String startDate, String endDate, String incomeType){
		List<Map<String, Object>> list = this.getCourierOrderGroups(courierId, startDate, startDate, incomeType);
		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		List<Map<String, Object>> deductRule = new ArrayList<Map<String,Object>>();
		if(courierType == Constants.COURIER_COOPERATE_BUSINESS){
			Integer bindUserId = courierInfoService.getCourierBindUserId(courierId);
			if(bindUserId != null){
				deductRule = deductService.getCourierDeductRule(courierId, bindUserId, courierType);
			}
		}
		if(courierType == Constants.COURIER){
			deductRule = deductService.getCourierDeductRule(courierId, 0, courierType);
		}
		Map<String, Object> totalMap = new HashMap<String, Object>();
		totalMap.put("name", "总计");
		Integer totalOrder = 0;
		Double totalDeduct = 0.0;
		for(Map<String, Object> map : list){
			Double total = Double.parseDouble(map.get("total").toString());//单数
			DeductLogEntity deductLog = new DeductLogEntity();
			deductLog.setCourierId(courierId);
			deductLog = this.getDeductByRule(deductRule, total, deductLog);
			Double deduct = deductLog.getTotalDeduct();
			map.put("deduct", deduct.toString());
			totalOrder += total.intValue();
			totalDeduct += deduct;
		}
		totalMap.put("total", totalOrder.toString());
		totalMap.put("deduct", totalDeduct.toString());
		list.add(totalMap);
		return list;
	}

	@Override
	public List<Map<String, Object>> getYesterdayIncome(Integer userId,
			String date) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT CASE WHEN SUM(cdl.deduct) is null then 0 ELSE SUM(cdl.deduct) end orderDeduct, ");
		query.append(" CASE WHEN SUM(cdl.reward) is null then 0 ELSE SUM(cdl.reward) end  reward, ");
		query.append(" CASE WHEN SUM(cdl.orders) is null then 0 ELSE SUM(cdl.orders) end totalorders");
		query.append(" from ( ");
		query.append(" SELECT  deduct, reward, deduct_type, orders");
		query.append(" from 0085_courier_deduct_log");
		query.append(" WHERE courier_id = ? AND DATE(FROM_UNIXTIME(account_date)) = ? ) cdl ");
		query.append(" where deduct_type in (0,3)");
		return  findForJdbc(query.toString(), userId, date);
		
	}
	
	@Override
	public List<Map<String, Object>> getYesterday(Integer userId,
			String date) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT CASE WHEN SUM(cdl.deduct) is null then 0 ELSE SUM(cdl.deduct) end deduct, ");
		query.append(" CASE WHEN SUM(cdl.reward) is null then 0 ELSE SUM(cdl.reward) end  reward, ");
		query.append(" CASE WHEN SUM(cdl.orders) is null then 0 ELSE SUM(cdl.orders) end totalorders");
		query.append(" from ( ");
		query.append(" SELECT  deduct, reward, deduct_type, orders");
		query.append(" from 0085_courier_deduct_log");
		query.append(" WHERE courier_id = ? AND DATE(FROM_UNIXTIME(account_date)) = ? ) cdl");
		query.append(" where deduct_type in (1,2)");
		return  findForJdbc(query.toString(), userId, date);
		
	}
	
	
	/**
	 * 获取快递员每月的日提成详情
	 * @param courierId
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getEverydayDeduct(Integer courierId, String date, int page, int rows){
		
		String key =CacheKeyUtil.getEverydayDeductRecordKey(courierId, date, page);
		Object object = AliOcs.getObject(key);
		if(object != null){
			return (List<Map<String, Object>>) object;
		}
		else{
			StringBuilder query = new StringBuilder();
			query.append(" select date_format(from_unixtime(account_date), '%m月%d日 ') accountDate, sum(quantity) quantity, sum(total_deduct) everydayDeduct");
			query.append(" from 0085_courier_deduct_log");
			query.append(" where courier_id = ? and date_format(from_unixtime(account_date),'%Y-%m') = ?");
			query.append(" group by accountDate");
			List<Map<String, Object>> everydayDeductList = findForJdbcParam(query.toString(), page, rows, courierId, date);
			AliOcs.set(key, everydayDeductList, 60*60*24);
			return everydayDeductList;
		}
		
	}

	@Override
	public DeductLogEntity getCrowdsouringCourierEverydayDeduct(List<Map<String, Object>> deductRule, Integer totalOrder, DeductLogEntity deductLog) {
		if(null == deductLog){
			deductLog = new DeductLogEntity();
		}
		if(CollectionUtils.isEmpty(deductRule)){
			logger.warn("未找到快递员【" + deductLog.getCourierId() + "】对应的提成规则");
			return deductLog;
		}
		if(totalOrder <= 0){
			logger.warn("该快递员"+ deductLog.getCourierId() + "昨日可拿提成订单数为0");
			return deductLog;
		}
		else{
			logger.info("计算快递员【" + deductLog.getCourierId() + "】日提成, 可拿提成订单数" + totalOrder);
			Double deduct = Double.parseDouble(deductRule.get(0).get("deduct").toString())*100;
			deduct = deduct * totalOrder;
			deductLog.setOrders(totalOrder.doubleValue());
			deductLog.setDeduct(deduct/100);
			deductLog.setTotalDeduct(deduct/100);
		}
		return deductLog;
	}
}