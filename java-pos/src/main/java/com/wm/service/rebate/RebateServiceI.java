package com.wm.service.rebate;

import java.util.Date;

import org.jeecgframework.core.common.model.json.AjaxJson;


public interface RebateServiceI{
	
	/**
	 * 1、商家每天返点奖励补贴计算，步骤（1）得到前一天所有扫码用户首单（商家ID，首单总额），按商家分组 （2）通过商家ID得到当月返点率5% 3% 1% (3)统计总额 * 返点率，并且<=最高金额
	 */
	public void statMerchantRebateByEveryday();

	/**
	 * 2、物流人员每天奖励计算，根据商家签约的快递员职位来判定：
	 * （1）他是普通业务员：金额1%（1个ID按首单计算提成），他的营长、团长：0.1元/单/ID/天（同1个ID同1个门店，只计算1个订单）
	 * （2）他是营长: 金额1%（1个ID按首单计算提成），他的团长：0.1元/单/ID/天（同1个ID同1个门店，只计算1个订单）
	 * （3）他是团长： 金额1%（1个ID按首单计算提成）
	 */
	public void statCourierRebateByEveryday();
	
	/**
	 * 统计时间段商家提成
	 */
	public void statMerchantRebateByBefore(Date beginDate, Date endDate, Integer merchant_Id);
	
	/**
	 * 统计时间段快递员提成
	 */
	public void statCourierRebateByBefore(Date beginDate, Date endDate);
	
	/**
	 * 发放商家提成
	 */
	public abstract void payMerchantRebate();
	
	/**
	 * 发放快递员提成
	 */
	public abstract void payCourierRebate() throws Exception;
	
	/**
	 * 人工发放
	 * @param merchantId
	 * @param rebateMoney
	 */
	public AjaxJson manualGrant(Integer grantId);
	
	/**
	 * 补录专用
	 * 发放商家提成
	 * @param date
	 */
	public abstract void payMerchantRebate(Date date, Integer merchantId);
}
