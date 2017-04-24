package com.wm.service.recharge;



import org.jeecgframework.core.common.service.CommonService;

public interface RechargeServiceI extends CommonService{
	/**
	 * 保存充值记录并更新用余额
	 * 
	 * @param outTradeNo
	 * @param transactionId
	 * @param totalFee
	 * @param opUserId
	 */
	public boolean saveRechargeAndUserMoney(String outTradeNo,
			String transactionId, String totalFee, String opUserId);
	
	/**
	 * 保存充值记录
	 * 
	 * @param outTradeNo
	 * @param transactionId
	 * @param totalFee
	 * @param opUserId
	 */
	public boolean saveRecharge(String outTradeNo,
			String transactionId, String totalFee, String opUserId);
	
	
	/**
	 * 保存预充值记录和订单流水
	 * 
	 * @param outTradeNo
	 * @param body
	 * @param totalFee
	 * @param userId
	 */
	public boolean saveRechargeAndOrder(String outTradeNo,
			String body, String totalFee, int userId,String payType);
	
	/**
	 * 更新充值状态为已充值
	 * @param outTradeNo pay_id
	 */
	public void updateRechargeState (String outTradeNo);
}
