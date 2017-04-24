package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**
 * 
 * 统计，订单统计接口
 *
 */
public interface CourierOrderstatService {
	
	/**(OvO)
	 * 获取外卖订单、堂食订单、扫码订单数量、快递员数量
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @param state		1:已支付  0:未支付
	 * @return	takeAwayCount	外卖订单
	 * 			scanCount		扫码订单
	 * 			dineInCount		堂食订单
	 * 			courierCount	快递员数量
	 */
	public abstract Map<String, Object> getOrderCountDetails(String orgIdsStr, Integer state, SearchVo vo);
	
	/**
	 * 获取当前订单总金额
	 * @param ids
	 * @state  1:已支付  0:未支付
	 * @return
	 */
	public Double getMoneyCurrent(List<Integer> ids, Integer state);
	
	/**
	 * 获取当天订单总数
	 * @param ids
	 * @state 1:已支付  0:未支付
	 * @return
	 */
	public Integer getOrderNumCurrent(List<Integer> ids, Integer state);
	
	/**
	 * 获取新店铺订单总数
	 * @param id
	 * @return
	 */
	public Integer getOrderNumNew(Integer id);
	
	/**
	 * 按网点，并按金额 从大到小排序的列表（字段：网点名，总订单数，金额）
	 * @param orgIds	网点ID列表
	 * @return	orgName			网点名
	 * 			ordnum			总订单数
	 * 			takeAwayCount	外卖订单数
	 * 			scanCount		扫码订单数
	 * 			dineInCount		堂食订单数
	 * 			origin			金额
	 * 			avg				客单价
	 * 			userCount		用户数
	 * 			rank			排名(序号)
	 * 			merchantCount	网点下商铺数量
	 */
	public List<Map<String, Object>> getOrderstatByOrg(List<Integer> orgIds, Integer page, Integer rows, SearchVo searchVo);
	
	/**
	 * 按店铺，并按金额 从大到小排序的列表（字段：大厦名，订单数，份数，金额）
	 * @param courierIds
	 * @return
	 */
	public List<Map<String, Object>> getOrderstatByMerchant(List<Integer> orgIds, Integer page, Integer rows, SearchVo searchVo);
	
	/**(OvO)
	 * 按店铺，并按金额 从大到小排序的列表（字段：大厦名，订单数，份数，金额）
	 * @param userId	合作商用户ID
	 * @return
	 */
	public List<Map<String, Object>> getOrderstatByMerchant4Agent(Integer userId, Integer page, Integer rows,SearchVo searchVo);

	/**
	 * 按商家统计订单总金额
	 * @param orgIds	快递员管理的区域IDs
	 * @param searchVo
	 * @return
	 */
	public Map<String, Object> getOrderSumByMerchant(List<Integer> orgIds, SearchVo searchVo);
	
	
	/**
	 * 根据机构的 id 
	 * @param id 机构id
	 * @return
	 */
	public Integer getOrderNum(Integer id);
	
	/**
	 * 根据机构的ID 获取 获取新商铺
	 * @param id  机构id
	 * @return
	 */
	public Integer getMerchantNewCount(Integer id);
	
	/**
	 * 根据机构的ID 获取 获取老商铺
	 * @param id  机构id
	 * @return
	 */
	public Integer getMerchantOldCount(Integer id);
	
	/**
	 * 新增商家排名
	 * @param ids
	 */
	public List<Map<String,Object>> getMerchantNew(List<Integer> ids, SearchVo vo,Integer page,Integer rows);
	
	/**
	 * 商家统计
	 * @param ids
	 * @param state 1:已完成  0:未完成
	 * @return
	 */
	public Integer getMerchantCount(List<Integer> ids,Integer state);
	
	/**
	 * 用户数
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @param state 1:完成  0:完成
	 * @return	userCount	用户量
	 * 			oldUser		老用户数
	 */
	public Map<String, Object> getUserCount(String orgIdsStr, Integer i,SearchVo timeType);
	
	/**
	 * 商家统计-网点排名
	 * @param list 机构id
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String,Object>> getDotRank(List<Integer> list, SearchVo vo,Integer page,Integer rows);
	
	/**
	 * 决策报表已完成
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @return
	 */
	public Map<String, Object> getDecisionMakingReports(String orgIdsStr, SearchVo vo);
	
	/**
	 * 决策报表未完成
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @return
	 */
	public Map<String, Object> getDecisionMakingReportsNo(String orgIdsStr, SearchVo vo);
	
	/**
	 * 品类报表
	 * @param orgIds    机构ID
	 * @param searchVo  
	 * @return
	 */
	public List<Map<String, Object>> getCategoryByReports(List<Integer> orgIds,SearchVo searchVo);
	
	/**
	 * 品类报表详情列表
	 * @param orgIds	     机构ID
	 * @param searchVo	  
	 * @param categoryId  品类ID
	 * @return
	 */
	public List<Map<String, Object>> getCategoryByReportsInfo(List<Integer> orgIds, SearchVo searchVo, Integer categoryId,Integer page,Integer rows);
	
	/**
	 * 交易金额  前5天
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getTransactionFive(List<Integer> orgIds);
}
