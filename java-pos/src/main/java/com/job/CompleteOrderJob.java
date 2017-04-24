package com.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;

/**
 * 定时任务-自动处理已支付过的订单
 * @author Simon
 */
public class CompleteOrderJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(CompleteOrderJob.class);

	@Autowired
	private OrderServiceI orderService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-自动完成订单");
		
		//查询出所有未处理的订单
//		String hql = "from OrderEntity where payState='pay' and orderType in('normal','mobile','supermarket') and state in ('delivery','pay','accept')";
//		List<OrderEntity> list = orderService.findHql(hql);
//		String sql = " select o.id, o.courier_id from `order` o where o.pay_state = 'pay' and order_type in('normal','mobile','supermarket') and state in ('delivery','pay','accept')";
		String sql = " select o.id, o.courier_id "
				+ "from `order` o "
				+ "where o.pay_state = 'pay' "
				+ "and flash_order_id=-1 " //排除闪购订单
				+ "and order_type in('normal','mobile','supermarket') "
				+ "and state in ('delivery','pay','accept') "
				+ "and rstate in ('normal') ";
		List<Map<String, Object>> orderIdMaps = orderService.findForJdbc(sql);
		if(orderIdMaps != null && orderIdMaps.size() > 0){
			for(Map<String, Object> orderIdMap:orderIdMaps ){
				OrderEntity order = new OrderEntity();
				order.setId(Integer.parseInt(orderIdMap.get("id").toString()));
				if(orderIdMap.get("courier_id") != null){
					order.setCourierId(Integer.parseInt(orderIdMap.get("courier_id").toString()));
				}
				try {
					orderService.autoCompleteOrder(order);
				} catch (Exception e) {
					logger.error("更新订单失败，订单id:" + order.getId(), e);
				}
				
			}
		}
	}
	
}
