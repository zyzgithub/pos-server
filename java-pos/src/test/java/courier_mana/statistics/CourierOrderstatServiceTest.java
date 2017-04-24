package courier_mana.statistics;

import java.util.ArrayList;
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
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.CourierOrderstatService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierOrderstatServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(CourierOrderstatServiceTest.class);

	@Autowired
	private CourierOrderstatService courierOrderstatService;
	
	@Test
	public void getOrderstatByMerchant() {
		SearchVo searchVo = new SearchVo();
//		searchVo.setTimeType("month");
		searchVo.setTimeType("other");
		searchVo.setBeginTime(1422720000);
		searchVo.setEndTime(1454256000);
		List<Integer> orgIds = new ArrayList<Integer>();
		orgIds.add(6);
		orgIds.add(7);
		orgIds.add(65);
		List<Map<String, Object>> orderstatList = courierOrderstatService.getOrderstatByMerchant(orgIds, 1, 10, searchVo);
		System.out.println(JSON.toJSONString(orderstatList));
	}
	
	@Test
	public void getOrderstatByOrg(){
		SearchVo searchVo = new SearchVo();
//		searchVo.setTimeType("month");
		searchVo.setTimeType("other");
		searchVo.setBeginTime(1422720000);
		searchVo.setEndTime(1456329600);
		List<Integer> orgIds = new ArrayList<Integer>();
		orgIds.add(6);
		orgIds.add(7);
		orgIds.add(65);
		List<Map<String, Object>> orderstatList = courierOrderstatService.getOrderstatByOrg(orgIds, 1, 10, searchVo);
		System.out.println(JSON.toJSONString(orderstatList));
	}
}
