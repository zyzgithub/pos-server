package com.courier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.service.statistics.CourierStatisticsDalyServiceI;
import com.wm.service.user.WUserServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc-dev.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class TestCourier {
	
	@Autowired
	private CourierStatisticsDalyServiceI courierStatisticsDalyService;

	@Autowired
	private WUserServiceI userService;

	@Test
	public void statisticsDayly() throws Exception{
//		courierStatisticsDalyService.statisticsDayly(234989);
		String userType = userService.getCustType(0);
		System.out.println("===================userType:" + userType);
		userType = userService.getCustType(47832);
		System.out.println("===================userType:" + userType);
		userType = userService.getCustType(200157);
		System.out.println("===================userType:" + userType);
		userType = userService.getCustType(200157);
		System.out.println("===================userType:" + userType);
		userType = userService.getCustType(200157);
		System.out.println("===================userType:" + userType);
		System.in.read();
		
	}
}
