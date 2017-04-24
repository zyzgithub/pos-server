package com.wm.service.courier;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.controller.courier.dto.MerchantInfoDTO;
import com.wm.entity.merchantapply.MerchantApplyEntity;

public interface CourierOpenUpMerchantServiceI {		
	
	/** 
	 * 保存商家信息
	 * @param merchant
	 * @return
	 */
	public AjaxJson saveMerchantInformation(Integer courierId, MerchantInfoDTO merchantInfomation, String remark);
	
	/**
	 * 根据快递员id获取开拓商家的记录
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getOpenUpMerchantRecord(Integer courierId, int page, int rows);
	
	/**
	 * 获取开拓商家的详细资料
	 * @param merchantId
	 * @return
	 */
	public Map<String, Object> getMerchantApplyInfo(Integer id);

	/**
	 * 获取 商家 扫码扣点类型列表
	 * @param income_date 账期类型
	 * @return
	 */
	public List<String> getAgentMerchantBucklePoint(Integer income_date);
	
	/**
	 * 获取 商家 扫码扣点类型列表
	 * @param income_date 账期类型
	 * @return
	 */
	public List<Map<String, Object>> getAgentMerchantBucklePoint();
	
	/** 
	 * 获取 商家 扫码 账期 列表
	 */
	public List<Integer> getAgentMerchantIncomeDates(); 
}
