package courier_mana.monitor;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.courier_mana.monitor.service.CourierMerchantServicI;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierMerchantServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(CourierMerchantServiceTest.class);

	@Autowired
	private CourierMerchantServicI courierMerchantServicImpl;
	
	@Test
	public void getManageMerchantsTest() {
		//List<Map<String, Object>> results = courierMerchantServicImpl.getMerchants(59271);
		List<Map<String, Object>> results = courierMerchantServicImpl.getMerchants(95040);
		logger.info(">>>>>>>>>>>>"+results);
	}
	@Test
	public void getManageMerchantsCountTest() {
		//List<Map<String, Object>> results = courierMerchantServicImpl.getMerchants(59271);
		Long results = courierMerchantServicImpl.getMerchantCount("95040");
		logger.info(">>>>>>>>>>>>"+results);
	}
	@Test
	public void getManageBindMerchantsTest() {
		Long results = courierMerchantServicImpl.getBindMerchants("95040");
		logger.info(">>>>>>>>>>>>"+results);
	}
	@Test
	public void getManageUnBindMerchantsCountTest() {
		Long results = courierMerchantServicImpl.getUnBindMerchantCount(416);
		logger.info(">>>>>>>>>>>>"+results);
	}
	@Test
	public void getAllAndUnBindMerchantCountTest() {
		Map<String, Long> results = courierMerchantServicImpl.getAllAndUnBindMerchantCount(95040);
		logger.info(">>>>>>>>>>>>"+results);
	}

	
}
