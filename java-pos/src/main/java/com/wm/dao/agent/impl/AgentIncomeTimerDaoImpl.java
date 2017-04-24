package com.wm.dao.agent.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wm.dao.agent.AgentIncomeTimerDao;
import com.wm.dao.agent.AgentRebateDao;
import com.wm.entity.agent.AgentIncomeTimerEntity;
import com.wm.entity.agent.AgentIncomeTimerHistoryEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantDeductionEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchantinfo.MerchantDeductionServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 代理商返点扣点收入定时器表agent_income_timer，DAO实现
 */
@Repository("agentIncomeTimerDao")
@Transactional
public class AgentIncomeTimerDaoImpl extends GenericBaseCommonDao<AgentIncomeTimerEntity, Integer>
		implements AgentIncomeTimerDao {
	
	private final static Logger logger = LoggerFactory.getLogger(AgentIncomeTimerDaoImpl.class);
	
	
	@Autowired
	private WUserServiceI userService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private MerchantDeductionServiceI merchantDeductionService;
	@Autowired
	private AgentRebateDao agentRebateDao;
	

	@Override
	public int deleteByOrderId(Integer orderId) {
		String sql = "delete from agent_income_timer where order_id=?";
		return executeSql(sql, orderId);
	}

	@Override
	public List<Map<String, Object>> findAgentTimerList() {
		String sql = "SELECT * FROM agent_income_timer WHERE create_time<UNIX_TIMESTAMP(CURDATE()) AND pay_state='unpay' ORDER BY order_id";
//		String sql = "SELECT * FROM agent_income_timer ORDER BY order_id";//test
		return this.findForJdbc(sql);
	}

	@Override
	public List<Map<String, Object>> findAgentHisList(String date) {
		String sql = "SELECT user_id FROM agent_income_timer_history "
				+ "WHERE FROM_UNIXTIME(create_time,'%Y-%m-%d')= ? "
				+ "GROUP BY user_id";
		return this.findForJdbc(sql, date);
	}

	@Override
	public List<Map<String, Object>> findAgentHisSum(String date) {
		String sql = "SELECT SUM(income) sum,FROM_UNIXTIME(create_time,'%Y-%m-%d') date,sub_user_id  "
				+ "FROM agent_income_timer_history "
				+ "WHERE type=1 "
				+ "AND FROM_UNIXTIME(create_time,'%Y-%m-%d')= ? "
				+ "GROUP BY sub_user_id ";
		return this.findForJdbc(sql, date);
	}
	
	@Override
	public List<Map<String, Object>> findAgentPoints(String date){
		String sql = "SELECT SUM(a.order_money) orderSum,SUM(a.income) income,o.merchant_id,FROM_UNIXTIME(a.create_time,'%Y-%m-%d') date,a.user_id  "
				+ "FROM agent_income_timer_history a LEFT JOIN `order` o ON a.order_id=o.id "
				+ "WHERE FROM_UNIXTIME(a.create_time,'%Y-%m-%d')=? "
				+ "AND a.type=2 "
				+ "GROUP BY o.merchant_id ";
		return this.findForJdbc(sql, date);
	}

	@Override
	public void addDayAgentIncome(List<Map<String, Object>> list) {
		logger.info("应统计代理商分销收入共 ：  [  "+list.size()+" ]  条记录");
		int record = 0;
		if(list!=null && list.size()!=0){
			String tdate = "";
			Integer userId = 0;
			Integer result = 0;
			BigDecimal sum = BigDecimal.ZERO;
			AgentInfoEntity agentInfo = null;
			OrgEntity org = null;
			WUserEntity user = null;
			String sql = "insert into agent_income(tdate,org_name,attract_name,business_telephone,username,user_id,income,p_path) "
					+ "value(?,?,?,?,?,?,?,?) ";
			for(Map<String, Object> map : list){
				if(map==null || map.size()==0){
					logger.error("数据错误");
					continue ;
				}
				
				tdate = map.get("date").toString();
				userId = Integer.valueOf(map.get("sub_user_id").toString());
				result = userService.findOneForJdbc("SELECT COUNT(id) FROM agent_income WHERE tdate=? AND user_id=?", Integer.class, tdate, userId);
				if(result!=0){
					logger.info("代理商的子级代理商userId:{}已统计过date:{}的分销返点收入结算", userId, tdate);
					continue ;
				}
				agentInfo = userService.findUniqueByProperty(AgentInfoEntity.class, "userId", userId);
				if(agentInfo==null){
					logger.error("找不到userId:{}的代理商信息", userId);
					continue ;
				}
				org = userService.get(OrgEntity.class, agentInfo.getOrgId());
				
				sum = new BigDecimal(map.get("sum").toString()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
				user = userService.get(WUserEntity.class, userId);
				this.executeSql(sql, tdate, org==null?"":org.getOrgName(), agentInfo.getAttractName(), agentInfo.getBusinessTelephone(), 
						user==null?"":user.getUsername(), userId, sum.intValue(), agentInfo.getpPath());
				record++;
			}
		}
		logger.info("实际统计代理商分销收入共 ：  [  "+record+" ]  条记录");
	}

	@Override
	public void addDayAgentPoints(List<Map<String, Object>> list) {
		logger.info("应统计代理商直营收入共 ：  [  "+list.size()+" ]  条记录");
		int record = 0;
		if(list!=null && list.size()!=0){
			int incomeDate = 1;
			String tdate = "";
			Integer userId = 0;
			Integer merchantId = 0;
			Integer result = 0;
			BigDecimal rate = BigDecimal.ZERO;
			BigDecimal totalPrice = null;
			MerchantEntity merchant = null;
			AgentInfoEntity agentInfo = null;
			WUserEntity muser = null;
			MerchantInfoEntity merchantInfo = null;
			MerchantDeductionEntity merchantDeduction = null;
			String sql = "insert into agent_points(tdate,merchant_name,merchant_id,username,usertype,telephone,total_price,income,agent,rate,rate_type) "
					+ "value(?,?,?,?,?,?,?,?,?,?,?) ";
			for(Map<String, Object> map : list){
				if(map==null || map.size()==0){
					logger.error("数据错误");
					continue ;
				}
				tdate = map.get("date").toString();
				userId = Integer.valueOf(map.get("user_id").toString());
				merchantId = Integer.valueOf(map.get("merchant_id").toString());
				if(merchantId==null){
					logger.error("数据错误，userId:{}", userId);
					continue ;
				}
				result = userService.findOneForJdbc("SELECT COUNT(id) FROM agent_points WHERE tdate=? AND merchant_id=?", Integer.class, tdate, merchantId);
				if(result!=0){
					logger.info("商家merchantId:{}已统计过date:{}的直营返点收入结算", merchantId, tdate);
					continue ;
				}
				merchant = userService.get(MerchantEntity.class, merchantId);
				if(merchant==null){
					logger.error("无法找到商家merchantId:["+merchantId+"]信息");
					continue ;
				}
				agentInfo = userService.findUniqueByProperty(AgentInfoEntity.class, "userId", userId);
				if(agentInfo==null){
					logger.error("找不到userId:{}的代理商信息", userId);
					continue ;
				}
				muser = userService.get(WUserEntity.class, merchant.getWuser().getId());
				if (muser == null) {
					logger.error("无法找到商家merchantId:["+merchant.getId()+"]对应的用户信息");
					continue ;
				}
				
				merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 2);
				if(merchantDeduction!=null && merchantDeduction.getIncomeDate() != null){
					incomeDate = merchantDeduction.getIncomeDate();
				}else{
					incomeDate = 1;
				}
				
				merchantInfo = userService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
				rate = agentRebateDao.getAgentRebate(agentInfo, 1, incomeDate, merchantInfo.getDeductionType());
				totalPrice = new BigDecimal(map.get("orderSum").toString()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
				
				this.executeSql(sql, tdate, merchant.getTitle(), merchantId, muser.getUsername(), merchant.getType(), merchant.getMobile(), 
						totalPrice.intValue(), new BigDecimal(map.get("income").toString()).setScale(4, BigDecimal.ROUND_HALF_DOWN), agentInfo.getId(), rate, merchantInfo.getDeductionType());
				record++;
			}
		}
		logger.info("实际统计代理商直营收入共 ：  [  "+record+" ]  条记录");
	}

	/**
	 * 删除代理商定时器的记录
	 * @param id
	 * @return
	 */
	@Override
	public	int deleteAgentIncomeTimerRecord(Integer id){
		String sql = "DELETE FROM agent_income_timer WHERE id=?";
		return this.executeSql(sql, id);
	}

	@Override
	public void orderIncomeDayly(String date) {
		try {
			//返点统计日收入  结算类型type   1 分销返点收入   2 直营返点收入
			List<Map<String, Object>> incomeList = findAgentHisSum(date);// 分销返点收入
			List<Map<String, Object>> pointsList = findAgentPoints(date);// 直营返点收入统计
			logger.info("orderIncomeDayly total size:" + (incomeList.size()+pointsList.size()));
			addDayAgentIncome(incomeList);
			//统计代理商日直营返点收入
			addDayAgentPoints(pointsList);
		} catch (Exception e) {
			logger.error("统计代理商收入异常",  e);
		}
	}

	/**
	 * type 结算类型   1 分销返点   2 直营返点
	 */
	@Override
	public AgentIncomeTimerEntity getAgentIncomeTimerEntity(Integer orderId,
			Integer userId, Integer type) {
		String sql = "SELECT id FROM agent_income_timer WHERE order_id=? AND user_id=? AND type=?";
		Integer id = this.findOneForJdbc(sql, Integer.class, orderId, userId, type);
		if(id!=null){
			AgentIncomeTimerEntity ait = this.get(AgentIncomeTimerEntity.class, id);
			return ait;
		}else{
			return null;
		}
	}

	@Override
	public AgentIncomeTimerHistoryEntity getAgentIncomeTimerHistoryEntity(
			Integer orderId, Integer userId, Integer type) {
		String sql = "SELECT id FROM agent_income_timer_history WHERE order_id=? AND user_id=? AND type=?";
		Integer id = this.findOneForJdbc(sql, Integer.class, orderId, userId, type);
		if(id!=null){
			AgentIncomeTimerHistoryEntity aith = this.get(AgentIncomeTimerHistoryEntity.class, id);
			return aith;
		}else{
			return null;
		}
	}

}
