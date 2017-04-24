package com.wm.service.pay;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface PayServiceI extends CommonService{
	
	/**
	 * 订单支付回调
	 * @param order
	 */
	public void orderPayCallback(OrderEntity order) throws Exception;
	
	/**
	 * 支付宝扫码支付回调
	 * @param order
	 * @param aliAcNo 支付宝账号
	 */
	public void orderAlipayScanDone(OrderEntity order, String aliAcNo) throws Exception;
	
	/**
	 * QQ扫码支付回调
	 * @param orderId
	 * @throws Exception
	 */
	public void orderQQpayScanDone(Integer orderId) throws Exception;
	
	
	

	/**
	 *订单支付
	 * @param orderid 订单id	
	 * @param userid 用户id
	 * @param mobile 手机号码
	 * @param cardid 代金券id
	 * @param cardMoney 代金券金额
	 * @param score 积分
	 * @param balance 余额
	 * @param alipayBalance 在线支付金额
	 * @param payType 支付类型：balance-余额支付，weixinpay微信支付，alipay支付宝支付
	 * @param timeRemark 送达时间备注
	 * @param request
	 * @param response
	 * @return
	 */
	public AjaxJson orderPay(int orderid, int userid,String mobile, String cardid, int cardMoney,
			int score, double balance, double alipayBalance,String payType,String timeRemark,HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	
	/**
	 * 扫码买单支付
	 * @param orderid
	 * @param userid
	 * @param mobile
	 * @param cardid
	 * @param cardMoney
	 * @param score
	 * @param balance
	 * @param alipayBalance
	 * @param payType
	 * @return
	 */
	public AjaxJson directPayOrderPay(int orderid, int userid, String mobile,
			String cardid, int cardMoney, int score, double balance,
			double alipayBalance, String payType,HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	public AjaxJson rechargeOrderPay(int orderid, int userid, double alipayBalance,
			String payType,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 获取充值记录
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> findPayRecord(Integer userId);
	
	/**
	 * 更新支付表的状态为已支付
	 * @param orderId
	 */
	public void updatePayState(Integer orderId);
	
	/**
	 * 保存POS背扫处理日志
	 * @param order
	 * @param cashierId
	 * @throws Exception
	 */
	public void saveSettleMentLog(OrderEntity order, Integer cashierId) throws Exception;
	
	/**
	 * 打印结算小票
	 * @param order
	 * @param cashierId
	 * @throws Exception
	 */
	public void printSettlement(OrderEntity order, Integer cashierId) throws Exception;
	
}
