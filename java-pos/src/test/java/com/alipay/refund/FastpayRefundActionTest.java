package com.alipay.refund;

import java.math.BigDecimal;

import javax.annotation.Resource;

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
import org.springframework.transaction.annotation.Transactional;

import com.alipay.refund.FastpayRefundParam;
import com.alipay.refund.nopwd.NopwdFastpayRefundAction;
import com.alipay.refund.nopwd.NopwdFastpayRefundApplyResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-mvc-context.xml",
		"classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class FastpayRefundActionTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(FastpayRefundActionTest.class);

	@Resource
	private NopwdFastpayRefundAction nopwdFastpayRefundAction;

	@Test
	@Transactional
	public void test() throws Exception {
		// get from `order`
		String outTraceId = "2016032921001004710288516358";
		BigDecimal onlineMoney = new BigDecimal("0.1");
		
		FastpayRefundParam fastpayRefundData = new FastpayRefundParam(outTraceId, onlineMoney);
		NopwdFastpayRefundApplyResult nopwdFastpayRefundApplyResult = nopwdFastpayRefundAction.apply(fastpayRefundData);
		Assert.assertTrue(nopwdFastpayRefundApplyResult.isSuccess());
		
		LOGGER.info("batchNo: {}", nopwdFastpayRefundApplyResult.getBatchNo());
	}

}
