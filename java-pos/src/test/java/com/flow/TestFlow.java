package com.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.service.flow.FlowServiceI;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc-test.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class TestFlow {
	
	@Autowired
	private FlowServiceI flowService;
	
	@Test
	public void testMerchantOrderIncome() throws Exception {
		int totalOrders = 50;
		for (int i = 0; i < totalOrders; i++) {
			flowService.merchantOrderIncome(200157, 10, 111);
		}
		System.in.read();
	}
}
