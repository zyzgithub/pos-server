package courier_mana.personal;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.courier_mana.personal.service.CourierRegionService;

import courier_mana.statistics.CourierUserServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierRegionServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(CourierUserServiceTest.class);
	
	@Autowired
	private CourierRegionService courierRegionService;
	
	private void showMap(Map<String, Object> map){
		StringBuilder str = new StringBuilder();
		str.append("\n");	
		if(map!=null){
			for(Map.Entry<String, Object> entry:map.entrySet()){
				str.append(entry.getKey());
				str.append(":");
				str.append(entry.getValue());
			}
			logger.info(str.toString());
		}
		else{
			logger.info("Map == null");
		}
	}
	
	@SuppressWarnings("unused")
	private void showList(List<Map<String, Object>> list){
		for(Map<String, Object> m:list){
			showMap(m);
		}
	}
	
	/**
	 * 获取当前用户所在机构
	 * done
	 */
//	@Test
	public void getCourierOrgInfoTest(){
		showMap(this.courierRegionService.getCourierOrgInfo(59287));//done
//		showMap(this.courierRegionService.getCourierOrgInfo(592811));
	}
	
	/**
	 * 获取指定机构的详情
	 * done
	 */
//	@Test
	public void getOrgInfoTest(){
//		showMap(this.courierRegionService.getOrgInfo(7));//done
		showMap(this.courierRegionService.getOrgInfo(700));//done
	}
	
	/**
	 * 获取快递员(管理员)管辖范围
	 * done
	 */
//	@Test
	public void getAdminRegionTest(){
//		showMap(this.courierRegionService.getAdminRegion(59287));//done
		showMap(this.courierRegionService.getAdminRegion(5928711));
	}
}
