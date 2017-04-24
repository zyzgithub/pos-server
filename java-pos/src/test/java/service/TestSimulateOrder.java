package service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.cib.epay.sdk.EPay;
import com.wm.controller.open_api.iwash.MD5Util;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.dto.ShopcartDTO;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;

/**
 * 模拟下单
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml"})
public class TestSimulateOrder {

	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private FlowServiceI flowService;
	
	@Autowired
	OrderStateServiceI orderStateService;
	
	@Autowired
	private  MenuServiceI menuService;
	
	@Autowired
	private  WUserServiceI wUserService;
	

	@Test
	public void start() throws Exception {
		
		String sql = " select o.id, o.courier_id from `order` o where o.pay_state = 'pay' and order_type in('normal','mobile','supermarket') and state in ('delivery','pay','accept')";
		List<Map<String, Object>> orderIdMaps = orderService.findForJdbc(sql);
		if(orderIdMaps != null && orderIdMaps.size() > 0){
			for(Map<String, Object> orderIdMap:orderIdMaps ){
				OrderEntity order = new OrderEntity();
				order.setId(Integer.parseInt(orderIdMap.get("id").toString()));
//				order.setMerchant(wUserService.getEntity(MerchantEntity.class, Integer.parseInt(orderIdMap.get("merchant_id").toString())));
				if(orderIdMap.get("courier_id") != null){
					order.setCourierId(Integer.parseInt(orderIdMap.get("courier_id").toString()));
				}
				try {
					orderService.autoCompleteOrder(order);
				} catch (Exception e) {
					System.out.println("更新订单失败，订单id:" + order.getId());
				}
				break;
			}
		}
	}

	private void genPayedOrder(Integer userId, ShopcartDTO shopCar, List<Shopcart> carts) throws Exception {
		Integer orderId = orderService.createOrderFromWX(userId, shopCar, carts);
		if(null != orderId) {
			OrderEntity order = orderService.get(OrderEntity.class, orderId);
        	if(order != null && OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getPayState())){
        		String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
        		order.setOrderNum(orderNum);
        		order.setState(OrderStateEnum.PAY.getOrderStateEn());
        		order.setPayType(PayEnum.weixinpay.getEn());
       			order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
       			order.setOnlineMoney(order.getOrigin());
       			order.setPayTime(DateUtils.getSeconds());
       			order.setOutTraceId("123456");
       			orderService.saveOrUpdate(order);
                
       			WUserEntity user = wUserService.get(WUserEntity.class, userId);
       			
       			//增加积分
       			user.setScore(user.getScore() + order.getOrigin().intValue());
       			orderService.saveOrUpdate(user);
       			
//    			flowService.buyFlowCreate(user.getId(), order.getOrigin(), order.getId(), order.getTitle());
    			orderStateService.payOrderState(order.getId());

    			// 打印订单
//				if (orderService.orderPrint(order)) {
					orderService.merchantAcceptOrder(order);
//				}
    			
    			menuService.buyCount(order.getId());// 销量统计
    			menuService.buyCountMenuPromotion(order.getId());// 促销剩余数量
            }
		}
	}
	
	
	public static void main(String[] args) {
		
		Long xx = Long.parseLong("6".toString());
		System.out.println(xx.doubleValue());
		
//		UUID uuid = UUID.randomUUID();
//		String ret = EPay.pyPay(uuid.toString().replaceAll("-", ""), "308584000013", "6214831201520936", "汤根生", "0", "8.88", "测试");
//		System.out.println(ret);
//		DateTime dt = DateTime.now().minusDays(1);
//		System.out.println(dt.getMillis()/1000);
		/*
		for(int i=0;i<100;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i=0;i<10000;i++){
						Double merchantId = (Double)(Math.random()*100);
						System.out.println(AliOcs.genOrderNum(merchantId.intValue()+""));
					}
					System.out.println(AliOcs.genOrderNum(null));
				}
			}).start();
		}*/
	}

}
