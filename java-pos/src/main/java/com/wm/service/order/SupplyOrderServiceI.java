package com.wm.service.order;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.controller.order.dto.OrderFromSupplyDTO;


/**
 * 
 * 供应链订单
 *
 */
public interface SupplyOrderServiceI  {

	/**
	 * 
	 * @param orderFromSupplyDTO
	 * @return
	 */
	AjaxJson receiveSupplyChainMerchantOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) throws Exception;
	
	/**
	 * 
	 * @param orderFromSupplyDTO
	 * @return
	 */
	AjaxJson receiveSupplyChainWarehouseOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) throws Exception;
	
	
	/**
	 * 通知供应链系统订单的状态
	 * @param couierId
	 * @param orderId
	 * @param state
	 * @return
	 */
	boolean noticeSupplyChain(Integer userId, Integer orderId, String state);
}
