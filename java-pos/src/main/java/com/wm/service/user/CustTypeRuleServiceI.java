package com.wm.service.user;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface CustTypeRuleServiceI extends CommonService{

	/**
	 * 获取客户等级规则配置
	 * @return
	 */
	public List<Map<String, Object>> getRules();
	
	/**
	 * 根据累计消费金额，界定客户等级
	 * @param consumeAmount
	 * @return
	 */
	public String getCustTypeByConsumeAmount(Double consumeAmount);
	
}
