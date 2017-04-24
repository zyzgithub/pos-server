package com.wm.service.impl.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.common.Constants;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.statistics.CourierStatisticsDalyEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.comment.CommentServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.deduct.DeductServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.statistics.CourierStatisticsDalyServiceI;
import com.wm.service.statistics.OrgStatisticsDaylyServiceI;
import com.wm.service.user.WUserServiceI;

@Service("courierStatisticsDalyService")
@Transactional
public class CourierStatisticsDalyServiceImpl extends CommonServiceImpl implements CourierStatisticsDalyServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(CourierStatisticsDalyServiceImpl.class);
	@Autowired
	WUserServiceI wUserService;
	
	@Autowired
	OrgServiceI orgService;
	@Autowired
	OrderServiceI orderService;
	@Autowired
	CommentServiceI commentService;
	@Autowired
	private DeductServiceI deductService;
	@Autowired
	private DeductLogServiceI deductLogService;
	@Autowired
	private CourierServiceI courierService;
	@Autowired
	private OrgStatisticsDaylyServiceI orgStatisticsDaylyService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	public void updateAgentBond(Integer agentId, int money){
		executeSql("update agent_info a set a.bond=a.bond-? where  a.user_id = ?", money, agentId);
	}
	
	public void statisticsDayly(Integer courierId) throws Exception {
		DateTime deductDate = DateTime.now().minusDays(1);
		
		//众包提成（单位:分）
		Long crowdSourceDeduct = orderService.getCrowdsourcingDeduct(courierId, deductDate.toString("yyyy-MM-dd"));		
		Integer crowdSourceDeductQuantity = orderService.getCrowdsourcingQuantity(courierId, deductDate.toString("yyyy-MM-dd"));
		if(crowdSourceDeductQuantity.intValue() > 0){
			DeductLogEntity crowdSourceDeductLog = new DeductLogEntity();
			crowdSourceDeductLog.setCourierId(courierId);
			crowdSourceDeductLog.setDeduct(crowdSourceDeduct.doubleValue()/100);
			crowdSourceDeductLog.setQuantity(crowdSourceDeductQuantity);
			crowdSourceDeductLog.setOrders(crowdSourceDeductQuantity.doubleValue());
			crowdSourceDeductLog.setDeductType(Constants.CROWDSOURCING_DEDUCT);
			crowdSourceDeductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
			crowdSourceDeductLog.setTotalDeduct(crowdSourceDeduct.doubleValue()/100);
			deductService.saveAndUpdateMoney(crowdSourceDeductLog);
		}
		
		//普通单区分快递员类型和订单来源
		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		logger.info("获取快递员{}的类型为{}", courierId, courierType);
		List<Map<String, Object>> deductRule = new ArrayList<Map<String,Object>>();
		if(courierType != null){
			if(Constants.COURIER_CROWDSOURING.equals(courierType)){
				List<Map<String, Object>> list = orderService.getMerchantTypeAndOrderQuantityBycourierId(courierId, deductDate.toString("yyyy-MM-dd"));
				//总订单数
				Integer orders = 0;
				//平台订单数
				Integer platformOrders = 0;
				Double deduct = 0.00;
				deductRule = deductService.getCourierDeductRule(courierId, 0, courierType);
				if(CollectionUtils.isNotEmpty(list)){
					for(Map<String, Object> map : list){
						Integer creator = Integer.parseInt(map.get("creator").toString());
						Double mapOrders = Double.parseDouble(map.get("orders").toString());
						if(!creator.equals(0)){
							DeductLogEntity deductLog = new DeductLogEntity();
							String userType = findOneForJdbc("select user_type userType from `user` where id = ?", String.class, creator);
							//如果订单商家的创建者是代理商
							if(userType.equals("agent")){
								deductLog.setCourierId(courierId);
								deductLog = deductLogService.getCrowdsouringCourierEverydayDeduct(deductRule, mapOrders.intValue(), deductLog);
								deductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
								deductLog.setAgentId(creator);
								deductService.saveAndUpdateMoney(deductLog);
								
								updateAgentBond(creator, (int)(deductLog.getTotalDeduct()* 100));
								
								//获取代理商普通单的总单数 和 总提成
								deduct = deduct + deductLog.getTotalDeduct();
								orders = orders + mapOrders.intValue();
							}
						}
						platformOrders = platformOrders + mapOrders.intValue();
					}
					
					//如果众包快递员抢的是平台的订单
					DeductLogEntity deductLog = new DeductLogEntity();
					deductLog.setCourierId(courierId);
					deductLog = deductLogService.getCrowdsouringCourierEverydayDeduct(deductRule, platformOrders, deductLog);
					deductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
					deductLog.setAgentId(0);
					deductService.saveAndUpdateMoney(deductLog);
					
					//普通订单总提成 = 平台订单提成 + 代理商订单提成
					deduct = deduct + deductLog.getTotalDeduct();
					//普通总订单数  = 平台订单数 + 代理商订单数
					orders = orders + platformOrders;
					
					CourierStatisticsDalyEntity csde = new CourierStatisticsDalyEntity();
					csde.setCourierId(courierId);
					csde.setDalyTotalQuantity(crowdSourceDeductQuantity.intValue());
					csde.setDaylyTotalOrder(orders+crowdSourceDeductQuantity.intValue());
					csde.setDaylyTotalDeduct(deduct+crowdSourceDeduct/100);
			
					Integer[] commScore = commentService.getCourierDaylyCommentScore(courierId, deductDate.toString("yyyy-MM-dd"));
					csde.setDaylyTotalComment(commScore[0]);
					csde.setDaylyTotalCommentScore(commScore[1]);
					csde.setUpdateDate((int)(deductDate.getMillis()/1000));
					this.save(csde);
				}
			} else if (Constants.COURIER_TYPE_SUPPLY_DRIVER.equals(courierType) || Constants.COURIER_TYPE_SUPPLY_NORMAL.equals(courierType)){
				logger.info("统计供应链司机或者供应链快递员提成，courierId:{}", courierId);
				//普通订单提成规则，类型为4，入参就转换成41;5->51
				deductRule = deductService.getCourierDeductRule(courierId, 0, courierType * 10 + 1);
				Long yestodayMenus = 0L;
				Long yestodayOrders = orderService.getCourierNormalOrders(courierId, deductDate.toString("yyyy-MM-dd"));
				DeductLogEntity deductLog = new DeductLogEntity();
				deductLog.setCourierId(courierId);
				deductLog = deductLogService.getDeductByRule(deductRule, yestodayOrders.doubleValue(), deductLog);
				deductLog.setQuantity(yestodayMenus.intValue());
				deductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
				deductService.saveAndUpdateMoney(deductLog);
				
				
				//供应链订单提成规则
				deductRule = deductService.getCourierDeductRule(courierId, 0, courierType);
				//供应链订单总金额
				Double yestodayOrdersMoneys = deductService.getSupplyOrderMoney(courierId, deductDate.toString("yyyy-MM-dd"));

				DeductLogEntity deductLogSupply = new DeductLogEntity();
				deductLogSupply.setCourierId(courierId);
				deductLogSupply.setDeductType(courierType);
				deductLogSupply = deductLogService.getDeductByRule(deductRule, yestodayOrdersMoneys.doubleValue(), deductLogSupply);
				deductLogSupply.setQuantity(yestodayMenus.intValue());
				deductLogSupply.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
				deductService.saveAndUpdateMoney(deductLogSupply);
				
				CourierStatisticsDalyEntity csde = new CourierStatisticsDalyEntity();
				csde.setCourierId(courierId);
				csde.setDalyTotalQuantity(yestodayMenus.intValue()+crowdSourceDeductQuantity.intValue());
				csde.setDaylyTotalOrder(yestodayOrders.intValue()+crowdSourceDeductQuantity.intValue());
				csde.setDaylyTotalDeduct(deductLog.getTotalDeduct() + deductLogSupply.getTotalDeduct() + crowdSourceDeduct / 100);
		
				Integer[] commScore = commentService.getCourierDaylyCommentScore(courierId, deductDate.toString("yyyy-MM-dd"));
				csde.setDaylyTotalComment(commScore[0]);
				csde.setDaylyTotalCommentScore(commScore[1]);
				csde.setUpdateDate((int)(deductDate.getMillis()/1000));
				this.save(csde);
				
			} else {
				Integer agentId = courierInfoService.getCourierBindUserId(courierId);
				//合作商快递员
				if(Constants.COURIER_COOPERATE_BUSINESS.equals(courierType)){
					if(agentId !=null && agentId.intValue() != 0){
						deductRule = deductService.getCourierDeductRule(courierId, agentId, courierType);
					}
				}
				else {
					deductRule = deductService.getCourierDeductRule(courierId, 0, courierType);
				}
				//昨天可拿提成的送餐份数
				Long yestodayMenus = 0L;
				//昨天可拿提成的订单数(去除众包订单)
				Long yestodayOrders = orderService.getCourierNormalOrders(courierId, deductDate.toString("yyyy-MM-dd"));
				logger.info("courier: {} yestodayOrders : {}", courierId, yestodayOrders);
				// 新的提成方案，以订单数为提成条件，不再是计算送餐份数
				DeductLogEntity deductLog = new DeductLogEntity();
				deductLog.setCourierId(courierId);
				deductLog = deductLogService.getDeductByRule(deductRule, yestodayOrders.doubleValue(), deductLog);
				Double deduct = deductLog.getTotalDeduct();
				deductLog.setQuantity(yestodayMenus.intValue());
				deductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
				deductLog.setAgentId(agentId);
				
				//保存提成日志表时，把配送提成加到快递员的余额中
				deductService.saveAndUpdateMoney(deductLog);
				//合作商快递员
				if(Constants.COURIER_COOPERATE_BUSINESS.equals(courierType)){
					if(agentId !=null && agentId.intValue() != 0){
						updateAgentBond(agentId, (int)(deduct * 100));
					}
				}
				
				CourierStatisticsDalyEntity csde = new CourierStatisticsDalyEntity();
				csde.setCourierId(courierId);
				csde.setDalyTotalQuantity(yestodayMenus.intValue()+crowdSourceDeductQuantity.intValue());
				csde.setDaylyTotalOrder(yestodayOrders.intValue()+crowdSourceDeductQuantity.intValue());
				csde.setDaylyTotalDeduct(deduct+crowdSourceDeduct/100);
		
				Integer[] commScore = commentService.getCourierDaylyCommentScore(courierId, deductDate.toString("yyyy-MM-dd"));
				csde.setDaylyTotalComment(commScore[0]);
				csde.setDaylyTotalCommentScore(commScore[1]);
				csde.setUpdateDate((int)(deductDate.getMillis()/1000));
				this.save(csde);
			}
		} else {
			logger.warn("未知快递员{}类型{}", courierId, courierType);
		}
	}
	
	@Override
	public void statisticsMonthly(Integer leaderId, Integer subOrgId) throws Exception {
		Integer courierType = courierInfoService.getCourierTypeByUserId(leaderId);
		if(courierType == Constants.COURIER){
			DateTime preMonth = DateTime.now().minusMonths(1); // 上一个月
			DateTime firstDayOfPreMonth = preMonth.dayOfMonth().withMinimumValue();
			DateTime lastDayOfPreMonth = preMonth.dayOfMonth().withMaximumValue();
			
			Integer monthOrders = 0; // 月累积订单数
			Integer monthDays = 0; // 月累积天数
			Double orderPerDay = 0d; // 人均日订单
			
			// 统计子网点下的快递员月人均日订单
			String sql = "select id,org_id,dayly_total_order,dayly_total_courier,dayly_order,date(from_unixtime(update_date)) as updateDate "
					+ " from 0085_org_statistics_dayly where org_id=? and date(from_unixtime(update_date)) >= ? and date(from_unixtime(update_date)) <= ?";
			List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{subOrgId, firstDayOfPreMonth.toString("yyyy-MM-dd"), lastDayOfPreMonth.toString("yyyy-MM-dd")});
			if(list != null && list.size() > 0){
				for(Map<String, Object> map : list){
					Double daylyOrder = Double.parseDouble(map.get("dayly_order").toString()) * 100;
					String updateDate = map.get("updateDate").toString();
					Integer daylyOnDutyPerson = orgService.orgOnDutyPerson(subOrgId, updateDate);
					if(daylyOnDutyPerson > 0){
						monthDays ++;
						orderPerDay += daylyOrder/daylyOnDutyPerson;
						logger.warn("【" + updateDate + "】该网点【" + subOrgId + "】共【" + daylyOnDutyPerson + "】人上班");
					} else {
						logger.warn("【" + updateDate + "】该网点【" + subOrgId + "】无人上班");
					}
					monthOrders += Integer.parseInt(map.get("dayly_total_order").toString());
				}
			} else {
				logger.warn("未找到该子网点【" + subOrgId + "】上个月的提成记录！");
			}
			if(monthDays > 0){
				orderPerDay = orderPerDay/monthDays;
				orderPerDay /= 100;
			} else {
				orderPerDay = 0d;
				logger.warn("该网点【" + subOrgId + "】本月无人上班");
			}
			// 根据提成规则计算该子网点能拿提成
			List<Map<String, Object>> deductRule = deductService.getCourierDeductRule(leaderId, 0, 1);
			Double deduct = deductLogService.getDeductByRule(deductRule, orderPerDay, monthOrders);
			logger.info("一级子网点提成：管理层【" + leaderId + "】，子网点月总订单量【" + monthOrders + 
					"】，月工作日【" + monthDays + "】，月人均日订单量【" + orderPerDay + "】，提成【" + deduct + "】");
			DeductLogEntity deductLog = new DeductLogEntity();
			deductLog.setCourierId(leaderId);
			deductLog.setDeduct(deduct);
			deductLog.setQuantity(0);
			deductLog.setOrders(monthOrders.doubleValue());
			deductLog.setDeductType(2);
			deductLog.setAccountDate(Integer.parseInt(preMonth.getMillis()/1000+""));
			//保存提成日志表时，把配送提成加到快递员的余额中
			deductService.saveAndUpdateMoney(deductLog);
		}
	}

	@Override
	public void statisticsLeaderDayly(Integer leaderId) throws Exception {
		Integer courierType = courierInfoService.getCourierTypeByUserId(leaderId);
		if(courierType == Constants.COURIER){
			DateTime deductDate = DateTime.now().minusDays(1);
			Integer totalOrder = 0;
			List<Map<String, Object>> courierOrgs = courierService.getCourierOrg(leaderId);
			if(courierOrgs != null && courierOrgs.size() > 0){
				Map<String, Object> courierOrgMap = courierOrgs.get(0);
				Integer courierOrg = Integer.parseInt(courierOrgMap.get("id").toString());
				List<OrgEntity> orgList = orgService.loadAll(OrgEntity.class);
				List<OrgEntity> childOrgs = orgService.findChildOrgList(orgList, courierOrg);
				if(childOrgs != null && childOrgs.size() > 0){
					for(OrgEntity org : childOrgs){
						Map<String, Object> map = orgStatisticsDaylyService.getYestodaySta(org.getId(), deductDate.toString("yyyy-MM-dd"));
						if(map != null){
							totalOrder += Integer.parseInt(map.get("dayly_total_order").toString());
						} else {
							logger.warn("未找到管理层快递员【" + leaderId + "】管辖的子网点【" + org.getId() + "】昨天的订单统计");
						}
					}
				} else {
					logger.warn("未找到管理层快递员【" + leaderId + "】管辖的子网点");
				}
			} else {
				logger.warn("未找到管理层快递员【" + leaderId + "】所属组织架构");
			}
			List<Map<String, Object>> deductRule = deductService.getCourierDeductRule(leaderId, 0, 1);
			DeductLogEntity deductLog = new DeductLogEntity();
			deductLog.setCourierId(leaderId);
			deductLog.setDeductType(1);
			deductLog = deductLogService.getDeductByRule(deductRule, totalOrder.doubleValue(), deductLog);
			deductLog.setAccountDate(Integer.parseInt(deductDate.getMillis()/1000+""));
			deductService.saveAndUpdateMoney(deductLog);
		}
	}
	
	@Override
	public List<WUserEntity> getCouriersNeedToCalcDeduct(){
		// 统计、结算所有快递员日提成
		List<WUserEntity> allCouriers = wUserService.getAllServingCouriers();
				
		List<Integer> blackList = getCourierInBlacklist();
		
		List<WUserEntity> needToCalaDeductCouriersList = new ArrayList<WUserEntity>();
		//黑名单集合为空
		if(CollectionUtils.isEmpty(blackList)){
			needToCalaDeductCouriersList = allCouriers;
		}
		else{
			
			if(CollectionUtils.isNotEmpty(allCouriers)){
				//过滤黑名单中的快递员
				for(WUserEntity courier: allCouriers){
					if(!blackList.contains(courier.getId())){
						needToCalaDeductCouriersList.add(courier);
					}
				}
			}
		}
		logger.info("统计快递员提成，共有快递{}名, 黑名单中有{}名,本次统计:{}名", 
				new Object[]{allCouriers.size(), blackList.size(), needToCalaDeductCouriersList.size()});
		return needToCalaDeductCouriersList;
	}
	
	/**
	 * 获取在在黑名单中的快递员ID列表
	 * @return
	 */
	public List<Integer> getCourierInBlacklist(){
		List<Integer> courierIds = new ArrayList<Integer>();
		
		String sql = "select distinct courier_id from 0085_courier_deduct_blacklist" ;
		List<Map<String, Object>> list = findForJdbc(sql);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map: list){
				courierIds.add(Integer.parseInt(map.get("courier_id").toString()));
			}
		}
		
		return courierIds;
	}
	
}