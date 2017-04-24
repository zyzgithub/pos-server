package courier_mana.personal;

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
import com.courier_mana.personal.service.CourierMyInfoService;
import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WUserEntity;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierMyInfoServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(CourierMyInfoServiceTest.class);

	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Test
	public void getMyInfo() {
		Map<String, Object> user = courierMyInfoService.getMyInfo(59300);
		System.out.println(JSON.toJSONString(user));
	}
	
	@Test
	public void updatePassword() {
		Boolean flag = courierMyInfoService.updatePassword(59304, "888888");
		System.out.println(flag);
	}
}
