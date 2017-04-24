package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.service.flow.FlowServiceI;

/**
 * 测试服务
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class TestPayCallbackService {

	@Autowired
	private FlowServiceI flowService;

	@Test
	public void initDb() throws Exception {
//		flowService.rechargeFlowCreate(46777, 0.01, 1, 1);
	}

}
