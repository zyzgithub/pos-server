package com.wm.service.merchantinfo;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;


public interface MerchantInfoServiceI extends CommonService{

	/**
	 * 获取商家信息
	 * @return
	 */
	List<Map<String, Object>> getMerchantList();

}
