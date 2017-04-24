package com.wm.service.impl.pay.orderhandler;

import java.math.BigDecimal;
import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.wm.controller.open_api.iwash.HttpUtils;
import com.wm.entity.opendevelopuser.OpenDeveloperUserEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderthirdbatch.OrderThirdBatchEntity;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.opendevelopuser.OpenDevelopUserServiceI;
import com.wm.service.orderthirdbatch.OrderThirdBatchServiceI;
import com.wm.service.pay.OrderHandler;

/**
 * 第三方订单回调处理器
 */
@Component("thirdOrderHandler")
public class ThirdOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ThirdOrderHandler.class);
	
	@Autowired
    private WmessageServiceI messageService;
	@Autowired
	private OpenDevelopUserServiceI openDevelopUserService;
	@Autowired
	private OrderThirdBatchServiceI orderThirdBatchService;

	@Override
	public void handle(OrderEntity order) throws Exception {
		//根据订单的商家ID查询返回的回调的URL
		logger.info("============开始第三方支付订单回调================");
		String merchantId =order.getMerchant().getId().toString(); 
		String oid =order.getId().toString(); 
		logger.info("merchantId:"+merchantId+"orderId:"+oid);
        //OpenDeveloperUserEntity openDeveloperUserEntity = openDevelopUserService.findOneForJdbc( "select id,mch_id as mchId,user_id as userId,secret_key as secretKey,notify_url as notifyUrl,secure_pay_address as securePayAddress from open_developer_user where mch_id = ?",OpenDeveloperUserEntity.class ,merchantId);
        //OrderThirdBatchEntity orderThirdBatchEntity =orderThirdBatchService.findOneForJdbc("select id,order_id as orderId,biz_id as bizId,pay_status as payStatus,callback_status as callbackStatus,create_time as createTime  from order_third_batch where order_id = ?",  OrderThirdBatchEntity.class ,oid);
        List<OpenDeveloperUserEntity> openDeveloperUserEntity = openDevelopUserService.findHql(" from com.wm.entity.opendevelopuser.OpenDeveloperUserEntity o where o.mchId = ? ", merchantId);
        List<OrderThirdBatchEntity> orderThirdBatchEntity = openDevelopUserService.findHql(" from com.wm.entity.orderthirdbatch.OrderThirdBatchEntity o where o.orderId = ? ", oid);
        JSONObject params = new JSONObject();
        params.put("return_code", "success");
        params.put("order_status", order.getPayState());
        params.put("total_fee", new BigDecimal(order.getOrigin()).multiply(new BigDecimal("100")).intValue() );
        params.put("out_trade_no", order.getId());//1号生活订单号
        params.put("transaction_id", orderThirdBatchEntity.get(0).getBizId());//业务流水号
        try{
	        logger.info("回调url"+openDeveloperUserEntity.get(0).getNotifyUrl()+"参数："+params);
	        JSONObject retJson =  HttpUtils.postForm(openDeveloperUserEntity.get(0).getNotifyUrl(), params);
	        logger.info("返回的结果"+retJson.toJSONString());
	        if(retJson != null){
	        	if("success".equals((String)retJson.get("status"))){
	        		String orderId = retJson.getString("out_trade_no");
	        		orderThirdBatchService.executeSql("update order_third_batch set callback_status = 1,pay_status = 1 where order_id = ?", orderId);
	        		logger.info("=======回调修改订单状态成功============");
	        	}
	        }
        }catch(Exception e){
        	logger.info("============第三方支付返回异常================");
        }
        logger.info("============结束第三方支付订单回调================");
	}
}
