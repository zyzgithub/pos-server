package print;

import junit.framework.TestCase;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.entity.order.OrderEntity;
import com.wm.service.orderincome.OrderIncomeServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class OrderIncomeServiceTest extends TestCase{
	@Autowired
	private OrderIncomeServiceI orderIncomeServiceI;
	
	/**
	 * 测试预收入
	 */
	@Test
	public void createOrderIncome() {
		OrderEntity orderEntity = orderIncomeServiceI.get(OrderEntity.class, NumberUtils.toInt("434661"));  //DEV11530
		if(orderEntity == null){
			return;
		}
		try {
			orderIncomeServiceI.createOrderIncome(orderEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
