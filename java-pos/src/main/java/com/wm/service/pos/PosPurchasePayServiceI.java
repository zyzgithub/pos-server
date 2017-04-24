package com.wm.service.pos;

import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.service.impl.pay.BarcodePayResponse;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月26日 上午11:31:51    
 * @return
 * @version 1.0
 */
public interface PosPurchasePayServiceI extends CommonService{

	public BarcodePayResponse onWeixinPay(Map<String, String> params);
	
	public BarcodePayResponse onAliPay(Map<String, Object> params);
	
	public AjaxJson onBalancePay(Integer merchantUserId, String payPassword, Integer mainOrderId, Integer merchantId);
}
