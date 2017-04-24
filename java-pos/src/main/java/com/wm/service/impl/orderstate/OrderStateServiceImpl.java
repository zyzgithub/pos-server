package com.wm.service.impl.orderstate;

import jeecg.system.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.OrderStateEnum;
import com.wm.controller.open_api.ValidUtil;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderstate.OrderStateEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.orderstate.OrderStateServiceI;

@Service("orderStateService")
@Transactional
public class OrderStateServiceImpl extends CommonServiceImpl implements OrderStateServiceI {
	
    @Autowired
    private UserService userService;

	@Override
	public void createOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("未支付");
		orderState.setDetail("新增一个订单");
		this.save(orderState);
	}

	@Override
	public void createThirdOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("未支付");
		orderState.setDetail("i玩派订单");
		this.save(orderState);
	}

	@Override
	public void createPhoneOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity wUser = order.getWuser();
		MerchantEntity merchant = order.getMerchant();

		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		if (OrderStateEnum.PAY.getOrderStateEn().equals(order.getPayState())) {
			orderState.setState("已支付");
		} else {
			orderState.setState("未支付");
		}
		orderState.setDetail("商家" + merchant.getTitle() + "生成用户" + wUser.getUsername() + "一个电话订单");
		this.save(orderState);
	}

	@Override
	public void payOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("已付款");
		orderState.setDetail("支付成功");
		this.save(orderState);
	}

	@Override
	public void payOrderState(OrderEntity order) {
		OrderStateEntity orderState = new OrderStateEntity();
		int dealTime = DateUtils.getSeconds();
		if(StringUtils.isNotEmpty(order.getRemark())&&order.getRemark().startsWith("[offline_order]")){
			dealTime = order.getCreateTime();
		}
		orderState.setDealTime(dealTime);
		orderState.setOrderId(order.getId());
		orderState.setState("已付款");
		orderState.setDetail("支付成功");
		this.save(orderState);
	}

	@Override
	public void noAcceptOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("取消订单");
		orderState.setDetail("商家已拒绝接单");
		this.save(orderState);
	}

	@Override
	public void deliveryOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("配送中");
        orderState.setDetail("配送员已开始配送");
        this.save(orderState);
	}

	@Override
	public void doneOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("已完成");
		orderState.setDetail("订单已完成");
		this.save(orderState);
	}

	@Override
	public void evaluateOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity wUser = order.getWuser();
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("已评价");
		orderState.setDetail(wUser.getUsername() + "评价订单。");
		this.save(orderState);
	}

	@Override
	public void askedRefundOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity wUser = order.getWuser();
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("已接单");
		orderState.setDetail(wUser.getUsername() + "申请取消订单");
		this.save(orderState);
	}

	@Override
	public void refundSuccessOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		MerchantEntity merchant = order.getMerchant();
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("退款成功");
		orderState.setDetail("商家" + merchant.getWuser().getUsername() + "退款成功");
		this.save(orderState);

	}
	
	@Override
	public void acceptRefundOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		MerchantEntity merchant = order.getMerchant();
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("同意退款申请");
		orderState.setDetail("商家" + merchant.getWuser().getUsername() + "同意退款申请");
		this.save(orderState);

	}

	@Override
	public void refundFailedOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("拒绝退款");
		orderState.setDetail("商家不同意取消订单");
		this.save(orderState);
	}

	@Override
	public void deliveryDoneOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity courier = this.get(WUserEntity.class, order.getCourierId());
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("完成配送");
		orderState.setDetail("配送员" + courier.getUsername() + "完成配送");// 改配送员为快递员，之前是店家
		this.save(orderState);

	}

	@Override
	public void deliveryPayOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity courier = this.get(WUserEntity.class, order.getCourierId());
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("转账");
		orderState.setDetail("配送员" + courier.getUsername() + "向用户转账￥" + order.getOrigin());
		this.save(orderState);
	}

	@Override
	public void deliveryPayOrderState(String courier, String user, int orderId, double money) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("代付订单");
		orderState.setDetail("配送员:" + courier + ",代付用户:" + user + ",代付金额:￥" + money);
		this.save(orderState);
	}

	@Override
	public void cookDoneOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		OrderEntity order = this.get(OrderEntity.class, orderId);
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("制作完成");
		orderState.setDetail(order.getMerchant().getTitle() + "制作完成");
		this.save(orderState);

	}

	@Override
	public void autoCompleteOrderState(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("自动完成");
		orderState.setDetail("系统自动完成订单。");
		this.save(orderState);

	}

	@Override
	public void merchantUpdateOrder(int orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setDetail("商家将订单改为电话订单");
		orderState.setOrderId(orderId);
		orderState.setState("已接单");
		this.save(orderState);
	}

	@Override
	public void accessOrderStateChoice(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(order.getCreateTime());
		orderState.setOrderId(orderId);
		orderState.setState("待接单");
		orderState.setDetail("等待私厨商家接单");
		this.save(orderState);
	}

	@Override
	public void accessOrderState(int orderId) {
		OrderEntity order = this.get(OrderEntity.class, orderId);
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(order.getAccessTime());
		orderState.setOrderId(orderId);
		if(order.getFlashOrderId() != -1){
			orderState.setState("已发货");
			orderState.setDetail("商家已发货，包裹正在配送中");
			this.save(orderState);
			return ;
		}
		orderState.setState("已接单");
		orderState.setDetail("商家已接单");
		this.save(orderState);
		if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
			OrderStateEntity orderState2 = new OrderStateEntity();
			orderState2.setDealTime(DateUtils.getSeconds());
			orderState2.setOrderId(orderId);
			orderState2.setState("待评价");
			orderState2.setDetail("待客户评价");
			this.save(orderState2);
		}
	}

	@Override
	public void scrambleOrder(Integer courierId, Integer orderId) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(orderId);
		orderState.setState("已接单");
    	WUserEntity courUser = userService.getEntity(WUserEntity.class, courierId);
        if (!ValidUtil.anyEmpty(courUser, courUser.getUsername(), courUser.getMobile())) {
            orderState.setDetail("配送员" + courUser.getUsername() + "(" + courUser.getMobile() + ")" + "已接单");
        }
		this.save(orderState);
	}

}