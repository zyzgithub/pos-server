package com.wm.service.deduct;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.deduct.DeductLogEntity;

public interface DeductServiceI  extends CommonService{
	
	/**
	 * 查找快递员提成规则
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCourierDeductRule(Integer courierId, Integer userId, Integer courierType);
	
	/**
	 * 根据订单获取快递员能拿提成的送餐数
	 * @param orderId
	 * @param countDeduct 是否计算提成
	 * @return
	 */
	public Long getQuantityByOrder(Integer orderId, boolean countDeduct);

	/**
	 * 根据订单数，查找快递员提成数
	 * @param courierId
	 * @param totalOrder
	 * @return
	 */
	public Double getCourierDeduct(Integer courierId, Integer totalOrder, Integer userId);

	public void saveAndUpdateMoney(DeductLogEntity deductLog) throws Exception;

	/**
	 * 查询已完成供应链订单总金额
	 * @param courierId
	 * @param deductDate 'yyyy-DD-MM'
	 * @return
	 */
	public Double getSupplyOrderMoney(Integer courierId, String deductDate);

}
