package com.wm.service.courieraccount;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wm.entity.courieraccount.CourierAccountSuspendEntity;
import com.wm.entity.position.PositionEntity;

public interface CourierAccountSuspendServiceI {
	
	/**
	 * 判断打卡约束是否启用
	 * @return
	 */
	boolean isPunchConstraintEnable();
	
	/**
	 * 判断抢单约束是否启用
	 * @return
	 */
	boolean isScrambleConstraintEnable();
	
	/**
	 * 判断开拓商家约束是否启用
	 * @return
	 */
	boolean isOpenupConstraintEnable();
	
	/**
	 * 获取打卡约束
	 * @return
	 */
	Map<String, String> getPunchConstraint();
	
	/**
	 * 获取抢单约束
	 * @return
	 */
	Map<String, String> getScrambleConstraint();
	
	/**
	 * 获取BD开拓商家约束
	 * @return
	 */
	Map<String, String> getOpenupConstraint();
	
	/**
	 * 保存封号记录
	 * @param courierId
	 * @param suspendTime
	 * @param suspendReason
	 */
	void save(Integer courierId, Date suspendTime, String suspendReason);
	
	/**
	 * 封号
	 * @param courierId
	 * @param suspendReason
	 */
	void suspendAccount(Integer courierId, String suspendReason);
	
	/**
	 * 查询所有被封号且没有被被封号的记录
	 * @return
	 */
	Map<Integer, CourierAccountSuspendEntity> getAllSuspendAccounts();
	
	/**
	 * 查询被解封的账号
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	Map<Integer, CourierAccountSuspendEntity> getUnlockAccounts(Date startDate, Date endDate);
	
	
	/**
	 * 获取快递员的抢单日志
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> getScrambleLogs(Integer courierId, String startDate, String endDate);
	
	/**
	 * 获取需要封号快递员id
	 * @param positionIds
	 * @return
	 */
	List<Integer> getCourierIdsOfAccountSuspend(Map<String, List<Integer>> positionIds);
	
	/**
	 * 获取审核通过的开拓商家记录
	 * @return
	 */
	List<Map<String, Object>> getApprovalOpenupRecords(Integer courierId, String startDate, String endDate);
	
}
