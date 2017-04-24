package com.wm.service.deduct;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.deduct.DeductLogEntity;

public interface DeductLogServiceI extends CommonService{

	/**
	 * 根据送餐量和提成规则计算快递员提成
	 * @param deductRule 提成规则
	 * @param totalOrder 计算快递员提成时，表示日订单总量；计算管理层提成时，表示所属子网点日累积订单总量；计算供应链提成时，表示日订单金额
	 * @param deductLog 提成记录
	 * @return
	 */
	public DeductLogEntity getDeductByRule(List<Map<String, Object>> deductRule, Double totalOrder, DeductLogEntity deductLog);
	
	/**
	 * 根据子网点月订单量和提成规则计算组长提成
	 * @param deductRule 提成规则
	 * @param orderPerDay 子网点下的快递员月人均日订单
	 * @param monthOrders 子网点下的快递员月人订单总量
	 * @return
	 */
	public Double getDeductByRule(List<Map<String, Object>> deductRule, Double orderPerDay, Integer monthOrders);
	
	/**
	 * 按group分类统计快递员可拿提成的送餐份数
	 * @param courierId
	 * @param incomeType 
	 * @return
	 */
	public List<Map<String, Object>> getCourierGroupMenu(Integer courierId, String startDate, String endDate, String incomeType);
	
	public List<Map<String, Object>> getCourierDeductGroups(Integer courierId, String startDate, String endDate, String incomeType);
	
	public List<Map<String, Object>> getYesterdayIncome(Integer userId, String date);
	public List<Map<String, Object>> getYesterday(Integer userId, String date);
	
	/**
	 * 获取快递员每月的日提成详情
	 * @param courierId
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getEverydayDeduct(Integer courierId, String date, int page, int rows);
	
	/**
	 * 计算众包快递员配送普通订单的提成
	 */
	public DeductLogEntity getCrowdsouringCourierEverydayDeduct(List<Map<String, Object>> deductRule, Integer totalOrder, DeductLogEntity deductLog);
	
}
