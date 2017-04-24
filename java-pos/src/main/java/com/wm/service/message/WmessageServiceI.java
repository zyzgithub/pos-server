package com.wm.service.message;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.open_api.ThirdPlat;
import com.wm.entity.order.OrderEntity;

public interface WmessageServiceI extends CommonService{

	/**
	 * 发送订单完成信息到微信公众号（微信公众号关注并进入过微店），并保存消息至message表
	 * @param order
	 * @param state ：paySuccess-支付成功,accept-商家接单,delivery-开始配送,done-完成配送
	 */
	public void sendMessage(OrderEntity order, String state);
	
	
	/**
	 * 发送第三方订单完成信息到微信公众号（微信公众号关注并进入过微店），并保存消息至message表
	 * @param thirdPlat
	 * @param stateCode 1=支付成功 2=退款成功
	 * @param orderid
	 */
	public void sendThirdMessage(ThirdPlat thirdPlat, Integer stateCode, Integer orderid);

	
	/**
	 * 闪购订单发送微信推送消息
	 * @param order
	 * @param stateCode ： delivery--订单发货,refundapply--退款申请结果通知,returnproductapply--退货申请结果通知,refuneSuccess--退款成功通知
	 */
	public void flashSendMessage(OrderEntity order, String stateCode);
	
	/**
	 * 乡村基微信通知
	 * @param order
	 * @param stateCode
	 */
	public void sendRuralbaseMessage(OrderEntity order, String stateCode,String pin,Long orderType);
	
}
