package courier_mana.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.courier_mana.monitor.service.CourierAttendanceServiceI;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})

public class CourierAttendanceServiceITest extends TestCase {
	
	@Autowired
	private CourierAttendanceServiceI courierAttendanceServiceI;
	
	@Test
	public void getCourierOndutySumTest(){
		Integer sum = courierAttendanceServiceI.getCourierOndutySum(76758);
		System.out.println(sum);
		
	}

}
