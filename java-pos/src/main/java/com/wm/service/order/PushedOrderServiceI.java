package com.wm.service.order;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;
import org.joda.time.DateTime;

import com.wm.entity.order.PushedOrderEntity;

public interface PushedOrderServiceI extends CommonService{
	
	/**
	 * 查询快递员可抢订单数
	 * @param courierId
	 * @param isRepush
	 * @return
	 */
	public Long canScrambleNum(Integer courierId, Boolean isRepush);
	
	
	
	/**
	 * 查询快递员可抢订单列表
	 * @param courierId
	 * @param isRepush
	 * @return
	 */
	public List<Map<String, Object>> canScramble(Integer courierId, Boolean isRepush);

	/**
	 * 重新推送抢单信息
	 * @param courierId
	 */
	void rePushOrder(Integer courierId);
	
	/**
	 * @param orderId
	 */
	List<PushedOrderEntity> findPushedOrders(Integer orderId);
	
	/**
	 * 根据订单ID删除推送临时记录
	 * @param orderId
	 * @return
	 */
	public Integer deletePushedOrders(Integer orderId);

	/**
	 * 删除已失效的推送记录（昨天的）
	 */
	public Integer deleteInvalid();
	
	/**
	 * 备份并删除自动完成的订单
	 * @param oid 订单id
	 */
	public void deleteAndBackupExpired(Integer oid);
	
	/**
	 * 备份并删除过期没有处理的订单
	 * @param before 在某个时间点以前的推送的订单 格式 yyyy-MM-dd HH:mm:ss
	 */
	public void deleteAndBackupExpiredRecord(String before);
	
	/**
	 * 重新推送抢单信息
	 * @param courierId
	 */
	void rePushOrderNew(Integer courierId);
	
	/**
	 * 保存过期的订单记录
	 * @param orderId
	 * @param courierId
	 * @param createTime
	 * @param originCreateTime
	 */
	void saveExpiredOrder(Integer orderId, Integer courierId, Long createTime);
	
	/**
	 * 重新推单
	 */
	void executeRepush();

}
