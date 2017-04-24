package com.wm.service.supermarket;


import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.base.exception.BusinessException;
import com.wm.controller.order.dto.OrderFromSuperMarketDTO;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.order.OrderEntity;

public interface SuperMarketServiceI extends CommonService{
	
	public OrderFromSuperMarketDTO getSuperMarketOrderDetail(Integer orderId);

	void paySupermarketOrder(Integer orderId, Integer cashierId, String payType, int createTime) throws Exception;

	/**
	 * 创建超市订单
	 * @param merchantId
	 * @param cashierId
	 * @param params {menuId:xx,num:xx}
	 * @param uuid
	 * @return
	 */
	OrderFromSuperMarketDTO createOrderFromSuperMarket(Integer merchantId, Integer cashierId, String params, Integer createTime, String uuid)
		throws BusinessException;

	/**
	 * 为超市现金订单创建结算订单
	 * @param cashierId 收银员ID
	 * @return
	 * @throws
	 */
	OrderEntity createSettlementOrder(Integer cashierId)
		throws BusinessException;
	
	/**
	 * 获取收银员最近一条结算订单
	 * @param merchantId
	 * @param beginTime 开始时间
	 * @param endTime   结算时间
	 * @return
	 */
	OrderEntity getLatestSettlementOrder(Integer cashierId, String beginTime, String endTime);	
	
	/**
	 * 收银员结算后统计
	 * @param cashierId
	 * @return
	 * @throws 
	 */
	Map<String, Object> statisticAfterSettlement(Integer cashierId) throws BusinessException;
	
	/**
	 * 获取结算订单对应的现金订单ID列表
	 * @param settlementOrderId 结算订单ID
	 * @return
	 */
	List<Integer> getCashOrderIdsOfSettlementOrder(Integer settlementOrderId);
	
	/**
	 * 
	 * @param merchantId
	 * @return
	 */
	String getBeginTimeForStatictis(Integer merchantId, Integer cashierId);
	
	/**
	 * 查询收银员是否有待结算的现金订单
	 * @param cashierId
	 * @return
	 */
	boolean existUnsettledCashOrder(Integer cashierId);
	
	/**
	 * 保存收银员结算时清点的现金金额
	 * @param cashierVo
	 * @param cashInventoryMoney  收银员清点的现金
	 * @param settlementOrderId   计算订单id
	 */
	void saveCashierInventoryMoney(CashierVo cashierVo, String cashInventoryMoney, Integer settlementOrderId);
	
	/**
	 * 获取结算信息
	 * @param cashier
	 * @param settlementOrderId
	 * @return
	 */
	Map<String, Object> getSettlementInfo(Integer cashierId, Integer settlementLogId) throws BusinessException;
	
	Map<String, Object> cashierSettlement(Integer cashierId, String cash, String state) throws BusinessException;
	
	Map<String, Object> newStatisticAfterSettlement(Integer cashierId, Map<String, Object> params) throws BusinessException;
	
	
	/**
	 * 判断收银员是否有结算订单
	 * @param cashierId 收银员ID
	 * @return
	 */
	Map<String, Object> isHaveCashOrder(Integer cashierId);
	
	
	Map<String, Object> calChange(Integer orderId,  Double cash);
	
	void franchiseSettle(OrderEntity orderEntity) throws Exception;
	
	Map<String, Object> settlementAndExit(Integer cashierId, String cash, String state, Integer cashierSettlementLogId) throws BusinessException;
	
	/**
	 * 校验支付最大金额
	 * @param orderId 订单id
	 * @param payType 支付方式
	 */
	void checkMaxPayMoney(Integer orderId, String payType) throws Exception;
}
