package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;

/**
 * @author dianba
 */
public interface CourierCommentService {

	/**(OvO)
	 * 获得大项统计信息
	 * @param userId	当前登录用户ID
	 * @param vo		搜索条件VO(时间, 区域条件)
	 * @return
	 */
	public abstract Map<String, Object> getCommentStatistic(Integer userId, SearchVo vo);
	
	/**(OvO)
	 * 评价列表
	 * @param userId		当前登录用户ID
	 * @param vo			搜索条件VO(时间, 区域条件)
	 * @param commentGrade	评论星级(用于筛选)
	 * @param page			页码
	 * @param rows			每页显示记录数
	 * @return
	 */
	public abstract List<Map<String, Object>> getCommentList(Integer userId, SearchVo vo, Integer commentGrade, Integer page, Integer rows);
	
	/**
	 * 根据评分星级统计订单评价 分为 1，2, 3, 4, 5 个区段
	 * @return
	 */
	public List<Map<String,Object>> getCommentClassify(Integer courierId);
	
	/**
	 * 
	 * 根据星级不同查询不同的评价
	 * @param courierId 快递员id
	 * @param star星级
	 * @return
	 */
	public List<Map<String,Object>> getCommentByStar(Integer courierId,Integer star,Integer page,Integer rows);
	
	
	/**
	 * 根据用户等级不同查询不同的评价(根据消费总金额来决定)
	 * @param max 上限金额
	 * @param min 下限金额
	 * @param page
	 * @param rows
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getCommentByCustType(Double min,Double max,Integer page,Integer rows,Integer courierId);
	
	
	/**
	 * 得到管辖下所有订单评价
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String,Object>> getComment(Integer courierId,Integer page,Integer rows);
	
	/**
	 * 获得详细评价
	 * @param commentId
	 * @return
	 */
	public Map<String,Object> getDetailComment(Integer commentId);
	
	
	/**
	 * 获得客户等级类型列表
	 * @return
	 */
	public List<Map<String,Object>> getCustTypeList();
	
	/**
	 * 搜索功能
	 * @param courierIds
	 * @param page
	 * @param rows
	 * @param searchVo
	 * @return
	 */
	public List<Map<String, Object>> orderCommentSearch(List<Integer> orgIds, Integer page, Integer rows, 
			SearchVo searchVo);
	
	/**
	 * 通过机构查找所属机构，如果底下没有机构，返回入参
	 * @param orgId
	 * @return
	 */
	public List<Integer> getOrgIds(Integer orgId); 
	
	
	/**
	 * 获得回访信息
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> getReturnVisit(Integer orderId);
	
	
	
	/**
	 * 获取用户类型
	 * @param userId
	 * @return
	 */
	public String getCustType(Integer userId);
}