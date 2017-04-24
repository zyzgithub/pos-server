package com.wm.service.order.scamble;

import java.util.List;

import org.jeecgframework.core.common.model.json.AjaxJson;


/**
 * 快递员抢单算法
 * @author wjw
 *
 */
public interface ScambleAlgorithmServiceI {
	/**
	 * 获取最近多少分钟（minutes）上传地理位置的快递员列表
	 * @param lng
	 * @param lat
	 * @param minutes
	 * @return
	 */
	List<Integer> getLatestReportPosCourierIds(double lng, double lat, int minutes);
	/**
	 * 从快递员列表中找出离商家（lng, lat）距离在radis（米）范围内num个快递员
	 * @param lng
	 * @param lat
	 * @return
	 */
	List<Integer> findNearestCouiriers(double lng, double lat, int radis, int num);
	
	/**
	 * 通过计算地图上直线距离排除距商家距离远的快递员
	 * @param courierIds
	 * @param lng
	 * @param lat
	 * @return
	 */
	void filterByLineDistance(List<Integer> courierIds, double lng, double lat);
	
	/**
	 * 处理已退款订单
	 * @param orderId
	 * @return
	 */
	boolean deleteRefundOrder(Integer orderId);
	
	/**
	 * 快递员可抢订单数，如果是重新推单，排除三分钟内已经推送过的订单。
	 * @param courierId 快递员ID
	 * @param isRepush 是否是重新单
	 * @return
	 */
	Long canScrambleNum(Integer courierId, Boolean isRepush);
	
	
	/**
	 * 快递员是否存在可抢订单
	 * @param courierId
	 * @return
	 */
	public boolean existCanScambleOrder(Integer courierId);

	/**
	 * 快递员courierId抢走订单orderId
	 * @param courierId
	 * @param orderId
	 */
	void courierScambleOrder(Integer courierId, Integer orderId);
	
	/**
	 * 快递员抢单
	 * @param courierId 快递员ID
	 * @return 
	 */
	AjaxJson courierScamble(Integer courierId);
	
	/**
	 * 快递员对应的可抢订单列表
	 * @param courierId 快递员ID
	 * @return
	 */
	List<Integer> canScambleOrders(Integer courierId);
	
	
	/**
	 * 把订单推送给满足条件的快递员
	 * @param orderId
	 * @param courierIds
	 */
	void pushToCouriers(Integer orderId, List<Integer> courierIds);
	
	
	/**
	 * 把订单推送给某一个指定的快递员
	 * @param orderId
	 * @param courierId
	 */
	void pushToCourier(Integer orderId, Integer courierId);
	
	/**
	 * 
	 */
	void executeRepushNew();
	/**
	 * 删除过期没有处理的订单
	 * @param before
	 */
	public void deleteAndBackupExpiredRecord(String before);
	
}
