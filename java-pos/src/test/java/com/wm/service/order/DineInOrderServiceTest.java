package com.wm.service.order;

import javax.annotation.Resource;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-mvc-context.xml",
		"classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class DineInOrderServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(DineInOrderServiceTest.class);

	@Resource
	private DineInOrderServiceI dineInOrderService;
	
	private static final Integer ORDER_ID = 433269;
	
	private static final Integer OP_USER_ID = 200576;

	@Test
	public void chargebackTest() throws Exception {
		AjaxJson chargeback = dineInOrderService.chargeback(ORDER_ID, OP_USER_ID);
		Assert.assertNotNull(chargeback);
		LOGGER.info("success:{}, msg:{}, statusCode:{}", chargeback.isSuccess(), chargeback.getMsg(), chargeback.getStateCode());
	}

}
