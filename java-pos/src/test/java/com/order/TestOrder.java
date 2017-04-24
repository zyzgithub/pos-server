package com.order;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.base.schedule.ScheduledUtil;
import com.test.service.TestOrderI;
import com.wm.controller.takeout.QRCodeController;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.security.HttpUtils;
import com.wp.XMLUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc-test.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class TestOrder {
	
	private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);
	
	private static String wftNotifyUrlTest = "http://apptest.0085.com/takeOutController/wftnotify.do";
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private TestOrderI testOrder;
	
	@Test
	public void testCopyOrder() throws IOException {
		testOrder.testCopyOrder();
		System.in.read();
	}
	
	@Test
	public void testBatchOrder() throws IOException {
		int totalOrders = 50;
		for (int i = 0; i < totalOrders; i++) {
			BatchOrderTask task = new BatchOrderTask();
			ScheduledUtil.runNodelayTask(task, ScheduledUtil.OTHER_POOL);
		}
		System.in.read();
	}

//	@Test
	public Integer createOrder() {
		Integer userId = 200157;
		String merchantId = "4882";
		Double money = 0.01;
		String saleType = "2";
		String payType = "wft_pay";
		String payId = getPayId("40","60","2", "1", "0");
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		Integer orderId = orderService.createQcCodeOrder(payId, user, merchantId, money, saleType, payType);
		logger.info("生成模拟订单：{}", orderId);
		return orderId;
	}
	
//	@Test
	public void payOrder(Integer orderId) {
		if(orderId == null){
			orderId = 441979;
		}
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		String payId = order.getPayId();
		
		JSONObject postMap = new JSONObject();
		postMap.put("result_code", "SUCCESS");
		postMap.put("total_fee", 100 * order.getOrigin());
		postMap.put("out_trade_no", payId);
		postMap.put("transaction_id", "test|" + payId);

		String ret = HttpUtils.postStr(wftNotifyUrlTest, postMap, false);
		logger.info("1号生活返回:{}", ret);
		if (StringUtils.isNotEmpty(ret)) {
			Map<String, String> retMap;
			try {
				retMap = XMLUtil.doXMLParse(ret);
				if (retMap != null && "SUCCESS".equals(retMap.get("return_code"))) {
					logger.info("deal success payId:{}", payId);
				} else {
					logger.error("deal failed payId:{}", payId);
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("deal failed payId:{}", payId);
		}
	}
	
	public class OrderTask implements Runnable {
		Integer orderId;
		public OrderTask(Integer orderId) {
			this.orderId = orderId;
		}
		public void run() {
			payOrder(orderId);
		}
	}
	
	public class BatchOrderTask implements Runnable {
		int batchOrders = 50;
		public void run() {
			for (int i = 0; i < batchOrders; i++) {
				Integer orderId = createOrder();
				payOrder(orderId);
//				OrderTask task = new OrderTask(orderId);
//				ScheduledUtil.runNodelayTask(task);
			}
		}
	}
	
	/**
     * 生成pay_id 唯一
     *  2位标识订单来源
        2位标识订单类型
        1位标识获取商品方式
        1位标识订单配送类型
        1位标识预收入状态
        6位保留标识
        12位uuidHash(百万级重复0 千万级重复100以内 亿级重复在2000以内)
     * @param uid
     * @return
     */
    public String getPayId(String source, String type, String way, String dtype, String pStatus) {
        return new StringBuilder()
                        .append(source)
                        .append(type)
                        .append(way)
                        .append(dtype)
                        .append(pStatus)
                        .append("000")
                        .append(hash(12)).toString();
    }
    
    public String hash(int length) {
        String hashCode = "" + Math.abs(UUID.randomUUID().hashCode());
        String userIdHashCode = "" + System.currentTimeMillis();
        return userIdHashCode.substring(userIdHashCode.length() - (length - hashCode.length()))
                        + hashCode;
    }
}
