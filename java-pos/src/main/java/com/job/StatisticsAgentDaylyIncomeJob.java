package com.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.OrderServiceI;

public class StatisticsAgentDaylyIncomeJob extends QuartzJobBean  {
	
	private static final Logger logger = Logger.getLogger(StatisticsAgentDaylyIncomeJob.class);
	
	@Autowired
	private OrderServiceI orderService;

	@Override
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		logger.info("定时器统计代理商返点扣点日收入！");
		String sql = "SELECT user_id FROM agent_income_timer_history "
				+ "WHERE FROM_UNIXTIME(create_time,'%Y-%m-%d')=DATE_SUB(CURDATE(),INTERVAL 1 DAY) "
				+ "GROUP BY user_id";
		List<Map<String, Object>> list = orderService.findForJdbc(sql);
		logger.info("查询共    "+list.size()+" 个代理商需进行统计 ");
		int i = 0;
		sql = "SELECT SUM(income) sum,FROM_UNIXTIME(create_time,'%Y-%m-%d') date,order_id  "
				+ "FROM agent_income_timer_history "
				+ "WHERE user_id=? "
				+ "AND pay_state='pay' "
				+ "AND type=? "
				+ "AND FROM_UNIXTIME(create_time,'%Y-%m-%d')=DATE_SUB(CURDATE(),INTERVAL 1 DAY)";
		if(list!=null && list.size()>0){
			for(Map<String, Object> map : list){
				Integer userId = Integer.valueOf(map.get("user_id").toString());
				WUserEntity user = orderService.get(WUserEntity.class, userId);
				if(user!=null){
					AgentInfoEntity agentInfo = orderService.findUniqueByProperty(AgentInfoEntity.class, "userId", userId);
					if(agentInfo!=null){
						//返点统计日收入
						Map<String, Object> m = orderService.findOneForJdbc(sql, userId, 1);//结算类型   1 返点   2 扣点
						Map<String, Object> ma = orderService.findOneForJdbc(sql, userId, 2);//结算类型   1 返点   2 扣点
						if(m!=null && ma!=null && m.size()>0 && ma.size()>0){
							//统计代理商日返点收入
							addDayAgentIncome(m, userId, agentInfo);
							//扣点统计日收入
							int result = addDayAgentPoints(ma, agentInfo.getId());
							if(result==0){
								logger.error("用户userId:["+userId+"]统计扣点收入失败");
							}
							i++;
						}
					}else{
						logger.error("找不到 userId:[" + userId + "]的代理商信息");
						continue ;
					}
				}else{
					logger.error("代理商统计日返点扣点收入，用户userId:["+userId+"]未找到相关用户信息！");
					continue ;
				}
			}
			logger.info("共成功统计  "+ i +" 个代理商的扣点返点");
		}
		
	}

	/**
	 * 统计代理商日返点收入
	 * @param map
	 * @param userId
	 * @param agentInfo
	 */
	private void addDayAgentIncome(Map<String, Object> map, Integer userId, AgentInfoEntity agentInfo){
		String sql = "insert into agent_income(tdate,org_name,attract_name,business_telephone,username,user_id,income,p_path) "
				+ "value(?,?,?,?,?,?,?,?) ";
		String tdate = map.get("date").toString();
		String attractName = agentInfo.getAttractName();
		String businessTelephone = agentInfo.getBusinessTelephone();
		Double sum = Double.valueOf(map.get("sum").toString());
		Double inc = sum*100;//1元=100
		Integer income = inc.intValue();
		String pPath = agentInfo.getpPath();
		WUserEntity user = orderService.get(WUserEntity.class, userId);
		String userName = user.getNickname();
		orderService.executeSql(sql, tdate, null, attractName, businessTelephone, userName, userId, income, pPath);
	}

	/**
	 * 统计代理商日扣点收入
	 * @param map
	 * @param agentId
	 * @return
	 */
	private int addDayAgentPoints(Map<String, Object> map, Integer agentId){
		String sql = "insert into agent_points(tdate,merchant_name,merchant_id,username,usertype,telephone,total_price,income,agent) "
				+ "value(?,?,?,?,?,?,?,?,?) ";
		String tdate = map.get("date").toString();
		Integer orderId = Integer.valueOf(map.get("order_id").toString());
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		if(order==null){
			logger.error("无法找到orderId:["+orderId+"]的订单信息");
			return 0;
		}
		Double totalPrice = order.getOnlineMoney()+order.getCredit();
		MerchantEntity merchant = order.getMerchant();
		if(merchant==null){
			logger.error("无法找到订单orderId:["+orderId+"]对应的商家信息");
			return 0;
		}
		String title = merchant.getTitle();
		String userType = merchant.getType();
		String mobile = merchant.getMobile();
		WUserEntity muser = merchant.getWuser();
		if(muser==null){
			logger.error("无法找到商家merchantId:["+merchant.getId()+"]对应的用户信息");
			return 0;
		}
		String userName = muser.getUsername();
		Integer merchantId = merchant.getId();
		
		String income = map.get("sum").toString();
		int result = orderService.executeSql(sql, tdate, title, merchantId, userName, userType, mobile, totalPrice, income, agentId);
		return result;
	}


}
