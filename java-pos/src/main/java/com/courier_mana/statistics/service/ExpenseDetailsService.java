package com.courier_mana.statistics.service;

import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**(OvO)
 * 总支出费用模块Service
 * @author hyj
 */
public interface ExpenseDetailsService {
	/**(OvO)
	 * 获取员工支出
	 * @param userId	当前登录用户ID
	 * @param vo		搜索条件Vo(包含orgId和时间条件)
	 * @return	courierDeduct	飞行员提成
	 * 			BDDeduct		BD提成
	 * 			courierWages	飞行员基本工资
	 * 			BDWages			BD基本工资
	 */
	public abstract Map<String, Object> wageAndDeductExpense(Integer userId, SearchVo vo);
	
	/**(OvO)
	 * 获取员工支出
	 * @author hyj
	 * @param orgIdsStr		若干个区域ID(ID用","分隔)
	 * @param courierIdsStr	若干个快递员ID(ID用","分隔)
	 * @param vo		搜索条件Vo(时间条件)
	 * @return	courierDeduct	飞行员提成
	 * 			BDDeduct		BD提成
	 * 			courierWages	飞行员基本工资
	 * 			BDWages			BD基本工资
	 */
	public abstract Map<String, Object> wageAndDeductExpense(String orgIdsStr, String courierIdsStr, SearchVo vo);
	
	/**(OvO)
	 * 获取营销支出
	 * @param userId	当前登录用户ID
	 * @param vo		搜索条件Vo(包含orgId和时间条件)
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return	cardSum		优惠券优惠支出
	 * 			member		会员卡优惠支出
	 * 			ranTotal	随机立减优惠支出
	 * 			scanRebate	扫码支付返点支出
	 */
	public abstract Map<String, Object> marketingExpense(Integer userId, SearchVo vo, boolean isConfirm);
	
	/**(OvO)
	 * 获取营销支出
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @param vo		搜索条件Vo(包含orgId和时间条件)
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return	cardSum		优惠券优惠支出
	 * 			member		会员卡优惠支出
	 * 			ranTotal	随机立减优惠支出
	 * 			scanRebate	扫码支付返点支出
	 */
	public abstract Map<String, Object> marketingExpense(String orgIdsStr, SearchVo vo, boolean isConfirm);
}
