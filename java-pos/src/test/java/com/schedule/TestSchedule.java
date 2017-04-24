package com.schedule;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.base.enums.AppTypeConstants;
import com.wm.service.order.jpush.JpushServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc-test.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class TestSchedule {
	
	
	@Autowired
    private JpushServiceI jpushService;

	@Test
	public void start() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			Map<String, String> pushMap = new HashMap<String, String>();
			pushMap.put("orderId", i + "");
//			pushMap.put("scramble", i + "");
			String title = "您有一条新的订单";
			pushMap.put("title", title);
			pushMap.put("content", title);
			pushMap.put("appType", AppTypeConstants.APP_TYPE_MERCHANT);
			pushMap.put("voiceFile", "new_order.mp3");
//			jpushService.push(424245, pushMap);
			jpushService.push(785713, pushMap);
		}
		Thread.sleep(60000);
	}
	
}
