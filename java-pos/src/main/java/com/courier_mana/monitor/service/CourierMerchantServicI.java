package com.courier_mana.monitor.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 快递员商家服务接口
 *
 */
public interface CourierMerchantServicI {
	/**
	 * 查询指定用户id下所有的商家
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getMerchants(Integer courierId);
	
	/**
	 * 查询指定用户id下商家数,包含绑定未绑定总数
	 * @param courierId
	 * @return
	 */
//	public Long getMerchantCount(Integer courierId);
	
	/**
	 * 查询指定用户ID下的总商家数
	 * @author hyj
	 * @param orgIdsStr	若干个区域ID(多个区域ID之间用","隔开，中间不要留空格)
	 * @return
	 */
	public abstract Long getMerchantCount(String orgIdsStr);
	
	/**
	 * 查询指定用户id下所有的绑定商家
	 * @author hyj
	 * @param orgIdsStr	若干个区域ID(多个区域ID之间用","隔开，中间不要留空格)
	 * @return
	 */
	public abstract Long getBindMerchants(String orgIdsStr);
	
	/**
	 * 查询指定用户id下所有的绑定商家
	 * @param courierId
	 * @return
	 */
//	public List<Map<String, Object>> getBindMerchants(Integer courierId);
	
	/**
	 * 查询指定用户id下所有未绑定商家数
	 * @param courierId
	 * @return
	 */
	public Long getUnBindMerchantCount(Integer courierId);
	/**
	 * 查询指定用户id下所有商家数
	 * @param courierId
	 * @return
	 */
	public Map<String,Long> getAllAndUnBindMerchantCount(Integer courierId);
	
}
