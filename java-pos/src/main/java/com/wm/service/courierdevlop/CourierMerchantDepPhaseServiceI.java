package com.wm.service.courierdevlop;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.controller.courier.dto.CourierDevMerchantPhase;
import com.wm.controller.courier.dto.DevMerchantPhase;
import com.wm.controller.courierdevelop.dto.MerchantDevRecDTO;

public interface CourierMerchantDepPhaseServiceI {
	/**
	 * 获取招商录入的阶段定义
	 * @return
	 */
	List<DevMerchantPhase> getDevMerchantDefinition();
	
	/**
	 * 获取某一个商家的招商录入阶段信息
	 * @param devId 招商录入表的ID
	 * @return
	 * @throws 
	 */
	CourierDevMerchantPhase getCourierDevMerchantPhase(Integer devId);
	
	/**
	 * 获取某一个快递员招商录入的历史记录
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getCourierDevMerchantHistory(Integer courierId, int page, int rows);
	
	/**
	 * 更新商家录入的阶段信息
	 * @param devId 商家录入表的主键ID
	 * @param phaseIds 完成的子任务ID
	 */
	public AjaxJson updateCourierDevPhase(MerchantDevRecDTO vo);
	
	/**
	 * 创建一个新的商家录入阶段
	 * @param courierId 快递员ID
	 * @param merchantName 商家名称
	 * @param merchantHolder 店主姓名
	 * @param merchantMobile 商家电话
	 * @param subTaskIds 完成的子任务ID列表
	 */
	public AjaxJson createCourierDevPhase(MerchantDevRecDTO vo);
	
	/**
	 * 校验数据的有限性
	 * @param devId 商家录入ID
	 * @param subTaskIds 完成的子任务ID列表
	 * @return
	 */
	Map<String, String> validate(Integer devId, List<Integer> subTaskIds);
	
	/**
	 * 校验数据的有限性
	 * @param subTaskIds 完成的子任务ID列表
	 * @param phases 阶段定义，可以为空，为空则从数据库中查一次。不为空，表示已经标记check的阶段列表
	 * @return
	 */
	Map<String, String> validate(List<Integer> subTaskIds, List<DevMerchantPhase> phases);
	
	/**
	 * 是否是两个相邻的子任务
	 * @param subTaskId1
	 * @param subTaskId2
	 * @return
	 */
	boolean isNeighbourSubTask(Integer subTaskId1, Integer subTaskId2);
	
	/**
	 * 获取一个阶段中子任务的最大排序
	 * @param phaseId
	 * @return
	 */
	Integer getSubTaskMaxOrderNo(Integer phaseId);
}
