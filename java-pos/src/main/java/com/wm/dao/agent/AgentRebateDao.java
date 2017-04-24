package com.wm.dao.agent;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.agent.AgentRebateEntity;
import com.wm.entity.merchant.AgentInfoEntity;

public interface AgentRebateDao extends IGenericBaseCommonDao {

	/**
	 * 获取AgentRebateEntity实体
	 * @param userId 用户id
	 * @param type 类型，1：T+1
	 * @return
	 */
	public AgentRebateEntity getAgentRebateEntity(Integer userId, String type, int incomeDate);
	
	/**
	 * 获取代理商返点值
	 * @param agentInfo AgentInfoEntity实体
	 * @param type 1 直营返点率  2 分销返点率
	 * @param incomeDate 账期
	 * @return
	 */
	public BigDecimal getAgentRebate(AgentInfoEntity agentInfo, int type, int incomeDate, String deductionType );
}
