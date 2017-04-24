package courier_mana.statistics;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.service.ruralbase.MealServiceI;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class MealServiceTest extends TestCase{

	@Autowired
	private MealServiceI mealService;
	
	@Test
	public void getMealPreList() {
		mealService.getMealPreList(821,"A",1,10);
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void getMealList(){
		mealService.getMealList(821,"A",1,10,2);
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void setTop(){
		mealService.setTop("5458", 821);
		System.out.println(">>>>>>>>>>");
	}
	
	
}
