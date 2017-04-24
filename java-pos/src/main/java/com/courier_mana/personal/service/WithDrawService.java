package com.courier_mana.personal.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhaoyue
 * 快递员提现接口
 */
public interface WithDrawService {
	
	/**
	 * @param userId 快递员的员工号
	 * @return 快递员的卡号、银行名
	 */
	List<Map<String,Object>> getCourierCardId(Integer userId);
	
	/**
	 * @param CourierId 快递员员工号
	 * @return 余额、日期、日提成
	 */
	List<Map<String,Object>> getCourierStatistics(Integer courierId,Integer page,Integer rows);
	
	/**
	 * 插入申请提现记录
	 * @param courierId
	 * @param bankCardId
	 * @param getMoney
	 * @param password
	 * @return
	 */
	List<Map<String,Object>> applyFor(Integer courierId,Long bankCardId,Double getMoney,String password);
	
	/**
	 * 查询提现记录
	 * @param courierId
	 * @return
	 */
	List<Map<String,Object>> getWithRecord(Integer courierId,Integer page,Integer rows);
}
