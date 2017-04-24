package com.pay;

import com.alibaba.fastjson.JSONObject;

public interface TestPaySimulate {
	
	/**
	 * 获取订单状态-微富通
	 * @param setting test：测试环境，prod：生产环境
	 * @param payId 订单号
	 * @return
	 */
	public JSONObject getOrderStatus(String setting, String payId);
	
	/**
	 * 威富通支付补单
	 * @param payUrl
	 * @param notifyUrl 1.5支付回调接口
	 * @param payId
	 * @throws
	 */
	public void fixOrder(String setting, String payId);
	
	
}
