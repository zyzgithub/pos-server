package com.courier_mana.statistics.service.impl;

import java.util.Calendar;
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
import com.courier_mana.statistics.service.CouriersMerchantService;

@Service
public class CouriersMerchantServiceImpl extends CommonServiceImpl implements CouriersMerchantService {
	private static final Logger logger = LoggerFactory.getLogger(CouriersMerchantServiceImpl.class);

	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public Map<String, Object> merchantsCount(SearchVo vo, Integer courierId) {
		logger.info("Invoke method: merchantsCount, params: courierId - {}, vo.timeType - {}, vo.orgId - {}, vo.beginTime - {}, vo.endTime - {}",
															courierId, vo.getTimeType(), vo.getOrgId(), vo.getBeginTime(), vo.getEndTime());
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 根据搜索条件(时间)调整SQL条件
		 */
		String period = "";	//查询时间
		{
			String timeType = vo.getTimeType();
			if(timeType == null || timeType.equals("day")){
				period = " AND DATE(FROM_UNIXTIME(o.create_time)) >= CURDATE()";			//按天搜索的条件
			}else if(timeType.equals("week")){
			    period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL WEEKDAY(CURDATE()) DAY)) ";//近7天搜索的条件
			}else if(timeType.equals("month")){
			    period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";//近1个月的搜索条件
			}else if(timeType.equals("other") && !vo.getBeginTime().equals(0) && !vo.getBeginTime().equals(0)){//SearchVo中beginTime, endTime会自动初始化为0
				period = " AND o.create_time >= " + this.getStartTimeOfTheDayInSecond(vo.getBeginTime()) + " AND o.create_time < " + this.getEndTimeOfTheDayInSecond(vo.getEndTime()) + " ";
			}
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(CASE WHEN m.group_id = 101 THEN 1 ELSE NULL END),0)fastFoodMerchant ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN m.group_id = 507 THEN 1 ELSE NULL END),0)drinkMerchant ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN m.group_id = 511 THEN 1 ELSE NULL END),0)communityMerchant ");
		sql.append(" 	,IFNULL(COUNT(1),0)totalMerchant ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT o.merchant_id id ");
		sql.append(" 	FROM `order` o ");
		sql.append(" 	WHERE o.pay_state='pay' AND o.pay_id is not null AND o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(period);
		sql.append(" 	GROUP BY o.merchant_id ");
		sql.append(" )mIds, merchant m, 0085_merchant_org mo ");
		sql.append(" WHERE mIds.id = m.id AND m.id = mo.merchant_id  ");
		/**
		 * 根据搜索条件(地区)调整SQL条件
		 */
		List<Integer> orgIds = null;
		if(vo.getOrgId() != null){
			orgIds = this.courierOrgService.getManageOrgIds(courierId, vo.getOrgId());	//如果传入了orgId, 就按传入的orgId查询
		}else{
			orgIds = this.courierOrgService.getManageOrgIds(courierId);					//如果没有传入orgId, 就按快递员的管辖区域查询
		}
		sql.append(" 			AND mo.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" 			) ");
		
		logger.debug("Inside method: merchantsCount, SQL: {}", sql.toString());
		
		Map<String, Object> merchantCountData = findOneForJdbc(sql.toString());
		
		if(merchantCountData != null){
			return this.putOtherMerchantCountIntoMap(merchantCountData);
		}
		
		return null;
	}
	
	@Override
	public List<Map<String, Object>> merchantsRank(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage, String merchantType) {
		logger.info("Invoke method: merchantsRank, params: courierId - {}, vo.timeType - {}, vo.orgId - {}, vo.beginTime - {}, vo.endTime - {}, page - {}, rowsPerPage - {}, merchantType - {}",
															courierId, vo.getTimeType(), vo.getOrgId(), vo.getBeginTime(), vo.getEndTime(), page, rowsPerPage, merchantType);
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 初始化分页参数
		 */
		page = this.initPageVar(page);
		rowsPerPage = this.initRowsPerPageVar(rowsPerPage);
		
		/**
		 * 根据搜索条件(时间)调整SQL条件
		 */
		String period = "";			//查询的时间条件
		String prePeriod = "";		//上一个周期的时间, 获取趋势
		String detailPeriod = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE())-3*86400 AND o.create_time < UNIX_TIMESTAMP(CURDATE()) ";	//查询近3天销售记录的时间条件
		{
			String timeType = vo.getTimeType();
			if(timeType == null || timeType.equals("day")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE()) ";			//按天搜索的条件
				prePeriod = " AND od.create_time >= UNIX_TIMESTAMP(CURDATE())-86400 AND od.create_time < UNIX_TIMESTAMP(CURDATE()) ";				//按天搜索时上一周期的查询条件
			}else if(timeType.equals("week")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE())-6*86400 ";	//近7天搜索的条件
				prePeriod = " AND od.create_time >= UNIX_TIMESTAMP(CURDATE())-13*86400 AND od.create_time < UNIX_TIMESTAMP(CURDATE())-6*86400 ";	//近7天搜索时上一周期的查询条件
			}else if(timeType.equals("month")){
				period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE())-29*86400 ";	//近30天搜索的条件
				prePeriod = " AND od.create_time >= UNIX_TIMESTAMP(CURDATE())-59*86400 AND od.create_time < UNIX_TIMESTAMP(CURDATE())-29*86400 ";	//近30天搜索时上一周期的查询条件
			}else if(timeType.equals("other") && !vo.getBeginTime().equals(0) && !vo.getBeginTime().equals(0)){//SearchVo中beginTime, endTime会自动初始化为0
				Integer beginning = this.getStartTimeOfTheDayInSecond(vo.getBeginTime());
				Integer ending = this.getEndTimeOfTheDayInSecond(vo.getEndTime());
				Integer periodLength = ending - beginning;
				period = " AND o.create_time >= " + beginning + " AND o.create_time < " + ending + " ";
				prePeriod = " AND od.create_time >= " + (beginning - periodLength) + " AND od.create_time < " + beginning + " ";
				/**
				 * 自定义时间需要重新定义查询近3天销售记录的时间条件
				 */
				detailPeriod = " AND od.create_time >= " + (ending - 3*86400) + " AND od.create_time < " + ending + " ";
			}
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT sourceList.* ");
		sql.append(" 		,case when @prevrank = sourceList.todayOrigin then @currank when @prevrank := sourceList.todayOrigin then @currank := @currank + 1 when not @prevrank := sourceList.todayOrigin then @currank := @currank + 1 end as rank ");
		sql.append(" 	FROM( ");
		sql.append(" 		SELECT o.merchant_id merchantId, m.title merchantName, SUM(o.origin) todayOrigin ");
		sql.append(" 			,(SELECT SUM(od.origin) FROM `order` od WHERE od.merchant_id = m.id AND od.pay_state='pay' AND od.pay_id is not null AND od.state='confirm' and (od.rstate='norefund' OR od.rstate='normal'))totalOrigin ");
		sql.append(" 			,(SELECT IFNULL(SUM(od.origin),0) FROM `order` od WHERE od.merchant_id = m.id ");
		sql.append(prePeriod);
		sql.append(" AND od.pay_state='pay' AND od.pay_id is not null AND od.state='confirm' and (od.rstate='norefund' OR od.rstate='normal'))preOrigin ");
		sql.append(" 		FROM `order` o, merchant m, 0085_merchant_org mo ");
		sql.append(" 		WHERE o.pay_state='pay' AND o.pay_id is not null AND o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 			AND m.id = o.merchant_id AND m.id = mo.merchant_id ");
		sql.append(period);
		/**
		 * 根据搜索条件(店铺类型)调整SQL条件
		 */
		sql.append(this.appendMerchantTypeInSQL(merchantType));
		/**
		 * 根据搜索条件(地区)调整SQL条件
		 */
		List<Integer> orgIds = null;
		if(vo.getOrgId() != null){
			orgIds = this.courierOrgService.getManageOrgIds(courierId, vo.getOrgId());	//如果传入了orgId, 就按传入的orgId查询
		}else{
			orgIds = this.courierOrgService.getManageOrgIds(courierId);					//如果没有传入orgId, 就按快递员的管辖区域查询
		}
		sql.append(" 			AND mo.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" 			) ");
		sql.append(" 		GROUP BY o.merchant_id ");
		sql.append(" 		ORDER BY todayOrigin DESC, merchantId ");
		sql.append(" 	)sourceList, (select @currank :=0, @prevrank := null) var ");
		sql.append(" )rankList ");
		
		logger.debug("Inside method: merchantsRank, SQL: {}", sql.toString());
		
		/**
		 * 获取排序列表
		 */
		List<Map<String, Object>> result = this.findForJdbc(sql.toString(), page, rowsPerPage);
		
		/**
		 * 为店铺添加近三天的销售记录
		 */
		this.putDetailsIntoRankList(result, detailPeriod);
		
		return result;
	}
	
	/**(OvO)
	 * 整理vo中的秒数(vo所存的时间以秒为单位)
	 * 使它变成当天的最后一秒(第二天的0秒)
	 * 直接使用秒数查询比较快
	 */
	private Integer getEndTimeOfTheDayInSecond(Integer endTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(endTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	
	/**(OvO)
	 * 整理vo中的秒数(vo所存的时间以秒为单位)
	 * 使它变成当天的0秒
	 * 直接使用秒数查询比较快
	 */
	private Integer getStartTimeOfTheDayInSecond(Integer startTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	
	/**(OvO)
	 * 获取其他类型商家的数量
	 * 并将此数量放到Map中
	 * @return
	 */
	private Map<String, Object> putOtherMerchantCountIntoMap(Map<String, Object> merchantCountData){
		Integer fastFoodMerchant = Integer.parseInt(merchantCountData.get("fastFoodMerchant").toString());
		Integer drinkMerchant = Integer.parseInt(merchantCountData.get("drinkMerchant").toString());
		Integer communityMerchant = Integer.parseInt(merchantCountData.get("communityMerchant").toString());
		Integer totalMerchant = Integer.parseInt(merchantCountData.get("totalMerchant").toString());
		merchantCountData.put("otherMerchant", totalMerchant - communityMerchant - drinkMerchant - fastFoodMerchant);
		return merchantCountData;
	}

	/**(OvO)
	 * 初始化page(页码)变量
	 * @param page 需要初始化的页码变量
	 * @return 如果page为空则返回1
	 */
	private Integer initPageVar(Integer page){
		if(page == null){
			return 1;
		}
		return page;
	}
	
	/**(OvO)
	 * 初始化rowsPerPage(每页显示记录数)变量
	 * @param page 需要初始化的页码变量
	 * @return 如果page为空则返回1
	 */
	private Integer initRowsPerPageVar(Integer rowsPerPage){
		if(rowsPerPage == null){
			return 10;
		}
		return rowsPerPage;
	}
	
	/**(OvO)
	 * 根据搜索条件(店铺类型)调整SQL条件
	 * @param merchantType	店铺类型(a: 所有类型; f: 快餐店铺; d: 饮品店铺; c: 社区店铺; o: 其他店铺)
	 * @return
	 */
	private String appendMerchantTypeInSQL(String merchantType){
		if(merchantType == null || "a".equals(merchantType)){	//查询全部店铺的条件
			return "";
		}else if("f".equals(merchantType)){						//查询快餐店铺的条件
			return " AND m.group_id = 101 ";
		}else if("d".equals(merchantType)){						//查询饮品店铺的条件
			return " AND m.group_id = 507 ";
		}else if("c".equals(merchantType)){						//查询社区店铺的条件
			return " AND m.group_id = 511 ";
		}else if("o".equals(merchantType)){						//查询其他店铺的条件
			return " AND m.group_id NOT IN (101, 507, 511) ";
		}else{													//如果入参不在预期范围内, 则查询全部
			return "";
		}
	}
	
	/**
	 * 为排名列表添加近三天的销售记录
	 * @param rankList		店铺排名列表
	 * @param detailPeriod	查询近3天记录的时间条件
	 */
	private void putDetailsIntoRankList(List<Map<String, Object>> rankList, String detailPeriod){
		logger.debug("Invoke method: putDetailsIntoRankList, params: rankList - {}", rankList);
		/**
		 * 遍历列表, 查询商家近3天的销售记录并将数据添加到排名列表
		 */
		for(Map<String, Object> merchantData: rankList){
			merchantData.put("detail", this.getOriginDetails(Integer.parseInt(merchantData.get("merchantId").toString()), detailPeriod));
		}
	}
	
	/**
	 * 查询指定商家近3天的销售记录
	 * @param merchantId	商家ID
	 * @param detailPeriod	查询近3天记录的时间条件
	 * @return
	 */
	private List<Map<String, Object>> getOriginDetails(Integer merchantId, String detailPeriod){
		logger.debug("Invoke method: getOriginDetails, params: merchantId - {}, detailPeriod - {}", merchantId, detailPeriod);
		/**
		 * 先检查参数必要的参数
		 */
		if(merchantId == null){
			merchantId = 0;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.date, a.todayOrigin ");
		sql.append(" 	,(SELECT SUM(od.origin) FROM `order` od WHERE od.merchant_id = a.merchant_id AND od.pay_state='pay' AND od.pay_id is not null AND od.state='confirm' and (od.rstate='norefund' OR od.rstate='normal') AND DATE(FROM_UNIXTIME(od.complete_time)) <= a.date) totalOrigin ");
		sql.append(" FROM ( ");
		sql.append(" 	SELECT DATE(FROM_UNIXTIME(o.create_time))date ,SUM(origin) todayOrigin, o.merchant_id ");
		sql.append(" 	FROM `order` o ");
		sql.append(" 	WHERE o.merchant_id = ");
		sql.append(merchantId);
		sql.append(" 		AND o.pay_state='pay' AND o.pay_id is not null AND o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(detailPeriod);
		sql.append(" 	GROUP BY DATE(FROM_UNIXTIME(o.create_time)) ");
		sql.append(" 	ORDER BY date DESC ");
		sql.append(" )a ");
		
		logger.debug("Inside method: getOriginDetails, SQL: {}", sql.toString());
		
		return findForJdbc(sql.toString());
	}
}
