package com.wm.service.statistics;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.user.WUserEntity;

public interface CourierStatisticsDalyServiceI extends CommonService{

	/**
	 * 按天统计快递员提成
	 * @param courier
	 */
	void statisticsDayly(Integer courierId) throws Exception;

	/**
	 * 按月统计快递员组长的子网点提成
	 * @param leaderId 快递员组长
	 * @param subOrgId 子网点
	 */
	void statisticsMonthly(Integer leaderId, Integer subOrgId) throws Exception;

	/**
	 * 统计、结算管理层快递员的日提成
	 * @param courierId
	 */
	void statisticsLeaderDayly(Integer courierId) throws Exception;
	
	/**
	 * 获取在在黑名单中的快递员ID列表
	 * @return
	 */
	public List<Integer> getCourierInBlacklist();
	
	/**
	 * 获取需要计算提成的快递员列表
	 * @return
	 */
	List<WUserEntity> getCouriersNeedToCalcDeduct();
	
	void updateAgentBond(Integer agentId, int money);

}
