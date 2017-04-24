package com.wm.dao.agent.impl;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wm.dao.agent.AgentRebateDao;
import com.wm.entity.agent.AgentRebateEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.service.merchantinfo.MerchantDeductionServiceI;

@Repository("agentRebateDao")
public class AgentRebateDaoImpl extends GenericBaseCommonDao<AgentRebateEntity, Integer> 
			implements AgentRebateDao {
	Logger logger = LoggerFactory.getLogger(AgentRebateDaoImpl.class);
	
	@Autowired
	private MerchantDeductionServiceI merchantDeductionService;

	@Override
	public AgentRebateEntity getAgentRebateEntity(Integer userId, String type, int incomeDate) {
		String sql = "SELECT id FROM agent_rebate WHERE user_id=? AND type=? AND income_date=?";
		Integer id = this.findOneForJdbc(sql, Integer.class, userId, type, incomeDate);
		if(id==null){
			return null;
		}
		AgentRebateEntity agentRebate = this.get(AgentRebateEntity.class, id);
		return agentRebate;
	}

	
	/**
	 * 根据代理商的用户id和返点类型获取返点率
	 * @param userId
	 * @param type 1 直营返点率  2 分销返点率
	 * @param incomeDate 账期
	 * @return
	 */
	@Override
	public BigDecimal getAgentRebate(AgentInfoEntity agentInfo, int type, int incomeDate, String deductionType ){
		BigDecimal rebate = new BigDecimal(0.00);
		AgentRebateEntity agentRebate = getAgentRebateEntity(agentInfo.getUserId(), deductionType, incomeDate);//1表示账期为1
		if(agentRebate==null){
			logger.error("未找到合作商userId：{}的账期为：{}扣点类型为：{}的agent_rebate返点率信息", new Object[]{agentInfo.getUserId(), incomeDate, deductionType});
			return rebate;
		}
		if(type==1){
			rebate = agentRebate.getPoints();
		}else{
			rebate = agentRebate.getRebate();
		}
		return rebate;
	}
	
}
