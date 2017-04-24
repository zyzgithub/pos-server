package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.common.vo.SearchVo4UserRank;

/**
 * 
 * 快递员个人中心-我的资料接口
 *
 */
public interface CourierUserService {
	
	/**
	 * 获取指定用户在指定时间三天内的消费详情
	 * @param 	endTime	指定时间(秒)
	 * @param 	userId	用户ID
	 * @param	isAgent	当前登录用户是否合作商
	 * @param	agentUserId 合作商用户ID
	 * @return	userId		用户ID
	 * 			date		日期
	 * 			sum			当日消费金额
	 * 			totalSpent	用户注册到指定日期的消费总额
	 */
	public abstract List<Map<String, Object>> userExpensesDetails(Integer endTime, Integer userId, boolean isAgent, Integer agentUserId);
	
	/**
	 * 获取划分用户类型规则
	 * 排序: 高级类型优先
	 * @return	id		类型ID
	 * 			typeName类型名称
	 * 			typeDesc类型描述
	 * 			amount	类型的最低消费
	 */
	public abstract List<Map<String, Object>> getUserTypeRule();
	
	/**
	 * 获取制定地区内用户总消费列表(用于统计各类型用户数量)
	 * @param orgId 机构ID(按地区筛选用户)
	 * @return	注意消费总额单位为：分
	 */
	public abstract List<Map<String, Object>> getUserTotalSpent(SearchVo vo, Integer orgId);
	
	/**
	 * (合作商)获取制定地区内用户总消费列表(用于统计各类型用户数量)
	 * @param userId	合作商用户ID
	 * @return
	 */
	public abstract List<Map<String, Object>> getUserTotalSpent4Agent(SearchVo vo, Integer userId);
	
	/**
	 * 获取用户排名
	 * @param userId		当前登录用户ID
	 * @param isAgent		当前登录是否合作商
	 * @param vo			搜索条件Vo
	 * @param page			页码
	 * @param rowsPerPage	每页显示的记录数
	 * @return	userId		用户ID
	 * 			userName	用户名
	 * 			spent		搜索时间段内的花费
	 * 			orgName		用户所属区域的名字
	 * 			rank		用户在搜索时间段的排名
	 * 			preSpent	用户在前段时间的花费(用于判断涨幅)
	 * 			userType	用户类型
	 * 			userTypeId	用户类型ID
	 * 			userTypeDesc用户类型详细说明
	 * 			totalSpent	用户从注册至今的总花费(用于判断用户类别)
	 * 			totalCount	用户从注册至今的总订单数(用于debug)
	 */
	public abstract Map<String, Object> getUserRank(Integer userId, boolean isAgent, SearchVo4UserRank vo, int page, int rowsPerPage);
	
	/**
	 * 获取快递所属职位
	 * @param courierId 快递id
	 * @return Map<String,Object>
	 */
	public abstract Map<String,Object> getUserByPosition(Long courierId);
	
}
