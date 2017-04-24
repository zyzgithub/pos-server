package com.wm.dao.agent;

import java.util.List;
import java.util.Map;
import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;
import com.wm.entity.agent.AgentIncomeTimerEntity;
import com.wm.entity.agent.AgentIncomeTimerHistoryEntity;

/**
 * 代理商返点扣点收入定时器表agent_income_timer，DAO接口
 */
public interface AgentIncomeTimerDao extends IGenericBaseCommonDao {

	/**
	 * 根据order_id删除对应的记录
	 * @param orderId
	 * @return 删除的记录数
	 */
	int deleteByOrderId(Integer orderId);

	/**
	 * 查询昨天的定时表 
	 * @return
	 */
	List<Map<String, Object>> findAgentTimerList();

	/**
	 * 查询昨天的历史定时表
	 * @return
	 */
	List<Map<String, Object>> findAgentHisList(String date);

	/**
	 * 返点统计日收入
	 * @param userId
	 * @param type 结算类型   1 返点   2 扣点
	 * @return
	 */
	List<Map<String, Object>> findAgentHisSum(String date);
	
	List<Map<String, Object>> findAgentPoints(String date);

	/**
	 * 代理商日返点收入
	 * @param map
	 * @param userId
	 * @param agentInfo
	 */
	void addDayAgentIncome(List<Map<String, Object>> list);

	/**
	 * 扣点统计日收入
	 * @param map
	 * @param agentId
	 * @return
	 */
	void addDayAgentPoints(List<Map<String, Object>> list);
	
	/**
	 * 根据主键删除AgentIncomeTimerEntity实体
	 * @param id 主键
	 * @return
	 */
	public	int deleteAgentIncomeTimerRecord(Integer id);
	
	public void orderIncomeDayly(String date);
	
	/**
	 * 
	 * @param orderId
	 * @param userId
	 * @param type
	 * @return
	 */
	public AgentIncomeTimerEntity getAgentIncomeTimerEntity(Integer orderId, Integer userId, Integer type);
	
	/**
	 * 
	 * @param orderId
	 * @param userId
	 * @param type  结算类型   1 分销返点   2 直营返点
	 * @return
	 */
	public AgentIncomeTimerHistoryEntity getAgentIncomeTimerHistoryEntity(Integer orderId, Integer userId, Integer type);
}
