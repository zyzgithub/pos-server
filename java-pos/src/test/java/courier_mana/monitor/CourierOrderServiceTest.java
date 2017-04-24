package courier_mana.monitor;

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

import com.courier_mana.monitor.service.CourierOrderMonitorServicI;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierOrderServiceTest extends TestCase{
	private static final Logger logger = LoggerFactory.getLogger(CourierOrderServiceTest.class);

	@Autowired
	private CourierOrderMonitorServicI courierOrderServicImpl;
	
	/**
	 * 管理员订单统计
	 */
	@Test
	public void getManageOrdersCountTest() {
		Map<String,Object> map = courierOrderServicImpl.getCourierOrdersCounts(57465);
		for(Map.Entry<String, Object> entry:map.entrySet()){
			logger.info("=="+entry.getKey()+"===="+entry.getValue());
		}
	}
	/**
	 * 快递员订单统计
	 */
	@Test
	public void getManageOrdersCountByIdTest() {
		Map<String,Object> map = courierOrderServicImpl.getCourierOrdersCountById(94781);
		for(Map.Entry<String, Object> entry:map.entrySet()){
			logger.info("=="+entry.getKey()+"===="+entry.getValue());
		}
	}
	/**
	 * 查询指定订单id的明细
	 */
	@Test
	public void getOrderDetailByOrderIdTest() {
		List<Map<String,Object>> retList = courierOrderServicImpl.getOrderDetailByOrderId(840);
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 查询指定管理员的快递员位置和快递员订单数
	 */
	@Test
	public void getCouriersAndOrdersCountTest() {
		List<Map<String,Object>> retList = courierOrderServicImpl.getCouriersAndOrdersCount(94785);
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 根据机构查询指定管理员的快递员位置和快递员订单数
	 */
	@Test
	public void getCouriersAndOrdersCountByIdTest() {
		//List<Map<String,Object>> retList = courierOrderServicImpl.getCouriersAndOrdersCountById(57465,6);
		List<Map<String,Object>> retList = courierOrderServicImpl.getCouriersAndOrdersCountById(94785,149);
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 根据关键词查询今日指定用户下的订单列表
	 */
	@Test
	public void getOrdersByKeywordsTest() {
		List<Map<String,Object>> retList = courierOrderServicImpl.getOrdersByKeywords(1,10,94782,"13632372337");
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 根据关键词查询今日指定用户下的订单列表
	 */
	@Test
	public void getOrdersByOrgIdTest() {
		List<Map<String,Object>> retList = courierOrderServicImpl.getOrdersByOrgId(1,10,94782,149);
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 根据订单ID查询其他快递员信息
	 */
	@Test
	public void getCouriersByOrderIdTest() {
		List<Map<String,Object>> retList = courierOrderServicImpl.getCouriersByOrderId(85254);
		for(Map<String,Object> obj: retList){
			logger.info(">>>"+obj);
		}
	}
	/**
	 * 根据订单ID查询其他快递员信息
	 */
	@Test
	public void updateOrderCourierByIdTest() {
		Integer row = courierOrderServicImpl.updateOrderCourierById(33344,85254);
		logger.info(">>>>>>>"+row);
	}
}
