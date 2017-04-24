package courier.salary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.courier_mana.common.Constants;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.OrSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByOpenUp;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByPunch;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByScramble;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular.SuspendAccount;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.simulateordercomplete.SimulateOrderCcompleteServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.position.PositionServiceI;
import com.wm.service.statistics.CourierStatisticsDalyServiceI;
import com.wm.service.statistics.OrgStatisticsDaylyServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class SimulateTest {

	private final static Logger logger = LoggerFactory.getLogger(SimulateTest.class);
	
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Autowired
	private CourierAccountSuspendServiceI courierAccountSuspendService;
	
	@Autowired
	private OrgServiceI orgService;
	 
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private FlowServiceI flowService;
	
	@Autowired
	private PositionServiceI positionService;
	
	@Autowired
	private CourierStatisticsDalyServiceI courierStatisticsDalyService;
	
	@Autowired
	private OrgStatisticsDaylyServiceI orgStatisticsDaylyService;
	
	@Autowired
	private SimulateOrderCcompleteServiceI simulateOrderCcompleteService;
	
//	@Autowired
//	private PayServiceI payService;

	

	
	@Test
	public void testCourierPayOrder(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(333814);	
		list.add(340341);	
		list.add(347482);	
		list.add(347610);	
		list.add(353982);	
		list.add(368119);	
		list.add(372507);	
		list.add(372525);	
		list.add(372576);	
		list.add(377022);	
		list.add(377030);	
		list.add(377552);	
		list.add(374603);	
		list.add(380857);
//		list.add(8877);
		try {
			for(Integer orderId : list){
				logger.info("开始模拟快递员完成电话订单，订单id：" + orderId);
				OrderEntity order = orderService.get(OrderEntity.class, orderId);
				Double needPay = (Math.rint(order.getOrigin() * 100) + Math.rint(order.getDeliveryFee() * 100)
			                + Math.rint(order.getCostLunchBox() * 100) - Math.rint(order.getCard() * 100)
			                - Math.rint(order.getScoreMoney() * 100));
	
				order.setState(OrderStateEnum.PAY.getOrderStateEn());
				order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
				order.setOnlineMoney(needPay.intValue() / 100.0);
				//时间取哪一天的
				order.setPayTime(DateUtils.getSeconds());
				logger.info("开始支付订单，快递员id:" + order.getCourierId() +"需支付金额：" +needPay);
				orderService.saveOrUpdate(order);
				simulateOrderCcompleteService.orderAlipayDone(orderId);
				logger.info("模拟成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("模拟失败");
		}
	}

	
}
