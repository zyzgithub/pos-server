package com.wm.service.impl.rebate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.rebate.CourierRebateEntity;
import com.wm.entity.rebate.MerchantRebateEntity;
import com.wm.entity.rebate.MerchantRebateGrantEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.rebate.RebateServiceI;

@Service
@Transactional
public class RebateServiceImpl extends CommonServiceImpl implements RebateServiceI {
	private static final Logger logger = Logger.getLogger(RebateServiceImpl.class);
	
	@Autowired
	private FlowServiceI flowService;
	
	/**
	 * 1、商家每天返点奖励补贴计算，步骤（1）得到前一天所有扫码用户首单（商家ID，首单总额），按商家分组 （2）通过商家ID得到当月返点率5% 3% 1% (3)统计总额 * 返点率，并且<=最高金额
	 */
	@Override
	public void statMerchantRebateByEveryday() {
		logger.info("》》》》统计每日商家返点");
		Long begin = DateUtils.getYesterdayStartInSecond(); 
		Long end = DateUtils.getTodayStartInSecond();
		Date countDate = DateUtils.getYesterday();
		
		logger.info("每日统计订单开始时间="+begin+"，统计订单结束时间="+end+"，统计日期="+countDate);
		
		List<Map<String, Object>> firstOriginList = this.getFirstOrigin(begin, end);//按商家得到用户首单
		Map<Integer, Double> originSumMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orderCountMap = new HashMap<Integer, Integer>();
		
		//计算每个商家首单总额和总订单数
		if(firstOriginList!=null && firstOriginList.size()>0){
			for(int i=0;i<firstOriginList.size();i++){
				Map<String, Object> firstOrigin = firstOriginList.get(i);
				try{
					Integer orderId = Integer.parseInt(String.valueOf(firstOrigin.get("id")));
					Integer merchantId = Integer.parseInt(String.valueOf(firstOrigin.get("merchantId")));
					Double origin = Double.valueOf(String.valueOf(firstOrigin.get("origin")));
					logger.info("getFirstOrigin():商家ID="+merchantId+"，首单订单ID="+orderId+"，首单实付金额="+origin);
					
					//按门店类型，判断该单是否超过订单规定限额
					Map<String, Object> category = this.getCategoryHighMoney(merchantId);
					if(category!=null){
						Double highMoney = (Double) category.get("high_money");
						if(highMoney!=0 && origin>=highMoney){
							//首单金额大于或等于门店类型限额，都不返点
							continue;
						}else{
							if(origin>=1000){  //其他类别限额1000
								continue;
							}
							if(originSumMap.containsKey(merchantId)){
								Double originSum = originSumMap.get(merchantId);
								originSumMap.put(merchantId, originSum + origin); //总额
								
								Integer orderCount = orderCountMap.get(merchantId);
								orderCountMap.put(merchantId, orderCount + 1); //总订单数
								
							}else{
								originSumMap.put(merchantId, origin);
								orderCountMap.put(merchantId, 1);
							}
						}
					}
				}catch(Exception e){
					logger.info("计算每日商家返点出错：商家ID=" + firstOrigin.get("merchantId"));
					logger.info(e.getMessage());
				}
			}
			
			if(originSumMap !=null && originSumMap.size()>0){
				Iterator<Integer> keys = originSumMap.keySet().iterator();
				while(keys.hasNext()){
					Integer merchantId = keys.next();
					Double originSum = originSumMap.get(merchantId);
					Integer orderCount = orderCountMap.get(merchantId);

					//得到商家返点配置
					Map<String, Object> setup = this.getRebateSetup(merchantId, countDate);
					if(setup==null){
						continue;
					}
					
					//查询商家从签约时间到昨天有没有统计过，如果没统计过，统计签约时间到昨天0点的记录
					Date contractTime = (Date) setup.get("contract_time");
					List<Map<String, Object>> list = this.getMerchantRebate(merchantId, contractTime, DateUtils.getYesterday());
					if(list==null || list.size()==0){
						long beginlong = DateUtils.getSecond(contractTime); //签约的时间, 0点
						long endlong = DateUtils.getSecond(DateUtils.getYesterday()); //昨天0点
						long days = (endlong-beginlong)/86400;  //开始时间到结束时间之间的天数
						
						for(int j=0;j<days;j++){
							//统计每一天
							this.statMerchantRebate(beginlong, beginlong+86400, merchantId);
							beginlong = beginlong+86400;
						}
					}
					
					BigInteger rebateSetupId = (BigInteger) setup.get("id");
					Double rebateRate = (Double) setup.get("rebate_rate");
					Double highMoney = (Double) setup.get("high_money");
					logger.info("getRebateSetup()：商家返点率="+rebateRate+"，最高返点金额="+highMoney);
					
					MerchantRebateEntity mrEntity = new MerchantRebateEntity();
					mrEntity.setCountDate(countDate);  //统计日期
					mrEntity.setOrderCount(orderCount.intValue());	//订单数
					mrEntity.setOriginSum(originSum);	//订单金额
					mrEntity.setRebateSum(originSum*rebateRate/100 > highMoney ? highMoney : originSum*rebateRate/100);	//奖励金额
					mrEntity.setRebateSetupId(rebateSetupId.intValue());	//返点设置ID
					//mrEntity.setGrantTime(DateUtils.str2Timestamp("1700-01-01")); //默认发放时间
					mrEntity.setGrantState(0);	//未发放
					mrEntity.setMerchantId(merchantId); //商家ID
					mrEntity.setRebateRate(rebateRate); //返点率
					//保存商家日统计
					save(mrEntity);
					
					//新增明细
					this.addOrderMerchantRebate(mrEntity.getId(), begin, end, merchantId);
				}
			}
			
			
		}
	}
	
	//查询商家从签约时间到昨天有没有统计过
	public List<Map<String, Object>> getMerchantRebate(Integer merchantId, Date beginDate, Date endDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from 0085_merchant_rebate where merchant_id=? and count_date>=? and count_date<=? ");
		return findForJdbc(sql.toString(), merchantId, beginDate, endDate);
	}
	
	/**
	 * 统计一段时间内的返点奖励
	 * 商家每天返点奖励补贴计算，步骤（1）得到所有签约商家的签约日期与返点率5% 3% 1%（2）通过商家和日期得到所有扫码用户首单，按商家分组  (3)统计总额 * 返点率，并且<=最高金额
	 */
	@Override
	public void statMerchantRebateByBefore(Date beginDate, Date endDate, Integer merchant_Id) {
		logger.info("》》》》统计商家返点, 签约日期到功能上线之间的时间段");
		Long begin,end;
		//Long end = DateUtils.getTodayStartInSecond(); //运行时间当天0点0时0分
		
		//得到商家签约时间
		List<Map<String, Object>> setupList = this.getAllRebateSetup(merchant_Id);
		if(setupList!=null && setupList.size()>0){
			for(int i=0; i<setupList.size(); i++){
				Map<String, Object> setup = setupList.get(i);
				try{
					Date contractTime = (Date) setup.get("contractTime");
					Integer merchantId = (Integer) setup.get("merchant_id");
					
					if(beginDate!=null){
						begin = DateUtils.getSecond(beginDate); //手动传入开始时间, 0点
						contractTime = beginDate;
					}else{
						begin = DateUtils.getSecond(contractTime); //签约的时间, 0点
					}
					end = DateUtils.getSecond(endDate); //结束时间, 0点
					long days = (end-begin)/86400;  //开始时间到结束时间之间的天数
					
					//先清空，避免二次运行
					this.deleteMerchantRebate(contractTime, DateUtils.getYesterday(), merchantId);
					this.deleteOrderMerchantRebate(begin, end, merchantId);
					
					for(int j=0;j<days;j++){
						//统计每一天
						this.statMerchantRebate(begin, begin+86400, merchantId);
						begin = begin+86400;
					}
				}catch(Exception e){
					logger.info("计算一个时间段商家返点出错：商家ID=" + setup.get("merchant_id"));
					logger.info(e.getMessage());
				}
			}
		}
	}
	
	//统计签约商家每天提成(步骤2)
	public void statMerchantRebate(long begin, long end, Integer merchantId){
		logger.info("》》》》统计签约商家每天提成，参数：" + begin + ", " + end + ", " + merchantId);
		Date countDate = DateUtils.getDate(begin*1000l);
		//按商家得到首单
		List<Map<String, Object>> firstOriginList = this.getFirstOriginByMerchant(begin, end, merchantId);
		Map<Integer, Double> originSumMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orderCountMap = new HashMap<Integer, Integer>();
		
		if(firstOriginList!=null && firstOriginList.size()>0){
			for(int i=0;i<firstOriginList.size();i++){
				Map<String, Object> firstOrigin = firstOriginList.get(i);
				try{
					Integer orderId = Integer.parseInt(String.valueOf(firstOrigin.get("id")));
					Double origin = Double.valueOf(String.valueOf(firstOrigin.get("origin")));
					logger.info("getFirstOriginByMerchant():商家ID="+merchantId+"，首单订单ID="+orderId+"，首单实付金额="+origin);
					
					//按门店类型，判断该单是否超过订单规定限额
					Map<String, Object> category = this.getCategoryHighMoney(merchantId);
					if(category!=null){
						Double highMoney = (Double) category.get("high_money");
						if(highMoney!=0 && origin>=highMoney){
							//首单金额大于或等于门店类型限额，都不返点
							continue;
						}else{
							if(origin>=1000){  //其他类别限额1000
								continue;
							}
							if(originSumMap.containsKey(merchantId)){
								Double originSum = originSumMap.get(merchantId);
								originSumMap.put(merchantId, originSum + origin); //总额
								
								Integer orderCount = orderCountMap.get(merchantId);
								orderCountMap.put(merchantId, orderCount + 1); //总订单数
								
							}else{
								originSumMap.put(merchantId, origin);
								orderCountMap.put(merchantId, 1);
							}
						}
					}
				}catch(Exception e){
					logger.info("计算一个时间段商家返点出错：商家ID=" + firstOrigin.get("merchantId"));
					logger.info(e.getMessage());
				}
			}
			
			if(originSumMap !=null && originSumMap.size()>0){
				Iterator<Integer> keys = originSumMap.keySet().iterator();
				while(keys.hasNext()){
					merchantId = keys.next();
					Double originSum = originSumMap.get(merchantId);
					Integer orderCount = orderCountMap.get(merchantId);
					
					//得到返点率并计算
					Map<String, Object> setup = this.getRebateSetup(merchantId, countDate);
					if(setup==null){
						continue;
					}
					BigInteger rebateSetupId = (BigInteger) setup.get("id");
					Double rebateRate = (Double) setup.get("rebate_rate");
					Double highMoney = (Double) setup.get("high_money");
					
					MerchantRebateEntity mrEntity = new MerchantRebateEntity();
					mrEntity.setCountDate(countDate);  //统计日期
					mrEntity.setOrderCount(orderCount.intValue());	//订单数
					mrEntity.setOriginSum(originSum);	//订单金额
					mrEntity.setRebateSum(originSum*rebateRate/100 > highMoney ? highMoney : originSum*rebateRate/100);	//奖励金额
					mrEntity.setRebateSetupId(rebateSetupId.intValue());	//返点设置ID
					//mrEntity.setGrantTime(DateUtils.str2Timestamp("1700-01-01")); //默认发放时间
					mrEntity.setGrantState(0);	//未发放
					mrEntity.setMerchantId(merchantId); //商家ID
					mrEntity.setRebateRate(rebateRate); //返点率
					//保存商家日统计
					save(mrEntity);
					
					//新增明细
					this.addOrderMerchantRebate(mrEntity.getId(), begin, end, merchantId);
				}
			}
		}
	}
	
	//得到订单均价最高限额
	public Map<String, Object> getCategoryHighMoney(Integer merchantId){
		logger.info("》》》》得到订单均价最高限额，参数：" + merchantId);
		StringBuilder sql = new StringBuilder();
		sql.append(" select ca.high_money ");
		sql.append(" from merchant m, category ca where m.group_id = ca.id and m.id = ? ");
		return findOneForJdbc(sql.toString(), merchantId);
	}
	
	
	//清空签约时间到运行接口时间之间的商家提成
	public void deleteMerchantRebate(Date begin, Date end, Integer merchantId){
		logger.info("》》》》清空签约时间到运行接口时间之间的商家提成，参数：" + begin + ", " + end + ", " + merchantId);
		StringBuilder sql = new StringBuilder();
		sql.append("delete from 0085_merchant_rebate where count_date>=? and count_date<=? and merchant_id=? ");
		executeSql(sql.toString(), begin, end, merchantId);
	}
	
	//清空签约时间到运行接口时间之间的商家返点明细
	public void deleteOrderMerchantRebate(Long begin, Long end, Integer merchantId){
		logger.info("》》》》清空签约时间到运行接口时间之间的商家返点明细，参数：" + begin + ", " + end + ", " + merchantId);
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from 0085_order_rebate where order_id in ( ");
		sql.append(" 	select o.id from  ");
		sql.append(" 	( ");
		sql.append(" 		select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 		where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 		and pay_time >= ? and pay_time < ? ");
		sql.append(" 		and merchant_id = ? ");
		sql.append(" 		ORDER BY pay_time ");
		sql.append(" 	) o ");
		sql.append(" 	group by o.merchant_id, o.user_id ");
		sql.append(" ) ");
		executeSql(sql.toString(), begin, end, merchantId);
	}
	
	//得到返点设置
	public List<Map<String, Object>> getAllRebateSetup(Integer merchant_Id){
		logger.info("》》》》得到返点设置，参数：" + merchant_Id);
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT merchant_id, date(contract_time) contractTime  from 0085_rebate_setup ");
		if(merchant_Id!=null){
			sql.append(" where merchant_id = " + merchant_Id);
		}
		return findForJdbc(sql.toString());
	}
	
	//得到首单总额
	public List<Map<String, Object>> getFirstOrigin(Long begin, Long end){
		logger.info("》》》》得到首单总额，参数：" + begin + ", " + end);
		StringBuilder sql = new StringBuilder();
		//sql.append(" select oo.merchantId, sum(origin) originSum, count(id) orderCount from ");
		//sql.append(" ( ");
		sql.append(" 	select o.id, o.merchant_id merchantId, o.origin from ");
		sql.append(" 	( ");
		sql.append(" 		select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 		where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 		and pay_time > ? and pay_time <= ? ");
		sql.append(" 		ORDER BY pay_time ");
		sql.append(" 	) o ");
		sql.append(" 	group by o.merchant_id, o.user_id ");
		//sql.append(" ) oo ");
		//sql.append(" group by oo.merchant_id ");
		return findForJdbc(sql.toString(), begin, end);
	}
	
	//按商家得到首单总额
	public List<Map<String, Object>> getFirstOriginByMerchant(Long begin, Long end, Integer merchantId){
		logger.info("》》》》按商家得到首单总额，参数：" + begin + ", " + end + ", " + merchantId);
		StringBuilder sql = new StringBuilder();
		//sql.append(" select oo.merchantId, sum(origin) originSum, count(id) orderCount from ");
		//sql.append(" ( ");
		sql.append(" 	select o.id, o.merchant_id merchantId, o.origin from ");
		sql.append(" 	( ");
		sql.append(" 		select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 		where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 		and pay_time >= ? and pay_time < ? ");
		sql.append(" 		and merchant_id = ? ");
		sql.append(" 		ORDER BY pay_time ");
		sql.append(" 	) o ");
		sql.append(" 	group by o.merchant_id, o.user_id ");
		//sql.append(" ) oo ");
		//sql.append(" group by oo.merchant_id ");
		return findForJdbc(sql.toString(), begin, end, merchantId);
	}
	
	//得到返点设置
	public Map<String, Object> getRebateSetup(Integer merchantId, Date countDate){
		logger.info("》》》》得到返点设置，参数：" + merchantId + ", " + countDate);
		StringBuilder sql = new StringBuilder();
		sql.append(" select id, user_id, contract_time, rebate_rate, begin_time, end_time, high_money, is_advance_pay, pay_money, surplus_pay_money ");
		sql.append(" from 0085_rebate_setup where ?>=begin_time and ?<=end_time and merchant_id = ? ");
		return findOneForJdbc(sql.toString(), countDate, countDate, merchantId);
	}
	
	//新增商家返点明细
	public void addOrderMerchantRebate(Integer rebateId, Long begin, Long end, Integer merchantId){
		logger.info("》》》》得到返点设置，参数：" + rebateId + ", " + begin + ", " + end + ", " + merchantId);
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into 0085_order_rebate(order_id, rebate_id) ");
		sql.append(" select o.id, ? from  ");
		sql.append(" ( ");
		sql.append(" 	select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 	where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 	and pay_time >= ? and pay_time < ? ");
		sql.append(" 	and merchant_id = ? ");
		sql.append(" 	ORDER BY pay_time ");
		sql.append(" ) o ");
		sql.append(" group by o.merchant_id, o.user_id ");
		executeSql(sql.toString(), rebateId, begin, end, merchantId);
	}
	
	//更新商家每日统计发放状态和时间
	public void updateMerchantRebate(String begin, String end, Integer merchantId){ 
		logger.info("》》》》更新商家每日统计发放状态和时间，参数：" + begin + ", " + end + ", " + merchantId);
		StringBuilder sql = new StringBuilder();
		sql.append(" update 0085_merchant_rebate set grant_time = now(), grant_state=1 ");
		sql.append(" where count_date >= ? and count_date <= ? and merchant_id = ?");
		executeSql(sql.toString(), begin, end, merchantId);
	}
	
	/**
	 * 2、物流人员每天奖励计算，根据商家签约的快递员职位来判定：
	 * （1）他是普通业务员：金额1%（1个ID按首单计算提成），他的营长、团长：0.1元/单/ID/天（同1个ID同1个门店，只计算1个订单）
	 * （2）他是营长: 金额1%（1个ID按首单计算提成），他的团长：0.1元/单/ID/天（同1个ID同1个门店，只计算1个订单）
	 * （3）他是团长： 金额1%（1个ID按首单计算提成）
	 */
	@Override
	public void statCourierRebateByEveryday() {
		logger.info("》》》》统计签约快递员返点");
		Long begin = DateUtils.getYesterdayStartInSecond(); //(long) 1353651200;
		Long end = DateUtils.getTodayStartInSecond();
		Date countDate = DateUtils.getYesterday();
		
		this.commonStatCourier(begin, end, countDate);
	}
	
	/**
	 * 统计一段时间物流人员每日扫码订单奖励
	 * @param begin
	 * @param end
	 */
	@Override
	public void statCourierRebateByBefore(Date beginDate, Date endDate) {
		logger.info("》》》》统计一段时间物流人员每日扫码订单奖励，开始时间="+DateUtils.formatDate(beginDate) +"，结束时间="+DateUtils.formatDate(endDate));
		
		long begin = DateUtils.getSecond(beginDate); //开始时间
		long end = DateUtils.getSecond(endDate); //结束时间
		long days = (end-begin)/86400;  //天数
		
		//再分别统计每一天
		for(int j=0;j<days;j++){
			Date countDate = DateUtils.getDate(begin*1000l);
			this.commonStatCourier(begin, begin+86400, countDate);
			begin = begin+86400;
		}
	}
	
	//共用快递员计算
	public void commonStatCourier(long begin, long end, Date countDate){
		//按商家得到首单总额
		List<Map<String, Object>> firstOriginList = getCourierFirstOrigin(begin, end, countDate);
		if(firstOriginList!=null && firstOriginList.size()>0){
			for(int i=0;i<firstOriginList.size();i++){
				Map<String, Object> firstOrigin = firstOriginList.get(i);
				try{
					Integer userId = (Integer) firstOrigin.get("user_id");
					Double originSum = (Double) firstOrigin.get("originSum");
					BigDecimal orderCount = (BigDecimal) firstOrigin.get("orderCount");
					
					//计算提成
					Map<String, Object> position = getCourierPosition(userId);
					Integer positionId = (Integer) position.get("position_id");
					
					Integer courierId = 0;
					Double courierRebate = 0.00;
					Integer battalionId = 0;
					Double battalionRebate = 0.00;
					Integer delegationId = 0;
					Double delegationRebate = 0.00;
					if(positionId==2 || positionId==3){ //业务员
						courierId = userId;
						courierRebate = originSum * 0.01;
						/*//业务员找营长
						List<Map<String, Object>> battalionList = getCourierHead(courierId);
						if(battalionList!=null && battalionList.size()>0){
							Map<String, Object> battalion = battalionList.get(0);
							battalionId = (Integer) battalion.get("courier_id");
							battalionRebate = 0.1 * orderCount.intValue();
							
							//营长找团长
							List<Map<String, Object>> delegationList = getCourierHead(battalionId);
							if(delegationList!=null && delegationList.size()>0){
								Map<String, Object> delegation = delegationList.get(0);
								delegationId = (Integer) delegation.get("courier_id");
								delegationRebate = 0.1 * orderCount.intValue();
							}
						}*/
					}else if(positionId ==5){  //营长
						courierId = userId;
						courierRebate = originSum * 0.01;
						//营长找团长
						/*List<Map<String, Object>> delegationList = getCourierHead(courierId);
						if(delegationList!=null && delegationList.size()>0){
							Map<String, Object> delegation = delegationList.get(0);
							delegationId = (Integer) delegation.get("courier_id");
							delegationRebate = 0.1 * orderCount.intValue();
						}*/
					}else if(positionId ==7){	//团长
						courierId = userId;
						courierRebate = originSum * 0.01;
					}
					
					CourierRebateEntity crEntity = new CourierRebateEntity();
					crEntity.setCountDate(DateUtils.getYesterday());  //统计日期
					crEntity.setOrderCount(orderCount.intValue());	//订单数
					crEntity.setOriginSum(originSum);	//订单金额
					crEntity.setCourierId(courierId);	//业务员ID
					crEntity.setCourierRebate(courierRebate);	//业务员提成
					crEntity.setBattalionId(battalionId);	//营长ID
					crEntity.setBattalionRebate(battalionRebate);	//营长提成
					crEntity.setDelegationId(delegationId);	//团长ID
					crEntity.setDelegationRebate(delegationRebate);	//团长提成
					//crEntity.setGrantTime(DateUtils.str2Timestamp("1700-01-01")); //默认发放时间
					crEntity.setGrantState(0);	//未发放
					//保存商家日统计
					save(crEntity);
					
					//新增明细
					//addOrderCourierRebate(crEntity.getId(), begin, end, courierId);
					
					
				}catch(Exception e){
					logger.info("计算一个时间段快递员返点出错：商家ID=" + firstOrigin.get("user_id"));
					logger.info(e.getMessage());
				}
			}
		}
	}
	
	//得到签约人的职位
	public Map<String, Object> getCourierPosition(Integer courierId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select position_id from 0085_courier_position where courier_id = ? ");
		return findOneForJdbc(sql.toString(), courierId);
	}

	//得到快递员的领导
	public List<Map<String, Object>> getCourierHead(Integer courier){
		StringBuilder sql = new StringBuilder();
		sql.append(" select courier_id from 0085_courier_org co, 0085_org o ");
		sql.append(" where co.org_id = o.id and o.id =  ");
		sql.append(" (select o.pid from 0085_courier_org co, 0085_org o ");
		sql.append(" where co.org_id = o.id and co.courier_id = ? ) ");
		return findForJdbc(sql.toString(), courier);
	}
	
	//得到签约人的首单总额
	public List<Map<String, Object>> getCourierFirstOrigin(Long begin, Long end, Date countDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select rs.user_id, sum(ooo.originSum) originSum, sum(orderCount) orderCount from ( ");
		sql.append(" 	select oo.merchant_id, sum(origin) originSum, count(id) orderCount from ");
		sql.append(" 	( ");
		sql.append(" 		select o.id, o.merchant_id, o.origin from ");
		sql.append(" 		( ");
		sql.append(" 			select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 			where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 			and pay_time >= ? and pay_time < ? ");
		sql.append(" 			ORDER BY pay_time ");
		sql.append(" 		) o ");
		sql.append(" 		group by o.merchant_id, o.user_id ");
		sql.append(" 	) oo ");
		sql.append(" group by oo.merchant_id ");
		sql.append(" ) ooo, 0085_rebate_setup rs ");
		sql.append(" where ooo.merchant_id = rs.merchant_id and ?>=rs.begin_time and ?<=rs.end_time ");
		sql.append(" group by rs.user_id ");
		return findForJdbc(sql.toString(), begin, end, countDate, countDate);
	}
	
	//新增返点明细
	public void addOrderCourierRebate(Integer rebateId, Long begin, Long end, Integer courierId){
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into 0085_order_rebate(order_id, rebate_id) ");
		sql.append(" select oo.id, ? from (  ");
		sql.append("	select o.id, o.merchant_id from  ");
		sql.append(" 	( ");
		sql.append(" 		select id, user_id, merchant_id, online_money+credit origin from `order`  ");
		sql.append(" 		where pay_state ='pay' and order_type='scan_order' ");
		sql.append(" 		and pay_time >= ? and pay_time < ? ");
		sql.append(" 		ORDER BY pay_time ");
		sql.append(" 	) o ");
		sql.append(" 	group by o.merchant_id, o.user_id ");
		sql.append(" ) oo, 0085_rebate_setup rs ");
		sql.append(" where oo.merchant_id = rs.merchant_id and ?>=rs.begin_time and ?<=rs.end_time ");
		sql.append(" and rs.user_id = ? ");
		executeSql(sql.toString(), rebateId, begin, end, DateUtils.getYesterday(), DateUtils.getYesterday(), courierId);
	}
	
	//更新快递员发放状态和时间
	public void updateCourierRebate(String begin, String end){
		logger.info("》》》》更新快递员发放状态和时间，参数：" + begin + ", " + end);
		StringBuilder sql = new StringBuilder();
		sql.append(" update 0085_courier_rebate set grant_time = now(), grant_state=1 ");
		sql.append(" where count_date >= ? and count_date <= ? ");
		executeSql(sql.toString(), begin, end);
	}
		
	/**
	 * 发放商家提成(如果不在1号、16号执行不会对数据库作任何修改)
	 */
	@Override
	public void payMerchantRebate(){
		logger.info("invoke method payMerchantRebate without param");
		this.payMerchantRebate(null,null);
	}
	
	@Override
	public void payMerchantRebate(Date date, Integer mId){
		logger.info("invoke method payMerchantRebate, params:"+date + ", " + mId);
		String startDate = null;
		String endDate = null;
		
		Calendar calendar = Calendar.getInstance();
		/**
		 * 如果date为空则认为该方法是定时器调用的
		 * 当时不是1号就是16号
		 * calendar实例的时间不用修改
		 */
		if(date != null){
			calendar.setTime(date);
		}
		
		/**
		 * 判断传入的日期是几号
		 * 1号：统计上个月16号-月末的扫码订单
		 * 16号：统计当月1-15号的扫码订单
		 */
		if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
			startDate = DateUtils.getThe16thDayInLastMonth(date);
			endDate = DateUtils.getTheLastDayInLastMonth(date);
		}else if(calendar.get(Calendar.DAY_OF_MONTH) == 16){
			startDate = DateUtils.getTheFirstDayInThisMonth(date);
			endDate = DateUtils.getThe15thDayInThisMonth(date);
		}else{
			return;
		}
		
		/**
		 * 得到所有配置过返点的商家及总返点额
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(SUM(mr.rebate_sum),0) totalRebate, rs.merchant_id, rs.is_advance_pay, rs.surplus_pay_money ");
		sql.append(" FROM (select merchant_id, is_advance_pay, surplus_pay_money from 0085_rebate_setup GROUP BY merchant_id order by begin_time) rs ");
		sql.append(" LEFT JOIN (select rebate_sum, merchant_id from 0085_merchant_rebate where count_date>=? AND count_date<=? AND grant_state=0) mr on rs.merchant_id = mr.merchant_id ");
		sql.append(" LEFT JOIN merchant m on m.id = rs.merchant_id ");
		if(mId != null){
			sql.append("WHERE rs.merchant_id = ");
			sql.append(mId);
		}
		sql.append(" GROUP BY rs.merchant_id  ");
		List<Map<String, Object>> resultList = this.findForJdbc(sql.toString(), startDate, endDate);
		logger.info("SQL: " + sql.toString());
		
		/**
		 * 发放提成
		 */
		for(Map<String, Object> m: resultList){
			Integer merchantId = ((Integer)m.get("merchant_id")).intValue();
			Double rebateMoney = this.roundHalfUp(m.get("totalRebate").toString());  //返点金额
			Boolean isAdvancePay = (Boolean) m.get("is_advance_pay");  //是否有预付金
			Double surplusPayMoney = (Double) m.get("surplus_pay_money");	//预付金剩余多少
			Double actualMoney = rebateMoney;
			
			//防止重复新增明细
			List<Map<String, Object>> list = getRebateGrant(merchantId, calendar.getTime());
			if(list!=null && list.size()>0){
				logger.info("商家存在重复明细：商家ID="+merchantId+",统计时间="+calendar.getTime());
				continue;
			}
			
			//新增发放记录明细
			MerchantRebateGrantEntity mrgEntity = new MerchantRebateGrantEntity();
			mrgEntity.setBeforeMoney(surplusPayMoney);	//抵扣前金额
			if(isAdvancePay){
				//如果返点金额   大于   待扣余额
				if(rebateMoney >= surplusPayMoney){
					actualMoney = rebateMoney - surplusPayMoney;
					surplusPayMoney = 0.00;
				}else{
					surplusPayMoney = surplusPayMoney - rebateMoney;
					actualMoney = 0.00;
				}
			}
			mrgEntity.setRebateMoney(rebateMoney);  //返点金额
			mrgEntity.setAfterMoney(surplusPayMoney);	//抵扣后金额
			mrgEntity.setActualMoney(actualMoney);	//实际发放金额
			mrgEntity.setMerchantId(merchantId);	//商家ID
			mrgEntity.setStatDate(DateUtils.getCalendarTimestamp(calendar)); //统计发放明细时间
			mrgEntity.setGrantState(0);	//发放状态:未发放
			save(mrgEntity);   //保存发放明细记录
			
			//更新待扣余额
			this.updateRebateSetup(merchantId, surplusPayMoney);	
			
			//商家不自动打钱，变为人工打钱
			//this.flowService.userRebateIncome(userId, rebateMoney, "merchantRebate");
			
			//0085_merchant_rebate表，更新成“已发放”状态
			//this.updateMerchantRebate(startDate, endDate, merchantId);
		}
	}
	
	/**
	 * 人工发放
	 */
	@Override
	public AjaxJson manualGrant(Integer grantId){
		logger.info("》》》》执行人工发放，参数：" + grantId);
		AjaxJson aj = new AjaxJson();
		aj.setSuccess(true);
		aj.setMsg("发放奖励成功");
		
		String startDate = null;
		String endDate = null;
		
		MerchantRebateGrantEntity mrgEntity = get(MerchantRebateGrantEntity.class, grantId);
		if(mrgEntity !=null && mrgEntity.getGrantState()==0){
			Integer merchantId = mrgEntity.getMerchantId();	//商家ID
			Double rebateMoney = mrgEntity.getActualMoney(); //实际发放金额
			//Timestamp grantTime = mrgEntity.getGrantTime();	//发放时间
			Date statTime = mrgEntity.getStatDate(); //统计时间
			//Double surplusPayMoney = mrgEntity.getAfterMoney();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(statTime);
			
			/**
			 * 确定0085_merchant_rebate中要被修改状态的记录
			 */
			if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
				startDate = DateUtils.getThe16thDayInLastMonth(calendar.getTime());
				endDate = DateUtils.getTheLastDayInLastMonth(calendar.getTime());
			}else if(calendar.get(Calendar.DAY_OF_MONTH) == 16){
				startDate = DateUtils.getTheFirstDayInThisMonth(calendar.getTime());
				endDate = DateUtils.getThe15thDayInThisMonth(calendar.getTime());
			}else{
				aj.setStateCode("01");
				aj.setSuccess(false);
				aj.setMsg("发放奖励失败，发放表中存在异常数据");
				return aj;
			}
			
			Map<String, Object> merchant = getMerchant(merchantId);
			if(merchant != null){
				try{
					Long userId = (Long) merchant.get("user_id");
					Integer platformType = (Integer) merchant.get("platform_type");
					
					//打钱给商家余额
					this.flowService.userRebateIncome(userId.intValue(), rebateMoney, "merchantRebate");
					
					//如果是代理商商家，从代理商余额那里扣除
					if(platformType == 2){
						Map<String, Object> agent = this.getAgentUser(userId.intValue());
						Integer agentUserId = (Integer) agent.get("creator");
						if(agentUserId != 0){
							this.flowService.agentMerchantRebateIncome(agentUserId, -rebateMoney);
						}
					}
					
					//0085_merchant_rebate表，更新成“已发放”状态
					this.updateMerchantRebate(startDate, endDate, merchantId);
					
					//更新发放状态
					this.updateMerchantRebateGrant(grantId);
				}catch(Exception e){
					logger.info(e.getMessage());
					aj.setStateCode("01");
					aj.setSuccess(false);
					aj.setMsg("发放奖励失败，系统错误");
				}
				
			}else{
				aj.setStateCode("01");
				aj.setSuccess(false);
				aj.setMsg("发放奖励失败，找不到该商家");
			}
		}else{
			aj.setStateCode("02");
			aj.setSuccess(false);
			aj.setMsg("发放奖励失败，找不到该商家返点明细或已发放");
		}
		return aj;
	}
	
	//查询商家的上级代理
	public Map<String, Object> getAgentUser(Integer userId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select creator from user where id = ? ");
		return findOneForJdbc(sql.toString(), userId);
	}
	
	//更新商家发放状态和时间
	public void updateMerchantRebateGrant(Integer grantId){ 
		logger.info("》》》》更新商家发放状态和时间，参数：" + grantId);
		StringBuilder sql = new StringBuilder();
		sql.append(" update 0085_merchant_rebate_grant set grant_time = now(), grant_state=1 ");
		sql.append(" where id = ?");
		executeSql(sql.toString(), grantId);
	}
	
	//防止重复生成明细ld
	public List<Map<String, Object>> getRebateGrant(Integer merchantId, Date statDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from 0085_merchant_rebate_grant where merchant_id = ? and stat_date = date(?) ");
		return findForJdbc(sql.toString(), merchantId, statDate);
	}
	
	//通过商家ID 得到 用户ID
	public Map<String, Object> getMerchant(Integer merchantId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select m.user_id, mi.platform_type from merchant m, 0085_merchant_info mi where m.id = mi.merchant_id and m.id = ? ");
		return findOneForJdbc(sql.toString(), merchantId);
	}
	
	//更新商家返点配置表
	public void updateRebateSetup(Integer merchantId, Double surplusPayMoney){
		StringBuilder sql = new StringBuilder();
		sql.append(" update 0085_rebate_setup set surplus_pay_money = ? ");
		sql.append(" where merchant_id = ? ");
		executeSql(sql.toString(), surplusPayMoney, merchantId);
	}

	/**
	 * 发放快递员提成
	 * @throws Exception 
	 */
	@Override
	public void payCourierRebate() throws Exception {
		logger.info("invoke method payCourierRebate without param");
		String startDate = null;
		String endDate = null;
		
		startDate = DateUtils.getTheFirstDayInLastMonth();
		endDate = DateUtils.getTheLastDayInLastMonth();
		
		/**
		 * 查询提成信息
		 */
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cr.courier_id courierId, SUM(cr.courier_rebate) courierRebate, cr.battalion_id battalionId, SUM(cr.battalion_rebate) battalionRebate ");
		sql.append(", cr.delegation_id delegationId, SUM(cr.delegation_rebate) delegationRebate ");
		sql.append("FROM 0085_courier_rebate cr ");
		sql.append("WHERE cr.count_date>=? AND cr.count_date<=? AND cr.grant_state=0 ");
		sql.append("GROUP BY cr.courier_id ");
		List<Map<String, Object>> resultList = this.findForJdbc(sql.toString(), startDate, endDate);
		
		/**
		 * 发放提成
		 */
		for(Map<String, Object> m: resultList){
			/**
			 * 由于0085_courier_rebate表中，只有时间字段可以为空
			 * 所以此处不做null判断
			 */
			Integer courierId = (Integer)m.get("courierId");				//快递员ID
			Integer battalionId = (Integer)m.get("battalionId");			//营长ID
			Integer delegationId = (Integer)m.get("delegationId");			//团长ID
			Double courierRebate = this.roundHalfUp(m.get("courierRebate").toString());		//快递员提成
			Double battalionRebate = this.roundHalfUp(m.get("battalionRebate").toString());	//营长提成
			Double delegationRebate = this.roundHalfUp(m.get("delegationRebate").toString());	//团长提成
			this.flowService.userRebateIncome(courierId, courierRebate, "courierRebate");
			this.flowService.userRebateIncome(battalionId, battalionRebate, "courierRebate");
			this.flowService.userRebateIncome(delegationId, delegationRebate, "courierRebate");
		}
		
		/**
		 * 更新状态
		 */
		this.updateCourierRebate(startDate, endDate);
	}
	
	/**
	 * 四舍五入保留2位小数
	 * @param value	目标操作数(使用String类型的入参，能防止参数在传入时就发生漂移导致后续操作出现误差)
	 * @return	返回一个Double类型的数(精确到两位小数)
	 */
	private Double roundHalfUp(String value){
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
