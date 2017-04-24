package com.wm.service.impl.order;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.MsgResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.impl.flow.NotEnoughBalanceException;
import com.wm.service.order.DineInOrderServiceI;
import com.wm.service.order.DineInOrderTxServiceI;

/**
 * 堂食订单服务实现，用于包装处于事务管理下的方法调用（在必要时回滚事务），并转换返回结果。
 */
@Service("dineInOrderService")
@Transactional
public class DineInOrderServiceImpl implements DineInOrderServiceI {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DineInOrderServiceImpl.class);
	
	@Autowired
	private DineInOrderTxServiceI dineInOrderTxService;

	@Override
	public AjaxJson chargeback(Integer orderId, Integer opUserId) {
		try {
			MsgResp chargeback = dineInOrderTxService.chargeback(orderId, opUserId);
			return AjaxJson.fromMsgResp(chargeback);
		} catch (NotEnoughBalanceException e) {
			LOGGER.error("商家账号余额不足，无法退款", e);
			return AjaxJson.failJson("商家账号余额不足，无法退款");
		} catch (OrderRefundFailException e) {
			LOGGER.error("退款给用户失败，请联系管理员", e);
			return AjaxJson.failJson("退款给用户失败，请联系管理员");
		} catch (Exception e) {
			LOGGER.error("数据异常，请联系管理员", e);
			return AjaxJson.failJson("数据异常，请联系管理员");
		}
	}

}
