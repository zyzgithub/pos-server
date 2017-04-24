package courier_mana.personal;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.personal.service.CourierAchievementService;

import courier_mana.statistics.CourierUserServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierAchievementServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(CourierUserServiceTest.class);
	
	@Autowired
	private CourierAchievementService courierAchievementService;
	
	private void showAsJSON(Object obj){
		AjaxJson ajaxJson = BasicController.SUCCESS(obj);
		logger.info(JSON.toJSONString(ajaxJson));
	}
	
	/**
	 * 获取快递员(管理员)个人信息
	 * done
	 */
//	@Test
	public void getCourierInfoTest(){
//		showAsJSON(this.courierAchievementService.getCourierInfo(405));//done
		showAsJSON(this.courierAchievementService.getCourierInfo(404));//查询结果为空，则此方法返回null
	}
	
	/**
	 * 获取快递员排名
	 * done
	 */
//	@Test
	public void getCouriersRankTest(){
		Long startTime = System.currentTimeMillis();
		showAsJSON(this.courierAchievementService.getCouriersRank(null, "2015-10-10", "2016-01-01", false, 1, 20));
		System.out.println(System.currentTimeMillis()-startTime);
//		showList(this.courierAchievementService.getCouriersRank(59287, "2015-10-01", "2015-10-10", true, 1, 20));
	}
	
	/**
	 * 获取我的排名
	 */
//	@Test
	public void getMyRankTest(){
		showAsJSON(this.courierAchievementService.getMyRank(59287, "2015-10-01", "2015-10-10", false));
	}
	
	public void ctrlTest(){
		
	}
}
