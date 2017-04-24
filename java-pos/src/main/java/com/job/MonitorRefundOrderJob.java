package com.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.order.FlashOrderReturnEntity;
import com.wm.service.order.OrderServiceI;

/**
 * 定时任务  -15分钟内未接单自动退单
 * @author Administrator
 */
public class MonitorRefundOrderJob  extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(MonitorRefundOrderJob.class);

	@Autowired
	private OrderServiceI orderService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException{
		logger.info("15分钟内商家未接单自动退单");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		/**
		 * 查询所有私厨订单信息
		 * fromType 0:众包订单  1：用户订单   2：闪购订单
		 */
		List<Map<String, Object>> list = orderService.getOrderTimer();
	
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				Map<String, Object> orderMap = list.get(i);
				int id = Integer.parseInt(orderMap.get("id").toString());
				int orderId =Integer.parseInt(orderMap.get("orderId").toString());
				int merchantid = Integer.parseInt(orderMap.get("merchantId").toString());
				int fromType = Integer.parseInt(orderMap.get("fromType").toString());
				String creatTime = orderMap.get("createTime").toString();
				
				logger.info("自动退单任务, orderId:{}, fromType:{}", orderId, fromType);
				try {		
					Long time = (format.parse(format.format(date)).getTime() - format.parse(creatTime).getTime()) / (60*1000);
					Integer minute = time.intValue();
					boolean b = false;
					
					if(fromType == 1){
						logger.info("用户订单自动退单判断, orderId:{}, minute", orderId, minute);
						if(minute >= 15){
							b = orderService.kitMerchantUnAcceptOrder(orderId,merchantid,"商家15分钟内未接单");
						}
					}else if(fromType == 0 ){
						logger.info("众包订单自动退单判断, orderId:{}, minute", orderId, minute);
						if(minute >=60){  //1小时内快递员未接单，取消订单
							b = orderService.autoCancelCrowdsourcingOrder(orderId,merchantid);
						}
					}
					//用户申请退款/退货退款时启动定时器
					else if(fromType == 2){//闪购订单，商家3天不处理系统自动退款
						logger.info("闪购订单自动退单判断, orderId:{}, minute", orderId, minute);
						if(minute >= 4320){//60*24*3=4320 
							orderService.flashOrderAcceptRefundApply(orderId);//自动同意退款申请
							FlashOrderReturnEntity flash = orderService.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
							//0:退款,1:退货退款
							if('0'==flash.getType()){
								b = orderService.flashOrderAcceptRefund(orderId,merchantid);
							}else if('1'==flash.getType()){
								b = true;
							}
						}
					}
					logger.info("退单结果, orderId:{}, result:{}", orderId, b);
					if(b){
						/**
						 * 查一个删一个
						 */
						orderService.deleteOrderTimer(id);
					}		
				} catch(Exception e){
					logger.error("自动退单失败, orderId=" + orderId, e);
				}
			}
		}
	}
}
