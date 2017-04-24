
package com.wm.controller.open_api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.enums.OrderStateEnum;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.controller.open_api.iwash.IwashPortCall;
import com.wm.controller.open_api.tswj.PortCall;
import com.wm.controller.open_api.tswj.PortConfig;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderrefund.OrderRefundEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.util.MapUtil;
import com.wm.util.StringUtil;
import com.wm.util.security.DesEnc;
import com.wp.ConfigUtil;

/**
 * <h1>用于第三方接口对接【订单】部分操作</h1>
 * 
 * @author folo
 * @version 1.5
 *
 */
@Controller
@RequestMapping("/openapi/order")
public class OpenOrderController extends OpenBaseController {
	private static final Logger logger = Logger.getLogger(OpenOrderController.class);
	
	@Autowired 
	private OrderServiceI orderService;
	@Autowired
    private WmessageServiceI messageService;
	@Autowired 
	private OrderStateServiceI orderStateService;
	
	/**
	 * <h1>生成第三方订单</h1>
	 */
	@RequestMapping(value="/create")
	@ResponseBody
	private OpenResult create(String token, Long timestamp, String sign,
			String out_order_id, String out_order_title, String order_money, Long create_time, String cust_mobile, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		OpenResult ret = doCreate(token, timestamp, sign, out_order_id, out_order_title, order_money, create_time, cust_mobile);
		if(logger.isInfoEnabled())
			logger.info(String.format("\n --------【order create--%s】params:%s  \n\r return:%s", System.currentTimeMillis() - start,
					JSON.toJSONString(request.getParameterMap()), ret.toString()));
		return ret;
	}
	
	private OpenResult doCreate(String token, Long timestamp, String sign,
			String out_order_id, String out_order_title, String order_money, Long create_time, String cust_mobile){
		//校验传入参数
		if (StringUtil.isEmpty(out_order_id, out_order_title, order_money, create_time, cust_mobile)){
			return State.ParamError.ret();
		}
		
		//获得参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("out_order_id", out_order_id);
		params.put("out_order_title", out_order_title);
		params.put("order_money", order_money);
		params.put("create_time", create_time);
		params.put("cust_mobile", cust_mobile);
		
		//获取解密后的用户数据
		Object obj = getDesToken(token, timestamp, sign, params);
		if(obj instanceof OpenResult) return (OpenResult) obj;
		WUserEntity wuser = (WUserEntity) obj;
		double money = 0;
		try {
			Long longMoney = Long.parseLong(order_money);
			money = longMoney.doubleValue() / 100;//分为单位
		} catch (Exception e) { e.printStackTrace(); }
		if(money < 0) return State.ParamError.ret();
		
		//校验订单是否正确
		Object orderCheck = checkAndGetOrderId(wuser, out_order_id);
		if(orderCheck instanceof OrderEntity) return State.AlreadyFound.ret().msg("订单已存在");
		
		//保存订单
		int orderId = orderService.createOpenTswjOrder(wuser, out_order_id + "_" + PortConfig.TSWJ_APPID, out_order_title, money, create_time, cust_mobile);
		if(orderId == 0) return State.Error.ret();
		orderStateService.createThirdOrderState(orderId);// 订单生成状态信息

		return State.Success.ret();
	}
	
	/**
	 * 校验订单是否正确，并返回订单信息（包含：wuser.userId,id,title,state,rstate,origin）
	 * @param wuser 为空则校验订单与请求用户是否一致
	 * @param out_order_id 第三方商家订单号 放在本系统pay_id字段格式：(out_order_id + "_" + TSWJ_PARTNERID)
	 * @return
	 */
	private Object checkAndGetOrderId(WUserEntity wuser, String out_order_id){
		//校验订单是否类型正确、是否已支付
		String sql = "select id,pay_id payId, pay_time payTime,title,state,rstate,origin,user_id from `order` where pay_id=? and from_type=? and order_type=?";
		Map<String, Object> orderMap = orderService.findOneForJdbc(sql, out_order_id + "_" + PortConfig.TSWJ_APPID, "tswj", "third_part");
		if(null == orderMap) return State.NotFound.ret().msg("订单不存在");
		OrderEntity order = MapUtil.convertMapToBean(orderMap, OrderEntity.class);
		int req_user_id = Integer.valueOf(String.valueOf(orderMap.get("user_id"))).intValue();
		if(null != wuser && wuser.getId().intValue() != req_user_id)// 
			return State.Error.ret().msg("订单与用户不匹配");
		
		//组装order
		if(null == wuser){
			wuser = new WUserEntity();
			wuser.setId(req_user_id);
		}
		order.setWuser(wuser);
		return order;
	}
	
	
	/**
	 * 老版本天生玩家在使用
	 */
	@RequestMapping(value="/cancel")
	@ResponseBody
	@Deprecated
	private OpenResult cancel(Long timestamp, String sign,
			String out_order_id, String third_type, HttpServletRequest request){
		OpenResult ret = doCancel(timestamp, sign, out_order_id, request);
		if(logger.isInfoEnabled())
			logger.info(String.format("\n --------【order cancel】params:%s  \n\r return:%s",
					JSON.toJSONString(request.getParameterMap()), ret.toString()));
		return ret;
	}
	
	private OpenResult doCancel(Long timestamp, String sign,
			String out_order_id, HttpServletRequest request){
		//校验订单是否存在，是否为请求用户的订单
		Object orderCheck = checkAndGetOrderId(null, out_order_id);
		if(orderCheck instanceof OpenResult) return (OpenResult) orderCheck;
		OrderEntity order = (OrderEntity) orderCheck;
		return doCancelOrderWithTswj(order, out_order_id, ThirdPlat.tswj);
	}
	
	/**
	 * 天生玩家订单退款
	 * @param order
	 * @param out_order_id
	 * @return
	 */
	private OpenResult doCancelOrderWithTswj(OrderEntity order, String out_order_id, ThirdPlat thirdPlat){
		//检查 已支付未退款的订单 需要退款
		if(OrderStateEnum.REFUND.getOrderStateEn().equals(order.getState())){
			return State.Error.ret().msg("不能重复退款订单");
		}
		if(OrderStateEnum.PAY.getOrderStateEn().equals(order.getState()) && "normal".equals(order.getRstate())){
			//变更订单状态
			return doRefundOrderWithTswj(order, out_order_id, thirdPlat);
		}else{
			if(OrderStateEnum.CANCEL.getOrderStateEn().equals(order.getState())){
				return State.Error.ret().msg("不能重复取消订单");
			}
			//变更订单状态
			String sql = "update `order` set state='cancel' where id=?";
			orderStateService.executeSql(sql, order.getId());
			return State.Success.ret();
		}
	}
	
	/**
	 * 退款天生玩家订单
	 * @param order
	 * @param out_order_id
	 * @param thirdPlat
	 * @return
	 */
	private OpenResult doRefundOrderWithTswj(OrderEntity order, String out_order_id, ThirdPlat thirdPlat){
		final Integer orderid = order.getId();
		final String payid = out_order_id;
		new Thread(new Runnable() {
			@Override
			public void run() {
				//进行退款
				try {
					OrderRefundEntity  refundEntity = orderService.acceptThirdRefund(orderid, 0);
					if(null != refundEntity){//退款成功 同步到i玩派
						messageService.sendThirdMessage(ThirdPlat.tswj, 2, orderid);//推送微信消息
						String refundSql = "update `order` set state='refund',rstate='berefund' where id=?";
						orderStateService.executeSql(refundSql, orderid);
						//通知第三方平台
						PortCall.cpsRefundCallback(payid, refundEntity.getRefundFee()/100.0d, refundEntity.getOutRefundNo(), refundEntity.getCreateTime(), refundEntity.getStatus());
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("退款失败orderId=" + orderid);
				}
			}
		}).start();
		return State.Success.ret();
	}
	
	
	/**
	 * 退款第三方订单
	 */
	@RequestMapping(value="/refund")
	@ResponseBody
	private OpenResult refund(HttpServletRequest request, String third_type, String paramStr){
		try {
			//校验参数
			OpenResult checkRet = ValidUtil.checkEmptyParams(request, "paramStr", "third_type");
			if(null != checkRet) return checkRet;
			//校验第三方平台
			ThirdPlat thirdPlat = ThirdPlat.toThirdPlat(third_type);
			if(null == thirdPlat) return State.ParamError.ret("第三方平台标识错误!");
			ThirdPart third = ThirdConfig.getThird(thirdPlat, "true".equals(ConfigUtil.isTestThird));
			//解密参数
			String desParamStr = DesEnc.decrypt(paramStr, third.app_key);
			JSONObject jsonParam = JSON.parseObject(desParamStr);
			if(null == jsonParam) return State.ParamError.ret("参数解析错误!");
			//进行退款
			OpenResult ret = doRefund(request, jsonParam.getString("out_order_id"), jsonParam.getLong("user_id"), third);
			if(logger.isInfoEnabled())
				logger.info(String.format("\n --------【refund】params:%s  \n\r return:%s",
						JSON.toJSONString(request.getParameterMap()), ret.toString()));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return State.SysError.ret();
	}
	
	private OpenResult doRefund(HttpServletRequest request, String out_order_id, Long user_id, ThirdPart third){
		if(ValidUtil.anyEmpty(out_order_id, user_id)) return State.ParamError.ret();
		//校验订单是否存在，是否为请求用户的订单
		WUserEntity user = new WUserEntity();
		user.setId(user_id.intValue());
		Object orderCheck = checkAndGetOrderId(user, out_order_id);
		if(orderCheck instanceof OpenResult) return (OpenResult) orderCheck;
		OrderEntity order = (OrderEntity) orderCheck;
		//调用退款处理
		return doRefundOrder(order, out_order_id, third);
	} 
	
	
	/**
	 * 我要洗衣订单退款
	 * @param order
	 * @param out_order_id
	 * @return
	 */
	private OpenResult doRefundOrder(final OrderEntity order, final String out_order_id, final ThirdPart third){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//进行退款
				try{
					OrderRefundEntity  refundEntity = orderService.acceptThirdRefund(order.getId(), 0);
					if(null != refundEntity){
						messageService.sendThirdMessage(ThirdPlat.iwash, 2, order.getId());//推送微信消息
						//退款成功 变更状态
						String refundSql = "update `order` set state='refund',rstate='berefund' where id=?";
						orderStateService.executeSql(refundSql, order.getId());
						//退款成功 同步到i玩派
						if(third.thirdPlat.equals(ThirdPlat.tswj)) {
							PortCall.cpsRefundCallback(out_order_id, refundEntity.getRefundFee()/100.0d, refundEntity.getOutRefundNo(), refundEntity.getCreateTime(), refundEntity.getStatus());
						}
						//退款成功 同步到我要洗衣
						else if(third.thirdPlat.equals(ThirdPlat.iwash)){
							IwashPortCall.syncOrderStatus(third, out_order_id, "9", order.getCourierId()+"");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("退款失败orderId=" + order.getId());
				}
			}
		}).start();
		return State.Success.ret();
	}
}