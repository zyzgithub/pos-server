package service;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;

/**
 * 手动完成代付订单
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CompleteUnpayOrder {

	@Autowired
	private OrderServiceI orderService;
	@Autowired
	OrderIncomeServiceI orderIncomeService;
	
	
	@Test
//	@Transactional
	public void start() throws Exception{
		String sql = "select * from `order` ";
		sql += " where pay_state='unpay' ";
		sql += " and date(FROM_UNIXTIME(create_time))>='2015-09-09' ";
		sql += " and date(FROM_UNIXTIME(create_time))<='2015-09-27' ";
		sql += " and order_type='mobile' ";
		sql += " and pay_id not in ('1442488514189','1442305952550','1442283117390') ";
		sql += " order by id desc";
		List<Map<String, Object>> list = orderService.findForJdbc(sql);
		if(list != null && list.size() > 0){
			for(Map<String, Object> map : list){
				Integer orderId = Integer.parseInt(map.get("id").toString());
				OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
				this.completeUnpayOrder(order);
			}
		}
	}

	private void completeUnpayOrder(OrderEntity order) throws Exception{
		order.setPayState("pay");
		order.setPayTime(DateUtils.getSeconds());
		order.setState("confirm");
		order.setCompleteTime(DateUtils.getSeconds());
		orderService.updateEntitie(order);
		orderIncomeService.createOrderIncome(order);
	}
}
