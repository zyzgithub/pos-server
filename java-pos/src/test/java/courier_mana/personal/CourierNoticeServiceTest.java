package courier_mana.personal;

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

import com.alibaba.fastjson.JSON;
import com.courier_mana.personal.service.CourierNoticeService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierNoticeServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(CourierNoticeServiceTest.class);

	@Autowired
	private CourierNoticeService courierNoticeService;
	
	@Test
	public void getNoticeList() {
		List<Map<String,Object>> noticeList = courierNoticeService.getNoticeList(1,10);
		System.out.println(JSON.toJSONString(noticeList));
	}

}
