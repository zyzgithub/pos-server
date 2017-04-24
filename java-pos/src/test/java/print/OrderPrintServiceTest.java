package print;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
@ContextConfiguration(locations = { "classpath:spring-mvc-dev.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class OrderPrintServiceTest extends TestCase{
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private PrintServiceI printService;
	
	/**
	 * 测试客来乐打印机
	 */
	@Test
	public void printKLLOrder() {
		//OrderEntity orderEntity = orderService.get(OrderEntity.class, NumberUtils.toInt("747"));  //KP001951
		OrderEntity orderEntity = orderService.get(OrderEntity.class, NumberUtils.toInt("752"));  //DEV11530
		//System.out.println(JSON.toJSONString(orderEntity));
		if(orderEntity == null){
			return;
		}
		printService.print(orderEntity, false);
	}
	
	/**
	 * 测试飞鹅打印机
	 */
	@Test
	public void printFEOrder() {
		OrderEntity orderEntity = orderService.get(OrderEntity.class, NumberUtils.toInt("465117")); //有座位号431684,外卖432524,扫码431901
		//System.out.println(JSON.toJSONString(orderEntity));
		if(orderEntity == null){
			return;
		}
		printService.print(orderEntity, false);
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
