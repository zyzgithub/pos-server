package com.wm.controller.open_api.retail;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wm.util.security.HttpUtils;

/**
 * 连接到一号生活的 Retail转换中间件
 * @author Roar
 *
 */
public class RetailPortCall {
	public static final Logger logger = Logger.getLogger(RetailPortCall.class);

	public static void main(String[] args)
    {
        submitOrder(873438);
    }
	
	/**
	 * 提交订单  到 一号生活的 Retail转换中间件
	 * 使用场景及code触发位置： 
	 *     1.5生成订单的支付回调  PayServiceImpl  orderAlipayDone(..)
	 *     1.8余额支付  WeixinPayController  orderBanlancePay(..)
	 */
	public static void submitOrder(Integer orderId) {
		try {
			String url = PortConfig.URL + "/retail/submitOrder.action";
			Map<String ,Object> params = new HashMap<String, Object>(); 
			params.put("orderId", orderId); 
			HttpUtils.postAsyc(url, JSON.parseObject(JSON.toJSONString(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

    /**
     * 取消第三方订单  到一号生活的 Retail转换中间件
     * 使用场景及code触发位置： 
     *      1.5版取消订单 OrderServiceImpl orderRefund(..)
     */
    public static void cancelOrder(Integer orderId) {
        try {
            String url = PortConfig.URL + "/retail/cancelOrder.action";
            Map<String ,Object> params = new HashMap<String, Object>(); 
            params.put("orderId", orderId); 
            HttpUtils.postAsyc(url, JSON.parseObject(JSON.toJSONString(params)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新订单状态  到一号生活的 Retail转换中间件
     * 使用场景及code触发位置：
     *   --完成订单状态
     *     1.5版：快递员点确认           OrderServiceImpl    deliveryDone(..)
     *     1.5版：定时任务自动完成    OrderServiceImpl    autoCompleteOrder(..)
     *     1.8版：用户点确认               .....
     *     
     *   --已下单状态不需要更改,默认已下单处理
     */
    public static void updateOrder(Integer orderId) {
        try {
            String url = PortConfig.URL + "/retail/updateOrder.action";
            Map<String ,Object> params = new HashMap<String, Object>(); 
            params.put("orderId", orderId); 
            params.put("orderStatus", 1); 
            HttpUtils.postAsyc(url, JSON.parseObject(JSON.toJSONString(params)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
	
}
