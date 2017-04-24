package courier_mana.example;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.OrderMonitorService;
import com.courier_mana.statistics.service.CourierCommentService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class AdminUserServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(AdminUserServiceTest.class);

	@Autowired
	private CourierOrgServicI courierOrgServiceImpl;
	@Autowired
	private OrderMonitorService orderMonitorService;
	@Autowired
	private CourierCommentService courierCommentServiceImpl;
	
	@Test
	public void getManageOrgsTest() {
		List<Map<String, Object>> results = courierOrgServiceImpl.getManageOrgs(59271);
		System.out.println("result.size()="+results.size());
		assertEquals("", 7, results.size());
	}

	
	@Test
	public void getParentOrgTest2() {
		List<Map<String, Object>> results = courierOrgServiceImpl.getManageOrgs2(59271);
		assertEquals("", 52, results.size());
	}
	
	@Test
	public void getParentOrgTest() {
		Map<String, Object> orgMap = courierOrgServiceImpl.getParentOrg(59271);
		Integer orgId = Integer.parseInt(orgMap.get("id").toString());
		assertEquals(Integer.valueOf(9), orgId);
	}
	
	@Test
	public void getParentOrgIdTest() {
		Integer orgId = courierOrgServiceImpl.getParentOrgId(59271);
		assertEquals(Integer.valueOf(9), orgId);
	}
	
	@Test
	public void getManageCouriersTest(){
		List<Map<String, Object>> couriers = courierOrgServiceImpl.getManageCouriers(59271);
		System.out.println(couriers.size());
	}
	
	@Test
	public void getAddressIdTest(){
		List<Integer> addressIds=orderMonitorService.getAddressId(59271);
		System.out.println(StringUtils.join(addressIds,","));
	}
	@Test
	public void getNosendOrderTest(){
		List<Integer> addressIds=orderMonitorService.getAddressId(59271);
		List<Map<String,Object>> list = orderMonitorService.getNosendOrder(1, 3, addressIds);
		System.out.println(ArrayUtils.toString(list.toArray()) );
	}
	
	@Test
	public void getCastFifteenTest(){
		List<Map<String,Object>> list =orderMonitorService.getCastFifteen(59271, 1, 3);
		System.out.println(list.size());
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getCastThirdtyTest(){
		List<Map<String,Object>> list = orderMonitorService.getCastThirdty(59271, 1, 3);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test 
	public void getUrgeOrderTest(){
		List<Integer> addressIds = orderMonitorService.getAddressId(59271);
		List<Map<String,Object>> list = orderMonitorService.getUrgeOrder(1, 3, addressIds);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getCommentClassify(){
		List<Map<String,Object>> list = courierCommentServiceImpl.getCommentClassify(59271);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getCommentByStarTest(){
		List<Map<String,Object>> list = courierCommentServiceImpl.getCommentByStar(59271, 4, 1,3 );
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getCommentTest(){
		List<Map<String,Object>> list = courierCommentServiceImpl.getComment(59271, 1, 6);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getDetailCommentTest(){
		Map<String,Object> list = courierCommentServiceImpl.getDetailComment(2);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void geTCustTypeList(){
		List<Map<String,Object>> list = courierCommentServiceImpl.getCustTypeList();
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void getCommentByCustType(){
		List<Map<String,Object>> list = courierCommentServiceImpl.getCommentByCustType(0.0, 500., 1, 10, 59271);
		System.out.println(ArrayUtils.toString(list));
	}
	
	@Test
	public void orderCommentSearch(){
		List<Integer> orgIds = courierCommentServiceImpl.getOrgIds(4);
		SearchVo vo= new SearchVo();
		vo.setTimeType("day");
		List<Map<String,Object>> list = courierCommentServiceImpl.orderCommentSearch(orgIds, 1, 5, vo);
		System.out.println(ArrayUtils.toString(list));
	}
	
}
