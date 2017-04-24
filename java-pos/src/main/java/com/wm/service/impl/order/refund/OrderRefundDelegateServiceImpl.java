package com.wm.service.impl.order.refund;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.base.enums.PayEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.refund.OrderRefundDelegateService;
import com.wm.service.order.refund.OrderRefundExecutor;

@Component("orderRefundDelegateService")
public class OrderRefundDelegateServiceImpl implements OrderRefundDelegateService {
	
	@Resource
	private OrderRefundExecutor alipayOrderRefundExecutor;
	
	@Resource
	private OrderRefundExecutor balanceOrderRefundExecutor;
	
	@Resource
	private OrderRefundExecutor merchantOrderRefundExecutor;
	
	@Resource
	private OrderRefundExecutor weixinOrderRefundExecutor;
	
	@Resource
	private OrderRefundExecutor wftOrderRefundExecutor;
	
	@Resource
	private OrderRefundExecutor cashOrderRefundExecutor;

	@Override
	public OrderRefundExecutor getExecutor(String payType) {
		if (OrderEntity.PayType.ALIPAY.equals(payType) || PayEnum.supermarkt_alibarcode.getEn().equals(payType)) {
			return alipayOrderRefundExecutor;
		}
		if (OrderEntity.PayType.WEIXINPAY.equals(payType) || PayEnum.supermarkt_wxbarcode.getEn().equals(payType)) {
			return weixinOrderRefundExecutor;
		}
		if (OrderEntity.PayType.WFTPAY.equals(payType)) {
			return wftOrderRefundExecutor;
		}
		if (OrderEntity.PayType.MERCHANTPAY.equals(payType)) {
			return merchantOrderRefundExecutor;
		}
		if(PayEnum.supermarkt_cash.getEn().equals(payType)){
			return cashOrderRefundExecutor;
		}
		return balanceOrderRefundExecutor;
	}

}
