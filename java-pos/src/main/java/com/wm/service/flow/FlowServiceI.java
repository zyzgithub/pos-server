package com.wm.service.flow;

import java.math.BigDecimal;
import java.util.Map;

import com.wm.service.impl.flow.FlowVo;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.agent.AgentIncomeTimerEntity;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.entity.transfers.TransfersEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.impl.flow.NotEnoughBalanceException;

public interface FlowServiceI extends CommonService{

	
	/**
	 * 余额支付流水
	 * @param detailId 订单ID
	 * @param user
	 * @param balance 余额支付金额
	 */
	public void balancePayFlowCreate(Integer detailId, WUserEntity user, Double balance) throws Exception;

	/**
	 * 平台会员充值订单流水
	 * @param userid
	 * @param money
	 * @param detailId
	 * @param merchantId
	 */
	public void rechargeFlowCreate(Integer userid, Double money, Integer detailId, Integer merchantId) throws Exception;
	
	/**
	 * 商家会员充值订单流水
	 * @param userid
	 * @param money
	 * @param detailId
	 */
	public void merchantRechargeFlowCreate(int userid, double money, int detailId)throws Exception;
	
	public void agentRechargeFlowCreate(int userid, double money, int detailId)throws Exception;
	
	/**
	 * 退单流水
	 * @param userid
	 * @param money
	 * @param detailId
	 */
	public void refundFlowCreate(int userid, double money, int detailId) throws Exception;

	/**
	 * 商家订单收入流水
	 * @param userid
	 * @param money
	 * @param detailId
	 */
	public void merchantOrderIncome(int userid, double money, int detailId) throws Exception;
	
	/**
	 * 商家申请提现流水
	 * @param userId
	 * @param money
	 * @param withdrawId 提现记录ID
	 * @return 流水ID
	 */
	public Integer merchantWithdraw(Integer userId, Double money, Integer withdrawId) throws Exception;
	
	/**
	 * 代理商申请提现流水
	 * @param agent
	 * @param money
	 * @throws Exception
	 */
	public void agentWithdraw(Integer userId, Double money) throws Exception;
	
	/**
	 * 商家众包订单结算运费
	 * @param merchant 
	 * @param money
	 * @param detailId
	 */
	public void merchantCrowdsourcing(WUserEntity merchant, Double money, Integer detailId) throws Exception;
	
	/**
	 * 商家众包退单流水
	 * @param merchant
	 * @param orderId
	 */
	public void crowdsourcingRefund(WUserEntity merchant, Integer orderId) throws Exception;

	/**
	 * 快递员提成收入流水
	 * @param courierId
	 * @param money
	 */
	public void deductFlowCreate(DeductLogEntity deductLog) throws Exception;

	/**
	 * 快递员代付流水
	 * @param transfers
	 */
	public void courierPayOrderFlowCreate(TransfersEntity transfers) throws Exception;
	
	/**
	 * 快递员代购对冲流水
	 * @param courierId
	 * @param money
	 */
	public void courierPayOrderReturnFlowCreate(Integer userId, Double money, Integer detailId) throws Exception;
	
	/**
	 * 商家返点奖励补贴发放流水
	 * @param userId
	 * @param money
	 * @param action 类型：merchantRebate商家返点收入，courierRebate快递员返点
	 */
	public void userRebateIncome(int userId, double money, String action);
	
	/**
	 * 代理商商家返点奖励补贴发放流水
	 * @param userId
	 * @param money
	 * @param action 类型：merchantRebate商家返点收入，courierRebate快递员返点
	 */
	public void agentMerchantRebateIncome(int userId, double money);
	
	/**
	 * 快递员推广提成奖励
	 * @param orderId
	 * @param userId
	 * @param money
	 * @throws Exception
	 */
	public void courierRechargePromotion(Integer orderId, Integer userId, Double money) throws Exception;
	
	/**
	 * 创建代理商扣点收入和返点收入流水
	 * @param agentIncome
	 */
	public void agentIncomeFlowCreate(AgentIncomeTimerEntity agentIncome) throws Exception;
	
	/**
	 * 堂食订单退单时，商家余额扣除。<br>
	 * （该订单可能已完成并结算预收入给商家，但不管是否已结算预收入，预收入记录不改，从商家余额扣除即可）
	 * @param orderId 订单编号
	 * @param userId 商家的ID
	 * @param reducedMoney 退款金额
	 * @throws NotEnoughBalanceException 商家余额不足，无法进行扣除
	 */
	public void dineInOrderMerchantRefund(Integer orderId, Integer userId, BigDecimal reducedMoney) throws NotEnoughBalanceException;
	
	/**
	 * 代理商商家的订单，配送费归代理商，钱插入代理商余额和流水
	 * @param userId
	 * @param deliveryMoney
	 * @param detailId
	 */
	public void agentMerchantDeliveryIncome(int userId, double deliveryMoney, int detailId) throws Exception;
	
	/**
	 * 商家供应链订单普通门店支付
	 * @param userId
	 * @param money
	 */
	public void merchantSupplyOrderPay(Long orderId, Integer userId, BigDecimal money,String type, String payType, String detail);
	
	/**
	 * 商家供应链订单供应商收入
	 * @param userId
	 * @param money
	 */
	public void merchantSupplyOrderIncome(Long orderId, Integer userId, BigDecimal money);

	/**
	 * 商家连锁账户分店余额转出到主店
	 * @param userId
	 * @param money
	 */
	public void mainStoreAccountIn(Integer userId, BigDecimal money) throws Exception;

	/**
	 * 商家连锁账户分店余额转出到主店
	 * @param userId
	 * @param money
	 */
	public void branchStoreAccountOut(Integer userId, BigDecimal money) throws Exception;

	FlowVo payPOSBuyout(int userId, double addedMoney, int detailId, String payIndex) throws Exception;

	/**
	 * 查询提现申请对应的流水
	 * @param userId
	 * @param withDrawId 提现申请ID
	 * @return
	 */
	public Map<String, Object> findWithDrawFlow(Integer userId, Integer withDrawId);

	/**
	 * 查询两次提现记录期间的累计金额
	 * @param userId
	 * @param flowType 流水类型：pay、income
	 * @param withDrawOldId 较早的一次提现
	 * @param withDrawNewId 较晚的一次提现
	 * @return
	 */
	public Double findTotalMoney(Integer userId, String flowType, Integer withDrawOldId, Integer withDrawNewId);

}
