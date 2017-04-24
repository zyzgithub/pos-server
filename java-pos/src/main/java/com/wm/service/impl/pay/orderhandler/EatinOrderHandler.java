package com.wm.service.impl.pay.orderhandler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.base.enums.PayEnum;
import com.wm.controller.open_api.ValidUtil;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.EatInOrderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.util.security.HttpUtils;

/**
 * 堂吃订单回调处理器
 */
@Component("eatinOrderHandler")
public class EatinOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(EatinOrderHandler.class);

	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private  MenuServiceI menuService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private EatInOrderServiceI eatInOrderService;
	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
    private WmessageServiceI messageService;

	@Override
	public void handle(OrderEntity order) throws Exception {
		Integer orderId = order.getId();
		logger.info("handle eatin order. orderId={}", orderId);
		WUserEntity user = order.getWuser();
		if(PayEnum.balance.getEn().equals(order.getPayType())){
			flowService.balancePayFlowCreate(orderId, user, order.getCredit());
		}
		
		orderStateService.payOrderState(order);
		eatInOrderService.updateStatus(orderId);
		
		
		messageService.sendMessage(order, "paySuccess");
		orderService.autoPrint(order);
		
		merchantService.pushOrder(orderId);
		orderService.merchantAcceptOrder(order);
		
		
		menuService.buyCount(orderId);// 销量统计
		//handlerCoupon(order);
		// 堂吃系统 >> 同步订单到宏信系统
		syncOrderToHx(Long.parseLong(orderId + ""));
	}
	
	/**
	 * 优惠券
	 * @param order
	 */
	private void handlerCoupon(OrderEntity order){
    	String cardId = order.getCardId();
        if(!ValidUtil.anyEmpty(cardId)){
            Map<String, Object> couponUser = orderService.getCouponUser(order.getCardId(), order.getWuser().getId());
             Integer num = order.getWuser().getId() % 10;
             String sql = "update coupon_user_"+ num +" SET `status` = -1 where sn = ?";
             orderService.executeSql(sql, cardId);
             Integer amount = new Integer(couponUser.get("amount").toString());
             String sql2 = "update `order` SET `card` = ? where id = ?";
             orderService.executeSql(sql2, amount, order.getId());
        }
	}
	
	/**
	 * 堂食订单同步到第三方
	 * @param orderId
	 */
	public void syncOrderToHx(final Long orderId){
	    new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject params = new JSONObject();
                params.put("access_token", "2583E7390FFA4B1E9B7FE3B8FE107644");
                params.put("orderId", orderId);
                String url = EnvConfig.base.OPENAPI_HXKJ_PORT + "/admin/hxkj/updateOrder";
                logger.info("url : {}", url);
                HttpUtils.postAsyc(url, params);
            }
        }).start();
	}

}
