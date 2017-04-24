package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

/**
 * 统计-快递员统计接口
 * @author hyj
 *
 */
public interface CourierStatisticService {
	/**
	 * 统计-快递员统计-实时统计
	 * @param courierId		快递员ID(必选)
	 * @param orgId			机构ID(可选)(用于搜索)
	 * @param page			页数(可选, 默认为1)
	 * @param rowsPerPage	每页显示的记录数(可选, 默认为10)
	 * @return	返回快递员统计信息, 包含
	 * 			courierId	快递员ID
	 * 			courierName	姓名
	 * 			holding		未配送数量
	 * 			delivering	配送中数量
	 * 			timeOut		超时数量
	 * 			finished	已完成数量
	 */
	public abstract List<Map<String, Object>> realTimeStatistic(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage,String interval);
	
    /**
     * 快递员订单信息
     * @param courierId     快递员ID(必选)
     * @param page          page：页数(可选, 默认为1)
     * @param rowsPerPage   rowsPerPage：每页显示的记录数(可选, 默认为10)
     * @return  返回快递员订单信息
     *          realname：用户名
     *          sendTime：下单时间
     *          orderNum：排号
     *          mobile：手机
     *          orderId：订单号
     *          remark：备注
     *          address：地址
     *          orderType：订单类型 返回mobile 为电话订单
     *          userId：用户ID
     *          custType：用户等级
     *          courierName：快递员名称
     *          courierMobile：快递员电话
     *          courierId：快递员ID
     *          merchantId:商家ID
     */
    public abstract List<Map<String, Object>> getCourierOrderInfos(Integer courierId, Integer page, Integer rowsPerPage);
	
	/**
	 * 统计-快递员统计-实时排名
	 * @param courierId		快递员ID(必选)
	 * @param orgId			机构ID(可选)(用于搜索)
	 * @param page			页数(可选, 默认为1)
	 * @param rowsPerPage	每页显示的记录数(可选, 默认为10)
	 * @return	返回快递员排名信息, 包含
	 * 			rank			排名
	 * 			courierId		快递员ID
	 * 			name			姓名
	 * 			todayOrderCount	今日单数
	 * 			totalOrderCount	累计单数
	 */
	public abstract List<Map<String, Object>> realTimeRank(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage);
	
	/**
	 * 统计-快递员统计-实时排名
	 * @param courierId		快递员ID(必选)
	 * @param orgId			机构ID(用于搜索)
	 * @param interval		区间
	 * @return 
	 */
	public abstract List<Map<String, Object>> firstThreeRank(Integer courierId,Integer orgId,String interval);
}
