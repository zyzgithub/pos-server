package com.wm.service.orderincome;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.dto.merchant.MerchantSupplySaleOrderDto;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;

public interface OrderIncomeServiceI extends CommonService {

	/**
	 * 生成订单预收入
	 * @param order
	 * @throws Exception
	 */
	public void createOrderIncome(OrderEntity order) throws Exception;

	/**
	 * 结算订单
	 * @param orderIncomeId 预收入订单ID
	 * @throws Exception
	 */
	public void unOrderIncome(Integer orderIncomeId) throws Exception;
	
	/**
	 * 结算代理商商家收入和代理商返点扣点
	 * @param sql 需要结算的查询sql语句
	 * @return
	 */
	public void unAgentIncome(List<Map<String, Object>> list);

	/**
	 * 需要结算的预收入列表
	 * @return
	 */
	public List<Map<String, Object>> findSettleList();

	/**
	 * 结算
	 * @param orderIncomeId 预收入订单ID
	 */
	public void settleMent(Integer orderIncomeId)  throws Exception;

	/**
	 * 生成订单预收入
	 * @param saleOrderdto
	 * @throws Exception 
	 */
	public void createSupplyOrderIncome(MerchantSupplySaleOrderDto saleOrderdto) throws Exception;
	
	/**
	 * 供应链供应商家收入
	 * @return
	 */
	public void unSupplyOrderIncome(Integer orderIncomeId, Integer userId) throws Exception;

	/**
	 * 根据订单id和预收入类型查询预收入
	 * @param orderId
	 * @param type  预收入类型，0 普通预收入，1 供应链预收入
	 * @return
	 */
	public OrderIncomeEntity getOrderIncomeByOrderIdAndType(Integer orderId, int type);
	
	/**
	 * 超市加盟店结算
	 * @param orderIncomeId
	 */
	public void franchiseUnOrderIncome(Integer orderIncomeId);
	
	/**
	 * 超市直营店收银员结算
	 * @param orderIncomeId
	 */
	public void directCashierUnOrderIncome(Integer orderIncomeId) throws Exception ;
	
	/**
	 * 取九洲商户当前余额
	 * @return
	 */
	public Double getTotalMoney4JZ();
}
